package ivanov.boris.predictor.validation;

import ivanov.boris.predictor.classifier.Classifier;
import ivanov.boris.predictor.classifier.RandomGuess;
import ivanov.boris.predictor.classifier.knn.KNearestNeighbors;
import ivanov.boris.predictor.classifier.knn.SimpleProbability;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.dataset.parser.DoubleDatasetParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KFoldCrossValidation {

    private static final String PATH_TO_DATASET = "Data/SelectedLeaguesOnly.data";
    private static final int NUMBER_OF_TESTS = 100;

    private static double validate(int testingSetSize, Classifier<Double> classifier,
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
        classifier.buildModel(trainingData);

        int correctPredictions = 0;

        for (DatasetEntry<Double> entry : testingData.getEntries()) {
//            System.out.print(entry);
            String label = classifier.classify(entry);
            if (label.equals(entry.getLabel())) {
                correctPredictions++;
            }
        }

        double accuracy = (double)correctPredictions / testingSetSize * 100;

        if (isLoggingEnabled) {
            System.out.println("\tCorrect predictions: " + correctPredictions);
            System.out.println("\tIncorrect predictions: " + (testingSetSize - correctPredictions));
            System.out.println("\t***Accuracy: " + accuracy + "%***");
        }


        return accuracy;
    }

    /**
     * Perform validation for different kNeighbors values and print accuracy to STDOUT
     */
    private static void chooseKNeighbors() {
        DoubleDatasetParser parser = new DoubleDatasetParser();
        KNearestNeighbors knn = new KNearestNeighbors();

        for (int k = 2; k <= 20; k++) {
            double accuracy = 0;
            for (int j = 0; j < NUMBER_OF_TESTS; j++) {
                knn.setK(k);
                Dataset<Double> dataset = parser.fromFile(PATH_TO_DATASET, "\\s+");
                accuracy += validate((int)(dataset.size() * 0.1), knn, dataset, false);
            }
            System.out.println("k = " + k + " => Accuracy: " + (accuracy / NUMBER_OF_TESTS) + " %");
        }
    }

    private static void testClassifiers(List<Classifier<Double>> classifiers) {
        DoubleDatasetParser parser = new DoubleDatasetParser();

        for (Classifier<Double> classifier : classifiers) {
            double accuracy = 0;

            for (int i = 0; i < NUMBER_OF_TESTS; i++) {
                Dataset<Double> dataset = parser.fromFile(PATH_TO_DATASET, "\\s+");
                accuracy += validate((int)(dataset.size() * 0.1), classifier, dataset, false);
            }

            System.out.println(classifier.getClass() + " => Accuracy: " + (accuracy / NUMBER_OF_TESTS) + " %");
        }

    }

    public static void main(String[] args) {
//        chooseKNeighbors();

        List<Classifier<Double>> classifiers = new ArrayList<>();
        for (int k = 2; k <= 20; k++) {
            KNearestNeighbors knn = new KNearestNeighbors();
            knn.setK(k);
            classifiers.add(knn);
        }

        classifiers.add(new RandomGuess());
        classifiers.add(new SimpleProbability());

        testClassifiers(classifiers);
    }
}
