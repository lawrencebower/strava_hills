package utils;

import model.RefinedSegmentSummaryData;
import model.GradBins;
import model.GradUnit;
import model.SegmentSummaryData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RefinedSegmentSummaryUtil {

    private static final int UNIT_DISTANCE = 20;

    private GradBinUtils gradBinUtils = new GradBinUtils();

    public RefinedSegmentSummaryData segmentSummaryToRefinedSegmentSummary(SegmentSummaryData segmentSummaryData) {

        System.out.println(segmentSummaryData.name);

        RefinedSegmentSummaryData results = new RefinedSegmentSummaryData();
        results.id = segmentSummaryData.id;
        results.name = segmentSummaryData.name;
        results.elevation = segmentSummaryData.elevation;
        results.distance = segmentSummaryData.distance;
        results.leaderTime = segmentSummaryData.leaderTime;
        results.averageGrad = segmentSummaryData.averageGrad;
        results.maxGrad = segmentSummaryData.getMaxGrad();
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

    public Map<String, RefinedSegmentSummaryData> segmentSummaryToRefinedSegmentSummary(List<SegmentSummaryData> segmentSummaryValues) {

        Map<String, RefinedSegmentSummaryData> results = new LinkedHashMap<>();

        for (SegmentSummaryData segmentSummaryValue : segmentSummaryValues) {
            RefinedSegmentSummaryData refinedSegmentSummaryData = segmentSummaryToRefinedSegmentSummary(segmentSummaryValue);
            results.put(refinedSegmentSummaryData.id, refinedSegmentSummaryData);
        }

        return results;
    }
}
