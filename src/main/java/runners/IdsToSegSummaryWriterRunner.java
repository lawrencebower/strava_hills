package runners;

import model.SegmentSummaryData;
import reader.IdReader;
import utils.SegmentInfoUtils;
import writer.SegmentSummaryWriter;

import java.io.*;
import java.util.List;

public class IdsToSegSummaryWriterRunner {

    public static void main(String[] args) throws FileNotFoundException {

        FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\small_segments.txt");

        List<Integer> segIds = new IdReader().readIds(inputStream);

        System.out.println("starting...");
        SegmentInfoUtils segmentInfoUtils = new SegmentInfoUtils();
        segmentInfoUtils.setupSession();
//        List<Integer> segmentIds = Arrays.asList(6665368, 6677446);
        List<SegmentSummaryData> segmentSummaryValues = segmentInfoUtils.getSegmentSummaryValues(segIds);
        System.out.println("...done");

        OutputStream outStream = new FileOutputStream(new File("C:\\Users\\lawrence\\uk_hill\\maps\\segment_stats.tsv"));

        new SegmentSummaryWriter().writeSegSummariesFile(segmentSummaryValues, outStream);

    }
}
