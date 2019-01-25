package ivanov.boris.predictor.classifier.knn;

import ivanov.boris.predictor.classifier.Classifier;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.dataset.IllegalDatasetException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KNearestNeighbors implements Classifier<Double> {

    private Dataset<Double> dataset = null;
    private int k;

    private double getDistance(DatasetEntry<Double> a, DatasetEntry<Double> b) {
        double sum = 0;

        for (int i = 0; i < a.getAttributes().size(); i++) {
            sum += Math.pow((a.getAttributes().get(i) - b.getAttributes().get(i)), 2);
        }

        return Math.sqrt(sum);
    }

    @Override
    public void buildModel(Dataset<Double> dataset) {
        this.dataset = dataset;
        this.k = (int) Math.sqrt(dataset.getEntries().size());
    }

    public void buildModel(Dataset<Double> dataset, int k) {
        this.dataset = dataset;
        this.k = k;
    }

    @Override
    public String classify(DatasetEntry<Double> entry) {
        List<DatasetEntry<Double>> entries = dataset.getEntries();

        final double epsilon = 0.001;

        entries.sort((x, y) -> {
            double distanceToX = getDistance(entry, x);
            double distanceToY = getDistance(entry, y);
//
//            return Double.compare(distanceToX, distanceToY);
            if (/*Math.sqrt(distanceToX - distanceToY) <= epsilon*/ distanceToX == distanceToY) {
                return 0;
            }
            else if (distanceToX < distanceToY) {
                return -1;
            }
            else {
                return 1;
            }
        });

        // find first K neighbors
        Map<String, Integer> labelsOccurrences = new HashMap<>();
        for (int i = 0; i < this.k; i++) {
            DatasetEntry<Double> current = entries.get(i);
            if (!labelsOccurrences.containsKey(current.getLabel())) {
                labelsOccurrences.put(current.getLabel(), 1);
            }
            else {
                int newValue = labelsOccurrences.get(current.getLabel()) + 1;
                labelsOccurrences.put(current.getLabel(), newValue);
            }
        }

        List<String> candidates = new ArrayList<>();
        int maxOccurrences = 0;

        for (String key : labelsOccurrences.keySet()) {
            if (labelsOccurrences.get(key) > maxOccurrences) {
                maxOccurrences = labelsOccurrences.get(key);
                candidates = new ArrayList<>();
                candidates.add(key);
            }
            else if (labelsOccurrences.get(key) == maxOccurrences) {
                candidates.add(key);
            }
        }

        for (int i = 0; i < this.k; i++) {
            String current = entries.get(i).getLabel();
            if (candidates.contains(current)) {
                return current;
            }

        }

        // this line can not be reached with correct arguments
        throw new IllegalDatasetException();
    }
}
