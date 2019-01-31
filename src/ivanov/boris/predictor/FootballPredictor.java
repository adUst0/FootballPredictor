package ivanov.boris.predictor;

import ivanov.boris.predictor.classifier.Classifier;
import ivanov.boris.predictor.classifier.knn.KNearestNeighbors;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

public class FootballPredictor implements Classifier<Double> {

    private static final int K_NEIGHBORS = 3;
    private static final double SIMPLE_PROBABILITY_THRESHOLD = 0.65;

    private KNearestNeighbors kNN =  new KNearestNeighbors();

    public FootballPredictor(int kNeighbors) {
        kNN.setK(kNeighbors);
    }

    public FootballPredictor() {

    }

    @Override
    public void buildModel(Dataset<Double> dataset) {
        TrainingDataPreprocessor.prepare(dataset);
        kNN.buildModel(dataset, K_NEIGHBORS);
    }

    @Override
    public String classify(DatasetEntry<Double> entry) {
        Fixture fixture = Fixture.fromDatasetEntry(entry);

        var probabilities = fixture.getOutcomeProbabilities();
        var iterator = probabilities.entrySet().iterator();
        var mostProbableOutcome = iterator.next();

        while (iterator.hasNext()) {
            var currentOutcome = iterator.next();

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

    public void predictAll(Dataset<Double> toPredict) {
        for (DatasetEntry entry : toPredict.getEntries()) {
            System.out.println(entry.getAttributes() + " " + entry.getLabel());
            System.out.println("\tPrediction: " + kNN.classify(entry));
        }
    }
}
