package ivanov.boris.predictor.dataset.parser;

import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoubleDatasetParser implements DatasetParser<Double> {

    @Override
    public Dataset<Double> fromFile(String fileName, String delimiter) {
        Dataset<Double> dataset = new Dataset<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.isEmpty() || line.charAt(0) == '@' || line.charAt(0) == '%') {

                    line = bufferedReader.readLine();
                    continue;
                }

                List<String> words = Arrays.asList(line.split(delimiter));
                String label = words.get(words.size() - 1);

                List<Double> entry = new ArrayList<>();
                words.subList(0, words.size() - 1).forEach(x -> entry.add(Double.parseDouble(x)));

                dataset.addEntry(new DatasetEntry<>(entry, label));

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }
}
