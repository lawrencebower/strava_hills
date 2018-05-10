package writer;

import model.LatLng;
import model.SegmentSummaryData;
import reader.IdReader;
import utils.ClassPathStringLoader;
import utils.SegmentInfoUtils;

import java.io.*;
import java.util.List;

public class KmlWriter {

    public void write() {

        SegmentInfoUtils segmentInfoUtils = new SegmentInfoUtils();
        segmentInfoUtils.setupSession();

        System.out.println("starting...");
//        List<Integer> segmentIds = Arrays.asList(6665368, 6677446);
        List<Integer> segmentIds = readIdsFile();
        List<SegmentSummaryData> segmentSummaryValues = segmentInfoUtils.getSegmentSummaryValues(segmentIds);
        System.out.println("...done");

        String kmlString = mapSegmentsToKML(segmentSummaryValues);

        writeKML(kmlString);

        int i = 0;
    }

    private List<Integer> readIdsFile() {

        try {
            FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\segments.txt");
            List<Integer> segmentIds = new IdReader().readIds(inputStream);
            return segmentIds;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeKML(String kmlString) {
        try {
            FileWriter fileWriter = new FileWriter("C:\\Users\\lawrence\\uk_hill\\maps\\climbs.kml");
            PrintWriter writer = new PrintWriter(fileWriter, true);
            writer.write(kmlString);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String mapSegmentsToKML(List<SegmentSummaryData> segments) {

        ClassPathStringLoader loader = new ClassPathStringLoader();
        String dirRoot = "/templates/";

        String templateString = loader.getStringFromClassPathFile(dirRoot + "template.xml");
        String lineTemplateString = loader.getStringFromClassPathFile(dirRoot + "line_fragment.xml");
        String placemarkTemplateString = loader.getStringFromClassPathFile(dirRoot + "placemark_fragment.xml");

        StringBuilder linesBuilder = new StringBuilder();
        StringBuilder placemarksBuilder = new StringBuilder();

        for (SegmentSummaryData segment : segments) {

            processSegment(linesBuilder,
                    segment,
                    lineTemplateString);

            processSegment(placemarksBuilder,
                    segment,
                    placemarkTemplateString);
        }

        String kmlString = templateString.replaceAll("\\{PLACEMARKS\\}", linesBuilder.toString());

        kmlString = kmlString.replaceAll("\\{MARKERS\\}", placemarksBuilder.toString());

        return kmlString;
    }

    private void processSegment(StringBuilder linesBuilder,
                                SegmentSummaryData segment,
                                String templateString) {

        templateString = templateString.replaceAll("\\{NAME\\}", segment.name);
        templateString = templateString.replaceAll("\\{DESCRIPTION\\}", segment.description);
        templateString = templateString.replaceAll("\\{SERIES\\}", segment.series);

        String lineCoordinates = coordinatesToString(segment.lineCoordinates);
        String startCoordinate = coordinatesToStartString(segment.lineCoordinates);

        templateString = templateString.replaceAll("\\{COORDINATES\\}", lineCoordinates);
        templateString = templateString.replaceAll("\\{START_COORDINATE\\}", startCoordinate);

        linesBuilder.append(templateString);
        linesBuilder.append("\n");
    }

    private String coordinatesToString(List<LatLng> lineCoordinates) {

        StringBuilder builder = new StringBuilder();

        for (LatLng lineCoordinate : lineCoordinates) {
            builder.append(coordinateToString(lineCoordinate));
            builder.append("\n");
        }

        return builder.toString();
    }

    private String coordinatesToStartString(List<LatLng> lineCoordinates) {

        StringBuilder builder = new StringBuilder();

        builder.append(coordinateToString(lineCoordinates.get(0)));
        builder.append("\n");

        return builder.toString();
    }

    private String coordinateToString(LatLng lineCoordinate) {
        return lineCoordinate.longitude + "," + lineCoordinate.latitude;
    }
}
