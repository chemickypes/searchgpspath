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

import com.angelo.datastructures.eventqueue.VSiteEvent;
import com.angelo.voronoi.VPoint;
import com.angelo.voronoi.VoronoiShared;

public class HalfEdge {
	//needed variables
	private DCELVertex origin;
	private HalfEdge twin;
	private Face IncidentFace;
	private HalfEdge next;
	private HalfEdge previous;
	//unique variables
	private static int sId = 1000;
	private int pID;
	//Variables to know bisector: two site divided by this halfedge
	private VPoint v1;
	private VPoint v2;
	private VPoint midpoint;
	private double m; //slope
	private double b; //variations of line
	private VPoint direction;
	
	public int extern=1;
	
	
	public HalfEdge(){
		pID = sId;
		sId++;
		
	}
	
	public DCELVertex getOrigin() {
		return origin;
	}
	public void setOrigin(DCELVertex origin) {
		this.origin = origin;
	}
	public HalfEdge getTwin() {
		return twin;
	}
	public void setTwin(HalfEdge twin) {
		this.twin = twin;
	}
	public Face getIncidentFace() {
		return IncidentFace;
	}
	public void setIncidentFace(Face incidentFace) {
		IncidentFace = incidentFace;
	}
	public HalfEdge getNext() {
		return next;
	}
	public void setNext(HalfEdge next) {
		this.next = next;
	}
	public HalfEdge getPrevious() {
		return previous; 
	}
	public void setPrevious(HalfEdge previous) {
		this.previous = previous;
	}

	public static int getsId() {
		return sId;
	}

	public static void setsId(int sId) {
		HalfEdge.sId = sId;
	}

	public int getpID() {
		return pID;
	}

	public void setpID(int pID) {
		this.pID = pID;
	}

	////////////////////////////////////////////////////////////////////////////////////
	/*
	 * this method section is used to calculate
	 * bisector between two site
	 * and intersection between bisector and boundary box
	 */
	
	public VPoint getV1() {
		return v1;
	}

	public void setV1(VPoint v1) {
		this.v1 = v1;
	}

	public VPoint getV2() {
		return v2;
	}

	public void setV2(VPoint v2) {
		this.v2 = v2;
	}
	
	public VPoint getMidPoint(){
		return midpoint;
	}
	
	public double getSlope(){
		return m;
	}
	
	public double getVariationsB(){
		return b;
	}
	
	//set v1 and v2 and calculate slope and variations of line
	public void setV1V2(VPoint v1, VPoint v2){
		setV1(v1);
		setV2(v2);
		
		//we calculate slope of perpendicular line 
		double diffx = v1.x-v2.x;
		double diffy = v2.y-v1.y;
		this.m = diffx/diffy;
		
		//we calculate midpoint
		double mx = (v2.x+v1.x)/2;
		double my= (v2.y+v1.y)/2;
		this.midpoint = new VPoint(mx, my);
		
		
		this.b= midpoint.y - (m*midpoint.x);
	}
	
	//calculate the intersection between bisector and y-axis
	public double intersectionY(double y){
		double yb = y-this.b;		
		return yb/this.m;
	}
	
	// calculate the intersection between bisector and X-axis
	public double intersctionX(double x){
		return (this.m*x)+this.b;
	}

	public VPoint getDirection() {
		return direction;
	}

	public void setDirection(VPoint direction) {
		this.direction = direction;
		
	}
	
	public int getIntDirection(VPoint e){
		VPoint o = (getOrigin()!=null)?getOrigin(): twin.getOrigin();
		try{
			if(o.x>=e.x){
				return (o.y>=e.y)?0:1;
			}else{
				return (o.y>=e.y)?3:2;
			}
		}catch(NullPointerException ex){
			return -1;
		}
	}
	
	public VPoint getOpMidPoint(){
		VPoint o = (getOrigin()!=null)?getOrigin(): twin.getOrigin();
		VPoint me = getMidPoint(); 
		return new VPoint((2*o.x)-me.x, (2*o.y)-me.y);
		
	}
	
	
	
	public VPoint getIntersectionSitePoint(double sweepline){
		
		VSiteEvent vv1 = new VSiteEvent(v1);
		VSiteEvent vv2 = new VSiteEvent(v2);
		// Calculate a, b and c of the parabola
        vv1.calcParabolaConstants(sweepline);
        vv2.calcParabolaConstants(sweepline);
        
        // Determine where two parabola meet
        double intersects[] = VoronoiShared.solveQuadratic((vv2.a-vv1.a),
        		(vv2.b-vv1.b), (vv2.c-vv1.c));
//        double u = (intersects.length>1)?intersects[1]:intersects[0];
        double u = intersects[0];
        VPoint d = new VPoint( (int) u , sweepline + vv1.getYValueOfParabola(u));
       // System.out.println(d.toString());
        setDirection(d );
        return this.direction;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////

	

}
