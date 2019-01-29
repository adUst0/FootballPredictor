package ivanov.boris.predictor.validation;

import ivanov.boris.predictor.classifier.knn.KNearestNeighbors;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.dataset.parser.DoubleDatasetParser;

import java.util.Collections;

public class KFoldCrossValidation {

    static double crossValidateKNN(int kSubsets, int kNeighbors,
                              Dataset<Double> dataset, boolean isLoggingEnabled) {

        Collections.shuffle(dataset.getEntries());

        int testingSetSize = dataset.getEntries().size() / kSubsets;

        double meanAccuracy = 0;

        for (int i = 0; i < dataset.getEntries().size(); i += testingSetSize) {
            Dataset<Double> trainingData = new Dataset<>();
            Dataset<Double> testingData = new Dataset<>();

            for (int j = 0; j < dataset.getEntries().size(); j++) {
                if (j >= i && j < i + testingSetSize) {
                    testingData.addEntry(dataset.getEntries().get(j));
                }
                else {
                    trainingData.addEntry(dataset.getEntries().get(j));
                }
            }

            KNearestNeighbors knn = new KNearestNeighbors();
            knn.buildModel(trainingData, kNeighbors);

            int correctPredictionsCount = 0;

            for (DatasetEntry entry : testingData.getEntries()) {
                String label = knn.classify(entry);
                if (label.equals(entry.getLabel())) {
                    correctPredictionsCount++;
                }
            }

            if (isLoggingEnabled) {
                System.out.println("kNeighbors = " + kNeighbors);
                System.out.println("\tCorrect predictions: " + correctPredictionsCount);
                System.out.println("\tIncorrect predictions: " + (testingSetSize - correctPredictionsCount));
                System.out.println("\t***Accuracy: " + ((double)correctPredictionsCount / testingSetSize * 100) + "%***");
            }

            meanAccuracy += (double)correctPredictionsCount / testingSetSize * 100;
        }

        return meanAccuracy / kSubsets;
    }

    /**
     * Perform K-Fold-Cross validation for different kNeighbors values and print accuracy to STDOUT
     */
    private static void chooseKNeighbors() {
        DoubleDatasetParser parser = new DoubleDatasetParser();

        Dataset<Double> dataset = parser.fromFile("Data/SelectedLeaguesOnly.data", "\\s+");
        TrainingDataPreprocessor.prepare(dataset);

        for (int k = 2; k <= 20; k++) {
            double accuracy = 0;
            int numberOfTests = 100;
            for (int j = 0; j < numberOfTests; j++) {
                accuracy += crossValidateKNN(10, k, dataset, false);
            }
            System.out.println("k = " + k + " => Accuracy: " + (accuracy / numberOfTests) + " %");
        }
    }

    public static void main(String[] args) {
        chooseKNeighbors();
    }
}
