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
import java.util.HashMap;
import java.util.Set;

import com.lorenzo.pointlocations.Point;

import br.zuq.osm.parser.model.Way;

public class SearchPaymentWay {
	HashMap<String,SimplyWay> sw ;
	ArrayList<Point> gps ;

	public SearchPaymentWay(Set<Way> sw, ArrayList<Point> gps) {
		this.sw = createSimplyWays(sw);
		this.gps = gps;
	}

	private static HashMap<String,SimplyWay> createSimplyWays(Set<Way> sw){
		HashMap<String,SimplyWay> nsw = new HashMap<>();
		for(Way w:sw){
			String tollvalue = w.tags.get("toll");
			String feevalue = w.tags.get("fee");
			if((tollvalue != null || feevalue != null) && 
					(!tollvalue.equals("no") || !feevalue.equals("no"))){
				SimplyWay s = new SimplyWay(w.id);
				nsw.put(s.id,s);
			}
		}
		return nsw;
	}
	
	public boolean paymentWay(){
		for(Point p:gps){
			GraphPoint gp = (GraphPoint) p;
			for(PointEdge pe:gp.neig){
				if(sw.get(pe.way)!= null) return true;
			}
		}
		return false;
	}
}
