package ivanov.boris.predictor.classifier;

import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

import java.util.*;

public class RandomGuess implements Classifier<Double> {
    private List<String> possibleLabels = new ArrayList<>();
    private Random random = new Random();

    @Override
    public void buildModel(Dataset<Double> dataset) {
        Set<String> labels = new HashSet<>();

        for (DatasetEntry entry : dataset.getEntries()) {
            labels.add(entry.getLabel());
        }

        possibleLabels = new ArrayList<String>();
        possibleLabels.addAll(labels);
    }

    @Override
    public String classify(DatasetEntry<Double> entry) {
        return possibleLabels.get(random.nextInt(possibleLabels.size()));
    }
}
