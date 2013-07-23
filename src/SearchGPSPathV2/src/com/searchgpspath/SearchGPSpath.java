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

import br.zuq.osm.parser.OSMParser;
import br.zuq.osm.parser.model.OSM;
import br.zuq.osm.parser.util.LatLongUtil;
import com.angelo.datastructures.dcel.DCEL;
import com.angelo.gpxparser.GPXParser;
import com.angelo.gpxparser.TrackPoint;
import com.angelo.routing.GraphPoint;
import com.angelo.routing.OSMGraph;
import com.angelo.routing.SearchPaymentWay;
import com.angelo.voronoi.VPoint;
import com.angelo.voronoi.Voronoi;
import com.lorenzo.iterface.point_voron.Interface;
import com.lorenzo.iterface.point_voron.InterfaceSegment;
import com.lorenzo.pointlocations.Point;
import com.lorenzo.pointlocations.Trapezoid;
import com.lorenzo.pointlocations.TrapezoidalMaps;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public class SearchGPSpath {
    
    public static HashMap<String, Point> track = new HashMap<>();
    public static HashMap<String, Point> ftrack = new HashMap<>();
    public static final String path = "/home/angelo/Documenti/NuoviEsperimenti/";
    
       
       private static void getTrack(String filename,int traccia){
		GPXParser gpxp = new GPXParser(filename);
		gpxp.parse();
                
		ArrayList<GPXParser.TrackSegment> tracks = (ArrayList<GPXParser.TrackSegment>) gpxp.getTracks();
		int i = 0;
		for(TrackPoint tp:tracks.get(traccia).getPoints()){
			//the point have geographic coordinates to use you must change them
                        Point p = new Point(tp.getLatitude(), tp.getLongitude(), ""+i);
                        track.put(p.nome,p);
                        double[] xyz = LatLongUtil.flatCoordinates(p.x, p.y);
                        Point fp = new Point(xyz[0], xyz[1], p.nome);
                        ftrack.put(p.nome, fp);
                        i++;
                }
	}
       
       
       private static String searchSite(Trapezoid T, Point mioP) {
                InterfaceSegment s1 = (InterfaceSegment) T.bottom;
                InterfaceSegment s2 = (InterfaceSegment) T.top;
                Point sf1;
                Point sf2;
                if(mioP.distanceTo(s1.getSito1()) > mioP.distanceTo(s1.getSito2())){
                    sf1 = s1.sito2;
                }else{
                    sf1 = s1.sito1;
                }
                if(mioP.distanceTo(s2.getSito1()) > mioP.distanceTo(s2.getSito2())){
                    sf2 = s2.sito2;
                }else{
                    sf2 = s2.sito1;
                }
                if(mioP.distanceTo(sf1) > mioP.distanceTo(sf2)){
                    System.out.println("sito = (" + sf2.x + ", " + sf2.y +")");
                    return sf2.nome;
                }else{
                    System.out.println("sito = (" + sf1.x + ", " + sf1.y +")");
                    return sf1.nome;
                }
    }
    
    
       private static void execute(String map, String gpx,String out, int distance){
           OSM o;
           try {
               o = OSMParser.parse( map);
               OSMGraph og = new OSMGraph();
               og.insertFakeNodes(o,distance);
               og.buildGraph(o);
               ArrayList<VPoint> points = new ArrayList<>(og.getFnodes().values());
               DCEL dcel=Voronoi.generateVoronoi(points);
                   
                   Interface interfaccia = new Interface(dcel.getHalfEdges());
                   interfaccia.convPoint(dcel.getVertex());
                   interfaccia.covertionSegment(dcel.getHalfEdges());
                   interfaccia.setNewCorners();
                   TrapezoidalMaps mappa = new TrapezoidalMaps(interfaccia.convSegments, interfaccia.corners);
                   
                   SearchGPSpath.getTrack(gpx, 0);
                   System.out.println(SearchGPSpath.ftrack.size());
                   ArrayList<Point> listapp = new ArrayList<>();
                   for(int i=0;i<SearchGPSpath.ftrack.size();i++){
                           Point fp = SearchGPSpath.ftrack.get(""+i);
                       Trapezoid T = mappa.findPoint(fp);
                       String idp = searchSite(T, fp);
                       GraphPoint mp = og.getGNodes().get(idp);
                       listapp.add(mp);
                   }
                   DAO.writeGPXFile(listapp, out);
                   
                   SearchPaymentWay pw = new SearchPaymentWay(o.getWays(), listapp);
                   String payresult = (pw.paymentWay())?"ci sono":"non ci sono";
                   System.out.println(payresult+" strade a pagamento");
               
           } catch (ParserConfigurationException ex) {
               Logger.getLogger(SearchGPSpath.class.getName()).log(Level.SEVERE, null, ex);
           } catch (SAXException ex) {
               Logger.getLogger(SearchGPSpath.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException ex) {
               Logger.getLogger(SearchGPSpath.class.getName()).log(Level.SEVERE, null, ex);
           } catch(Exception ex){
               Logger.getLogger(SearchGPSpath.class.getName()).log(Level.SEVERE, null, ex);
               System.out.println("Errore nell'esecuzione");
           }
       }
       
       
       public static void main(String[] args){
           
           int distance;
           try{
             distance= (!args[2].equals("0"))?Integer.valueOf(args[2]):20000;   
           }catch(ArrayIndexOutOfBoundsException e){
                   distance = 20000;
           }
           if(args[0].equals("-h") || args[0].equals("--help")  ){
                   System.out.println("Execute: java -jar searchgpspath.jar [map] [track] [distance or null]");
           }else{
                   System.out.println(args.length);
                   for (String s: args) {
                   System.out.println(s);
               }
                   String[] output = args[1].split("/");
                   output[output.length-1] = "";
                   String out="";
                   for(int i=0; i< output.length-1;i++ ){
                           out += output[i]+"/";
                   }
                   out+="jarresult.gpx";
                   System.out.println("Generating "+ out+" ...");
                   execute(args[0], args[1], out, distance);
           }
                   
       }


 
}
