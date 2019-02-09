package ivanov.boris.predictor.classifier.other;

import ivanov.boris.predictor.classifier.Classifier;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

import java.util.*;

public class RandomGuess implements Classifier {
    private List<String> possibleLabels = new ArrayList<>();
    private Random random = new Random();

    @Override
    public void buildModel(Dataset dataset) {
        Set<String> labels = new HashSet<>();

        for (DatasetEntry entry : dataset.getEntries()) {
            labels.add(entry.getLabel());
        }

        possibleLabels = new ArrayList<>(labels);
    }

    @Override
    public String classify(DatasetEntry entry) {
        return possibleLabels.get(random.nextInt(possibleLabels.size()));
    }
}
