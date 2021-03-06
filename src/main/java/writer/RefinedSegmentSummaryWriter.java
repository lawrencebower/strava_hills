package writer;

import model.RefinedSegmentSummaryData;
import model.LatLng;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class RefinedSegmentSummaryWriter {

    public void writeClimbSummaries(Map<String, RefinedSegmentSummaryData> climbSummaries, OutputStream outputStream) {

        PrintWriter writer = new PrintWriter(outputStream);

        for (RefinedSegmentSummaryData summaryData : climbSummaries.values()) {
            System.out.println("Writing climb " + summaryData.segData.name);
            String climbSummaryString = getRefinedSegmentString(summaryData);
            writer.write(climbSummaryString);
//            break;
        }

        writer.flush();
        writer.close();

    }

    private String getRefinedSegmentString(RefinedSegmentSummaryData refinedSegmentSummaryData) {
        return detailsToString(refinedSegmentSummaryData);
    }

    private String detailsToString(RefinedSegmentSummaryData summaryData) {

        StringBuilder builder = new StringBuilder();
        appendWithTab(builder, summaryData.segData.id);
        appendWithTab(builder, summaryData.segData.name);
        appendWithTab(builder, summaryData.segData.distance.toString());
        appendWithTab(builder, summaryData.segData.elevation.toString());
        appendWithTab(builder, "MAX_GRAD_BAD");
        appendWithTab(builder, summaryData.segData.averageGrad.toString());
        appendWithTab(builder, summaryData.segData.leaderTime);
        appendWithTab(builder, summaryData.getSummaryString());
        appendWithNewline(builder, summaryData.gradBins.getBinString());

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
