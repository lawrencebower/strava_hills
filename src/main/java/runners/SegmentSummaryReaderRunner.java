package runners;

import model.SegmentSummaryData;
import reader.SegmentSummaryReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class SegmentSummaryReaderRunner {

    public static void main(String[] args) throws FileNotFoundException {

//        FileInputStream inputStream = new FileInputStream("C:\Users\lawrence\software\strava\src\main\resources\output\spreadsheets\\segment_stats.tsv");
        FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\software\\strava\\src\\main\\resources\\output\\spreadsheets\\small_segment_stats.tsv");
        List<SegmentSummaryData> segmentSummaryValues = new SegmentSummaryReader().readSummaryFile(inputStream);

        int i = 0;
    }
}
