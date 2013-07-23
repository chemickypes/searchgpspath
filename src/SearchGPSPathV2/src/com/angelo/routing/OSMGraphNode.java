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
import java.util.Map;

import br.zuq.osm.parser.model.OSMNode;
import br.zuq.osm.parser.util.LatLongUtil;

public class OSMGraphNode extends OSMNode implements Comparable<OSMGraphNode> {
	private ArrayList<Edge> neig = new ArrayList<>();
	
	public float ddistance = 0.0f; //distance for Dijkstra algorithm
	public boolean bfsvisited = false; //visited tag for bfs
	public OSMGraphNode dparent = null; //parent for Dijkstra algorithm 

	public OSMGraphNode(String id, String visible, String timestamp,
			String version, String changeset, String user, String uid,
			String lat, String lon, Map<String, String> tags) {
		super(id, visible, timestamp, version, changeset, user, uid, lat, lon,
				tags);
		
	}
	
	
	
	public void addEdge(Edge e){
		neig.add(e);
	}
	
	public void addEdge(OSMNode n,String way){
		float d = super.distance(n);
		neig.add(new Edge(n,d,way));
	}
	
	
	public ArrayList<Edge> getNeig(){
		return this.neig;
	}



	@Override
	public int compareTo(OSMGraphNode arg0) {
		
		return Float.compare(ddistance, arg0.ddistance);
	}
	

}
