package model;

public class RefinedSegmentSummaryData {

    private static final String format = "length - %s gain - %s max grad. - %s av. grad. - %s leader time - %s";

    public String id;
    public String name;
    public String elevation;
    public String distance;
    public String leaderTime;
    public String averageGrad;
    public String maxGrad;
    public GradBins gradBins;

    public String getSummaryString() {
        return String.format(format, distance, elevation, averageGrad, maxGrad, leaderTime);
    }
}
