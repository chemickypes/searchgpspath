package com.lorenzo.pointlocations;


public class Trapezoid {
    
    public Trapezoid next;
    public Trapezoid up;
    public Trapezoid down;
    public Trapezoid end;
    public Segment top;
    public Segment bottom;
    public Point left;
    public Point right;
    public Node node;
    public Trapezoid lLeft;
    public Trapezoid uLeft;
    public Trapezoid lRight;
    public Trapezoid uRight;
    public String name;
        
    Trapezoid()
    {
        next = null;
        top = null;
        bottom = null;
        left = null;
        right = null;
    }

    Trapezoid(Segment lsegment, Segment lsegment1, Point point, Point point2)
    {
        top = lsegment;
        bottom = lsegment1;
        left = point;
        right = point2;
    }

    public Segment top()
    {
        return top;
    }

    public Segment bottom()
    {
        return bottom;
    }

    public Point leftp()
    {
        return left;
    }

    public Point rightp()
    {
        return right;
    }

    public void addTop(Segment lsegment)
    {
        top = lsegment;
    }

    public void bottom(Segment lsegment)
    {
        bottom = lsegment;
    }

    public void leftp(Point point)
    {
        left = point;
    }

    public void rightp(Point point)
    {
        right = point;
    }

    public double getMiddleX() {
        return (left.x+right.x)/2;
    }
    public double getMiddleY() {
        return (left.y+right.y)/2;
    }

}

