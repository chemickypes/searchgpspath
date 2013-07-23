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

package com.angelo.routing;

import java.util.ArrayList;

import br.zuq.osm.parser.util.LatLongUtil;

import com.lorenzo.pointlocations.Point;

public class GraphPoint extends Point implements Comparable<GraphPoint> {
	public float ddistance = 0.0f; //distance for Dijkstra algorithm
	public boolean bfsvisited = false; //visited tag for bfs
	public String parent =null; //path parent
	
	ArrayList<PointEdge> neig = new ArrayList<>();

	public GraphPoint() {
		
	}

	public GraphPoint(double i, double j, String n) {
		super(i, j, n);
		
	}
	
	public void addPointEdge(PointEdge pe){
		this.neig.add(pe);
	}
	
	public void addPointEdge(GraphPoint p, String way){
		float d = (float) LatLongUtil.distance(super.x, super.y, p.x, p.y);
		neig.add(new PointEdge(p.nome,d,way));
	}
	
	public ArrayList<PointEdge> getNeig(){
		return this.neig;
	}

	@Override
	public int compareTo(GraphPoint arg0) {
		
		return Float.compare(ddistance, arg0.ddistance);
	}

}
