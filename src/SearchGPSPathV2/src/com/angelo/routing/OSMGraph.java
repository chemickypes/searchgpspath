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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

//import com.angelo.searchgpspath.Utility;
import com.lorenzo.pointlocations.Point;


import br.zuq.osm.parser.OSMParser;
import br.zuq.osm.parser.model.OSM;
import br.zuq.osm.parser.model.OSMNode;
import br.zuq.osm.parser.model.Way;
import br.zuq.osm.parser.util.LatLongUtil;
import com.angelo.voronoi.VPoint;
import com.searchgpspath.Utility;

public class OSMGraph {
	
	private HashMap<String,GraphPoint> gnodes;
        private HashMap<String, VPoint> fnodes;
	
	private static int id_system_node = 1000000;

	
	public OSMGraph(){
		gnodes = new HashMap<>();
                fnodes = new HashMap<>();
	}
	
	public void buildNodes(OSM o){
		for(OSMNode n:o.getNodes()){
			GraphPoint gn = new GraphPoint(Double.valueOf(n.lat), Double.valueOf(n.lon), n.id);
			gnodes.put(n.id, gn); //this operation O(n)
                        double[] xyz = LatLongUtil.flatCoordinates(gn.x, gn.y);
                        VPoint fn = new VPoint(xyz[0], xyz[1]);
                        fn.name = gn.nome;
                        fnodes.put(n.id, fn);
		}
	}
	
	public void buildGraph(OSM o){ 
		buildNodes(o);		
		for(Way w: o.getWays()){
			
			for(int i = 0; i<w.nodes.size();i++){
				try{
					GraphPoint gn = gnodes.get(w.nodes.get(i).id);
					if((i-1)>=0) gn.addPointEdge(gnodes.get(w.nodes.get(i-1).id), w.id);
					if((i+1)<w.nodes.size())  gn.addPointEdge(gnodes.get(w.nodes.get(i+1).id), w.id);
				}catch(NullPointerException ex){
					
				}
			}
		}
		
	}
	
	//this method inserts fake nodes if distance between two nodes are greater than distance
	//this method mast be called before than buildGraph(OSM o) 
	public void insertFakeNodes(OSM o, float distance_m){
		ArrayList<OSMNode> newnodes;
		for(Way w:o.getWays()){
			newnodes = new ArrayList<>();
			newnodes.add(w.nodes.get(0));
			for(int i=0;i<(w.nodes.size()-1);i++){
				try{
					OSMNode n1 = w.nodes.get(i);
					OSMNode n2 = w.nodes.get(i+1);
					if(n1.distance(n2)>distance_m){
						double lat1 = Double.valueOf(n1.lat);
						double lon1 = Double.valueOf(n1.lon);
						double lat2 = Double.valueOf(n2.lat);
						double lon2 = Double.valueOf(n2.lon);
						for(Point p: Utility.findAllPoints(lat1, lon1, lat2, lon2,(int) distance_m)){
							OSMNode n = new OSMNode("system"+id_system_node, null, null, "1", null, 
									"system", "01", ""+p.x, ""+p.y, null);
							id_system_node++;
							newnodes.add(n);
							o.getNodes().add(n);
						}
					}
					newnodes.add(n2);
				}catch(NullPointerException ex){
					//do nothing
				}
			}
			w.nodes = newnodes;
			
		}
	}				
	
	
	public HashMap<String, GraphPoint> getGNodes(){
		return this.gnodes;
	}

    public HashMap<String, VPoint> getFnodes() {
        return fnodes;
    }

    public void setFnodes(HashMap<String, VPoint> fnodes) {
        this.fnodes = fnodes;
    }
	
        
	
	
	
	public static void main(String[] args) {
		try {
			OSM o =  OSMParser.parse("/Users/martinaspignoli/Documents/Lorenzo/TESI/NuoviEsperimenti/OSM/villa_ada.osm.xml");
			OSMGraph og = new OSMGraph();
			//og.insertFakeNodes(o, 30);
			og.buildGraph(o);
			System.out.println("OSM node: "+o.getNodes().size());
			System.out.println("osegraph node: " + og.getGNodes().size());
			
//			Iterator it = og.getNodes().iterator();
//			OSMGraphNode n = (OSMGraphNode) it.next();
//			System.out.println("node: "+n.id );
//			for(int i = 0; i<5;i++){
//				
//				n =(OSMGraphNode) n.getNeig().get(0).nnear;
//				System.out.println("node: "+n.id + " way: "+n.getNeig().get(0).way + 
//						" distance: "+ n.getNeig().get(0).distance );
//			}
//			for(OSMGraphNode n:og.nodes){
//				if(n.id.startsWith("sy")) System.out.println(n.id);
//			}
//			ArrayList<OSMGraphNode> path = og.getDijkstraPath("869917901", "869917932");
//			for(OSMGraphNode n:path){
//				System.out.println("node: "+n.id);
//			}
//			
//			System.out.println("1: "+path.get(0).id + " 2:"+path.get(2).id
//					+" distance:"+ path.get(0).distance(path.get(2)));
//			
//			System.out.println("1: "+path.get(0).id + " 2:"+path.get(1).id
//					+" distance:"+ path.get(0).distance(path.get(1)));
//			
//			System.out.println("1: "+path.get(1).id + " 2:"+path.get(2).id
//					+" distance:"+ path.get(1).distance(path.get(2)));
			
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
