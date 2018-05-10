package writer;

import model.LatLng;
import model.SegmentSummaryData;

import java.io.*;
import java.util.List;

public class SegmentSummaryWriter {

    public void writeSegSummariesFile(List<SegmentSummaryData> segmentSummaryValues, OutputStream outputStream) {

        buildSummaryString(segmentSummaryValues, outputStream);

        int i = 0;
    }

    private void buildSummaryString(List<SegmentSummaryData> segments, OutputStream outputStream) {

        PrintWriter writer = new PrintWriter(outputStream);

        for (SegmentSummaryData segment : segments) {
            System.out.println("Writing segment " + segment.name);
            String segmentString = buildSummaryString(segment);
            writer.write(segmentString);
        }

        writer.flush();
        writer.close();

    }

    private String buildSummaryString(SegmentSummaryData segmentSummaryData) {
        return summaryDataToString(segmentSummaryData);
    }

    private String summaryDataToString(SegmentSummaryData segmentSummaryData) {

        StringBuilder builder = new StringBuilder();
        appendWithTab(builder, segmentSummaryData.name);
        appendWithTab(builder, segmentSummaryData.description);
        appendWithTab(builder, segmentSummaryData.series);

        LatLng startCoordinate = segmentSummaryData.startCoordinate;
        String startCoordinatesString = coordinatesToString(startCoordinate);
        appendWithTab(builder, startCoordinatesString);

        String lineString = coordinatesToString(segmentSummaryData.lineCoordinates);
        String altitudeString = listToString(segmentSummaryData.altitudeValues);
        String distanceString = listToString(segmentSummaryData.distanceValues);

        appendWithTab(builder, altitudeString);
        appendWithTab(builder, distanceString);

        appendWithNewline(builder, lineString);

        return builder.toString();
    }

    private String listToString(List<Float> values) {

        StringBuilder builder = new StringBuilder();

        for (Float value : values) {
            builder.append(value);
            builder.append(";");
        }

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
}
