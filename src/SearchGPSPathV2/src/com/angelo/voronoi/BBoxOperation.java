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

package com.angelo.voronoi;

import java.util.ArrayList;
import java.util.TreeMap;

import com.angelo.datastructures.dcel.DCEL;
import com.angelo.datastructures.dcel.DCELVertex;
import com.angelo.datastructures.dcel.HalfEdge;
import com.angelo.datastructures.eventqueue.VSiteEvent;

public class BBoxOperation {
	private ArrayList<DCELVertex> vertex;
	private ArrayList<HalfEdge> halfedge;
	private TreeMap<Double, DCELVertex> upside;
	private TreeMap<Double, DCELVertex> downside;
	private TreeMap<Double, DCELVertex> leftside;
	private TreeMap<Double, DCELVertex> rightside;
	
	HalfEdge e[];
	
	static double maxX;
	 static double minX;
	 static double maxY;
	 static double minY;
	
	
	public BBoxOperation(){
		vertex = new ArrayList<>();
		halfedge = new ArrayList<>();
		upside = new TreeMap<>();
		downside = new TreeMap<>();
		leftside = new TreeMap<>();
		rightside = new TreeMap<>();
	}
	
	public void setMargin(){
		
	}
	
	public static void setMargin(double[] margin){
		minX = margin[0];
		maxX = margin[1];
		minY = margin[2];
		maxY = margin[3];
	}
	
	public static double[] getMargin(){
		double[] margin = new double[4];
		margin[0] = minX;
		margin[1] = maxX;
		margin[2] = minY;
		margin[3] = maxY;
		return margin;
	}
	//this method calculates min and max of x and y to 
	//build boundary box
	public void calcolaMaxMin(VSiteEvent event){
		double evX = event.getX();
		double evY = event.getY();
		
		//set X boundary
		if(evX<minX){
			minX=evX;
		}
		if(evX>maxX){
			maxX=evX;
		}
		//set Y boundary
		if(evY<minY){
			minY=evY;
		}
		if(evY>maxY){
			maxY=evY;
		}
		
	}
	
	public static boolean isOnCorner(DCELVertex orhe) {
		boolean xc = (orhe.x == minX || orhe.x==maxX)? true:false;
		boolean yc = (orhe.y == minY || orhe.y == maxY )? true:false;
		return xc && yc;
	}
	
	public static boolean isOut(DCELVertex v){
		boolean xOut = (v.x < minX || v.x > maxX)?true:false;
		boolean yOut = (v.y < minY || v.y > maxY)?true:false;
		return xOut || yOut;
	}
	
	//this method get DCELVERTEX and HalfEdge on bounding BOX
		public void buildSimplyBBox(){
			DCELVertex c0 = new DCELVertex(minX, minY);
			DCELVertex c1 = new DCELVertex(minX, maxY);
			DCELVertex c2 = new DCELVertex(maxX, maxY);
			DCELVertex c3 = new DCELVertex(maxX,minY);
			c0.cornerVertex=0;
			c1.cornerVertex=1;
			c2.cornerVertex=2;
			c3.cornerVertex=3;
			
			 e = new HalfEdge[4];
			for(int i = 0;i<4;i++){
				ArrayList<HalfEdge> h0 = DCEL.getNewTwinHalfEdge();
				halfedge.addAll(h0);
				e[i] = h0.get(0);
			}
			for(int i=0;i<4;i++){
				HalfEdge e0 = e[i];
				HalfEdge e1 = e[(i+1)%4];

				e0.setNext(e1);
				e1.setPrevious(e0);
				e0.getTwin().setPrevious(e1.getTwin());
				e1.getTwin().setNext(e0.getTwin());

			}
			
			c0.getEdges().add(e[0]);
			e[0].setOrigin(c0);
			c0.getEdges().add(e[3].getTwin());
			e[3].getTwin().setOrigin(c0);
			
			c1.getEdges().add(e[1]);
			e[1].setOrigin(c1);
			c1.getEdges().add(e[0].getTwin());
			e[0].getTwin().setOrigin(c1);
			
			c2.getEdges().add(e[2]);
			e[2].setOrigin(c2);
			c2.getEdges().add(e[1].getTwin());
			e[1].getTwin().setOrigin(c2);
			
			c3.getEdges().add(e[3]);
			e[3].setOrigin(c3);
			c3.getEdges().add(e[2].getTwin());
			e[2].getTwin().setOrigin(c3);
			

			
			vertex.add(c0);
			vertex.add(c1);
			vertex.add(c2);
			vertex.add(c3);
			

		}
		
		public void insertBoundingVertex(HalfEdge edge){
			VPoint direction = null;
			if(edge.extern == 0) direction = edge.getDirection();
			direction = (edge.extern < 0)?(edge.getOpMidPoint()):edge.getMidPoint();
			
			int id = edge.getIntDirection(direction);
			DCELVertex nw;
			if(id==0){
				nw = vertexOnBounding(minX,minY,edge);
			}else if(id==1){
				nw = vertexOnBounding(minX,maxY,edge);
			}else if(id==2){
				nw = vertexOnBounding(maxX,maxY,edge);
			}else if(id==3){
				nw = vertexOnBounding(maxX,minY,edge);
			}else{
				//no origin
				nw = vertexOnBounding(minX, minY, edge);

			}
			HalfEdge etoadd = (edge.getOrigin()==null)?edge:edge.getTwin();
			etoadd.setOrigin(nw);
			nw.getEdges().add(etoadd);
			if(isOnCorner(nw)){
				setCornerHalfEdge(nw);
			}else{
				addToSide(nw);
				vertex.add(nw);
			}
		}
		
		private void setCornerHalfEdge(DCELVertex nw){
			DCELVertex cov = getExistingVertex(nw.x, nw.y);
			HalfEdge ed = e[cov.cornerVertex];
			HalfEdge hetoadd = nw.getEdge();
			
			cov.getEdges().add(hetoadd);
			hetoadd.setOrigin(cov);
			
			ed.getTwin().setNext(hetoadd);
			hetoadd.setPrevious(ed.getTwin());
			
			hetoadd.getTwin().setNext(ed.getPrevious().getTwin());
			ed.getPrevious().getTwin().setPrevious(hetoadd.getTwin());
		}
		
		public DCELVertex getExistingVertex(double fx, double fy){
			DCELVertex foundVertex = null;
			for(DCELVertex v:vertex){
				if(v.x==fx && v.y==fy){
					foundVertex = v;
				}
			}
			return foundVertex;
		}
		
		private void addToSide(DCELVertex nw){
	
			if(nw.x == maxX) rightside.put(-1*nw.y, nw);
			if(nw.x == minX) leftside.put(nw.y, nw);
			if(nw.y == maxY) upside.put(nw.x, nw);
			if(nw.y == minY) downside.put(-1*nw.x, nw);
			
		}
		
		private static DCELVertex vertexOnBounding(double x, double y,HalfEdge edge) {
			double nx = edge.intersectionY(y);
			if(Double.isNaN(nx)) {
				nx = (edge.getOrigin()!=null)?edge.getOrigin().x:edge.getTwin().getOrigin().x;
			}else if(Double.isInfinite(nx)){
				nx=minX-1;
			}
			return (nx>=minX && nx<=maxX)?new DCELVertex(nx,y):new DCELVertex(x,edge.intersctionX(x));
		}
		
		public void completeBBox(){
			completeSingleSide(upside, e[1]);
			completeSingleSide( leftside, e[0]);
			completeSingleSide( rightside, e[2]);
			completeSingleSide( downside, e[3]);
		}
		
		private void completeSingleSide(TreeMap<Double, DCELVertex> side,HalfEdge e){
			while(side.size()>0){
				DCELVertex d = side.get(side.firstKey());
				side.remove(side.firstKey());
				DCELVertex eo = e.getOrigin();
				HalfEdge dh = d.getEdge();
				if(d.x==eo.x && d.y==eo.y){ //same vertex
					dh.setPrevious(e.getTwin());
					e.getTwin().setNext(dh);
					
					dh.getTwin().setNext(e.getTwin().getNext());
					e.getTwin().getNext().setPrevious(dh.getTwin());
					
					eo.getEdges().add(dh);
					dh.setOrigin(eo);
					vertex.remove(d);
				}else{
					//link half edge
					ArrayList<HalfEdge> newhe = DCEL.getNewTwinHalfEdge();
					newhe.get(0).setPrevious(e.getPrevious());
					e.getPrevious().setNext(newhe.get(0));
					
					newhe.get(1).setNext(e.getTwin().getNext());
					e.getTwin().getNext().setPrevious(newhe.get(1));
					
					newhe.get(0).setNext(e);
					e.setPrevious(newhe.get(0));
					
					e.getTwin().setNext(dh);
					dh.setPrevious(e.getTwin());
					
					dh.getTwin().setNext(newhe.get(1));
					newhe.get(1).setPrevious(dh.getTwin());
					
					//set origins
					e.setOrigin(d);
					d.getEdges().add(e);
					newhe.get(1).setOrigin(d);
					d.getEdges().add(newhe.get(1));
					
					eo.getEdges().remove(e);
					eo.getEdges().add(newhe.get(0));
					newhe.get(0).setOrigin(eo);
					
					halfedge.addAll(newhe);
				}
			}
		}
		
	public TreeMap<Double, DCELVertex> getUpside() {
		return upside;
	}
	public void setUpside(TreeMap<Double, DCELVertex> upside) {
		this.upside = upside;
	}
	public TreeMap<Double, DCELVertex> getDownside() {
		return downside;
	}
	public void setDownside(TreeMap<Double, DCELVertex> downside) {
		this.downside = downside;
	}
	public TreeMap<Double, DCELVertex> getLeftside() {
		return leftside;
	}
	public void setLeftside(TreeMap<Double, DCELVertex> leftside) {
		this.leftside = leftside;
	}
	public TreeMap<Double, DCELVertex> getRightside() {
		return rightside;
	}
	public void setRightside(TreeMap<Double, DCELVertex> rightside) {
		this.rightside = rightside;
	}
	
	public ArrayList<DCELVertex> getVertex(){
		return this.vertex;
	}
	public void setVertex(ArrayList<DCELVertex> vertex){
		this.vertex=vertex;
	}

	public ArrayList<HalfEdge> getHalfedge() {
		return halfedge;
	}

	public void setHalfedge(ArrayList<HalfEdge> halfedge) {
		this.halfedge = halfedge;
	}

}
