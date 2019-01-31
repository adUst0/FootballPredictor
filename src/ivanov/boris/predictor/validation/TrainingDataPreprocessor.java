package ivanov.boris.predictor.validation;

import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

import java.util.*;

/**
 * The purpose of this class is to prepare the training dataset before
 * passing it to the classification algorithms.
 * <p>
 * Keep in mind that this class has strong dependency with the
 * Dataset format and it has to be updated accordingly on
 * every format change.
 * <p>
 * TODO: This functionality can be refactored to work with a Game representation class with field names
 *       and not raw entries.
 */

public class TrainingDataPreprocessor {
    private static final int HOME_TEAM_1V1_WINS_POSITION = 9;
    private static final int HOME_TEAM_HOMELAST6_POSITION = 6;
    private static final int AWAY_TEAM_AWAYLAST6_POSITION = 18;

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
            double wins = entry.getAttributes().get(HOME_TEAM_1V1_WINS_POSITION);
            double draws = entry.getAttributes().get(HOME_TEAM_1V1_WINS_POSITION + 1);
            double looses = entry.getAttributes().get(HOME_TEAM_1V1_WINS_POSITION + 2);
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
            int totalGamesPerCategory = 12;
            int examinedGroups = 2;
            double pHomeTeam =
                    (
                            entry.getAttributes().get(HOME_TEAM_HOMELAST6_POSITION) +
                                    entry.getAttributes().get(AWAY_TEAM_AWAYLAST6_POSITION + 2) +
                                    entry.getAttributes().get(HOME_TEAM_1V1_WINS_POSITION) * 2) / totalGamesPerCategory / examinedGroups;
            double pDraw =
                    (
                            entry.getAttributes().get(HOME_TEAM_HOMELAST6_POSITION + 1) +
                                    entry.getAttributes().get(AWAY_TEAM_AWAYLAST6_POSITION + 1) +
                                    entry.getAttributes().get(HOME_TEAM_1V1_WINS_POSITION + 1) * 2) / totalGamesPerCategory / examinedGroups;
            double pAwayTeam =
                    (
                            entry.getAttributes().get(HOME_TEAM_HOMELAST6_POSITION + 2) +
                                    entry.getAttributes().get(AWAY_TEAM_AWAYLAST6_POSITION) +
                                    entry.getAttributes().get(HOME_TEAM_1V1_WINS_POSITION + 2) * 2) / totalGamesPerCategory / examinedGroups;
            Map<String, Double> probabilities = new HashMap<>();
            probabilities.put("1", pHomeTeam);
            probabilities.put("X", pDraw);
            probabilities.put("2", pAwayTeam);

            if (probabilities.get(label) < MIN_PROBABILITY) {
                it.remove();
            }

        }
    }

}
