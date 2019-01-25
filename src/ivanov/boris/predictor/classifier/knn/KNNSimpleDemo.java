package ivanov.boris.predictor.classifier.knn;

import ivanov.boris.predictor.classifier.Classifier;
import ivanov.boris.predictor.classifier.knn.KNearestNeighbors;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.dataset.parser.DoubleDatasetParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KNNSimpleDemo {

    private static final int TESTING_SET_SIZE = 5;

    public static void main(String[] args) {
        DoubleDatasetParser parser = new DoubleDatasetParser();

        Dataset<Double> dataset = parser.fromFile("Data/iris.footballapi", ",");

        Collections.shuffle(dataset.getEntries());

        Dataset<Double> trainingData = new Dataset<>();
        Dataset<Double> testingData = new Dataset<>();

        int testingSetSize;
//        testingSetSize = TESTING_SET_SIZE;
        testingSetSize = (int) (dataset.size() * 0.1);

        for (int i = 0; i < dataset.size(); i++) {
            DatasetEntry<Double> entry = dataset.getEntries().get(i);

            if (i < testingSetSize) {
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
        System.out.println("Incorrect predictions: " + (testingSetSize - correctPredictions));
        System.out.println("Accuracy: " + ((float)correctPredictions / testingSetSize * 100) + "%");
    }

}
