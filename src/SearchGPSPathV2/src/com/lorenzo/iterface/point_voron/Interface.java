package com.lorenzo.iterface.point_voron;

import com.angelo.datastructures.dcel.DCELVertex;
import com.angelo.datastructures.dcel.HalfEdge;
import com.lorenzo.pointlocations.Point;
import com.lorenzo.pointlocations.Segment;
import java.util.ArrayList;


public class Interface {
    public Point[] corners = new Point[4];
    public Segment[] convSegments;
    public Point[] convPoints;
    
    public Interface(ArrayList<HalfEdge> hedges){
    }
    
    public Segment[] covertionSegment(ArrayList<HalfEdge> hedges){
        int curs = 0;
        InterfaceSegment[] seg = new InterfaceSegment[(hedges.size())/2];
        for(HalfEdge h : hedges){
            Point p1 = searchPoint(h.getOrigin().x, h.getOrigin().y);
            Point p2 = searchPoint(h.getTwin().getOrigin().x, h.getTwin().getOrigin().y);
            Point s1 = new Point(h.getV1().x,h.getV1().y, h.getV1().name);
            Point s2 = new Point(h.getV2().x,h.getV2().y, h.getV2().name);
            InterfaceSegment s = new InterfaceSegment(p1, p2, s1, s2, ""+curs);
            boolean flag = false;
            for(int i = 0; i<curs; i++){
                if(seg[i].left.x == s.left.x && seg[i].left.y == s.left.y && seg[i].right.x == s.right.x && seg[i].right.y == s.right.y){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                seg[curs] = s;
            curs++;
            }
        }
        convSegments = seg;
        return seg;
    }
    
    public Point[] convPoint(ArrayList<DCELVertex> ver){
        int lett = 97; int curs = 0;
        Point[] po = new Point[ver.size()];
        for(DCELVertex v : ver){
            char n = (char) lett++;
            Point p = new Point(v.x, v.y, ""+n);
            if(v.cornerVertex != -1){
                corners[v.cornerVertex] = p;
            }
            po[curs++] = p;
        }
        convPoints = po;
        return po;
    }
    
    public Point searchPoint(double x, double y){
        for(Point p : convPoints){
            if(p.x == x && p.y == y)
                return p;
        }
        return null;
    }

    public Point[] getCorners() {
        return corners;
    }

    public void setCorners(Point[] corners) {
        this.corners = corners;
    }

    public Segment[] getConvSegments() {
        return convSegments;
    }

    public void setConvSegments(Segment[] convSegments) {
        this.convSegments = convSegments;
    }

    public void setNewCorners() {
        corners[0] = new Point((corners[0].x)- 5, (corners[0].y)- 5, "s");
        corners[1] = new Point((corners[1].x)- 5, (corners[1].y)+ 5, "s");
        corners[2] = new Point((corners[2].x)+ 5, (corners[2].y)+ 5, "s");
        corners[3] = new Point((corners[3].x)+ 5, (corners[3].y)- 5, "s");
    }
    
    
}
