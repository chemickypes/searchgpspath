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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


import com.lorenzo.pointlocations.Point;
	

public class DAO {
	public static final String filepath = "/Users/martinaspignoli/Documents/Lorenzo/TESI/NuoviEsperimenti/";
	
	public static void writeGPXFile(ArrayList<Point> path, String namefile) throws FileNotFoundException, UnsupportedEncodingException{
		Calendar cal = Calendar.getInstance();
		double ele = 0.0;
		File f = new File(filepath+namefile);
		f.delete();
		PrintWriter writer = new PrintWriter(namefile, "UTF-8");
		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<gpx version=\"1.0\" creator=\"SearchGPSPath\">");
		writer.println("<name>Output track</name>");
		writer.println("<trk>");
		writer.println("<trkseg>");
		for(Point n : path){
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			//get current date time with Date()
			
//			System.out.println(cal.getTime());
			cal.add(Calendar.MINUTE, 5);
			
			writer.println("<trkpt lon=\""+n.y+"\" lat=\""+n.x+"\" ><ele>"+ele+"</ele><time>"
			+dateFormat.format(cal.getTime())+"</time></trkpt>");
			ele++;
		}
		
		writer.println("</trkseg>");
		writer.println("</trk>");
		writer.println("</gpx>");
		writer.close();
	}
	


}
