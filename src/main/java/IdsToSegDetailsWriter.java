import javastrava.api.v3.model.StravaSegment;

import java.io.*;
import java.util.List;

public class IdsToSegDetailsWriter {

    public static void main(String[] args) {
        new IdsToSegDetailsWriter().write();
    }

    private void write() {

        SegmentInfoUtils segmentInfoUtils = new SegmentInfoUtils();
        segmentInfoUtils.setupSession();

        System.out.println("starting...");
//        List<Integer> segmentIds = Arrays.asList(6665368, 6677446);
        List<Integer> segmentIds = readIdsFile();
        List<StravaSegment> segments = segmentInfoUtils.getSegments(segmentIds);
        System.out.println("...done");

        List<SegmentSummaryValues> segmentSummaryValues = segmentInfoUtils.getSegmentValues(segments);

        getSegmentDetailsString(segmentSummaryValues);

        int i = 0;
    }

    private void getSegmentDetailsString(List<SegmentSummaryValues> segments) {

        try {
            FileWriter fileWriter = null;
            fileWriter = new FileWriter("C:\\Users\\lawrence\\uk_hill\\maps\\segment_stats.tsv");
            PrintWriter writer = new PrintWriter(fileWriter);

            for (SegmentSummaryValues segment : segments) {
                System.out.println("Writing segment " + segment.name);
                String segmentString = getSegmentDetailsString(segment);
                writer.write(segmentString);
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getSegmentDetailsString(SegmentSummaryValues segmentSummaryValues) {

        String segmentString = detailsToString(segmentSummaryValues);

        return segmentString;
    }

    private String detailsToString(SegmentSummaryValues segmentSummaryValues) {

        StringBuilder builder = new StringBuilder();
        appendWithTab(builder, segmentSummaryValues.name);
        appendWithTab(builder, segmentSummaryValues.description);
        appendWithTab(builder, segmentSummaryValues.series);

        LatLng startCoordinate = segmentSummaryValues.startCoordinate;
        String startCoordinatesString = coordinatesToString(startCoordinate);
        appendWithTab(builder, startCoordinatesString);

        String lineString = coordinatesToString(segmentSummaryValues.lineCoordinates);
        appendWithNewline(builder, lineString);

        return builder.toString();
    }

    private String coordinatesToString(LatLng coordinates) {
        return coordinates.toString();
    }

    private String coordinatesToString(List<LatLng> coordinatesList) {

        StringBuilder builder = new StringBuilder();

        for (LatLng latLng : coordinatesList) {
            builder.append(coordinatesToString(latLng));
            builder.append(";");
        }

        return builder.toString();
    }

    private void appendWithTab(StringBuilder builder, String value) {
        builder.append(value);
        builder.append("\t");
    }

    private void appendWithNewline(StringBuilder builder, String value) {
        builder.append(value);
        builder.append("\n");
    }

    private List<Integer> readIdsFile() {

        try {
            FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\small_segments.txt");
            List<Integer> segmentIds = new IdReader().readIds(inputStream);
            return segmentIds;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
