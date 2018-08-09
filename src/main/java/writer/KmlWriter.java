package writer;

import model.LatLng;
import model.SegmentSummaryData;
import model.Series;
import reader.SegmentAnnotationReader;
import reader.SegmentSummaryReader;
import utils.ClassPathStringLoader;
import utils.PolyUtil;
import utils.SegmentSummaryUtils;

import java.io.*;
import java.util.List;

public class KmlWriter {

    private SegmentSummaryUtils segUtils = new SegmentSummaryUtils();

    public void write() {


        List<SegmentSummaryData> segmentSummaryValues = readSegmentSummaryFileAndAnnotate();
        String kmlString = mapSegmentsToKML(segmentSummaryValues);

        writeKML(kmlString);

        int i = 0;
    }

    private List<SegmentSummaryData> readSegmentSummaryFileAndAnnotate() {

        try {
            FileInputStream segmentStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\segment_stats.tsv");
//            FileInputStream segmentStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\small_segment_stats.tsv");
            SegmentSummaryReader segmentSummaryReader = new SegmentSummaryReader();
            List<SegmentSummaryData> segmentSummaries = segmentSummaryReader.readSummaryFile(segmentStream);
            readSegmentAnnotationFileAndAnnotate(segmentSummaries);

            return segmentSummaries;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void readSegmentAnnotationFileAndAnnotate(List<SegmentSummaryData> segmentSummaryValues) {

        try {
            FileInputStream annotationStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\segment_annotation.tsv");
//            FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\small_segment_stats.tsv");
            SegmentAnnotationReader annotationReader = new SegmentAnnotationReader();
            annotationReader.readAnnotationsAndAnnotate(annotationStream, segmentSummaryValues);
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

            if (segment.seriesNames.contains(Series.O_100)) {
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

        if (segment.name.contains("Mow")) {
            int i = 0;
        }

        templateString = templateString.replaceAll("\\{NAME\\}", segment.name);
        templateString = templateString.replaceAll("\\{DIFFICULTY\\}", segment.getDifficultyString());

        String seriesNameString = segUtils.seriesNamesToString(segment.seriesNames);
        templateString = templateString.replaceAll("\\{SERIES\\}", seriesNameString);

        templateString = templateString.replaceAll("\\{CITY\\}", segment.city);
        templateString = templateString.replaceAll("\\{LENGTH\\}", String.format("%.0f", segment.distance));
        templateString = templateString.replaceAll("\\{GAIN\\}", String.format("%.0f", segment.elevation));
        templateString = templateString.replaceAll("\\{AV_GRAD\\}", segment.averageGrad.toString());
        templateString = templateString.replaceAll("\\{MAX_GRAD\\}", segment.getMaxGrad().toString());
        templateString = templateString.replaceAll("\\{LEADER_TIME\\}", segment.leaderTime);
        templateString = templateString.replaceAll("\\{STRAVA\\}", "https://www.strava.com/segments/" + segment.id);
        templateString = templateString.replaceAll("\\{VELOVIEWER\\}", "https://veloviewer.com/segment/" + segment.id);
        templateString = templateString.replaceAll("\\{YOUTUBE\\}", segment.getVideoUrl());

        boolean hasVideo = !segment.getVideoUrl().isEmpty();
        Integer difficultyCat = segment.getDifficultyCategory();
        String style = diffToStyle(hasVideo, difficultyCat);

        templateString = templateString.replaceAll("\\{LINE_STYLE\\}", style);

        List<LatLng> latLngs = PolyUtil.decode(segment.polyline);
        String lineCoordinates = coordinatesToString(latLngs);
        String startCoordinate = coordinateToString(segment.startCoordinate);

        templateString = templateString.replaceAll("\\{COORDINATES\\}", lineCoordinates);
        templateString = templateString.replaceAll("\\{START_COORDINATE\\}", startCoordinate);

        linesBuilder.append(templateString);
        linesBuilder.append("\n");
    }

    private String diffToStyle(boolean hasVideo, Integer difficultyCat) {

        String style;

        switch(difficultyCat){
            case 0: style = "#norating-style";break;
            case 1 : style = hasVideo ? "#video1" : "#novideo1";break;
            case 2: style = hasVideo ? "#video2" : "#novideo2";break;
            case 3: style = hasVideo ? "#video3" : "#novideo3";break;
            case 4: style = hasVideo ? "#video4" : "#novideo4";break;
            case 5: style = hasVideo ? "#video5" : "#novideo5";break;
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
