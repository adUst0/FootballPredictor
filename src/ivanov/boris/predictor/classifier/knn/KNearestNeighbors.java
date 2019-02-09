package ivanov.boris.predictor.classifier.knn;

import ivanov.boris.predictor.classifier.Classifier;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.dataset.IllegalDatasetException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KNearestNeighbors implements Classifier {
    private static final int DEFAULT_K = 10;

    private List<Entry> myDataset;
    private int k = DEFAULT_K;

    private static double getDistance(Entry point1, Entry point2) {
        double sum = 0;

        for (int i = 0; i < point1.attributes.size(); i++) {
            double q1 = point1.attributes.get(i);
            double q2 = point2.attributes.get(i);

            sum += Math.pow(q1 - q2, 2);
        }

        return Math.sqrt(sum);
    }

    @Override
    public void buildModel(Dataset dataset) {
        myDataset = new ArrayList<>();
        for (DatasetEntry entry : dataset.getEntries()) {
            myDataset.add(new Entry(entry));
        }
    }

    public void buildModel(Dataset dataset, int k) {
        myDataset = new ArrayList<>();

        for (DatasetEntry entry : dataset.getEntries()) {
            myDataset.add(new Entry(entry));
        }

        this.k = k;
    }

    @Override
    public String classify(DatasetEntry datasetEntry) {
        // TODO: performance optimizations required
        Entry toClassify = new Entry(datasetEntry);

        myDataset.sort((x, y) -> {
            double distanceToX = getDistance(toClassify, x);
            double distanceToY = getDistance(toClassify, y);

            return Double.compare(distanceToX, distanceToY);
        });

        // find first K neighbors
        Map<String, Integer> labelsOccurrences = new HashMap<>();
        for (int i = 0; i < this.k; i++) {
            Entry current = myDataset.get(i);
            if (!labelsOccurrences.containsKey(current.label)) {
                labelsOccurrences.put(current.label, 1);
            }
            else {
                int newValue = labelsOccurrences.get(current.label) + 1;
                labelsOccurrences.put(current.label, newValue);
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
            String current = myDataset.get(i).label;
            if (candidates.contains(current)) {
                return current;
            }

        }

        // this line can not be reached with correct arguments
        throw new IllegalDatasetException();
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }
}