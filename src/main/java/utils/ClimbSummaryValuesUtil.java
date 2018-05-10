package utils;

import model.ClimbSummaryData;
import model.GradBins;
import model.GradUnit;
import model.SegmentSummaryData;

import java.util.ArrayList;
import java.util.List;

public class ClimbSummaryValuesUtil {

    private static final int chunkSize = 100;

    public ClimbSummaryData segmentSummaryToClimbSummary(SegmentSummaryData segmentSummaryData) {
        System.out.println("ClimbSummaryValuesUtil.segmentSummaryToClimbSummary");

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

        for (int index1 = 0; index1 < alts.size(); index1 = (index1 + chunkSize)) {

            int index2 = index1 + chunkSize;

            if ((index2) < alts.size()) {
                GradUnit gradUnit = calculateGradUnit(
                        dists.get(index1),
                        alts.get(index1),
                        dists.get(index2),
                        alts.get(index2));

                results.add(gradUnit);
                System.out.println(gradUnit.toString());
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
