package model;

import java.util.List;

public class SegmentSummaryData {
    public String id;
    public String name;
    public String city;
    public List<Series> seriesNames;
    public String polyline;
    public List<LatLng> lineCoordinates;
    public LatLng startCoordinate;
    public List<Float> altitudeValues;
    public List<Float> distanceValues;
    public Float averageGrad;
    public Float distance;
    public String category;
    public Float elevation;
    public String leaderTime;
    public Integer effortCount;

    public void checkAltAndDistLengths() {
        if (altitudeValues.size() != distanceValues.size()) {
            System.out.print(String.format("%s\t%s\t%s\n", name, id, seriesNames));
        }
    }

}
