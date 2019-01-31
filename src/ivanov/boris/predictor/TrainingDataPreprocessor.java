package ivanov.boris.predictor;

import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

import java.util.*;

/**
 * The purpose of this class is to prepare the training dataset before
 * passing it to the classification algorithms.
 */

public class TrainingDataPreprocessor {
    /**
     * If the probability of the real outcome of a game is below this constant,
     * this entry will be removed from the Dataset.
     * See removeOutliers() for more info.
     */
    private static final double MIN_PROBABILITY = 0.1;

    /**
     * Call all dataset preprocessing functions
     *
     * @param dataset the dataset which will be transformed
     */
    public static void prepare(Dataset<Double> dataset) {
        removeNoiseData(dataset);
        removeOutliers(dataset);
    }

    /**
     * Remove noise data from the dataset.
     * Currently the only entries that are removed are the ones that have the 1v1 data as (2.0, 2.0, 2.0).
     *
     * @param dataset the dataset which will be transformed
     */
    private static void removeNoiseData(Dataset<Double> dataset) {
        Iterator<DatasetEntry<Double>> it = dataset.getEntries().iterator();
        while (it.hasNext()) {
            DatasetEntry<Double> entry = it.next();

            Fixture stats = Fixture.fromDatasetEntry(entry);
            double wins = stats.getTeam1Last6Games1v1().wins;
            double draws = stats.getTeam1Last6Games1v1().draws;
            double looses = stats.getTeam1Last6Games1v1().looses;
            if (wins == draws && wins == looses) {
                it.remove();
            }

        }
    }

    /**
     * Remove entries for which the mathematical odds are very low (beyond MIN_PROBABILITY)
     *
     * @param dataset the dataset which will be transformed
     */
    private static void removeOutliers(Dataset<Double> dataset) {

        Iterator<DatasetEntry<Double>> it = dataset.getEntries().iterator();
        while (it.hasNext()) {

            DatasetEntry<Double> entry = it.next();
            String label = entry.getLabel();
            Map<String, Double> probabilities = Fixture.fromDatasetEntry(entry).getOutcomeProbabilities();

            if (probabilities.get(label) < MIN_PROBABILITY) {
                it.remove();
            }

        }
    }

    /**
     * Balance the dataset if the data is unbalanced.
     * There must be the same number of entries for all the classes (labels).
     * Shuffle the dataset. For every class keep only MIN examples, where MIN is the class with the least examples.
     *
     * @param dataset the dataset which will be transformed
     */
    private static void normalizeDataset(Dataset<Double> dataset) {
        Map<String, Integer> classOccurrences = new HashMap<>();

        for (DatasetEntry<Double> entry : dataset.getEntries()) {
            String label = entry.getLabel();
            if (classOccurrences.containsKey(label)) {
                classOccurrences.put(label, classOccurrences.get(label) + 1);
            } else {
                classOccurrences.put(label, 1);
            }
        }

        int minOccurrences = Collections.min(classOccurrences.values());

        Collections.shuffle(dataset.getEntries());

        Iterator<DatasetEntry<Double>> it = dataset.getEntries().iterator();
        while (it.hasNext()) {
            DatasetEntry<Double> entry = it.next();
            String label = entry.getLabel();
            if (classOccurrences.get(label) > minOccurrences) {
                it.remove();
                classOccurrences.put(label, classOccurrences.get(label) - 1);
            }

        }
    }

}
