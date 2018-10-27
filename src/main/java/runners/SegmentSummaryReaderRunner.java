package runners;

import model.SegmentSummaryData;
import reader.SegmentSummaryReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class SegmentSummaryReaderRunner {

    public static void main(String[] args) throws FileNotFoundException {

//        FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\segment_stats.tsv");
        FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\small_segment_stats.tsv");
        List<SegmentSummaryData> segmentSummaryValues = new SegmentSummaryReader().readSummaryAndAnnotationFile(inputStream);

        int i = 0;
    }
}
