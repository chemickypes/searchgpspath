package com.angelo.gpxparser;

/**
 * A Track Point.
 * <p/>A track point is a point in time and space.
 */
public class TrackPoint extends LocationPoint {
    private long mTime;

    void setTime(long time) {
        mTime = time;
    }
    
    public long getTime() {
        return mTime;
    }
}