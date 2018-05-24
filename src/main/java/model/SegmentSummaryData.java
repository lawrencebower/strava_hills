package model;

import javastrava.api.v3.model.reference.StravaClimbCategory;

import java.util.List;
import java.util.Set;

public class SegmentSummaryData {
    public String id;
    public String name;
    public String city;
    public List<String> seriesNames;
    public String polyline;
    public List<LatLng> lineCoordinates;
    public LatLng startCoordinate;
    public List<Float> altitudeValues;
    public List<Float> distanceValues;
    public Float averageGrad;
    public Float maxGrad;
    public Float distance;
    public String category;
    public Float elevation;
    public String leaderTime;
    public String difficulty = "NOT_SET";
    public Integer effortCount;

    public void checkAltAndDistLengths() {
        if(altitudeValues.size() != distanceValues.size()) {
            System.out.println("Different lengths - " + id);
        }
    }
}
