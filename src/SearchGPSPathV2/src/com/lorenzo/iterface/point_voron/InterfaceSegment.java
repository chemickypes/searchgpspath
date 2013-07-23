package com.lorenzo.iterface.point_voron;

import com.lorenzo.pointlocations.Point;
import com.lorenzo.pointlocations.Segment;


public class InterfaceSegment extends Segment {
    public Point sito1;
	public Point sito2;

	public InterfaceSegment(Point p1, Point p2, String n) {
		super(p1, p2, n);
		setSito1(null);
		setSito2(null);
	}
	
	public InterfaceSegment(Point p1, Point p2,Point sito1,Point sito2,String n){
		super(p1,p2,n);
		setSito1(sito1);
		setSito2(sito2);
	}

	public Point getSito1() {
		return sito1;
	}

	public void setSito1(Point sito1) {
		this.sito1 = sito1;
	}

	public Point getSito2() {
		return sito2;
	}

	public void setSito2(Point sito2) {
		this.sito2 = sito2;
	}
}
