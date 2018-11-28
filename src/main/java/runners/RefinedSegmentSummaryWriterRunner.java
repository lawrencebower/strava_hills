package runners;

import model.RefinedSegmentSummaryData;
import model.SegmentAnnotation;
import model.SegmentSummaryData;
import reader.SegmentSummaryReader;
import utils.RefinedSegmentSummaryUtil;
import utils.SegmentSummaryUtils;
import writer.RefinedSegmentSummaryWriter;

import java.io.*;
import java.util.List;
import java.util.Map;

public class RefinedSegmentSummaryWriterRunner {

    public static void main(String[] args) throws FileNotFoundException {

        FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\software\\strava\\src\\main\\resources\\output\\spreadsheets\\segment_stats.tsv");
//        FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\box_hill.tsv");
//            FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\small_segment_stats.tsv");
        List<SegmentSummaryData> segmentSummaryValues = new SegmentSummaryReader().readSummaryFile(inputStream);

        SegmentSummaryUtils segUtils = new SegmentSummaryUtils();
        Map<String, SegmentAnnotation> annotations = segUtils.readSegmentAnnotationFile();

        RefinedSegmentSummaryUtil refinedSegmentSummaryUtil = new RefinedSegmentSummaryUtil();
        Map<String, RefinedSegmentSummaryData> refinedSummaryData =
                refinedSegmentSummaryUtil.segmentSummaryToRefinedSegmentSummary(segmentSummaryValues, annotations);

//        File outFile = new File("C:\\Users\\lawrence\\uk_hill\\maps\\box_hill_summary.tsv");
//        File outFile = new File("C:\\Users\\lawrence\\uk_hill\\maps\\refined_segment_summaries.tsv");
        File outFile = new File("C:\\Users\\lawrence\\software\\strava\\src\\main\\resources\\output\\spreadsheets\\\\refined_segment_summaries.tsv");
        FileOutputStream outputStream = new FileOutputStream(outFile);

        RefinedSegmentSummaryData refinedSegmentSummaryData = refinedSummaryData.get("13952286");

        new RefinedSegmentSummaryWriter().writeClimbSummaries(refinedSummaryData, outputStream);
    }
}
