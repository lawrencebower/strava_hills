package reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IdReader {

    public List<Integer> readIds(InputStream stream) {

        Scanner scanner = new Scanner(stream);

        ArrayList<Integer> segmentIds = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (!line.isEmpty()) {
                System.out.println("line = " + line);
                String[] tokens = line.split("\t");
                Integer segmentId = Integer.parseInt(tokens[1]);
                segmentIds.add(segmentId);
            }
        }

        scanner.close();

        return segmentIds;
    }
}
