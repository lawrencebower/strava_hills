package runners;

import model.ClimbSummaryData;
import model.SegmentSummaryData;
import reader.SegmentSummaryReader;
import utils.ClimbSummaryValuesUtil;
import writer.ClimbSummaryWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class SegmentSummaryReaderRunner {

    public static void main(String[] args) throws FileNotFoundException {

        FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\segment_stats.tsv");
        List<SegmentSummaryData> segmentSummaryValues = new SegmentSummaryReader().readSummaryFile(inputStream);

        ClimbSummaryValuesUtil climbsUtil = new ClimbSummaryValuesUtil();
        List<ClimbSummaryData> climbSummaryValues = climbsUtil.segmentSummaryToClimbSummary(segmentSummaryValues);

        File outFile = new File("C:\\Users\\lawrence\\uk_hill\\maps\\refined_segment_summaries.tsv");
        FileOutputStream outputStream = new FileOutputStream(outFile);

        new ClimbSummaryWriter().writeClimbSummaries(climbSummaryValues, outputStream);

        int i = 0;
    }
}
