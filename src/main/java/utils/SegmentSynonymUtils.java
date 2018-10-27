package utils;

import reader.SegmentSynonymReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class SegmentSynonymUtils {

    public Map<String, String> readSegmentSynonymsFile() {

        try {
            FileInputStream synonymStream = new FileInputStream("C:\\Users\\lawrence\\uk_hill\\maps\\segment_synonyms.tsv");
            SegmentSynonymReader annotationReader = new SegmentSynonymReader();
            return annotationReader.readSegmentSynonyms(synonymStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
