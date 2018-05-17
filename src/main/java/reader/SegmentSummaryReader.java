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
                System.out.println("line = " + line);
                String[] tokens = line.split("\t");
                SegmentSummaryData values = new SegmentSummaryData();
                values.id = tokens[0];
                values.name = tokens[1];
                values.city = tokens[2];
                values.seriesNames = mapSeriesNames(tokens[3]);
                values.startCoordinate = PolyUtil.stringToLatLong(tokens[4]);
                values.altitudeValues = SegmentSummaryUtils.stringsToFloats(tokens[5]);
                values.distanceValues = SegmentSummaryUtils.stringsToFloats(tokens[6]);
                values.polyline = tokens[6];

                segmentSummaryValues.add(values);
            }
        }

        scanner.close();

        return segmentSummaryValues;
    }

    private List<String> mapSeriesNames(String token) {
        return null;
    }
}
