/*
 * Author Moroni Angelo
 * This file is part of SeachGPSPath.

    SeachGPSPath is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SeachGPSPath is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SeachGPSPath.  If not, see <http://www.gnu.org/licenses/>. 
 */

package com.angelo.datastructures.dcel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.angelo.datastructures.bst.VInternalNode;
import com.angelo.datastructures.bst.VLeafNode;
import com.angelo.datastructures.bst.VLinkedNode;
import com.angelo.voronoi.BBoxOperation;
import com.angelo.voronoi.VPoint;






public class DCEL {
	private ArrayList<DCELVertex> vertex;
	private ArrayList<Face> faces;
	private ArrayList<HalfEdge> halfEdges;
	 

	public DCEL() { 
		halfEdges = new ArrayList<>();
		faces = new ArrayList<>();
		vertex = new ArrayList<DCELVertex>();
		
	}
	public void dcelClear(){
		vertex = null;
		faces = null;
		halfEdges = null;
	}
	
	public void addHalfEdge(VLinkedNode n1 , VLinkedNode n2 , VLinkedNode n3){
		VLeafNode leaf1 = (VLeafNode) n1;
        VLeafNode leaf2 = (VLeafNode) n2;
        VLeafNode leaf3 = (VLeafNode) n3;
        
        VInternalNode parent1 = leaf1.getFirstCommonParent(leaf2);
        VInternalNode parent2 = leaf2.getFirstCommonParent(leaf3);
        
        //a new couple of half edges
        HalfEdge _he1 = new HalfEdge();
        HalfEdge _he2 = new HalfEdge();
        _he1.setOrigin(null);
        _he2.setOrigin(null);
        _he1.setTwin(_he2);
        _he2.setTwin(_he1);
        //set divided site point and bisector
        _he1.setV1V2(n1.siteevent.getPoint(), n2.siteevent.getPoint());
        _he2.setV1V2(n1.siteevent.getPoint(), n2.siteevent.getPoint());
                
        //set internal node half edge
        parent1.set_halfedge(_he1);
        parent2.set_halfedge(_he2);
        
        //add new edges to list
        this.halfEdges.add(_he1);
        this.halfEdges.add(_he2);
	}
	
	public void addFace(Face face){
		faces.add(face);
	}
	
	public HalfEdge addVertex(VLinkedNode n1 , VLinkedNode n2 , VLinkedNode n3, double x, double y){
		VLeafNode leaf1 = (VLeafNode) n1;
        VLeafNode leaf2 = (VLeafNode) n2;
        VLeafNode leaf3 = (VLeafNode) n3;
        
        VInternalNode left  = leaf1.getFirstCommonParent(leaf2);
        VInternalNode right = leaf2.getFirstCommonParent(leaf3);
  //      VInternalNode down  = leaf1.getFirstCommonParent(leaf3);
        
        //this edges ends to a same vertex
        HalfEdge heLeft = left.get_halfedge();
        HalfEdge heRight = right.get_halfedge();
        ArrayList<HalfEdge> newCouple = DCEL.getNewTwinHalfEdge();
        
        //////////// DEBUG //////////////////////
       // System.out.println("node left:"+left.toString() +" edge left: " +heLeft.getpID());
        //System.out.println("node right:"+right.toString() +" edge right: " +heRight.getpID());
        /////////// END DEBUG ////////////////////////////////
        
        //new Vertex 
        DCELVertex newVertex = checkExistingVertex(x, y,this.vertex);
        if(newVertex == null){
        	newVertex = new DCELVertex(x, y);
        	this.vertex.add(newVertex);
        }
        //set newVertex half edge
        newVertex.getEdges().add(newCouple.get(0));
        //set origin of new edge
        newCouple.get(0).setOrigin(newVertex);
        
        
        //check if heleft ora heright have newvertex as origin
        HalfEdge fromLeft = (heLeft.getOrigin()==null) ? heLeft : heLeft.getTwin();
        HalfEdge fromRight = (heRight.getOrigin()==null) ? heRight : heRight.getTwin();
     
	    //set origin other half edge        
        fromLeft.setOrigin(newVertex);       
        fromRight.setOrigin(newVertex);
        
        //set correctly halfedge sites
//        VPoint v1l = fromLeft.getV1();
//        VPoint v2l = fromLeft.getV2();
//        VPoint v1r = fromRight.getV1();
//        VPoint v2r = fromRight.getV2();
//        
//        if(v1l.x == v1r.x && v1l.y == v1r.y) fromLeft.setV1V2(v2l, v1l);
//        if(v1l.x == v2r.x && v1l.y == v2r.y){
//        	fromLeft.setV1V2(v2l, v1l);
//        	fromRight.setV1V2(v2r, v1r);
//        }
//        if(v2l.x == v2r.x && v2l.y == v2r.y) fromRight.setV1V2(v2r,v1r);
        
        //set prev and next of the edge
        fromLeft.getTwin().setNext(newCouple.get(0));
        fromLeft.setPrevious(fromRight.getTwin());
        fromRight.getTwin().setNext(fromLeft);
        fromRight.setPrevious(newCouple.get(1));
        newCouple.get(1).setNext(fromRight);
        newCouple.get(0).setPrevious(fromLeft.getTwin());
        
        //add new Half edge to edges of new Vertex
        newVertex.getEdges().add(fromRight);
        newVertex.getEdges().add(fromLeft);         
        
        
        //add new half edge to halfedge
        this.halfEdges.addAll(newCouple);
       
       //check existing double half edge
        for(int i=0;i<newVertex.getEdges().size();i++){
        	HalfEdge currhe = newVertex.getEdges().get(i);
        	HalfEdge currTwinhe = currhe.getTwin();
        	if(currTwinhe.getOrigin()==newVertex){
        		//unlink currhe and its twin 
        		currhe.getPrevious().setNext(currhe.getNext());
        		currhe.getNext().setPrevious(currhe.getPrevious());
        		currTwinhe.getPrevious().setNext(currTwinhe.getNext());
        		currTwinhe.getNext().setPrevious(currTwinhe.getPrevious());
        		
        		//take off half edge from newvertex
        		newVertex.getEdges().remove(currhe);
        		newVertex.getEdges().remove(currTwinhe);
        		//take off half edge from newvertex
        		this.halfEdges.remove(currhe);
        		this.halfEdges.remove(currTwinhe);
        	}
        }
     
        return newCouple.get(0);
	}
	
	
	// this methos builds faces and sets relative site
	public void buildFaces(){
		int i = 0;
		//costruzione delle facce
		for(HalfEdge e: this.halfEdges){
			if(e.getIncidentFace()==null){
				ArrayList<VPoint> siti = new ArrayList<>();
				Face face = new Face(e, "face #"+i);
				this.faces.add(face);
				i++;
				do{
					try{
						VPoint v1 = e.getV1();
						VPoint v2 = e.getV2();
						if(face.getSito()==null){
							if(siti.contains(v1)) face.setSito(v1);
							else if(siti.contains(v2)) face.setSito(v2);
							siti.add(v2);
							siti.add(v1);
						}
					}catch(NullPointerException ef){
						
					}
					e.setIncidentFace(face);
					e = e.getNext();
				}while(e.getIncidentFace()==null);
			}
                }
		
	}
	
	//this method complete faces and bounding edges
	public void completeFace(){
		VPoint infp = new VPoint(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		for( Face f:this.faces){
			if(f.getSito() != null){
				HalfEdge e = f.getEdge();
				do{
					if(e.getV1() == null){
						e.setV1(f.getSito());
						e.setV2(infp);
						e.getTwin().setV1(infp);
						e.getTwin().setV2(f.getSito());
					}
					e = e.getNext();
				}while(e != f.getEdge());
			}else{
				f.setSito(infp);
			}
		}
	}


	
	public DCELVertex checkExistingVertex(double fx, double fy,ArrayList<DCELVertex> vvv){
		DCELVertex foundVertex = null;
		for(DCELVertex v:vvv){
			if(v.x==fx && v.y==fy){
				foundVertex = v;
			}
		}
		return foundVertex;
	}
	
	///CODICE PENOSO E LENTISSIMO  O(n*m) n numero di vertici m numero halfedge ///////////////////////////////
	public void removeExternalVertex() {
		
		for(Iterator<DCELVertex> it = vertex.iterator();it.hasNext();){
			DCELVertex v = it.next();
			if(BBoxOperation.isOut(v)){
				try{
					for(HalfEdge he : v.getEdges()){
						he.setOrigin(null);
						//tolgo l'half edge e il suo Twin dalla lista
						halfEdges.remove(he);
						halfEdges.remove(he.getTwin());
						
					}
				}catch(NullPointerException e){
					//some half edge can be removed before
				}finally{
					//remove v from vertex arrayList
					it.remove();
				}
			}
		}
				
	};
	
	public void removeUselessHalfEdge(){
		for(DCELVertex v:vertex){
			for(Iterator<HalfEdge> ith = v.getEdges().iterator(); ith.hasNext();){
				HalfEdge he = ith.next();
				if(he.getTwin().getOrigin()==null){
					ith.remove();
				}
			}
		}
			
	}
	
	public void removeInfinityEdge(){
		for(Iterator<HalfEdge> it = halfEdges.iterator();it.hasNext();){
			HalfEdge he = it.next();
			if(he.getOrigin()==null){
				he.getTwin().setTwin(null);
				it.remove();
			}
		}
		for(Iterator<HalfEdge> it = halfEdges.iterator();it.hasNext();){
			HalfEdge he = it.next();
			if(he.getTwin()==null){
				it.remove();
			}
		}

	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	/////// CODICE VELOCE O(m) dove m è il numero di half edge /////////////////////////////
	public void clearVoronoiDiagram(){
		//we remove all useless halfedge and useless vertex
		for(Iterator<HalfEdge> ut = halfEdges.iterator();ut.hasNext();){
			HalfEdge he = ut.next();
			DCELVertex orhe = he.getOrigin();
			DCELVertex orthe = he.getTwin().getOrigin();
			if(orhe==null || BBoxOperation.isOut(orhe)|| orthe ==null || BBoxOperation.isOut(orthe)){
				//set null next and prev
				if(he.getPrevious() != null) he.getPrevious().setNext(null);
				if(he.getNext() != null) he.getNext().setPrevious(null);
				ut.remove();
				if(orhe != null){
					orhe.getEdges().remove(he);
					if(BBoxOperation.isOut(orhe)) vertex.remove(orhe);
				}
			}

		}
	}
	
	public void setBoundingBox(){
		HashMap<DCELVertex,HalfEdge> boudingEdges = new HashMap<>();
		for(Iterator<HalfEdge> ut = halfEdges.iterator();ut.hasNext();){
			HalfEdge he = ut.next();
			DCELVertex orhe = he.getOrigin();
			if(he.getNext()==null){
				if (BBoxOperation.isOnCorner(orhe)) orhe.cornerVertex=1; //da correggere sta riga
				boudingEdges.put(orhe, he) ;
			}
		}
		Collection<HalfEdge> bhes =  boudingEdges.values();
		for(HalfEdge bhe:bhes){
			DCELVertex dest = bhe.getTwin().getOrigin();
			HalfEdge nexthe = boudingEdges.get(dest);
			bhe.setNext(nexthe);
			nexthe.setPrevious(bhe);
		}
	}
	

	
	
	
	
	

	/////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	public void completeEdge(DCELVertex v) {
		ArrayList<HalfEdge> edges = v.getEdges();
		int nElements = edges.size();
		System.out.println("Elements in list: "+ nElements);
		for(int i = 0;i<edges.size();i++){
			int prec = i-1;
			if(prec<0){
				prec = nElements-1;
			}
			int succ = (i+1) % nElements;
			System.out.println("Current element: "+ i+" prec: "+prec+" succ: "+succ );
			edges.get(i).getTwin().setNext(edges.get(prec));
			edges.get(i).setPrevious(edges.get(succ));
		}
		
		
	}
	*/
	
	

	static public ArrayList<HalfEdge> getNewTwinHalfEdge(){
		//two half edge _he1 and his twin
		HalfEdge _he1 = new HalfEdge();
		HalfEdge _he2 = new HalfEdge();
		_he1.setTwin(_he2);
		_he2.setTwin(_he1);
		//setOrigin
		_he1.setOrigin(null);
		_he2.setOrigin(null);
		//new couple of half edge
		ArrayList<HalfEdge> eds = new ArrayList<>();
		eds.add(_he1);
		eds.add(_he2);
		return eds;
	}
	//setter and getter method
	public ArrayList<DCELVertex> getVertex() {
		return vertex;
	}

	public void setVertex(ArrayList<DCELVertex> vertex) {
		this.vertex = vertex;
	}

	public ArrayList<Face> getFaces() {
		return faces;
	}

	public void setFaces(ArrayList<Face> faces) {
		this.faces = faces;
	}

	public ArrayList<HalfEdge> getHalfEdges() {
		return halfEdges;
	}

	public void setHalfEdges(ArrayList<HalfEdge> halfEdges) {
		this.halfEdges = halfEdges;
	}

	
	
	

}
