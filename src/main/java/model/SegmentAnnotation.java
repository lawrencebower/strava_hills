package model;

public class SegmentAnnotation {

    public String segmentId;
    public String name;
    public Float correctedMaxGrad;
    public Integer difficulty;
    public String videoURL;
    public String synonym;

    public SegmentAnnotation(String segId,
                             String name,
                             Float correctedMaxGrad,
                             Integer difficulty,
                             String videoURL) {

        this.segmentId = segId;
        this.name = name;
        this.correctedMaxGrad = correctedMaxGrad;
        this.difficulty = difficulty;
        this.videoURL = videoURL;
    }

    public void addSynonym(String synonym) {
        this.synonym = synonym;
    }
}
