package com.lorenzo.pointlocations;


public class Segment {
    
    public Point left;
    public Point right;
    public String nome;
    
    public Segment(Point p1, Point p2, String n)
    {
        if(p1.x < p2.x)
        {
            left = p1;
            right = p2;
            nome = n;
            return;
        }
        if(p1.x > p2.x)
        {
            left = p2;
            right = p1;
            nome = n;
            return;
        }
        if(p1.y < p2.y)
        {
            left = p1;
            right = p2;
            nome = n;
            return;
        } else
        {
            left = p2;
            right = p1;
            nome = n;
            return;
        }
    }

    public boolean isAbove(Point point)
    {
        return Computation.isAbove(point, left, right);
        
    }

    public Point getStartingPoint()
    {
        return left;
    }

    public Point getEndingPoint()
    {
        return right;
    }

    public void setStartingPoint(Point point)
    {
        right = point;
    }

    public void setEndingPoint(Point point)
    {
        left = point;
    }

    public String getNome() {
        return nome;
    }

}
