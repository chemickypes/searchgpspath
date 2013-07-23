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

import com.angelo.datastructures.eventqueue.VCircleEvent;
import com.angelo.datastructures.eventqueue.VSiteEvent;

public class VoronoiShared {
	// note: the order is very important
    //  make sure index 0 remains (-b + bsqrdplus4ac), and 1 (-b - bsqrdplus4ac)
    public static double[] solveQuadratic(double a, double b, double c) {
        if (a == 0.0) {
            if (b != 0.0) {
                double answers[] = new double[1];
                answers[0] = -1 * c / b;
                return answers;
            } else {
                throw new RuntimeException("Only given a non-zero c value");
            }
        } else {
            double answers[] = new double[2];
            double bsqrdplus4ac = Math.sqrt(b * b - 4 * a * c);
            answers[0] = (-b + bsqrdplus4ac) / ( 2 * a );
            answers[1] = (-b - bsqrdplus4ac) / ( 2 * a );
            return answers;
        }
    }
    
	public static VCircleEvent calculateCenter(VSiteEvent u, VSiteEvent v, VSiteEvent w) {
        double a  = (u.getX() - v.getX())*(v.getY() - w.getY()) - (u.getY() - v.getY())*(v.getX() - w.getX());
        if ( a > 0 ) {
            double b1 = (u.getX() - v.getX())*(u.getX() + v.getX()) + (u.getY() - v.getY())*(u.getY() + v.getY());
            double b2 = (v.getX() - w.getX())*(v.getX() + w.getX()) + (v.getY() - w.getY())*(v.getY() + w.getY());
            
            VCircleEvent centerpoint = new VCircleEvent();
            double x             = ( b1*(v.getY() - w.getY()) - b2*(u.getY() - v.getY()) ) / ( 2.0 * a );
            double center_y      = ( b2*(u.getX() - v.getX()) - b1*(v.getX() - w.getX()) ) / ( 2.0 * a );
            centerpoint.setX(       (int) x  );
            centerpoint.setY(       (int)( center_y + Math.sqrt( (x-u.getX())*(x-u.getX()) + (center_y-u.getY())*(center_y-u.getY()) ) ) );
            centerpoint.setCenterY(  center_y );
            centerpoint.setCenter_x(x);
            //centerpoint.setCenterY( (int) center_y );
            return centerpoint;
        } else {
            return null;
        }
    }
	
	public static double[] marginBoundingBox(ArrayList<VPoint> points){
		double[] margin = new double[4];
		margin[0] = points.get(0).x;//min x
		margin[1] = points.get(0).x;//max x
		margin[2] = points.get(0).y;//min y
		margin[3] = points.get(0).y;//max y
		for(VPoint p:points){
			//check x
			if(p.x<margin[0]){
				margin[0]=p.x;
			}else if(p.x>margin[1]){
				margin[1] = p.x;
			}
			//check y
			if(p.y<margin[2]){
				margin[2]=p.y;
			}else if(p.y>margin[3]){
				margin[3]=p.y;
			}
			
		}
		margin[0] = (int) margin[0] -10;
		margin[1] = (int) margin[1] +10;
		margin[2] = (int) margin[2] -10;
		margin[3] = (int) margin[3] +10;
//		margin[0] -= 10;
//		margin[1] += 10;
//		margin[2] -= 10;
//		margin[3] += 10;
		return margin;
	}
}
