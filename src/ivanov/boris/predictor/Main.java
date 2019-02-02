package ivanov.boris.predictor;

import ivanov.boris.predictor.classifier.Classifier;
import ivanov.boris.predictor.classifier.other.RandomGuess;
import ivanov.boris.predictor.classifier.knn.KNearestNeighbors;
import ivanov.boris.predictor.classifier.other.SimpleProbability;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.dataset.parser.DoubleDatasetParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    private static final String PATH_TO_DATASET = "Data/SelectedLeaguesOnly.data";
    private static final int NUMBER_OF_TESTS = 100;

    public static double validateClassifier(int testingSetSize, Classifier<Double> classifier,
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

    public static void testClassifiers(List<Classifier<Double>> classifiers) {
        DoubleDatasetParser parser = new DoubleDatasetParser();

        for (Classifier<Double> classifier : classifiers) {
            double accuracy = 0;

            for (int i = 0; i < NUMBER_OF_TESTS; i++) {
                Dataset<Double> dataset = parser.fromFile(PATH_TO_DATASET, "\\s+");
                accuracy += validateClassifier((int)(dataset.size() * 0.1), classifier, dataset, false);
            }

            System.out.println(classifier.getClass() + " => Accuracy: " + (accuracy / NUMBER_OF_TESTS) + " %");
        }

    }

    public static void testAllClassifiers() {
        List<Classifier<Double>> classifiers = new ArrayList<>();
        for (int k = 4; k <= 4; k++) {
            KNearestNeighbors knn = new KNearestNeighbors();
            knn.setK(k);
            classifiers.add(knn);
        }

        classifiers.add(new RandomGuess());
        classifiers.add(new SimpleProbability());

        for (int k = 4; k <= 4; k++) {
            classifiers.add(new FootballPredictor(k));
        }

        testClassifiers(classifiers);
    }

    public static void main(String[] args) {
        testAllClassifiers();

    }
}
