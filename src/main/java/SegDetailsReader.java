import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SegDetailsReader {

    public static void main(String[] args) {
        List<SegmentSummaryValues> segmentSummaryValues = new SegDetailsReader().readDetailsFile();
        int i = 0;
    }

    private List<SegmentSummaryValues> readDetailsFile() {

        try {
            FileInputStream inputStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\segment_stats.tsv");
            List<SegmentSummaryValues> segmentSummaryValues = readDetailsStream(inputStream);
            return segmentSummaryValues;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<SegmentSummaryValues> readDetailsStream(FileInputStream inputStream) {

        Scanner scanner = new Scanner(inputStream);

        ArrayList<SegmentSummaryValues> segmentSummaryValues = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (!line.isEmpty()) {
                System.out.println("line = " + line);
                String[] tokens = line.split("\t");
                SegmentSummaryValues values = new SegmentSummaryValues();
                values.name = tokens[0];
                values.description = tokens[1];
                values.series = tokens[2];
                values.startCoordinate = PolyUtil.stringToLatLong(tokens[3]);
                values.lineCoordinates = PolyUtil.stringsToLatLong(tokens[4]);

                segmentSummaryValues.add(values);
            }
        }

        scanner.close();

        return segmentSummaryValues;
    }
}
