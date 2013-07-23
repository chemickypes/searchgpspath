package com.angelo.datastructures.eventqueue;

import com.angelo.voronoi.VPoint;
import com.angelo.voronoi.VoronoiShared;

public class VSiteEvent extends VEvent {

	 /* ***************************************************** */
    // Variables
	//elements of parabolas
    private VPoint point;
    
    public double a;
    public double b;
    public double c;
    
    /* ***************************************************** */
    // Constructors
    
    public VSiteEvent(VPoint _point) {
        if ( _point==null ) {
            throw new IllegalArgumentException("Point for siteevent cannot be null");
        }
        this.point = _point;
    }

    /* ***************************************************** */
    // Methods
    
    public void calcParabolaConstants(double sweepline) {
        double yminussweepline = ( point.y - sweepline );
        a = 0.5 / yminussweepline;
        b = -1.0 * point.x / yminussweepline;
        //c = (point.x * point.x) / (2.0 * yminussweepline) + 0.5 * yminussweepline;
        c=((point.x*point.x)+(point.y*point.y)-(sweepline*sweepline))/(2*yminussweepline);
    }
    
    public int getYValueOfParabola(int x) {
        return (int) (( a * x + b ) * x + c);
    }
    
    public int getYValueOfParabola(double x) {
        return (int) (( a * x + b ) * x + c);
    }
    
    /* ***************************************************** */
    // Abstract Methods
    
    @Override
	public double getX() { return point.x; }
    
    @Override
	public double getY() { return point.y; }
    
    public VPoint getPoint() { return point; }
    
    @Override
	public boolean isSiteEvent() { return true; }
    
    @Override
	public boolean isCircleEvent() { return false; }
    
    /* ***************************************************** */
    // To String Method
    
    @Override
	public String toString() {
        return "VSiteEvent (" + point.x + "," + point.y + ")";
    }
    
    /* ***************************************************** */

    public static void main(String[] args){
    	VSiteEvent v =new VSiteEvent(new VPoint(2.0,1.0));
    	v.calcParabolaConstants(5);
    	System.out.println("a:"+v.a+" b:"+v.b+" c:"+v.c);
    	VSiteEvent v1 =new VSiteEvent(new VPoint(1.0,0.99999999));
    	v1.calcParabolaConstants(5);
    	System.out.println("a:"+v1.a+" b:"+v1.b+" c:"+v1.c);
    	// Determine where two parabola meet
        double intersects[] = VoronoiShared.solveQuadratic((v.a-v1.a),
        		(v.b-v1.b), (v.c-v1.c));
        System.out.println("1:"+intersects[0]+" 2:"+intersects[1]);
        VPoint d = new VPoint( (int) intersects[0] , 5 + v.getYValueOfParabola(intersects[0]));
         System.out.println("p1:"+d.toString());
         VPoint d1 = new VPoint( (int) intersects[1] , 5 + v.getYValueOfParabola(intersects[1]));
         System.out.println("p2:"+d1.toString());
    }
}
