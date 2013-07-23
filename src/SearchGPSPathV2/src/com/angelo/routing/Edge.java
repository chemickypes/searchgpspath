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

import br.zuq.osm.parser.model.OSMNode;

//this Class represents the edge between two nodes
public class Edge {
	public OSMNode nnear;
	public float distance; //distance between origin node and nnear 
	public String way; //way 
	
	public Edge(OSMNode nnear, float distance, String way){
		this.nnear = nnear;
		this.distance = distance;
		this.way = way;
	}
	
	public Edge(){}
	
}
