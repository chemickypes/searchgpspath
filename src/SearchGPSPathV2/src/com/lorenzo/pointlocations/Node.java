package com.lorenzo.pointlocations;


public class Node {
    
    public String node;
    public Point p;
    public Segment segment;
    public Trapezoid t;
    public Node left;
    public Node right;
    

    Node()
    {
        node = "";
        left = null;
        right = null;
        t = null;
        p = null;
    }
    
    public boolean isLeft(Point point)
    {
        return point.x < p.x; // il point sta a sx di p
    }

    public boolean isAbove(Point point)
    {
        return Computation.isAbove(point, segment.left, segment.right);
    }
}
