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

import com.angelo.datastructures.bst.BST;
import com.angelo.datastructures.bst.VInternalNode;
import com.angelo.datastructures.bst.VLinkedNode;
import com.angelo.datastructures.dcel.DCEL;
import com.angelo.datastructures.dcel.DCELVertex;
import com.angelo.datastructures.dcel.Face;
import com.angelo.datastructures.dcel.HalfEdge;
import com.angelo.datastructures.eventqueue.EventQueue;
import com.angelo.datastructures.eventqueue.VCircleEvent;
import com.angelo.datastructures.eventqueue.VEvent;
import com.angelo.datastructures.eventqueue.VSiteEvent;

public class Voronoi {
	
	/**
	 * @param points point list on which we want calculate the diagram 
	 */
	static public DCEL generateVoronoi(ArrayList<VPoint> points){
		
		//variables
		EventQueue eventqueue = new EventQueue();//event queue of site event
		DCEL dcel = new DCEL();//DCEL to represent the voronoi diagram
		BST bst = new BST(); //BST to represent beach line
		BBoxOperation bbox = new BBoxOperation();
		
		//initialize eventqueue
		//def margin
		BBoxOperation.setMargin( VoronoiShared.marginBoundingBox(points));
		
		/*ArrayList<VPoint> completeListpoints = createAllPoints(points);
		for ( VPoint point : completeListpoints ) {
            VSiteEvent newsiteevent = new VSiteEvent( point );
            eventqueue.addEvent( newsiteevent );
        }*/
		for ( VPoint point : points ) {
            VSiteEvent newsiteevent = new VSiteEvent( point );
            eventqueue.addEvent( newsiteevent );
        }
		
		
		
		// While the queue is not empty
		
        VEvent event = null;
        while (!( eventqueue.isEventQueueEmpty() )) {
        	event = eventqueue.getAndRemoveFirstEvent(); //it's like pop function
        	
        	//if event is a siteEvent
        	if ( event.isSiteEvent() ) {
        		VSiteEvent siteevent = (VSiteEvent) event;
        		//call method to handle site event
        		handleSiteEvent(siteevent,bst,eventqueue,dcel);
        	//otherwise if event is a circle event
        	}else if( event.isCircleEvent()){  
        		VCircleEvent circleevent = (VCircleEvent) event;
        		//debug mode
        		//System.out.println("Y Center is: "+circleevent.getCenterY());
        		//call method to handle circle event
        		handleCicleEvent(circleevent,bst,eventqueue,dcel);
        	}else{
        		throw new RuntimeException("Unknown event: we can't handle it " );
        	}
            //debug method
//            System.out.println(bst.toString());
        }
        
        //now we need to refine DCEL  
//          dcel.clearVoronoiDiagram();
          
        //set bounding box
        setBoundingBox(bst,dcel,bbox);
        //we build faces
       dcel.buildFaces();
       dcel.completeFace();
        //end return DCEL
        return dcel;
	}

	/*
	private static ArrayList<VPoint> createAllPoints(ArrayList<VPoint> points) {
		ArrayList<VPoint> allPoints = new ArrayList<>();
		double[] margin = DCEL.getMargin();
		for(VPoint p : points){
			double d0 = p.distanceTo(new VPoint(margin[0], p.y)); //distance between p and minx
			double d1 = p.distanceTo(new VPoint(margin[1], p.y)); //distance between p and maxX
			double d2 = p.distanceTo(new VPoint(p.x, margin[2])); //distance between p and miny
			double d3 = p.distanceTo(new VPoint(p.x, margin[3])); //distance between p and maxY
			VPoint p0 = new VPoint(p.x-(2*d0),p.y);
			VPoint p1 = new VPoint(p.x+(2*d1), p.y);
			VPoint p2 = new VPoint(p.x,p.y-(2*d2));
			VPoint p3 = new VPoint(p.x,p.y+(2*d3));
			allPoints.add(p3);
			allPoints.add(p2);
			allPoints.add(p1);
			allPoints.add(p0);
		}
		allPoints.addAll(points);
		return allPoints;
	}*/

	private static void setBoundingBox( BST bst,DCEL dcel,BBoxOperation bbox){
		//build simply bbox
		bbox.buildSimplyBBox();
		//for each internal node (half-infinite edge) check direction 
		ArrayList<VInternalNode> nodes = new ArrayList<>();
		nodes = bst.getAInternalNode();
		//System.out.println(nodes.size());
			
		for(VInternalNode node:nodes){
			bbox.insertBoundingVertex(node.get_halfedge());
		}
		
		bbox.completeBBox();
		
		
		
		dcel.getVertex().addAll(bbox.getVertex());
		dcel.getHalfEdges().addAll(bbox.getHalfedge());
		
	}

	/**
	 * this method handles Circle Event
	 */
	static private void handleCicleEvent(VCircleEvent circleevent,BST bst,EventQueue eventqueue,DCEL dcel) {

        // Get linked nodes
        VLinkedNode currnode = circleevent.leafnode;
        VLinkedNode prevnode = currnode.getPrev();
        VLinkedNode nextnode = currnode.getNext();
       
        //add vertex before circle event is deleted from bst
        HalfEdge newHalfEdge = dcel.addVertex(prevnode, currnode, nextnode, circleevent.getCenter_x(),circleevent.getCenterY() );
        
        // Remove any circle events, before removing node
        currnode.removeCircleEvents(eventqueue);
        
        // Remove event from structure. return modified internl node
        VInternalNode ces= bst.removeNode(eventqueue, currnode);    
        //set half edge to internal node
        ces.set_halfedge(newHalfEdge);
        //bisector of this new half edge
        newHalfEdge.setV1V2(ces.v1.getPoint(),ces.v2.getPoint());
        newHalfEdge.getTwin().setV1V2(ces.v2.getPoint(),ces.v1.getPoint());
        
        //check if medium point is on ray or not
        VPoint m = newHalfEdge.getMidPoint();
        VPoint siteH = newHalfEdge.getV1();
        VPoint siteNH = currnode.siteevent.getPoint();
        
        VPoint o = newHalfEdge.getOrigin();
        if(m.x== o.x && m.y == o.y){
        	//midpoint == origin
        	newHalfEdge.extern=0;
        	
        	//.out.println("2.ci sto: "+o.toString());
        	VPoint p1 = new VPoint((2*o.x)-1,(2*o.y)-1);
        	VPoint p2 = new VPoint((2*o.x)-p1.x, (2*o.x)-p1.y);
        	VPoint direction = (siteNH.distanceTo(p1)<siteNH.distanceTo(p2))?p1:p2;
        	newHalfEdge.setDirection(direction);
        }else if(m.distanceTo(siteH)>m.distanceTo(siteNH)) newHalfEdge.extern=-1;
        newHalfEdge.getTwin().extern=newHalfEdge.extern;
        
        newHalfEdge.getIntersectionSitePoint(circleevent.getY());
        
        // Remove Circle Events for prev (pi) and next (pk)
        if ( prevnode!=null )prevnode.removeCircleEvents(eventqueue);
        if ( nextnode!=null )nextnode.removeCircleEvents(eventqueue);
        
        // Determine circle events for p1,pi,(pjremoved),pk and pi,(pjremoved),pk,p2
        if ( prevnode!=null ) prevnode.addCircleEvent(eventqueue);
        if ( nextnode!=null ) nextnode.addCircleEvent(eventqueue);
		
	}

	/**
	 * this method handles Site Event
	 */
	static private void handleSiteEvent(VSiteEvent siteevent,BST bst,EventQueue eventqueue,DCEL dcel) {
        // If status structure is empty, insert so that the status
        //  structure consists of a single leaf storing the site event
        if ( bst.isStatusStructureEmpty() ) {
            // Set the root node
            bst.setRootNode( siteevent );
            
            // Also: check for the degrading case (equal y values between
            //  first and second nodes of queue)
            VEvent nextevent = eventqueue.getFirstEvent();
            if ( nextevent!=null && siteevent.getY()==nextevent.getY() ) {
                siteevent.getPoint().y-=0.00000001;
            }
            return;
        }
        
        // Search the status structure for leaf which represents
        //  the arc directly above the site event
        VLinkedNode leafabove = bst.getNodeAboveSiteEvent( siteevent.getX() , siteevent.getY() );
        
        // Delete any circle events
        leafabove.removeCircleEvents(eventqueue);
        
        // Insert the new node
        VLinkedNode newnode = bst.insertNode(leafabove, siteevent);
        VLinkedNode prevnode = newnode.getPrev();
        VLinkedNode nextnode = newnode.getNext();
        
        //set half edge
        dcel.addHalfEdge(prevnode, newnode, nextnode);

        
        // Determine circle events
        if ( prevnode!=null ) prevnode.addCircleEvent(eventqueue);
        if ( nextnode!=null ) nextnode.addCircleEvent(eventqueue);
	}


	
	//debug method
	//print bst
	
	public static void main(String args[]){
		VPoint a = new VPoint(2.0, 4.0);
		VPoint b = new VPoint(6.0, 2.0);
		VPoint c = new VPoint(5.0, 6.0);
		VPoint d = new VPoint(9.0, 5.0);
		VPoint e = new VPoint(10.0, 10.0);
                VPoint f = new VPoint(11.0,2.0);
		
		ArrayList<VPoint> points = new ArrayList<>();
		points.add(a);
		points.add(b);
		points.add(c);
		points.add(d);
		points.add(e);
                points.add(f);
		
		
		DCEL dcel=Voronoi.generateVoronoi(points);
		
		System.out.println("# Vertex is: "+dcel.getVertex().size());
		ArrayList<DCELVertex> dd = dcel.getVertex();
		for(DCELVertex v:dd){
			System.out.println(v.toString()+" halfedge :"+ v.heToString());
		}
		System.out.println("# half edges is: "+dcel.getHalfEdges().size());
		ArrayList<HalfEdge> eds = dcel.getHalfEdges();
		for(HalfEdge he :eds){
			try{
				System.out.println("Edge # "+he.getpID()+" Origin Vertex: "+ he.getOrigin().toString()+" Twin Edge # "+he.getTwin().getpID());
				System.out.println("          next HE: "+he.getNext().getpID()+" Prev HE: "+he.getPrevious().getpID());
			}catch(NullPointerException ex){
				System.out.println("Edge # "+he.getpID()+" Twin Edge # "+he.getTwin().getpID()+" no prev");
			}
		}
		ArrayList<Face> fs = dcel.getFaces();
		System.out.println("#face is: "+fs.size());
		for(Face faccia:fs){
			System.out.println(f.toString());
			HalfEdge es = faccia.getEdge();
			do{
				try{
					System.out.println(es.getpID()+es.getV1().toString() + es.getV2().toString());
				}catch (NullPointerException ed){
					
				}
				es = es.getNext();
			}while(es != faccia.getEdge());
		}
			
		System.out.println("fine");
	}
	
	
}
