package reader;

import model.SegmentSummaryData;
import utils.PolyUtil;
import utils.SegmentSummaryUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SegmentSummaryReader {

    public List<SegmentSummaryData> readSummaryFile(InputStream inputStream) {
        return readDetailsStream(inputStream);
    }

    private List<SegmentSummaryData> readDetailsStream(InputStream inputStream) {

        Scanner scanner = new Scanner(inputStream);

        ArrayList<SegmentSummaryData> segmentSummaryValues = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (!line.isEmpty()) {
//                System.out.println("line = " + line);
                String[] tokens = line.split("\t");
                SegmentSummaryData values = new SegmentSummaryData();
                values.id = tokens[0];
                values.name = tokens[1];
                values.city = tokens[2];
                values.seriesNames = mapSeriesNames(tokens[3]);
                values.category = tokens[4];
                values.distance = Float.parseFloat(tokens[5]);
                values.averageGrad = Float.parseFloat(tokens[6]);
                values.maxGrad = Float.parseFloat(tokens[7]);
                values.elevation = Float.parseFloat(tokens[8]);
                values.leaderTime = tokens[9];
                values.startCoordinate = PolyUtil.stringToLatLong(tokens[10]);
                values.altitudeValues = SegmentSummaryUtils.stringsToFloats(tokens[11]);
                values.distanceValues = SegmentSummaryUtils.stringsToFloats(tokens[12]);
                values.checkAltAndDistLengths();
                values.polyline = tokens[13];

                segmentSummaryValues.add(values);
            }
        }

        scanner.close();

        return segmentSummaryValues;
    }

    private List<String> mapSeriesNames(String token) {
        return SegmentSummaryUtils.stringsToList(token);
    }
}
