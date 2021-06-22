package utils;

import reader.SegmentSynonymReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class SegmentSynonymUtils {

    public Map<String, String> readSegmentSynonymsFile() {

        try {
            FileInputStream synonymStream = new FileInputStream("/home/lb584/git/strava_hills/src/main/resources/output/spreadsheets/segment_synonyms.tsv");
            SegmentSynonymReader annotationReader = new SegmentSynonymReader();
            return annotationReader.readSegmentSynonyms(synonymStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
