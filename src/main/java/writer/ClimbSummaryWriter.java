package writer;

import model.ClimbSummaryData;
import model.LatLng;
import model.SegmentSummaryData;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class ClimbSummaryWriter {

    public void writeClimbSummaries(List<ClimbSummaryData> climbSummaries, OutputStream outputStream) {

        PrintWriter writer = new PrintWriter(outputStream);

        for (ClimbSummaryData summaryData : climbSummaries) {
            System.out.println("Writing climb " + summaryData.name);
            String climbSummaryString = getClimbSummaryString(summaryData);
            writer.write(climbSummaryString);
            break;
        }

        writer.flush();
        writer.close();

    }

    private String getClimbSummaryString(ClimbSummaryData climbSummaryData) {
        return detailsToString(climbSummaryData);
    }

    private String detailsToString(ClimbSummaryData climbSummaryData) {

        StringBuilder builder = new StringBuilder();
        appendWithNewline(builder, climbSummaryData.gradBins.getGraphString());

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
