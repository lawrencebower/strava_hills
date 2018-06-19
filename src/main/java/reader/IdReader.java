package reader;

import model.SegInfo;

import java.io.InputStream;
import java.util.*;

public class IdReader {

    public ArrayList<SegInfo> readIds(InputStream stream) {

        Scanner scanner = new Scanner(stream);

        Map<Integer, SegInfo> results = new LinkedHashMap<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (!line.isEmpty()) {
                System.out.println(line);
                String[] tokens = line.split("\t");
                Integer segmentId = Integer.parseInt(tokens[1]);
                String seriesName = tokens[2];
                SegInfo segInfo = new SegInfo(segmentId, seriesName);

                checkAndAdd(results, segInfo);
            }
        }

        scanner.close();

        return new ArrayList<>(results.values());
    }

    private void checkAndAdd(Map<Integer, SegInfo> results, SegInfo segInfo) {

        Integer segmentId = segInfo.segmentId;

        if(!results.containsKey(segmentId)) {
            results.put(segmentId, segInfo);
        }else{
            SegInfo info = results.get(segmentId);
            info.seriesNames.add(segInfo.seriesNames.iterator().next());
        }
    }
}
