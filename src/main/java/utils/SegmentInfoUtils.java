package utils;

import javastrava.api.v3.auth.AuthorisationService;
import javastrava.api.v3.auth.impl.retrofit.AuthorisationServiceImpl;
import javastrava.api.v3.auth.model.Token;
import javastrava.api.v3.model.*;
import javastrava.api.v3.model.reference.StravaStreamType;
import javastrava.api.v3.service.Strava;
import model.LatLng;
import model.SegmentSummaryData;

import java.util.ArrayList;
import java.util.List;

public class SegmentInfoUtils {

    private Strava strava;

    public void setupSession() {

        Integer application_client_id = 20235;
        String client_secret = "8730e64ad94d2df3a1efda3c5391696ec6549e1d";
        String code = "e3c622c3a353b281fc28eeb37c57553cec29a253";
//        int athleteId = 20860518;

        AuthorisationService service = new AuthorisationServiceImpl();
        Token token = service.tokenExchange(application_client_id, client_secret, code);
        strava = new Strava(token);
    }

    public StravaSegment getSegment(int segmentId) {

        StravaSegment segment = strava.getSegment(segmentId);

        return segment;
    }

    public List<StravaStream> getSegmentStreams(int segmentId) {

        List<StravaStream> streams = strava.getSegmentStreams(segmentId);

        return streams;
    }

    public SegmentSummaryData getSegmentSummaryValues(int segmentId) {

        StravaSegment segment = getSegment(segmentId);
        List<StravaStream> segmentStreams = getSegmentStreams(segmentId);
        StravaStream altitudeStream = getSpecifiedStream(StravaStreamType.ALTITUDE, segmentStreams);
        StravaStream distanceStream = getSpecifiedStream(StravaStreamType.DISTANCE, segmentStreams);

        SegmentSummaryData summaryValues = new SegmentSummaryData();
        summaryValues.description = filterChars(segment.getName());
        summaryValues.name = filterChars(segment.getName());
        summaryValues.series = "100 climbs";
        segment.getAverageGrade();
        segment.getDistance();
        segment.getClimbCategory();
        segment.getTotalElevationGain();
        segment.getMaximumGrade();

        summaryValues.altitudeValues = altitudeStream.getData();
        summaryValues.distanceValues = distanceStream.getData();

        StravaMap map = segment.getMap();
        String polyline = map.getPolyline();
        List<LatLng> latLngs = PolyUtil.decode(polyline);
        summaryValues.lineCoordinates = latLngs;
        summaryValues.startCoordinate = latLngs.get(0);

        return summaryValues;
    }

    private StravaStream getSpecifiedStream(StravaStreamType type, List<StravaStream> segmentStreams) {

        for (StravaStream segmentStream : segmentStreams) {
            if(segmentStream.getType() == type) {
                return segmentStream;
            }
        }

        throw new RuntimeException("Cant find stream type " + type);
    }

    private String filterChars(String input) {
        return input.replaceAll("&", "and");
    }

    public List<StravaSegment> getSegments(List<Integer> segmentIds) {

        List<StravaSegment> segments = new ArrayList<>();

        for (Integer segmentId : segmentIds) {
            segments.add(getSegment(segmentId));
        }

        return segments;
    }

/*
    public List<SegmentSummaryData> getSegmentSummaryValues(List<StravaSegment> segments) {

        List<SegmentSummaryData> results = new ArrayList<>();

        for (StravaSegment segment : segments) {
            results.add(getSegmentSummaryValues(segment));
        }

        return results;
    }
*/

    public List<SegmentSummaryData> getSegmentSummaryValues(List<Integer> segmentIds) {

        List<SegmentSummaryData> results = new ArrayList<>();

        for (Integer segmentId : segmentIds) {
            SegmentSummaryData segmentValues = getSegmentSummaryValues(segmentId);
            results.add(segmentValues);
        }

        return results;
    }

    public static List<Float> stringsToFloats(String tokenString) {

        List<Float> results = new ArrayList<>();

        String[] tokens = tokenString.split(";");

        for (String token : tokens) {
            results.add(Float.parseFloat(token));
        }

        return results;
    }
}
