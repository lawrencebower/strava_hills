package reader;

import model.RefinedSegmentSummaryData;
import model.SegmentAnnotation;
import model.SegmentSummaryData;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class SegmentAnnotationReader {

    public Map<String, SegmentAnnotation> readAnnotations(InputStream stream) {

        Scanner scanner = new Scanner(stream);

        Map<String, SegmentAnnotation> results = new LinkedHashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (!line.isEmpty()) {
                System.out.println(line);
                String[] tokens = StringUtils.splitPreserveAllTokens(line, "\t");

                String segmentId = tokens[0];
                String segmentName = tokens[1];

                Float maxGrad = null;
                if (!tokens[2].isEmpty()) {
                    maxGrad = Float.parseFloat(tokens[2]);
                }

                Integer difficulty = null;
                if (!tokens[3].isEmpty()) {
                    difficulty = Integer.parseInt(tokens[3]);
                }

                String url = null;
                if (!tokens[4].isEmpty()) {
                    url = tokens[4];
                }

                SegmentAnnotation segInfo = new SegmentAnnotation(
                        segmentId,
                        segmentName,
                        maxGrad,
                        difficulty,
                        url);

                checkAndAdd(results, segInfo);
            }
        }

        scanner.close();

        return results;
    }

    private void checkAndAdd(Map<String, SegmentAnnotation> results, SegmentAnnotation segInfo) {

        String segmentId = segInfo.segmentId;

        if (!results.containsKey(segmentId)) {
            results.put(segmentId, segInfo);
        } else {
            System.out.println("Duplicate - " + segmentId);
        }
    }
}
