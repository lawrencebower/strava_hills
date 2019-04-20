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
        Map<String, RefinedSegmentSummaryData> segmentSummaryValues = readSegmentSummaryFileAndAnnotations();
        writeLines(segmentSummaryValues, false);
        writePlacemarks(segmentSummaryValues, false);
    }

    public void writeProgress() {
        Map<String, RefinedSegmentSummaryData> segmentSummaryValues = readSegmentSummaryFileAndAnnotations();
        writeLines(segmentSummaryValues, true);
        writePlacemarks(segmentSummaryValues, true);
    }

    private void writeLines(Map<String, RefinedSegmentSummaryData> segmentSummaryValues, boolean excludeDone) {
        String kmlString = mapSegmentsToKMLLines(segmentSummaryValues.values(), excludeDone);
        String fileName = "climbs.kml";
        if(excludeDone){
            fileName = "climbs_progress.kml";
        }
        writeKMLLines(kmlString, fileName);
    }

    private void writePlacemarks(Map<String, RefinedSegmentSummaryData> segmentSummaryValues, boolean excludeDone) {
        String kmlString = mapSegmentsToKMLPlacemarks(segmentSummaryValues.values(), excludeDone);
        String fileName = "placemarks.kml";
        if(excludeDone){
            fileName = "placemarks_progress.kml";
        }
        writeKMLplacemarks(kmlString, fileName);
    }

    private Map<String, RefinedSegmentSummaryData> readSegmentSummaryFileAndAnnotations() {


        try {
            FileInputStream segmentStream = new FileInputStream("C:\\Users\\lawrence\\software\\strava\\src\\main\\resources\\output\\spreadsheets\\segment_stats.tsv");
//            FileInputStream segmentStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\small_segment_stats.tsv");
            SegmentSummaryReader segmentSummaryReader = new SegmentSummaryReader();

            List<SegmentSummaryData> segmentSummaryValues = segmentSummaryReader.readSummaryFile(segmentStream);

            Map<String, SegmentAnnotation> annotations = segUtils.readSegmentAnnotationFile();

            RefinedSegmentSummaryUtil refinedSegmentSummaryUtil = new RefinedSegmentSummaryUtil();

            return refinedSegmentSummaryUtil.segmentSummaryToRefinedSegmentSummary(segmentSummaryValues, annotations);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeKMLLines(String kmlString, String fileName) {
        try {
            FileWriter fileWriter = new FileWriter("C:\\Users\\lawrence\\software\\strava\\src\\main\\resources\\output\\spreadsheets\\" + fileName);
            PrintWriter writer = new PrintWriter(fileWriter, true);
            writer.write(kmlString);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeKMLplacemarks(String kmlString, String fileName) {
        try {
            FileWriter fileWriter = new FileWriter("C:\\Users\\lawrence\\software\\strava\\src\\main\\resources\\output\\spreadsheets\\" + fileName);
            PrintWriter writer = new PrintWriter(fileWriter, true);
            writer.write(kmlString);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String mapSegmentsToKMLLines(Collection<RefinedSegmentSummaryData> segments, boolean excludeComplete) {

        ClassPathStringLoader loader = new ClassPathStringLoader();
        String dirRoot = "/templates/";

        final String templateString = loader.getStringFromClassPathFile(dirRoot + "template.xml");
        final String lineTemplateString = loader.getStringFromClassPathFile(dirRoot + "line_fragment.xml");

        StringBuilder linesBuilder = new StringBuilder();

        for (RefinedSegmentSummaryData segment : segments) {

            boolean exclude = excludeComplete && segment.annotation.complete;

            if(!exclude) {
                processSegment(linesBuilder,
                        segment,
                        lineTemplateString);
            }
        }

        String kmlLineString = templateString.replaceAll("\\{PLACEMARKS\\}", linesBuilder.toString());

        return kmlLineString;
    }

    private String mapSegmentsToKMLPlacemarks(Collection<RefinedSegmentSummaryData> segments, boolean excludeComplete) {

        ClassPathStringLoader loader = new ClassPathStringLoader();
        String dirRoot = "/templates/";

        final String templateString = loader.getStringFromClassPathFile(dirRoot + "placemarks_template.xml");
        final String placemarkTemplateString = loader.getStringFromClassPathFile(dirRoot + "placemark_fragment.xml");

        StringBuilder o100PlacemarksBuilder = new StringBuilder();
        StringBuilder a100PlacemarksBuilder = new StringBuilder();

        for (RefinedSegmentSummaryData segment : segments) {

            boolean exclude = excludeComplete && segment.annotation.complete;

            if (segment.segData.seriesNames.contains(Series.O_100) && !exclude) {
                String placemarkString = placemarkTemplateString.replaceAll("\\{PLACEMARK_TYPE\\}", "placemark");
                processSegment(o100PlacemarksBuilder,
                        segment,
                        placemarkString);
            } else if (segment.segData.seriesNames.contains(Series.A_100) && !exclude) {
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

        if (segment.getDifficulty() != 0) {

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
