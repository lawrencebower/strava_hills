package reader;

import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class SegmentSynonymReader {

    public Map<String, String> readSegmentSynonyms(FileInputStream synonymStream) {

        Map<String, String> synonyms = readSynonyms(synonymStream);

        return synonyms;
    }

    public Map<String, String> readSynonyms(InputStream stream) {

        Scanner scanner = new Scanner(stream);

        Map<String, String> results = new LinkedHashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (!line.isEmpty()) {
                String[] tokens = StringUtils.splitPreserveAllTokens(line, "\t");

                String segmentSynonym = tokens[0];
                String segmentId = tokens[1];

                results.put(segmentId, segmentSynonym);
            }
        }

        scanner.close();

        return results;
    }
}
