package ivanov.boris.predictor;

import ivanov.boris.predictor.classifier.Classifier;
import ivanov.boris.predictor.classifier.other.FootballPredictor;
import ivanov.boris.predictor.classifier.other.RandomGuess;
import ivanov.boris.predictor.classifier.knn.KNearestNeighbors;
import ivanov.boris.predictor.classifier.other.SimpleProbability;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    private static final String PATH_TO_DATASET = "Data/SelectedLeaguesOnly.data";
    private static final int NUMBER_OF_TESTS = 1000;
    private static final int NUMBER_OF_THREADS = 8;
    private static final double TESTING_SET_PERCENT = 0.05;
    private static final int KNN_MIN_K = 2;
    private static final int KNN_MAX_K = 4;
    private static final boolean IS_LOGGING_ENABLED = false;

    private static double validateClassifier(int testingSetSize, Classifier classifier,
                                             Dataset dataset, boolean isLoggingEnabled) {

        Collections.shuffle(dataset.getEntries());

        Dataset trainingData = new Dataset();
        Dataset testingData = new Dataset();

        for (int i = 0; i < dataset.size(); i++) {
            if (i < testingSetSize) {
                testingData.addEntry(dataset.getEntries().get(i));
            } else {
                trainingData.addEntry(dataset.getEntries().get(i));
            }
        }

        TrainingDataPreprocessor.prepare(trainingData);
        classifier.buildModel(trainingData);

        int correctPredictions = 0;

        for (DatasetEntry entry : testingData.getEntries()) {
//            System.out.print(entry);
            String label = classifier.classify(entry);
            if (label.equals(entry.getLabel())) {
                correctPredictions++;
            }
        }

        double accuracy = (double) correctPredictions / testingSetSize * 100;

        if (isLoggingEnabled) {
            System.out.println("\tCorrect predictions: " + correctPredictions);
            System.out.println("\tIncorrect predictions: " + (testingSetSize - correctPredictions));
            System.out.println("\t***Accuracy: " + accuracy + "%***");
        }


        return accuracy;
    }

    private static void runFootballPredictor() throws ExecutionException, InterruptedException {
        for (int currentK = KNN_MIN_K; currentK <= KNN_MAX_K; currentK++) {
            ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
            List<Future<Double>> futures = new ArrayList<>();

            for (int j = 0; j < NUMBER_OF_THREADS; j++) {
                int threadId = j;
                FootballPredictor footballPredictor = new FootballPredictor(currentK);
                futures.add(executorService.submit(() -> runClassifier(footballPredictor, threadId)));
            }

            double accuracySum = 0;

            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                accuracySum += futures.get(i).get();
            }

            executorService.shutdown();

            System.out.printf("FootballPredictor(%d) => Accuracy: %f%n", currentK, accuracySum / NUMBER_OF_TESTS);
        }
    }

    private static void runKNearestNeighbors() throws ExecutionException, InterruptedException {
        for (int currentK = KNN_MIN_K; currentK <= KNN_MAX_K; currentK++) {
            ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
            List<Future<Double>> futures = new ArrayList<>();

            for (int j = 0; j < NUMBER_OF_THREADS; j++) {
                int threadId = j;
                KNearestNeighbors kNearestNeighbors = new KNearestNeighbors();
                kNearestNeighbors.setK(currentK);
                futures.add(executorService.submit(() -> runClassifier(kNearestNeighbors, threadId)));
            }

            double accuracySum = 0;

            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                accuracySum += futures.get(i).get();
            }

            executorService.shutdown();

            System.out.printf("KNearestNeighbors(%d) => Accuracy: %f%n", currentK, accuracySum / NUMBER_OF_TESTS);
        }
    }

    private static void runRandomGuess() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        List<Future<Double>> futures = new ArrayList<>();

        for (int j = 0; j < NUMBER_OF_THREADS; j++) {
            int threadId = j;
            RandomGuess randomGuess = new RandomGuess();
            futures.add(executorService.submit(() -> runClassifier(randomGuess, threadId)));
        }

        double accuracySum = 0;

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            accuracySum += futures.get(i).get();
        }

        executorService.shutdown();

        System.out.printf("RandomGuess => Accuracy: %f%n", accuracySum / NUMBER_OF_TESTS);

    }

    private static void runSimpleProbability() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        List<Future<Double>> futures = new ArrayList<>();

        for (int j = 0; j < NUMBER_OF_THREADS; j++) {
            int threadId = j;
            SimpleProbability simpleProbability = new SimpleProbability();
            futures.add(executorService.submit(() -> runClassifier(simpleProbability, threadId)));
        }

        double accuracySum = 0;

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            accuracySum += futures.get(i).get();
        }

        executorService.shutdown();

        System.out.printf("SimpleProbability => Accuracy: %f%n", accuracySum / NUMBER_OF_TESTS);

    }

    private static double runClassifier(Classifier classifier, int threadId) {
        double accuracySum = 0;

        for (int i = threadId; i < NUMBER_OF_TESTS; i += NUMBER_OF_THREADS) {
            Dataset dataset = Dataset.fromFile(PATH_TO_DATASET, "\\s+");
            int testingSetSize = (int) (dataset.size() * TESTING_SET_PERCENT);
            accuracySum += validateClassifier(testingSetSize, classifier, dataset, IS_LOGGING_ENABLED);
        }

        return accuracySum;
    }

    private static void runAllClassifiers() throws ExecutionException, InterruptedException {
        runFootballPredictor();
        runKNearestNeighbors();
        runRandomGuess();
        runSimpleProbability();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

        runAllClassifiers();

        System.out.printf("%nExecution time: %d ms. Number of threads: %d.%n",
                System.currentTimeMillis() - startTime, NUMBER_OF_THREADS);
    }
}
