package ivanov.boris.predictor;

import ivanov.boris.predictor.classifier.knn.KNearestNeighbors;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.dataset.parser.DatasetParser;
import ivanov.boris.predictor.dataset.parser.DoubleDatasetParser;
import ivanov.boris.predictor.validation.TrainingDataPreprocessor;

public class FootballPredictor {

    private static final int K_NEIGHBORS = 3;

    public static void predict(String pathToDataset, String pathToPredict) {
        DatasetParser<Double> parser = new DoubleDatasetParser();

        Dataset<Double> dataset = parser.fromFile(pathToDataset, "\\s+");
        TrainingDataPreprocessor.prepare(dataset);

        KNearestNeighbors kNN = new KNearestNeighbors();
        kNN.buildModel(dataset, K_NEIGHBORS);

        Dataset<Double> toPredict = parser.fromFile(pathToPredict, "\\s+");
        for (DatasetEntry entry : toPredict.getEntries()) {
            System.out.println(entry.getAttributes() + " " + entry.getLabel());
            System.out.println("\tPrediction: " + kNN.classify(entry));
        }
    }

    public static void main(String[] args) {
        DatasetParser<Double> parser = new DoubleDatasetParser();

        Dataset<Double> dataset = parser.fromFile("Data/SelectedLeaguesOnly.data", "\\s+");

        KNearestNeighbors kNN = new KNearestNeighbors();
        kNN.buildModel(dataset, K_NEIGHBORS);

        Dataset<Double> toPredict = parser.fromFile("Data/data_predict.data", "\\s+");

        double accuracy = 0;

        for (DatasetEntry entry : toPredict.getEntries()) {
            if (kNN.classify(entry).equals(entry.getLabel())) {
                accuracy++;
            }
        }

        System.out.println("K = " + K_NEIGHBORS + " => " + (accuracy / toPredict.size()));
    }

}
