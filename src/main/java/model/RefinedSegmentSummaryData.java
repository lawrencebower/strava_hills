package model;

import org.w3c.dom.NameList;

import java.util.List;

public class RefinedSegmentSummaryData {

    private static final String format = "length - %.0fm gain - %.0fm max grad. - %.0f%% av. grad. - %.1f%% leader time - %s";

    public SegmentSummaryData segData;
    public GradBins gradBins;
    private SegmentAnnotation annotation;

    public RefinedSegmentSummaryData(SegmentSummaryData segmentSummaryData) {
        this.segData = segmentSummaryData;
    }

    public void setAnnotation(SegmentAnnotation annotation) {
        this.annotation = annotation;
    }

    public Integer getDifficulty() {

        Integer result = 0;//no rating

        if (annotation.difficulty != null) {
            result = annotation.difficulty;
        }

        return result;
    }

    public Integer getDifficultyCategory() {

        Integer result = 0;//no rating

        if (annotation != null && annotation.difficulty != null) {
            int rawDifficulty = annotation.difficulty;
            if (rawDifficulty > 0 && rawDifficulty <= 3) {
                result = 1;
            } else if (rawDifficulty >= 4 && rawDifficulty <= 6) {
                result = 2;
            } else if (rawDifficulty >= 7 && rawDifficulty <= 8) {
                result = 3;
            } else if (rawDifficulty == 9) {
                result = 4;
            } else if (rawDifficulty >= 10) {
                result = 5;
            }
        }

        return result;
    }

    public String getVideoUrl() {

        String result = "";

        if (annotation != null && annotation.videoURL != null && !annotation.videoURL.isEmpty()) {
            result = annotation.videoURL;
        }

        return result;
    }

    public String getDifficultyString() {

        String result = "not set";

        if (annotation != null && annotation.difficulty != null && annotation.difficulty != 0) {
            result = annotation.difficulty.toString();
        }

        return result;
    }

    public Float getMaxGrad() {

        Float result = gradBins.maxGrad;
        if (annotation != null && annotation.correctedMaxGrad != null) {
            result = annotation.correctedMaxGrad;
        }

        return result;
    }
    public String getSummaryString() {
        return String.format(format, segData.distance, segData.elevation, gradBins.maxGrad, segData.averageGrad, segData.leaderTime);
    }

    public String getName() {
        return segData.name;
    }

    public List<Series> getSeriesNames() {
        return segData.seriesNames;
    }

    public String getCity() {
        return segData.city;
    }

    public Float getDistance() {
        return segData.distance;
    }

    public Float getElevation() {
        return segData.elevation;
    }

    public Float getAverageGrad() {
        return segData.averageGrad;
    }

    public String getLeaderTime() {
        return segData.leaderTime;
    }

    public String getId() {
        return segData.id;
    }

    public String getPolyline() {
        return segData.polyline;
    }

    public LatLng getStartCoordinate() {
        return segData.startCoordinate;
    }

    public String getNameOrSynonym() {

        String result = segData.name;

        List<Series> seriesNames = getSeriesNames();
        boolean regionalHill = !seriesNames.contains(Series.O_100) && !seriesNames.contains(Series.A_100);

        if(annotation != null) {
            String synonym = annotation.synonym;

            if (synonym != null && regionalHill) {
                result = synonym;
            }
        }

        return result;
    }
}
