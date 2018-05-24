package model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SegInfo {

    public Integer segmentId;
    public Set<String> seriesNames = new LinkedHashSet<>();

    public SegInfo(Integer segmentId, String seriesName) {
        this.segmentId = segmentId;
        this.seriesNames.add(seriesName);
    }
}
