package utils;

import javastrava.api.v3.auth.AuthorisationService;
import javastrava.api.v3.auth.impl.retrofit.AuthorisationServiceImpl;
import javastrava.api.v3.auth.model.Token;
import javastrava.api.v3.model.*;
import javastrava.api.v3.model.reference.StravaStreamType;
import javastrava.api.v3.service.Strava;
import model.LatLng;
import model.SegInfo;
import model.SegmentSummaryData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SegmentSummaryUtils {

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

    public String getSegmentLeaderTime(int segmentId) {

        StravaSegmentLeaderboard leaderboard;

        leaderboard = strava.getSegmentLeaderboard(segmentId);
        List<StravaSegmentLeaderboardEntry> entries = leaderboard.getEntries();

        String timeString = "not set";

        if(!entries.isEmpty()) {
            StravaSegmentLeaderboardEntry fastest = entries.get(0);

            int totalSecs = fastest.getElapsedTime();

            int hours = totalSecs / 3600;
            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;

            timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        return timeString;
    }

    public List<StravaStream> getSegmentStreams(int segmentId) {

        List<StravaStream> streams = strava.getSegmentStreams(segmentId);

        return streams;
    }

    public SegmentSummaryData getSegmentSummaryValues(SegInfo segmentInfo) {

        Integer segmentId = segmentInfo.segmentId;
        StravaSegment segment = getSegment(segmentId);
        String leaderTime = getSegmentLeaderTime(segmentId);
        List<StravaStream> segmentStreams = getSegmentStreams(segmentId);
        StravaStream altitudeStream = getSpecifiedStream(StravaStreamType.ALTITUDE, segmentStreams);
        StravaStream distanceStream = getSpecifiedStream(StravaStreamType.DISTANCE, segmentStreams);

        SegmentSummaryData summaryValues = new SegmentSummaryData();
        summaryValues.city = filterChars(segment.getCity());
        summaryValues.name = filterChars(segment.getName());
        summaryValues.id = segmentId.toString();
        summaryValues.seriesNames = new ArrayList<>(segmentInfo.seriesNames);
        summaryValues.averageGrad = segment.getAverageGrade();
        summaryValues.maxGrad = segment.getMaximumGrade();
        summaryValues.distance = segment.getDistance();
        summaryValues.category = segment.getClimbCategory().getValue().toString();
        summaryValues.elevation = segment.getTotalElevationGain();
        summaryValues.leaderTime = leaderTime;

        summaryValues.altitudeValues = altitudeStream.getData();
        summaryValues.distanceValues = distanceStream.getData();

        StravaMap map = segment.getMap();
        summaryValues.polyline = map.getPolyline();
//        List<LatLng> latLngs = PolyUtil.decode(polyline);
//        summaryValues.lineCoordinates = latLngs;
        StravaMapPoint startLatlng = segment.getStartLatlng();
        summaryValues.startCoordinate = new LatLng(startLatlng.getLatitude(), startLatlng.getLongitude());

        return summaryValues;
    }

    private StravaStream getSpecifiedStream(StravaStreamType type, List<StravaStream> segmentStreams) {

        for (StravaStream segmentStream : segmentStreams) {
            if (segmentStream.getType() == type) {
                return segmentStream;
            }
        }

        throw new RuntimeException("Cant find stream type " + type);
    }

    private String filterChars(String input) {

        String result = "not_set";

        if(input != null){
            result = input.replaceAll("&", "and");
        }

        return result;
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

    public List<SegmentSummaryData> getSegmentSummaryValues(ArrayList<SegInfo> segmentInfos) {

        List<SegmentSummaryData> results = new ArrayList<>();

        for (SegInfo segmentInfo : segmentInfos) {
            SegmentSummaryData segmentValues = getSegmentSummaryValues(segmentInfo);
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

    public String listToString(List<Float> values) {

        StringBuilder builder = new StringBuilder();

        for (Float value : values) {
            builder.append(value);
            builder.append(";");
        }

        return builder.toString();
    }

    public String coordinatesToString(LatLng coordinates) {
        return coordinates.toString();
    }

    public String coordinatesToString(List<LatLng> coordinatesList) {

        StringBuilder builder = new StringBuilder();

        for (LatLng latLng : coordinatesList) {
            builder.append(coordinatesToString(latLng));
            builder.append(";");
        }

        return builder.toString();
    }

    public String seriesNamesToString(List<String> seriesNames) {

        StringBuilder builder = new StringBuilder();

        for (String seriesName : seriesNames) {
            builder.append(seriesName);
            builder.append(";");
        }

        return builder.toString();
    }
}
