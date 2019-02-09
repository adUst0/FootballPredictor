package ivanov.boris.predictor.classifier.other;

import ivanov.boris.predictor.Fixture;
import ivanov.boris.predictor.TrainingDataPreprocessor;
import ivanov.boris.predictor.classifier.Classifier;
import ivanov.boris.predictor.classifier.knn.KNearestNeighbors;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

import java.util.Map;

public class FootballPredictor implements Classifier {

    private static final int K_NEIGHBORS_DEFAULT = 3;
    private static final double SIMPLE_PROBABILITY_THRESHOLD = 0.65;

    private KNearestNeighbors kNN =  new KNearestNeighbors();

    public FootballPredictor() {

    }

    public FootballPredictor(int kNeighbors) {
        kNN.setK(kNeighbors);
    }

    @Override
    public void buildModel(Dataset dataset) {
        TrainingDataPreprocessor.prepare(dataset);
        kNN.buildModel(dataset, K_NEIGHBORS_DEFAULT);
    }

    @Override
    public String classify(DatasetEntry entry) {
        Fixture fixture = Fixture.fromDatasetEntry(entry);

        Map<String, Double> probabilities = fixture.getOutcomeProbabilities();
        var iterator = probabilities.entrySet().iterator();

        Map.Entry<String, Double> mostProbableOutcome = iterator.next();

        while (iterator.hasNext()) {
            Map.Entry<String, Double> currentOutcome = iterator.next();

            if (currentOutcome.getValue() > mostProbableOutcome.getValue()) {
                mostProbableOutcome = currentOutcome;
            }
        }

        if (mostProbableOutcome.getValue() > SIMPLE_PROBABILITY_THRESHOLD) {
            return mostProbableOutcome.getKey();
        }
        else {
            return kNN.classify(entry);
        }
    }

    public void predictAll(Dataset toPredict) {
        for (DatasetEntry entry : toPredict.getEntries()) {
            System.out.println(entry.getAttributes() + " " + entry.getLabel());
            System.out.println("\tPrediction: " + kNN.classify(entry));
        }
    }
}
