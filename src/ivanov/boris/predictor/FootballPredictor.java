package ivanov.boris.predictor;

import ivanov.boris.predictor.classifier.Classifier;
import ivanov.boris.predictor.classifier.knn.KNearestNeighbors;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.dataset.parser.DoubleDatasetParser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FootballPredictor {
    public static void main(String[] args) {
//        predict();
//        crossValidate(2, false);

        chooseK(100);
    }

    private static void chooseK(int maxK) {
        for (int k = 2; k <= maxK; k++) {
            double accuracy = 0;
            int numberOfTests = 100;
            for (int j = 0; j < numberOfTests; j++) {
                accuracy += crossValidate(k, true);
            }
            System.out.println("k = " + k + " => Accuracy: " + (accuracy / numberOfTests) + " %");
        }
    }

    public static void predict() {
        DoubleDatasetParser parser = new DoubleDatasetParser();

        Dataset<Double> dataset = parser.fromFile("Data/football.data", "\\s+");
//        Dataset<Double> dataset = parser.fromFile("Data/football_new.footballapi", "\\s+");
        makeWDLproportional(dataset);

        KNearestNeighbors classifier = new KNearestNeighbors();
        classifier.buildModel(dataset, 10);

        Dataset<Double> toPredict = parser.fromFile("Data/topredict.data", "\\s+");
        makeWDLproportional(toPredict);

        if (!isDatasetCorrect(dataset)) {
            return;
        }

        for (DatasetEntry entry : toPredict.getEntries()) {
            System.out.println(entry.getAttributes() + " " + entry.getLabel());
            System.out.println("\tPrediction: " + classifier.classify(entry));
        }
    }

    public static double crossValidate(int k, boolean quiet) {
        DoubleDatasetParser parser = new DoubleDatasetParser();

        Dataset<Double> dataset = parser.fromFile("Data/SelectedLeaguesOnly.data", "\\s+");
        makeWDLproportional(dataset);

        if (!isDatasetCorrect(dataset)) {
            return -1;
        }

        Collections.shuffle(dataset.getEntries());

        Dataset<Double> trainingData = new Dataset<>();
        Dataset<Double> testingData = new Dataset<>();

        int testingSetSize = (int) (dataset.size() * 0.20);

        for (int i = 0; i < dataset.size(); i++) {
            DatasetEntry<Double> entry = dataset.getEntries().get(i);

            if (i < testingSetSize) {
                testingData.addEntry(new DatasetEntry<>(entry));
            }
            else {
                trainingData.addEntry(new DatasetEntry<>(entry));
            }
        }

//        Classifier<Double> classifier = new KNearestNeighbors();
//        classifier.buildModel(trainingData);

        KNearestNeighbors classifier = new KNearestNeighbors();
        classifier.buildModel(trainingData, k);

        int correctPredictions = 0;

        for (DatasetEntry entry : testingData.getEntries()) {
            String label = classifier.classify(entry);
            if (label.equals(entry.getLabel())) {
                correctPredictions++;
            }
        }

        if (!quiet) {
            System.out.println("Correct predictions: " + correctPredictions);
            System.out.println("Incorrect predictions: " + (testingSetSize - correctPredictions));
            System.out.println("Accuracy: " + ((float)correctPredictions / testingSetSize * 100) + "%");
        }

        return ((double) correctPredictions / testingSetSize * 100);
    }

//    private static final int ATTRIBUTES_COUNT = 26;
private static final int ATTRIBUTES_COUNT = 24;
    private static final List<String> POSSIBLE_LABELS = Arrays.asList("1", "2", "X");

    private static boolean isDatasetCorrect(Dataset<Double> dataset) {
        boolean isCorrect = true;
        // System.out.println("Dataset size: " + dataset.size());

        for (DatasetEntry entry : dataset.getEntries()) {
            if (entry.getAttributes().size() != ATTRIBUTES_COUNT) {
                System.out.println(entry.getAttributes() + " " + entry.getLabel());
                System.out.println("\tError! Attributes are: " + entry.getAttributes().size());
                isCorrect = false;
            }

            if (!POSSIBLE_LABELS.contains(entry.getLabel())) {
                System.out.println(entry.getAttributes() + " " + entry.getLabel());
                System.out.println("\tError! Label is: " + entry.getLabel());
                isCorrect = false;
            }
        }

        return isCorrect;
    }

    private static void makeWDLproportional(Dataset<Double> dataset) {
//        for (DatasetEntry entry : dataset.getEntries()) {
//            List<Double> attributes = entry.getAttributes();
//            attributes.set(0, 0.0);
//            double[] wdl = {attributes.get(1), attributes.get(2), attributes.get(3)};
//            int games = (int)(wdl[0] + wdl[1] + wdl[2]);
//            attributes.set(1, attributes.get(1) / games);
//            attributes.set(2, attributes.get(2) / games);
//            attributes.set(3, attributes.get(3) / games);
//        }

        for (DatasetEntry entry : dataset.getEntries()) {
            List<Double> attributes = entry.getAttributes();
//            attributes.remove(0);
//            attributes.remove(0);
//            attributes.remove(0);
//            attributes.remove(0);
//            attributes.remove(9);
//            attributes.remove(9);
//            attributes.remove(9);
//            attributes.remove(9);

            attributes.remove(0);
            attributes.remove(12);

            double[] wdl = {attributes.get(0), attributes.get(1), attributes.get(2)};
            int games = (int)(wdl[0] + wdl[1] + wdl[2]);
            attributes.set(0, attributes.get(0) / games);
            attributes.set(1, attributes.get(1) / games);
            attributes.set(2, attributes.get(2) / games);

            double[] wd2 = {attributes.get(12), attributes.get(13), attributes.get(14)};
            games = (int)(wd2[0] + wd2[1] + wd2[2]);
            attributes.set(12, attributes.get(12) / games);
            attributes.set(13, attributes.get(13) / games);
            attributes.set(14, attributes.get(14) / games);
        }

    }
}
