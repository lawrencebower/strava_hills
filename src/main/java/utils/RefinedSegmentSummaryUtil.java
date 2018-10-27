package utils;

import model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RefinedSegmentSummaryUtil {

    private static final int UNIT_DISTANCE = 35;

    private GradBinUtils gradBinUtils = new GradBinUtils();

    public RefinedSegmentSummaryData segmentSummaryToRefinedSegmentSummary(SegmentSummaryData segmentSummaryData) {

        System.out.println(segmentSummaryData.name);

        RefinedSegmentSummaryData results = new RefinedSegmentSummaryData(segmentSummaryData);
        results.gradBins = mapSegmentSummaryToGradBins(segmentSummaryData);

        return results;
    }

    private GradBins mapSegmentSummaryToGradBins(SegmentSummaryData segmentSummaryData) {

        List<Float> alts = segmentSummaryData.altitudeValues;
        List<Float> dists = segmentSummaryData.distanceValues;

        List<GradUnit> gradUnits = calculateGradUnits(alts, dists);

        GradBins gradBins = new GradBins();

        gradBins.gradUnits = gradUnits;
        gradBins.totalLength = segmentSummaryData.distance;
        calculateAndSetGradStrings(segmentSummaryData.name, gradBins);

        return gradBins;
    }

    private void calculateAndSetGradStrings(String name, GradBins gradBins) {
        gradBinUtils.calculateAndSetGradStrings(name, gradBins);
    }

    private List<GradUnit> calculateGradUnits(List<Float> alts, List<Float> dists) {

        if (alts.size() != dists.size()) {
            throw new RuntimeException("alts and dists are different lengths!");
        }

        List<GradUnit> results = new ArrayList<>();

        int unitStartIndex = 0;
        float unitStartDistance = 0;
        float distanceSinceLast = 0;

        for (int currentIndex = 0; currentIndex < alts.size(); currentIndex++) {

            Float currentDistance = dists.get(currentIndex);
//            float incrementDistance = currentDistance - distanceSinceLast;
            distanceSinceLast = currentDistance - unitStartDistance;

            if (distanceSinceLast > UNIT_DISTANCE) {
                GradUnit gradUnit = calculateGradUnit(
                        dists.get(unitStartIndex),
                        alts.get(unitStartIndex),
                        dists.get(currentIndex),
                        alts.get(currentIndex));

                results.add(gradUnit);
//                System.out.println(gradUnit.toString());
//                if(gradUnit.grad > 30) {
//                    int i = 0;
//                }

//                distanceSinceLast = 0;//reset
                unitStartIndex = currentIndex++;
                unitStartDistance = dists.get(unitStartIndex);
            }
        }

        return results;
    }

    private GradUnit calculateGradUnit(Float dist1,
                                       Float alt1,
                                       Float dist2,
                                       Float alt2) {

        float unitDist = dist2 - dist1;
        float unitGain = alt2 - alt1;

        return new GradUnit(unitDist,
                unitGain,
                dist2,
                alt2);
    }

    public Map<String, RefinedSegmentSummaryData> segmentSummaryToRefinedSegmentSummary(List<SegmentSummaryData> segmentSummaryValues,
                                                                                        Map<String,SegmentAnnotation> annotations) {

        Map<String, RefinedSegmentSummaryData> results = new LinkedHashMap<>();

        for (SegmentSummaryData segmentSummaryValue : segmentSummaryValues) {
            RefinedSegmentSummaryData refinedSegmentSummaryData = segmentSummaryToRefinedSegmentSummary(segmentSummaryValue);
            annotateSummary(refinedSegmentSummaryData, annotations);
            results.put(refinedSegmentSummaryData.segData.id, refinedSegmentSummaryData);
        }

        return results;
    }

    private void annotateSummary(RefinedSegmentSummaryData summaryData, Map<String, SegmentAnnotation> annotations) {

        String segId = summaryData.getId();

        if(annotations.containsKey(segId)){
            summaryData.setAnnotation(annotations.get(segId));
        }
    }
}
