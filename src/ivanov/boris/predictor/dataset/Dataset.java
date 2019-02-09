package ivanov.boris.predictor.dataset;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dataset {
    private List<DatasetEntry> entries = new ArrayList<>();

    public void addEntry(DatasetEntry entry) {
        entries.add(entry);
    }

    public int size() {
        return entries.size();
    }

    public int getAttributesCount() {
        if (entries.size() == 0) {
            return 0;
        }

        return entries.get(0).getAttributes().size();
    }

    public List<DatasetEntry> getEntries() {
        return entries;
    }

    public static Dataset fromFile(String fileName, String delimiter) {
        Dataset dataset = new Dataset();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.isEmpty() || line.charAt(0) == '@' || line.charAt(0) == '%') {

                    line = bufferedReader.readLine();
                    continue;
                }

                List<String> entry = Arrays.asList(line.split(delimiter));
                String label = entry.get(entry.size() - 1);

                dataset.addEntry(new DatasetEntry(entry.subList(0, entry.size() - 1), label));

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }

}
