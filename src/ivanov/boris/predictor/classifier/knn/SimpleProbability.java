package ivanov.boris.predictor.classifier.knn;

import ivanov.boris.predictor.FixtureStats;
import ivanov.boris.predictor.classifier.Classifier;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

import java.util.Map;

public class SimpleProbability implements Classifier<Double> {

    @Override
    public void buildModel(Dataset dataset) {
        // Do nothing
    }

    @Override
    public String classify(DatasetEntry<Double> entry) {
        Map<String, Double> probabilities = FixtureStats.fromDatasetEntry(entry).getOutcomeProbabilities();

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
