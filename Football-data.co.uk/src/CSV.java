import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSV {
    private List<String> attributes = new ArrayList<>();
    private List<Map<String, String>> entries = new ArrayList<>();

    public CSV(String fileName) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String line = reader.readLine();
            long linesCount = 0;

            if (line == null) {
                return;
            } else {
                attributes.addAll(List.of(line.split(",")));
                linesCount++;
            }

            line = reader.readLine();
            while (line != null) {
                linesCount++;

                if (line.isEmpty()) {
                    line = reader.readLine();
                    continue;
                }

                List<String> attributes = List.of(line.split(","));

                if (attributes.size() != this.attributes.size()) {
                    throw new InvalidFormatException(
                            String.format("Line %d contains more/less attributes than expected", linesCount));
                }

                Map<String, String> entry = new LinkedHashMap<>();

                for (int i = 0; i < attributes.size(); i++) {
                    entry.put(this.attributes.get(i), attributes.get(i));
                }

                entries.add(entry);

                line = reader.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, String>> getEntries() {
        return entries;
    }
}
