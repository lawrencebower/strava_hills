package runners;

import model.RefinedSegmentSummaryData;
import model.SegmentSummaryData;
import reader.SegmentSummaryReader;
import utils.RefinedSegmentSummaryUtil;
import writer.RefinedSegmentSummaryWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class SegmentSummaryReaderRunner {

    public static void main(String[] args) throws FileNotFoundException {

//        FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\segment_stats.tsv");
        FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\small_segment_stats.tsv");
        List<SegmentSummaryData> segmentSummaryValues = new SegmentSummaryReader().readSummaryFile(inputStream);

        int i = 0;
    }
}
