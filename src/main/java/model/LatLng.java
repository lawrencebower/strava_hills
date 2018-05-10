package model;

public class LatLng {
    public double latitude;
    public double longitude;

    public LatLng(double lati, double longi) {
        latitude = lati;
        longitude = longi;
    }

    @Override
    public String toString() {
        return latitude + "," + longitude;
    }
}
