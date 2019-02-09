package ivanov.boris.predictor.classifier.other;

import ivanov.boris.predictor.Fixture;
import ivanov.boris.predictor.classifier.Classifier;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

import java.util.Map;

public class SimpleProbability implements Classifier {

    @Override
    public void buildModel(Dataset dataset) {
        // Do nothing
    }

    @Override
    public String classify(DatasetEntry entry) {
        Map<String, Double> probabilities = Fixture.fromDatasetEntry(entry).getOutcomeProbabilities();

        var iterator = probabilities.entrySet().iterator();
        var mostProbableOutcome = iterator.next();

        while (iterator.hasNext()) {
            var currentOutcome = iterator.next();

            if (currentOutcome.getValue() > mostProbableOutcome.getValue()) {
                mostProbableOutcome = currentOutcome;
            }
        }

        return mostProbableOutcome.getKey();
    }
}
