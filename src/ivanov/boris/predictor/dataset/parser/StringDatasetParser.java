package ivanov.boris.predictor.dataset.parser;

import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class StringDatasetParser implements DatasetParser<String> {

    @Override
    public Dataset<String> fromFile(String fileName) {
        Dataset<String> dataset = new Dataset<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.isEmpty() || line.charAt(0) == '@' || line.charAt(0) == '%') {

                    line = bufferedReader.readLine();
                    continue;
                }

                line = line.replaceAll("'", "");

                List<String> entry = Arrays.asList(line.split(","));
                String response = entry.get(entry.size() - 1);
                dataset.addEntry(new DatasetEntry<>(entry.subList(0, entry.size() - 1), response));

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }

}
