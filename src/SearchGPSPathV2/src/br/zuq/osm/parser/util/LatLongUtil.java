package br.zuq.osm.parser.util;

import com.lorenzo.pointlocations.Point;


public class LatLongUtil {
	
	/**
     * Constant: raggio equatoriale per WGS84 Ellipsoid
     */
    public static final double WGS_A = 6378137.0;

    /**
     * Constant: raggio porlare WGS84 Ellipsoid
     */
    public static final double WGS_B = 6356752.3142;

    /**
     * Constant: eccentricità per WGS84 Ellipsoid
     */
    public static final double WGS_E = 0.0818191908426;
    
    /**
     * Constant: Flatting des WGS84 Ellipsoid
     */
    public static final double WGS_F = (WGS_A - WGS_B) / WGS_A; //1/298.257223563

    

    public static double distance(double lat1, double lon1,
            double lat2, double lon2) {

        Double theta = lon1 - lon2;
        Double dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2)))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta)));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        
        dist = dist * 60 * 1.1515;

        dist = dist * 1.609344 * 1000;

        return dist;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @SuppressWarnings("unused")
	private static double fixAngle(double angle) {
        while (angle < -180) {
            angle += 360;
        }

        while (angle > 180) {
            angle -= 360;
        }

        return angle;
    }
    
    /*
     * https://josm.openstreetmap.de/browser/josm/trunk/src/org/openstreetmap/josm/data/projection/Ellipsoid.java?rev=4382
     */
    public static double[] flatCoordinates(double lat,double lon){
    	double phi = Math.toRadians(lat);
    	double lambda = Math.toRadians(lon);
    	double e2 = Math.pow(WGS_E,2);
    	double Rn = WGS_A / Math.sqrt(1 - e2 * Math.pow(Math.sin(phi), 2));
    	double[] XYZ = new double[3];
    	XYZ[0] = Rn * Math.cos(phi) * Math.cos(lambda);
    	XYZ[1] = Rn * Math.cos(phi) * Math.sin(lambda);
    	XYZ[2] = Rn * (1 - e2) * Math.sin(phi);
    		
    	return XYZ; 	
    }
    
    /*
     * 
     */
    
    static public Point cart2LatLon(double[] XYZ) {
       return cart2LatLon(XYZ, WGS_E);
    }
    static public Point cart2LatLon(double[] XYZ, double epsilon) {
    	double norm = Math.sqrt(XYZ[0] * XYZ[0] + XYZ[1] * XYZ[1]);
        double lg = 2.0 * Math.atan(XYZ[1] / (XYZ[0] + norm));
        double e2 = Math.pow(WGS_E,2);
        double lt = Math.atan(XYZ[2] / (norm * (1.0 - (WGS_A * e2 / Math.sqrt(XYZ[0] * XYZ[0] + XYZ[1] * XYZ[1] + XYZ[2] * XYZ[2])))));
        double delta = 1.0;
        while (delta > epsilon) {
            double s2 = Math.sin(lt);
            s2 *= s2;
            double l = Math.atan((XYZ[2] / norm)
                    / (1.0 - (WGS_A * e2 * Math.cos(lt) / (norm * Math.sqrt(1.0 - e2 * s2)))));
            delta = Math.abs(l - lt);
            lt = l;
        }
        return new Point(Math.toDegrees(lt), Math.toDegrees(lg),"kk");
    }		        
    
//    public static void main(String args[]){
//    	double[] dd = LatLongUtil.fatCoordinates(41.813467,12.941764);
//    	System.out.println(dd[0]+","+dd[1]);
//    	
//    }
}
