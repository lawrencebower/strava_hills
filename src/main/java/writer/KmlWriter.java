package writer;

import model.LatLng;
import model.SegmentSummaryData;
import reader.SegmentSummaryReader;
import utils.ClassPathStringLoader;
import utils.PolyUtil;
import utils.SegmentSummaryUtils;

import java.io.*;
import java.util.List;

public class KmlWriter {

    private SegmentSummaryUtils segUtils = new SegmentSummaryUtils();

    public void write() {


        List<SegmentSummaryData> segmentSummaryValues = readSegmentSummaryFile();
        String kmlString = mapSegmentsToKML(segmentSummaryValues);

        writeKML(kmlString);

        int i = 0;
    }

    private List<SegmentSummaryData> readSegmentSummaryFile() {

        try {
            FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\segment_stats.tsv");
//            FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\small_segment_stats.tsv");
            SegmentSummaryReader segmentSummaryReader = new SegmentSummaryReader();
            return segmentSummaryReader.readSummaryFile(inputStream);
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

            if(segment.seriesNames.contains("1")) {
                processSegment(placemarksBuilder,
                        segment,
                        placemarkTemplateString);
            }
        }

        String kmlString = templateString.replaceAll("\\{PLACEMARKS\\}", linesBuilder.toString());

        kmlString = kmlString.replaceAll("\\{MARKERS\\}", placemarksBuilder.toString());

        return kmlString;
    }

    private void processSegment(StringBuilder linesBuilder,
                                SegmentSummaryData segment,
                                String templateString) {

        templateString = templateString.replaceAll("\\{NAME\\}", segment.name);
        templateString = templateString.replaceAll("\\{DIFFICULTY\\}", segment.difficulty);

        String seriesNameString = segUtils.seriesNamesToString(segment.seriesNames);
        templateString = templateString.replaceAll("\\{SERIES\\}", seriesNameString);

        templateString = templateString.replaceAll("\\{CITY\\}", segment.city);
        templateString = templateString.replaceAll("\\{LENGTH\\}", segment.distance.toString());
        templateString = templateString.replaceAll("\\{GAIN\\}", segment.elevation.toString());
        templateString = templateString.replaceAll("\\{AV_GRAD\\}", segment.averageGrad.toString());
        templateString = templateString.replaceAll("\\{MAX_GRAD\\}", segment.maxGrad.toString());
        templateString = templateString.replaceAll("\\{LEADER_TIME\\}", segment.leaderTime);
        templateString = templateString.replaceAll("\\{STRAVA\\}", "https://www.strava.com/segments/" + segment.id);
        templateString = templateString.replaceAll("\\{VELOVIEWER\\}", "https://veloviewer.com/segment/" + segment.id);

        List<LatLng> latLngs = PolyUtil.decode(segment.polyline);
        String lineCoordinates = coordinatesToString(latLngs);
        String startCoordinate = coordinateToString(segment.startCoordinate);

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

/*    private String coordinatesToStartString(List<LatLng> lineCoordinates) {

        StringBuilder builder = new StringBuilder();

        builder.append(coordinateToString(lineCoordinates.get(0)));
        builder.append("\n");

        return builder.toString();
    }*/

    private String coordinateToString(LatLng lineCoordinate) {
        return lineCoordinate.longitude + "," + lineCoordinate.latitude;
    }
}
