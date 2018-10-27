package writer;

import model.*;
import reader.SegmentSummaryReader;
import utils.ClassPathStringLoader;
import utils.PolyUtil;
import utils.RefinedSegmentSummaryUtil;
import utils.SegmentSummaryUtils;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class KmlWriter {

    private SegmentSummaryUtils segUtils = new SegmentSummaryUtils();

    public void write() {
        Map<String, RefinedSegmentSummaryData> segmentSummaryValues = readSegmentSummaryFileAndWrite();
        writeLines(segmentSummaryValues);
        writePlacemarks(segmentSummaryValues);
    }

    private void writeLines(Map<String, RefinedSegmentSummaryData> segmentSummaryValues) {
        String kmlString = mapSegmentsToKMLLines(segmentSummaryValues.values());
        writeKMLLines(kmlString);
    }

    private void writePlacemarks(Map<String, RefinedSegmentSummaryData> segmentSummaryValues) {
        String kmlString = mapSegmentsToKMLPlacemarks(segmentSummaryValues.values());
        writeKMLplacemarks(kmlString);
    }

    private Map<String, RefinedSegmentSummaryData> readSegmentSummaryFileAndWrite() {


        try {
            FileInputStream segmentStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\segment_stats.tsv");
//            FileInputStream segmentStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\small_segment_stats.tsv");
            SegmentSummaryReader segmentSummaryReader = new SegmentSummaryReader();

            List<SegmentSummaryData> segmentSummaryValues = segmentSummaryReader.readSummaryAndAnnotationFile(segmentStream);

            Map<String, SegmentAnnotation> annotations = segUtils.readSegmentAnnotationFile();

            RefinedSegmentSummaryUtil refinedSegmentSummaryUtil = new RefinedSegmentSummaryUtil();
            Map<String, RefinedSegmentSummaryData> refinedSummaryData = refinedSegmentSummaryUtil.segmentSummaryToRefinedSegmentSummary(segmentSummaryValues, annotations);

            return refinedSummaryData;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeKMLLines(String kmlString) {
        try {
            FileWriter fileWriter = new FileWriter("C:\\Users\\lawrence\\uk_hill\\maps\\climbs.kml");
            PrintWriter writer = new PrintWriter(fileWriter, true);
            writer.write(kmlString);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeKMLplacemarks(String kmlString) {
        try {
            FileWriter fileWriter = new FileWriter("C:\\Users\\lawrence\\uk_hill\\maps\\placemarks.kml");
            PrintWriter writer = new PrintWriter(fileWriter, true);
            writer.write(kmlString);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String mapSegmentsToKMLLines(Collection<RefinedSegmentSummaryData> segments) {

        ClassPathStringLoader loader = new ClassPathStringLoader();
        String dirRoot = "/templates/";

        final String templateString = loader.getStringFromClassPathFile(dirRoot + "template.xml");
        final String lineTemplateString = loader.getStringFromClassPathFile(dirRoot + "line_fragment.xml");

        StringBuilder linesBuilder = new StringBuilder();

        for (RefinedSegmentSummaryData segment : segments) {

            processSegment(linesBuilder,
                    segment,
                    lineTemplateString);
        }

        String kmlLineString = templateString.replaceAll("\\{PLACEMARKS\\}", linesBuilder.toString());

        return kmlLineString;
    }

    private String mapSegmentsToKMLPlacemarks(Collection<RefinedSegmentSummaryData> segments) {

        ClassPathStringLoader loader = new ClassPathStringLoader();
        String dirRoot = "/templates/";

        final String templateString = loader.getStringFromClassPathFile(dirRoot + "placemarks_template.xml");
        final String placemarkTemplateString = loader.getStringFromClassPathFile(dirRoot + "placemark_fragment.xml");

        StringBuilder o100PlacemarksBuilder = new StringBuilder();
        StringBuilder a100PlacemarksBuilder = new StringBuilder();

        for (RefinedSegmentSummaryData segment : segments) {

            if (segment.segData.seriesNames.contains(Series.O_100)) {
                String placemarkString = placemarkTemplateString.replaceAll("\\{PLACEMARK_TYPE\\}", "placemark");
                processSegment(o100PlacemarksBuilder,
                        segment,
                        placemarkString);
            } else if (segment.segData.seriesNames.contains(Series.A_100)) {
                String placemarkString = placemarkTemplateString.replaceAll("\\{PLACEMARK_TYPE\\}", "placemark2");
                processSegment(a100PlacemarksBuilder,
                        segment,
                        placemarkString);
            }
        }

        String kmlplacemarksString = templateString.replaceAll("\\{100_MARKERS\\}", o100PlacemarksBuilder.toString());
        kmlplacemarksString = kmlplacemarksString.replaceAll("\\{A100_MARKERS\\}", a100PlacemarksBuilder.toString());

        return kmlplacemarksString;
    }

    private void processSegment(StringBuilder linesBuilder,
                                RefinedSegmentSummaryData segment,
                                String templateString) {

        if (segment.getName().contains("Mow")) {
            int i = 0;
        }

        templateString = templateString.replaceAll("\\{NAME\\}", segment.getNameOrSynonym());
        templateString = templateString.replaceAll("\\{DIFFICULTY\\}", segment.getDifficultyString());

        String seriesNameString = segUtils.seriesNamesToString(segment.getSeriesNames());
        templateString = templateString.replaceAll("\\{SERIES\\}", seriesNameString);

        templateString = templateString.replaceAll("\\{CITY\\}", segment.getCity());
        templateString = templateString.replaceAll("\\{LENGTH\\}", String.format("%.0f", segment.getDistance()));
        templateString = templateString.replaceAll("\\{GAIN\\}", String.format("%.0f", segment.getElevation()));
        templateString = templateString.replaceAll("\\{AV_GRAD\\}", segment.getAverageGrad().toString());
        templateString = templateString.replaceAll("\\{MAX_GRAD\\}", segment.getMaxGrad().toString());
        templateString = templateString.replaceAll("\\{LEADER_TIME\\}", segment.getLeaderTime());
        templateString = templateString.replaceAll("\\{STRAVA\\}", "https://www.strava.com/segments/" + segment.getId());
        templateString = templateString.replaceAll("\\{VELOVIEWER\\}", "https://veloviewer.com/segment/" + segment.getId());
        templateString = templateString.replaceAll("\\{YOUTUBE\\}", segment.getVideoUrl());

        boolean hasVideo = !segment.getVideoUrl().isEmpty();
        Integer difficultyCat = segment.getDifficultyCategory();
        String style = diffToStyle(hasVideo, difficultyCat);

        templateString = templateString.replaceAll("\\{LINE_STYLE\\}", style);

        List<LatLng> latLngs = PolyUtil.decode(segment.getPolyline());
        String lineCoordinates = coordinatesToString(latLngs);
        String startCoordinate = coordinateToString(segment.getStartCoordinate());

        templateString = templateString.replaceAll("\\{COORDINATES\\}", lineCoordinates);
        templateString = templateString.replaceAll("\\{START_COORDINATE\\}", startCoordinate);

        linesBuilder.append(templateString);
        linesBuilder.append("\n");
    }

    private String diffToStyle(boolean hasVideo, Integer difficultyCat) {

        String style;

        switch (difficultyCat) {
            case 0:
                style = "#norating-style";
                break;
            case 1:
                style = hasVideo ? "#video1" : "#novideo1";
                break;
            case 2:
                style = hasVideo ? "#video2" : "#novideo2";
                break;
            case 3:
                style = hasVideo ? "#video3" : "#novideo3";
                break;
            case 4:
                style = hasVideo ? "#video4" : "#novideo4";
                break;
            case 5:
                style = hasVideo ? "#video5" : "#novideo5";
                break;
            default:
                throw new RuntimeException("Cant map difficulty " + difficultyCat);
        }
        return style;
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
