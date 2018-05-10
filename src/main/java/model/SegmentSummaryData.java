package model;

import java.util.List;

public class SegmentSummaryData {
    public String name;
    public String description;
    public String series;
    public List<LatLng> lineCoordinates;
    public LatLng startCoordinate;
    public List<Float> altitudeValues;
    public List<Float> distanceValues;
}
