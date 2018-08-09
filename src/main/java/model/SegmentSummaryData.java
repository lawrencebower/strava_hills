package model;

import javastrava.api.v3.model.reference.StravaClimbCategory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SegmentSummaryData {
    public String id;
    public String name;
    public String city;
    public List<Series> seriesNames;
    public String polyline;
    public List<LatLng> lineCoordinates;
    public LatLng startCoordinate;
    public List<Float> altitudeValues;
    public List<Float> distanceValues;
    public Float averageGrad;
    private Float maxGrad;
    public Float distance;
    public String category;
    public Float elevation;
    public String leaderTime;
    public Integer effortCount;

    private SegmentAnnotation annotation;

    public void checkAltAndDistLengths() {
        if (altitudeValues.size() != distanceValues.size()) {
            System.out.println("Different lengths - " + id);
        }
    }

    public void setAnnotation(SegmentAnnotation annotation) {
        this.annotation = annotation;
    }

    public void setMaxGrad(float maxGrad) {
        this.maxGrad = maxGrad;
    }

    public Float getMaxGrad() {

        Float result = maxGrad;
        if (annotation.correctedMaxGrad != null) {
            result = annotation.correctedMaxGrad;
        }

        return result;
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

        if (annotation.difficulty != null) {
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

        if (annotation.videoURL != null && !annotation.videoURL.isEmpty()) {
            result = annotation.videoURL;
        }

        return result;
    }

    public String getDifficultyString() {

        String result = "not set";

        if (annotation.difficulty != null && annotation.difficulty != 0) {
            result = annotation.difficulty.toString();
        }

        return result;
    }
}
