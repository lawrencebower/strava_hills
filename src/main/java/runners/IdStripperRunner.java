package runners;

import reader.IdStripper;

import java.io.*;

public class IdStripperRunner {

    public static void main(String[] args) throws IOException {

        File htmlDir = new File(args[0]);

        File[] files = htmlDir.listFiles((dir, name) -> {
            return name.endsWith(".html");
//            return name.startsWith("1_");
        });

        IdStripper idStripper = new IdStripper();

        FileWriter fileWriter = new FileWriter(args[1]);
        PrintWriter writer = new PrintWriter(fileWriter);

        for (File file : files) {
            System.out.println("On file " + file);
            idStripper.processFile(file.getPath(), writer);
        }

        writer.flush();
        writer.close();
    }
}
