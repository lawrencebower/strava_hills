package model;

import java.util.List;

public class GradBins {

    public String subFive;
    public String five2ten;
    public String ten2fifteen;
    public String fifteen2twenty;
    public String twenty2twentyfive;
    public String twentyfivePlus;

    public List<GradUnit> gradUnits;
    public float totalLength;

    public String getGraphString() {
        StringBuilder builder = new StringBuilder();

        for (GradUnit gradUnit : gradUnits) {
            builder.append(gradUnit.dist);
            builder.append("\t");
            builder.append(gradUnit.gain);
            builder.append("\t");
            builder.append(gradUnit.totalDist);
            builder.append("\t");
            builder.append(gradUnit.totalGain);
            builder.append("\t");
            builder.append(gradUnit.grad);
            builder.append("\n");
        }

        return builder.toString();
    }

    public String getBinString() {

        String format = "<5%% - %s 5 > 10%% - %s 10 > 15%% - %s 15 > 20%% - %s 20 > 25%% - %s >25%% - %s";

        return String.format(format,
                subFive,
                five2ten,
                ten2fifteen,
                fifteen2twenty,
                twenty2twentyfive,
                twentyfivePlus);
    }
}
