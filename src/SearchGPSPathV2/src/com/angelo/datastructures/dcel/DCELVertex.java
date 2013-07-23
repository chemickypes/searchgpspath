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


import com.angelo.voronoi.VPoint;

public class DCELVertex extends VPoint{
	private ArrayList<HalfEdge> edges;
	public int cornerVertex =-1;
	
	public DCELVertex(double x,double y){
		super(x, y);
		edges = new ArrayList<>();
	}
	
	public DCELVertex(VPoint point){
		super(point);
		edges = new ArrayList<>();
	}
	
	public DCELVertex(){
		super();
		edges = new ArrayList<>();
	}
	
	public HalfEdge getEdge() {
		return edges.get(0);
	}
	public ArrayList<HalfEdge> getEdges() {
		return edges;
	}
	public void setEdges(ArrayList<HalfEdge> edges) {
		this.edges = edges;
	}
	
	public void clearEdges(){
		HalfEdge edge = this.edges.get(0);
		this.edges.clear();
		this.edges.add(edge);
	}
	
	public String heToString(){
		String s="";
		for(HalfEdge he:this.edges){
			s+= he.getpID()+" "; 
		}
		return s;
	}
	
	public String toString(){
		String toreturn = super.toString();
		return toreturn+"("+cornerVertex+")";
	}
	

}
