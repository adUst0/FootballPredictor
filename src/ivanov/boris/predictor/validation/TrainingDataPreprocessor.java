package ivanov.boris.predictor.validation;

import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

import java.util.*;

/**
 * The purpose of this class is to prepare the training dataset before
 * passing it to the classification algorithms
 */

public class TrainingDataPreprocessor {
    private static final int HOME_TEAM_WINS_POSITION = 0;
    private static final int AWAY_TEAM_WINS_POSITION = 12;

    /**
     * Call all dataset preprocessing functions
     * @param dataset the dataset which will be transformed
     */
    public static void prepare(Dataset<Double> dataset) {
        normalizeDataset(dataset);
        makeWDLproportional(dataset);
    }

    /**
     * Make Wins-Draws-Looses proportional for both teams
     * @param dataset the dataset which will be transformed
     */
    private static void makeWDLproportional(Dataset<Double> dataset) {

        for (DatasetEntry entry : dataset.getEntries()) {
            List<Double> attributes = entry.getAttributes();
            int playedGamesCount;

            playedGamesCount = (int)(
                    attributes.get(HOME_TEAM_WINS_POSITION + 0) +
                    attributes.get(HOME_TEAM_WINS_POSITION + 1) +
                    attributes.get(HOME_TEAM_WINS_POSITION + 2));
            attributes.set(HOME_TEAM_WINS_POSITION + 0, attributes.get(HOME_TEAM_WINS_POSITION + 0) / playedGamesCount);
            attributes.set(HOME_TEAM_WINS_POSITION + 1, attributes.get(HOME_TEAM_WINS_POSITION + 1) / playedGamesCount);
            attributes.set(HOME_TEAM_WINS_POSITION + 2, attributes.get(HOME_TEAM_WINS_POSITION + 2) / playedGamesCount);

            playedGamesCount = (int)(
                    attributes.get(AWAY_TEAM_WINS_POSITION + 0) +
                    attributes.get(AWAY_TEAM_WINS_POSITION + 1) +
                    attributes.get(AWAY_TEAM_WINS_POSITION + 2));
            attributes.set(AWAY_TEAM_WINS_POSITION + 0, attributes.get(AWAY_TEAM_WINS_POSITION + 0) / playedGamesCount);
            attributes.set(AWAY_TEAM_WINS_POSITION + 1, attributes.get(AWAY_TEAM_WINS_POSITION + 1) / playedGamesCount);
            attributes.set(AWAY_TEAM_WINS_POSITION + 2, attributes.get(AWAY_TEAM_WINS_POSITION + 2) / playedGamesCount);
        }
    }

    /**
     * Balance the dataset if the data is unbalanced.
     * There must be the same number of entries for all the classes (labels).
     * Shuffle the dataset. For every class keep only MIN examples, where MIN is the class with the least examples.
     * @param dataset the dataset which will be transformed
     */
    private static void normalizeDataset(Dataset<Double> dataset) {
        Map<String, Integer> classOccurrences = new HashMap<>();

        for (DatasetEntry<Double> entry : dataset.getEntries()) {
            String label = entry.getLabel();
            if (classOccurrences.containsKey(label)) {
                classOccurrences.put(label, classOccurrences.get(label) + 1);
            }
            else {
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
