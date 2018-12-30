package ivanov.boris.predictor;

import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.dataset.parser.DoubleDatasetParser;

import java.util.Collections;

public class Main {

    private static final int TESTING_SET_SIZE = 20;

    public static void main(String[] args) {
        DoubleDatasetParser parser = new DoubleDatasetParser();

        Dataset<Double> dataset = parser.fromFile("Data/iris.data");

        Collections.shuffle(dataset.getEntries());

        Dataset<Double> trainingData = new Dataset<>();
        Dataset<Double> testingData = new Dataset<>();

        for (int i = 0; i < dataset.size(); i++) {
            DatasetEntry<Double> entry = dataset.getEntries().get(i);

            if (i < TESTING_SET_SIZE) {
                testingData.addEntry(new DatasetEntry<>(entry));
            }
            else {
                trainingData.addEntry(new DatasetEntry<>(entry));
            }
        }

        Classifier<Double> classifier = new KNearestNeighbors();
        classifier.buildModel(trainingData);

        int correctPredictions = 0;

        for (DatasetEntry entry : testingData.getEntries()) {
            String label = classifier.classify(entry);
            if (label.equals(entry.getLabel())) {
                correctPredictions++;
            }
        }

        System.out.println("Correct predictions: " + correctPredictions);
        System.out.println("Incorrect predictions: " + (TESTING_SET_SIZE - correctPredictions));
        System.out.println("Accuracy: " + ((float)correctPredictions / TESTING_SET_SIZE * 100) + "%");
    }

}
