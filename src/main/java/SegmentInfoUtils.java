import javastrava.api.v3.auth.AuthorisationService;
import javastrava.api.v3.auth.impl.retrofit.AuthorisationServiceImpl;
import javastrava.api.v3.auth.model.Token;
import javastrava.api.v3.model.*;
import javastrava.api.v3.service.Strava;

import java.util.ArrayList;
import java.util.List;

public class SegmentInfoUtils {

    private Strava strava;

    public void setupSession() {

        Integer application_client_id = 20235;
        String client_secret = "8730e64ad94d2df3a1efda3c5391696ec6549e1d";
        String code = "e3c622c3a353b281fc28eeb37c57553cec29a253";
        int athleteId = 20860518;

        AuthorisationService service = new AuthorisationServiceImpl();
        Token token = service.tokenExchange(application_client_id, client_secret, code);
        strava = new Strava(token);
    }

    public StravaSegment getSegment(int segmentId) {

        StravaSegment segment = strava.getSegment(segmentId);

        StravaMap map = segment.getMap();
        String polyline = map.getPolyline();

//        System.out.println("polyline = " + polyline);

        return segment;
    }

    public SegmentSummaryValues getSegmentValues(StravaSegment segment) {

        SegmentSummaryValues values = new SegmentSummaryValues();
        values.description = filterChars(segment.getName());
        values.name = filterChars(segment.getName());
        values.series = "100 climbs";
        segment.getAverageGrade();
        segment.getDistance();
        segment.getClimbCategory();
        segment.getTotalElevationGain();
        segment.getMaximumGrade();

        StravaMap map = segment.getMap();
        String polyline = map.getPolyline();
        List<LatLng> latLngs = PolyUtil.decode(polyline);
        values.lineCoordinates = latLngs;
        values.startCoordinate = latLngs.get(0);

        return values;
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

    public List<SegmentSummaryValues> getSegmentValues(List<StravaSegment> segments) {

        List<SegmentSummaryValues> results = new ArrayList<>();

        for (StravaSegment segment : segments) {
            results.add(getSegmentValues(segment));
        }

        return results;
    }
}
