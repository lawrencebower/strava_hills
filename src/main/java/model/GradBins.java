package model;

import java.util.List;

public class GradBins {

    public String five2ten;
    public String ten2fifteen;
    public String fifteen2twenty;
    public String twenty2twentyfive;
    public String twentyfivePlus;

    public List<GradUnit> gradUnits;

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
}
