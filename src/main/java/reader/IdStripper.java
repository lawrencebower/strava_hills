package reader;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class IdStripper {

    public void processFile(String fileString, PrintWriter writer) throws IOException {

        InputStream ins = null; // raw byte-stream
        Reader r = null; // cooked reader
        BufferedReader br = null; // buffered for readLine()
        try {

            String lineString;
            File file = new File(fileString);
            ins = new FileInputStream(file);
            r = new InputStreamReader(ins, "UTF-8"); // leave charset out for default
            br = new BufferedReader(r);

            String seriesName = getSeriesNameFromFileName(file.getName());

            Map<String, String> results = new LinkedHashMap<>();
            while ((lineString = br.readLine()) != null) {
                if (lineString.contains("https://veloviewer.com/segment")) {
                    processLine(lineString, seriesName, results);
                }
            }

            printResults(results, writer);

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Throwable t) { /* ensure close happens */ }
            }
            if (r != null) {
                try {
                    r.close();
                } catch (Throwable t) { /* ensure close happens */ }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (Throwable t) { /* ensure close happens */ }
            }
        }
    }

    private String getSeriesNameFromFileName(String name) {

        String prefix = name.split(".html")[0];
        if (prefix.contains("_")) {
            prefix = prefix.substring(0, prefix.indexOf("_"));
        }

        return prefix;
    }

    private void printResults(Map<String, String> results, PrintWriter writer) {
        for (String string : results.values()) {
            writer.print(string);
        }
    }

    private Map<String, String> processLine(String lineString,
                                            String seriesName,
                                            Map<String, String> results) {

        String[] strings = lineString.split("</th>");
        for (String string : strings) {
            if (string.contains("segment")) {
                processSegmentString(
                        string,
                        seriesName,
                        results);
            }
        }

        return results;
    }

    private void processSegmentString(String segmentString,
                                      String seriesName,
                                      Map<String, String> results) {

        String trimmed = segmentString.trim();
        String url = trimmed.split("<a")[1];
        String[] urlTokens = url.split(" ");
        url = urlTokens[2];
        urlTokens = url.split("/");
        String segId = urlTokens[4];
        segId = segId.replace("\"", "");
        String[] tokens = trimmed.split("</a>");
        String name = tokens[1];
        String formatted = String.format("%s\t%s\t%s\n", name, segId, seriesName);
        System.out.print(formatted);

        results.put(segId, formatted);
    }
}
