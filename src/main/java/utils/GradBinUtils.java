package utils;

import model.GradBins;
import model.GradUnit;

public class GradBinUtils {

    private float sub5 = 0;
    private float five2ten = 0;
    private float ten2fifteen = 0;
    private float fifteen2twenty = 0;
    private float twenty2twentyfive = 0;
    private float twentyfivePlus = 0;

    public void calculateAndSetGradStrings(String segmentName, GradBins gradBins) {

        sub5 = 0;
        five2ten = 0;
        ten2fifteen = 0;
        fifteen2twenty = 0;
        twenty2twentyfive = 0;
        twentyfivePlus = 0;

        for (GradUnit gradUnit : gradBins.gradUnits) {
            if (gradUnit.grad >= 25) {
                twentyfivePlus += gradUnit.dist;
            } else if (gradUnit.grad >= 20) {
                twenty2twentyfive += gradUnit.dist;
            } else if (gradUnit.grad >= 15) {
                fifteen2twenty += gradUnit.dist;
            } else if (gradUnit.grad >= 10) {
                ten2fifteen += gradUnit.dist;
            } else if (gradUnit.grad >= 5) {
                five2ten += gradUnit.dist;
            } else {
                sub5 += gradUnit.dist;
            }
        }

        float totalLength = gradBins.totalLength;

        boolean distancesAddUp = distancesAddUp(totalLength);

        if(distancesAddUp) {
            gradBins.twentyfivePlus = String.format("%.0fm (%.0f%%)", twentyfivePlus, (twentyfivePlus/totalLength)*100);
            gradBins.twenty2twentyfive = String.format("%.0fm (%.0f%%)", twenty2twentyfive, (twenty2twentyfive / totalLength) * 100);
            gradBins.fifteen2twenty = String.format("%.0fm (%.0f%%)", fifteen2twenty, (fifteen2twenty / totalLength) * 100);
            gradBins.ten2fifteen = String.format("%.0fm (%.0f%%)", ten2fifteen, (ten2fifteen / totalLength) * 100);
            gradBins.five2ten = String.format("%.0fm (%.0f%%)", five2ten, (five2ten / totalLength) * 100);
            gradBins.subFive = String.format("%.0fm (%.0f%%)", sub5, (sub5 / totalLength) * 100);
        }else{
            throw new RuntimeException("Distances dont add up " + segmentName);
        }
    }

    private boolean distancesAddUp(float totalLength) {

        boolean errorLessThanTenPercent = false;

        float binLength = sub5 + five2ten + ten2fifteen + fifteen2twenty + twenty2twentyfive + twentyfivePlus;

        float remainder = totalLength % binLength;

        if(remainder/totalLength < 10) {
            errorLessThanTenPercent = true;
        }

        return errorLessThanTenPercent;
    }
}
