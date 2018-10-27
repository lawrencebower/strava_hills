package reader;

import model.SegmentSummaryData;
import model.Series;
import utils.PolyUtil;
import utils.SegmentSummaryUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SegmentSummaryReader {

    public List<SegmentSummaryData> readSummaryAndAnnotationFile(InputStream inputStream) {
        List<SegmentSummaryData> segmentSummaryData = readSegmentSummaryStream(inputStream);
        return segmentSummaryData;
    }

    private List<SegmentSummaryData> readSegmentSummaryStream(InputStream inputStream) {

        Scanner scanner = new Scanner(inputStream);

        ArrayList<SegmentSummaryData> segmentSummaryValues = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (!line.isEmpty()) {
//                System.out.println("line = " + line);
                String[] tokens = line.split("\t");
                SegmentSummaryData segmentSummaryData = new SegmentSummaryData();
                segmentSummaryData.id = tokens[0];

                if(segmentSummaryData.id.equals("3874601")){
                    int i = 0;
                }

                segmentSummaryData.name = tokens[1];
                segmentSummaryData.city = tokens[2];
                segmentSummaryData.seriesNames = mapSeriesNames(tokens[3]);
                segmentSummaryData.category = tokens[4];
                segmentSummaryData.distance = Float.parseFloat(tokens[5]);
                segmentSummaryData.averageGrad = Float.parseFloat(tokens[6]);
                segmentSummaryData.elevation = Float.parseFloat(tokens[8]);
                segmentSummaryData.leaderTime = tokens[9];
                segmentSummaryData.startCoordinate = PolyUtil.stringToLatLong(tokens[10]);
                segmentSummaryData.altitudeValues = SegmentSummaryUtils.stringsToFloats(tokens[11]);
                segmentSummaryData.distanceValues = SegmentSummaryUtils.stringsToFloats(tokens[12]);
                segmentSummaryData.checkAltAndDistLengths();
                segmentSummaryData.polyline = tokens[13];

                segmentSummaryValues.add(segmentSummaryData);
            }
        }

        scanner.close();

        return segmentSummaryValues;
    }

    private List<Series> mapSeriesNames(String token) {

        List<String> strings = SegmentSummaryUtils.stringsToList(token);
        List<Series> results = new ArrayList<>();

        for (String string : strings) {
            Series seriesEnum = Series.codeToSeries(string);
            results.add(seriesEnum);
        }

        return results;
    }
}
