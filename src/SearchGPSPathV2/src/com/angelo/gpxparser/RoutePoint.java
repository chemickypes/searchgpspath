package com.angelo.gpxparser;

/**
* A Route Point.
* <p/>A Route point is a point in time and space.
*/
public class RoutePoint extends LocationPoint {
   private long mTime;

   void setTime(long time) {
       mTime = time;
   }
   
   public long getTime() {
       return mTime;
   }
}
