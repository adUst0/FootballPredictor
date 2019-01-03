package ivanov.boris.predictor;

import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.dataset.parser.DoubleDatasetParser;
import ivanov.boris.predictor.knn.KNearestNeighbors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    private static final int TESTING_SET_SIZE = 5;

    private static void testKNN() {
        DoubleDatasetParser parser = new DoubleDatasetParser();

        Dataset<Double> dataset = parser.fromFile("Data/iris.data", ",");

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

    public static void main(String[] args) {
        DoubleDatasetParser parser = new DoubleDatasetParser();

        Dataset<Double> dataset = parser.fromFile("Data/football.data", "\\s+");
        Classifier<Double> classifier = new KNearestNeighbors();
        classifier.buildModel(dataset);


        Dataset<Double> toPredict = parser.fromFile("Data/topredict.data", "\\s+");

        for (DatasetEntry entry : dataset.getEntries()) {
            List<Double> attributes = entry.getAttributes();
            double[] wdl = {attributes.get(1), attributes.get(2), attributes.get(3)};
            int games = (int)(wdl[0] + wdl[1] + wdl[2]);
            attributes.set(1, attributes.get(1) / games);
            attributes.set(2, attributes.get(2) / games);
            attributes.set(3, attributes.get(3) / games);
        }

        for (DatasetEntry entry : toPredict.getEntries()) {
            List<Double> attributes = entry.getAttributes();
            double[] wdl = {attributes.get(1), attributes.get(2), attributes.get(3)};
            int games = (int)(wdl[0] + wdl[1] + wdl[2]);
            attributes.set(1, attributes.get(1) / games);
            attributes.set(2, attributes.get(2) / games);
            attributes.set(3, attributes.get(3) / games);
        }

        System.out.println(classifier.classify(toPredict.getEntries().get(0)));
    }

}
