package ivanov.boris.predictor;

import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.dataset.parser.DoubleDatasetParser;
import ivanov.boris.predictor.knn.KNearestNeighbors;

import java.util.Arrays;
import java.util.List;

public class FootballPredictor {
    public static void main(String[] args) {
        DoubleDatasetParser parser = new DoubleDatasetParser();

         Dataset<Double> dataset = parser.fromFile("Data/football.data", "\\s+");
//        Dataset<Double> dataset = parser.fromFile("Data/football_new.footballapi", "\\s+");
        makeWDLproportional(dataset);

        Classifier<Double> classifier = new KNearestNeighbors();
        classifier.buildModel(dataset);

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

    private static final int ATTRIBUTES_COUNT = 26;
    private static final List<String> POSSIBLE_LABELS = Arrays.asList("1", "2", "X");

    private static boolean isDatasetCorrect(Dataset<Double> dataset) {
        boolean isCorrect = true;
        System.out.println("Dataset size: " + dataset.size());

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
        for (DatasetEntry entry : dataset.getEntries()) {
            List<Double> attributes = entry.getAttributes();
            attributes.set(0, 0.0);
            double[] wdl = {attributes.get(1), attributes.get(2), attributes.get(3)};
            int games = (int)(wdl[0] + wdl[1] + wdl[2]);
            attributes.set(1, attributes.get(1) / games);
            attributes.set(2, attributes.get(2) / games);
            attributes.set(3, attributes.get(3) / games);
        }
    }
}
