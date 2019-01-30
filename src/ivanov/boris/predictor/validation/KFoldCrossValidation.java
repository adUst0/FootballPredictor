package ivanov.boris.predictor.validation;

import ivanov.boris.predictor.classifier.knn.KNearestNeighbors;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.dataset.parser.DoubleDatasetParser;

import java.util.Collections;

public class KFoldCrossValidation {

    static double validateKNN(int testingSetSize, int kNeighbors,
                              Dataset<Double> dataset, boolean isLoggingEnabled) {

        Collections.shuffle(dataset.getEntries());


        Dataset<Double> trainingData = new Dataset<>();
        Dataset<Double> testingData = new Dataset<>();

        for (int i = 0; i < dataset.size(); i++) {
            if (i < testingSetSize) {
                testingData.addEntry(dataset.getEntries().get(i));
            }
            else {
                trainingData.addEntry(dataset.getEntries().get(i));
            }
        }

        TrainingDataPreprocessor.prepare(trainingData);
        KNearestNeighbors knn = new KNearestNeighbors();
        knn.buildModel(trainingData, kNeighbors);

        int correctPredictions = 0;
        for (DatasetEntry entry : testingData.getEntries()) {
            String label = knn.classify(entry);
            if (label.equals(entry.getLabel())) {
                correctPredictions++;
            }
        }

        if (isLoggingEnabled) {
            System.out.println("kNeighbors = " + kNeighbors);
            System.out.println("\tCorrect predictions: " + correctPredictions);
            System.out.println("\tIncorrect predictions: " + (testingSetSize - correctPredictions));
            System.out.println("\t***Accuracy: " + ((double)correctPredictions / testingSetSize * 100) + "%***");
        }


        return (double)correctPredictions / testingSetSize * 100;
    }

    /**
     * Perform K-Fold-Cross validation for different kNeighbors values and print accuracy to STDOUT
     */
    private static void chooseKNeighbors() {
        DoubleDatasetParser parser = new DoubleDatasetParser();

        Dataset<Double> dataset = parser.fromFile("Data/SelectedLeaguesOnly.data", "\\s+");

        for (int k = 2; k <= 20; k++) {
            double accuracy = 0;
            int numberOfTests = 1000;
            for (int j = 0; j < numberOfTests; j++) {
                accuracy += validateKNN((int)(dataset.size() * 0.1), k, dataset, false);
            }
            System.out.println("k = " + k + " => Accuracy: " + (accuracy / numberOfTests) + " %");
        }
    }

    public static void main(String[] args) {
        chooseKNeighbors();
    }
}
