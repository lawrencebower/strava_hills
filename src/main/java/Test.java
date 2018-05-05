import javastrava.api.v3.auth.AuthorisationService;
import javastrava.api.v3.auth.impl.retrofit.AuthorisationServiceImpl;
import javastrava.api.v3.auth.model.Token;
import javastrava.api.v3.model.*;
import javastrava.api.v3.service.ActivityService;
import javastrava.api.v3.service.Strava;
import javastrava.api.v3.service.StreamService;

import java.util.List;

public class Test {

    public static void main(String[] args) {
        System.out.println("Hello");

        Integer application_client_id = 20235;
        String client_secret = "8730e64ad94d2df3a1efda3c5391696ec6549e1d";
        String code = "e3c622c3a353b281fc28eeb37c57553cec29a253";
        int athleteId = 20860518;

        AuthorisationService service = new AuthorisationServiceImpl();
        Token token = service.tokenExchange(application_client_id, client_secret, code);
        Strava strava = new Strava(token);
//        StravaAthlete athlete = strava.getAthlete(athleteId);
//        StreamService streamService = javastrava.api.v3.service.impl.StreamServiceImpl.instance(token);

//        StravaSegment segment = strava.getSegment(637403);
//        StravaSegmentLeaderboard allSegmentLeaderboard = strava.getSegmentLeaderboard(1095648);
//        StravaActivity activity = strava.getActivity(1148302107, true);

        new Test().hello(strava);

//        ActivityServiceImpl;

//        AuthorisationAPI auth = API.authorisationInstance();
//        TokenResponse response = auth.tokenExchange(application_client_id, client_secret, code);
//        Token token = new Token(response);
//
//        API api = new API(token);

//        StravaMap map = segment.getMap();
//        String polyline = map.getPolyline();

//        System.out.println("polyline = " + polyline);
//        PolyUtil.encode()


        int i = 0;
    }

    public void hello(Strava strava) {
        java.util.Arrays.asList("hello");

//        List<StravaStream> streams = strava.getSegmentStreams(637403);

        List<StravaStream> streams = strava.getActivityStreams(1148302107);

        StravaStream gradients = streams.get(7);

        int i = 0;
    }

    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);
        return dist;
    }
}
