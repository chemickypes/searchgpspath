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

//import br.zuq.osm.parser.util.LatLongUtil;

public class VPoint {
	 
    /* ***************************************************** */
    // Variables
    
    public double x;
    public double y;
    public String name;
    public String lat;
    public String lon;
    
    /* ***************************************************** */
    // Constructors
    
    public VPoint() {
        this(-1, -1);
    }
    public VPoint(double inhom, double inhom2) {
        this.x = inhom;
        this.y = inhom2;
    }
    public VPoint(VPoint point) {
        this.x = point.x;
        this.y = point.y;
    }
    
    public double distanceTo(VPoint point) {
        return Math.sqrt((Math.abs(this.x-point.x))*(Math.abs(this.x-point.x)) + 
        		(Math.abs(this.y-point.y))*(Math.abs(this.y-point.y)));
    }
    /*
    public double degDistanceTo(VPoint point){
    	return LatLongUtil.distance(point.x, this.x,point.y,this.y);
    }
    */
    public String toString() {
        return "VPoint (" + x + "," + y + ")";
    }
    
    /* ***************************************************** */
}
