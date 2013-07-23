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

package com.searchgpspath;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.lorenzo.pointlocations.Point;

import br.zuq.osm.parser.model.OSM;
import br.zuq.osm.parser.model.OSMNode;
import br.zuq.osm.parser.model.Way;
import br.zuq.osm.parser.util.LatLongUtil;

public class Utility {
	

	static public Point getMidPoint(OSMNode n1,OSMNode n2){
		double lat1 = Double.valueOf(n1.lat);
		double lon1 = Double.valueOf(n1.lon);
		double lat2 = Double.valueOf(n2.lat);
		double lon2 = Double.valueOf(n2.lon);
		 double dLon = Math.toRadians(lon2 - lon1);

		 //convert to radians
		 lat1 = Math.toRadians(lat1);
		 lat2 = Math.toRadians(lat2);
		 lon1 = Math.toRadians(lon1);

		 double Bx = Math.cos(lat2) * Math.cos(dLon);
		 double By = Math.cos(lat2) * Math.sin(dLon);
		 double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
		 double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);
		 
		 double lat = Math.toDegrees(lat3);
		 double lon = Math.toDegrees(lon3);
		 
		 return new Point(lat,lon,"midpoint");
	}
	
	/*
	 * method to return all points between two point (geographic)
	 */
	public static List<Point> findAllPoints(double lat1,double lon1,double lat2,double lon2,int minDistance){
		Point p1;
		Point p2;
		p1 = new Point(lat1,lon1,"s");
		p2 = new Point(lat2, lon2, "e");
		return FindAllPoints(p1,p2,(int)minDistance);
	}
	
	public static List<Point> FindAllPoints(Point start, Point end, double minDistance)
	{
	    double dx = end.x - start.x;
	    double dy = end.y - start.y;

	    int numPoints = (int) Math.floor(LatLongUtil.distance(start.x, start.y, end.x, end.y)/(int)minDistance)-1;

	    List<Point> result = new ArrayList<>();

	    double stepx = dx / numPoints;
	    double stepy = dy / numPoints;
	    double px = start.x + stepx;
	    double py = start.y + stepy;
	    for (int ix = 0; ix < numPoints; ix++)
	    {
	        result.add(new Point(px, py,"n"));
	        px += stepx;
	        py += stepy;
	    }

	    return result;
	}

}
