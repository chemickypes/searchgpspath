package com.angelo.gpxparser;
/**
 * A GPS/KML way point.
 * <p/>A waypoint is a user specified location, with a name and an optional description.
 */
public final class WayPoint extends LocationPoint {
    private String mName;
    private String mDescription;

    void setName(String name) {
        mName = name;
    }
    
    public String getName() {
        return mName;
    }

    void setDescription(String description) {
        mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }
}