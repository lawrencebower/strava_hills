package model;

public class RefinedSegmentSummaryData {

    private static final String format = "length - %.0fm gain - %.0fm max grad. - %.1f%% av. grad. - %.1f%% leader time - %s";

    public String id;
    public String name;
    public Float elevation;
    public Float distance;
    public String leaderTime;
    public Float averageGrad;
    public Float maxGrad;
    public GradBins gradBins;

    public String getSummaryString() {
        return String.format(format, distance, elevation, maxGrad, averageGrad, leaderTime);
    }
}
