package runners;

import model.SegInfo;
import model.SegmentSummaryData;
import reader.IdReader;
import utils.SegmentSummaryUtils;
import writer.SegmentSummaryWriter;

import java.io.*;
import java.util.ArrayList;

public class IdsToSegSummaryWriterRunner {

    private static int requests;

    public static void main(String[] args) throws FileNotFoundException {

//        FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\small_segments.tsv");
        FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\software\\strava\\src\\main\\resources\\output\\spreadsheets\\segments.tsv");

        ArrayList<SegInfo> segInfos = new IdReader().readIds(inputStream);

        System.out.println("starting...");
        SegmentSummaryUtils segmentSummaryUtils = new SegmentSummaryUtils();
        segmentSummaryUtils.setupSession();

        SegmentSummaryWriter summaryWriter = new SegmentSummaryWriter();
        OutputStream outStream = new FileOutputStream(new File("C:\\Users\\lawrence\\software\\strava\\src\\main\\resources\\output\\spreadsheets\\segment_stats.tsv"));
        summaryWriter.prepareWriter(outStream);

        requests = 0;
        int count = 0;
        int totalCount = segInfos.size();

        for (SegInfo segInfo : segInfos) {

            count++;
            requests++;

            SegmentSummaryData segmentSummaryValues = segmentSummaryUtils.getSegmentSummaryValues(segInfo);
            summaryWriter.writeSegSummaryLine(segmentSummaryValues);

            System.out.println(String.format("%s/%s - %s (%s)", count, totalCount, segmentSummaryValues.name, requests));

            checkRequestsCount();
        }

        summaryWriter.closeWriter();

        System.out.println("...done");
    }

    private static void checkRequestsCount() {

        if (requests == 180) {
            try {
                System.out.println("Hit maximum, sleeping...");
                Thread.sleep(900000);
                requests = 0;
                System.out.println("...continuing");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
