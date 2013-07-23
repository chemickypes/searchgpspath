package com.lorenzo.pointlocations;


public class Point {
    
    public double x;
    public double y;
    public String nome;

    public Point(){
    }

    public Point(double i, double j, String n)
    {
        x = i;
        y = j;
        nome = n;
    }
    
    public double distanceTo(Point point) {
        return Math.sqrt((Math.abs(this.x-point.x))*(Math.abs(this.x-point.x)) + 
        		(Math.abs(this.y-point.y))*(Math.abs(this.y-point.y)));
    }

    public boolean isRightOf(Point point)
    {
        return x > point.x; // x è a dx di point
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void setX(double i)
    {
        x = i;
    }

    public void setY(double i)
    {
        y = i;
    }

    public String getNome()
    {
        return nome;
    }

}

