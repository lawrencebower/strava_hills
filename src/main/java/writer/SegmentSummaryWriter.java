package writer;

import model.LatLng;
import model.SegmentSummaryData;
import utils.SegmentSummaryUtils;

import java.io.*;
import java.util.List;

public class SegmentSummaryWriter {

    private PrintWriter printWriter;
    private SegmentSummaryUtils segUtils = new SegmentSummaryUtils();

    public void prepareWriter(OutputStream outputStream){
        printWriter = new PrintWriter(outputStream);
    }

    public void closeWriter() {
        printWriter.close();
    }

//    private void writeSegSummariesFile(List<SegmentSummaryData> segments) {
//        for (SegmentSummaryData segment : segments) {
//            writeSegSummaryLine(segment);
//        }
//    }

    public void writeSegSummaryLine(SegmentSummaryData segment) {
        System.out.println("Writing segment " + segment.name);
        String segmentString = buildAndWriteSummaryString(segment);
        printWriter.write(segmentString);
    }

    private String buildAndWriteSummaryString(SegmentSummaryData segmentSummaryData) {
        return summaryDataToString(segmentSummaryData);
    }

    private String summaryDataToString(SegmentSummaryData segmentSummaryData) {

        StringBuilder builder = new StringBuilder();
        appendWithTab(builder, segmentSummaryData.id);
        appendWithTab(builder, segmentSummaryData.name);
        appendWithTab(builder, segmentSummaryData.city);
        appendWithTab(builder, segUtils.seriesNamesToString(segmentSummaryData.seriesNames));
        appendWithTab(builder, segmentSummaryData.category);
        appendWithTab(builder, segmentSummaryData.distance.toString());
        appendWithTab(builder, segmentSummaryData.averageGrad.toString());
        appendWithTab(builder, segmentSummaryData.maxGrad.toString());
        appendWithTab(builder, segmentSummaryData.elevation.toString());
        appendWithTab(builder, segmentSummaryData.leaderTime);

        LatLng startCoordinate = segmentSummaryData.startCoordinate;
        String startCoordinatesString = segUtils.coordinatesToString(startCoordinate);
        appendWithTab(builder, startCoordinatesString);

        String altitudeString = segUtils.listToString(segmentSummaryData.altitudeValues);
        String distanceString = segUtils.listToString(segmentSummaryData.distanceValues);

        appendWithTab(builder, altitudeString);
        appendWithTab(builder, distanceString);

        appendWithNewline(builder, segmentSummaryData.polyline);

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
