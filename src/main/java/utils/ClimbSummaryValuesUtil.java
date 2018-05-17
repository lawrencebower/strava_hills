package utils;

import model.ClimbSummaryData;
import model.GradBins;
import model.GradUnit;
import model.SegmentSummaryData;

import java.util.ArrayList;
import java.util.List;

public class ClimbSummaryValuesUtil {

    private static final int UNIT_DISTANCE = 20;

    public ClimbSummaryData segmentSummaryToClimbSummary(SegmentSummaryData segmentSummaryData) {

        System.out.println(segmentSummaryData.name);

        ClimbSummaryData results = new ClimbSummaryData();
        results.name = segmentSummaryData.name;
        results.gradBins = mapSegmentSummaryToGradBins(segmentSummaryData);

        return results;
    }

    private GradBins mapSegmentSummaryToGradBins(SegmentSummaryData segmentSummaryData) {

        List<Float> alts = segmentSummaryData.altitudeValues;
        List<Float> dists = segmentSummaryData.distanceValues;

        List<GradUnit> gradUnits = calculateGradUnits(alts, dists);

        GradBins gradBins = new GradBins();

        gradBins.gradUnits = gradUnits;

        return gradBins;
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
                System.out.println(gradUnit.toString());
                if(gradUnit.grad > 30) {
                    int i = 0;
                }

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

    public List<ClimbSummaryData> segmentSummaryToClimbSummary(List<SegmentSummaryData> segmentSummaryValues) {

        List<ClimbSummaryData> results = new ArrayList<>();

        for (SegmentSummaryData segmentSummaryValue : segmentSummaryValues) {
            ClimbSummaryData climbSummaryData = segmentSummaryToClimbSummary(segmentSummaryValue);
            results.add(climbSummaryData);
        }

        return results;
    }
}
