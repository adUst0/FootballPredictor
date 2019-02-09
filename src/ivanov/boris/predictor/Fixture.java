package ivanov.boris.predictor;

import ivanov.boris.predictor.dataset.DatasetEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * This class has strong dependency with the Dataset format and it has to be updated accordingly on
 * every format change.
 * <p>
 * Its main purpose is to build an abstraction layer for the other modules to use.
 */

public class Fixture {

    public class Stats {
        public double wins;
        public double draws;
        public double looses;
    }

    public Stats team1League = new Stats(); // all games in current league season
    public Stats team1Last6Games = new Stats();
    public Stats team1Last6GamesHome = new Stats();
    public Stats team1Last6Games1v1 = new Stats();

    public Stats team2League = new Stats(); // all games in current league season
    public Stats team2Last6Games = new Stats();
    public Stats team2Last6GamesAway = new Stats();

    public String outcome;

    /**
     * Calculate simple probabilities for this fixture
     *
     * @return Map where the Key is the Outcome (1, 2, X) and the value is the probability.
     */
    public Map<String, Double> getOutcomeProbabilities() {
        Map<String, Double> probabilities = new HashMap<>();

        final int totalGamesPerCategory = 12;
        final int examinedGroups = 2;

        double p1 = ((team1Last6GamesHome.wins + team2Last6GamesAway.looses) + team1Last6Games1v1.wins * 2) /
                (totalGamesPerCategory * examinedGroups);

        double pX = ((team1Last6GamesHome.draws + team2Last6GamesAway.draws) + team1Last6Games1v1.draws * 2) /
                (totalGamesPerCategory * examinedGroups);

        double p2 = ((team1Last6GamesHome.looses + team2Last6GamesAway.wins) + team1Last6Games1v1.looses * 2) /
                (totalGamesPerCategory * examinedGroups);

        probabilities.put("1", p1);
        probabilities.put("X", pX);
        probabilities.put("2", p2);

        return probabilities;
    }

    public static Fixture fromDatasetEntry(DatasetEntry entry) {
        Fixture stats = new Fixture();

        stats.team1League.wins = Double.parseDouble(entry.getAttributes().get(0));
        stats.team1League.draws = Double.parseDouble(entry.getAttributes().get(1));
        stats.team1League.looses = Double.parseDouble(entry.getAttributes().get(2));

        stats.team1Last6Games.wins = Double.parseDouble(entry.getAttributes().get(3));
        stats.team1Last6Games.draws = Double.parseDouble(entry.getAttributes().get(4));
        stats.team1Last6Games.looses = Double.parseDouble(entry.getAttributes().get(5));

        stats.team1Last6GamesHome.wins = Double.parseDouble(entry.getAttributes().get(6));
        stats.team1Last6GamesHome.draws = Double.parseDouble(entry.getAttributes().get(7));
        stats.team1Last6GamesHome.looses = Double.parseDouble(entry.getAttributes().get(8));

        stats.team1Last6Games1v1.wins = Double.parseDouble(entry.getAttributes().get(9));
        stats.team1Last6Games1v1.draws = Double.parseDouble(entry.getAttributes().get(10));
        stats.team1Last6Games1v1.looses = Double.parseDouble(entry.getAttributes().get(11));

        stats.team2League.wins = Double.parseDouble(entry.getAttributes().get(12));
        stats.team2League.draws = Double.parseDouble(entry.getAttributes().get(13));
        stats.team2League.looses = Double.parseDouble(entry.getAttributes().get(14));

        stats.team2Last6Games.wins = Double.parseDouble(entry.getAttributes().get(15));
        stats.team2Last6Games.draws = Double.parseDouble(entry.getAttributes().get(16));
        stats.team2Last6Games.looses = Double.parseDouble(entry.getAttributes().get(17));

        stats.team2Last6GamesAway.wins = Double.parseDouble(entry.getAttributes().get(18));
        stats.team2Last6GamesAway.draws = Double.parseDouble(entry.getAttributes().get(19));
        stats.team2Last6GamesAway.looses = Double.parseDouble(entry.getAttributes().get(20));

        stats.outcome = entry.getLabel();

        return stats;
    }

    public DatasetEntry toDatasetEntry() {
        DatasetEntry entry = new DatasetEntry();

        entry.getAttributes().add(String.valueOf(team1League.wins));
        entry.getAttributes().add(String.valueOf(team1League.draws));
        entry.getAttributes().add(String.valueOf(team1League.looses));

        entry.getAttributes().add(String.valueOf(team1Last6Games.wins));
        entry.getAttributes().add(String.valueOf(team1Last6Games.draws));
        entry.getAttributes().add(String.valueOf(team1Last6Games.looses));

        entry.getAttributes().add(String.valueOf(team1Last6GamesHome.wins));
        entry.getAttributes().add(String.valueOf(team1Last6GamesHome.draws));
        entry.getAttributes().add(String.valueOf(team1Last6GamesHome.looses));

        entry.getAttributes().add(String.valueOf(team1Last6Games1v1.wins));
        entry.getAttributes().add(String.valueOf(team1Last6Games1v1.draws));
        entry.getAttributes().add(String.valueOf(team1Last6Games1v1.looses));

        entry.getAttributes().add(String.valueOf(team2League.wins));
        entry.getAttributes().add(String.valueOf(team2League.draws));
        entry.getAttributes().add(String.valueOf(team2League.looses));

        entry.getAttributes().add(String.valueOf(team2Last6Games.wins));
        entry.getAttributes().add(String.valueOf(team2Last6Games.draws));
        entry.getAttributes().add(String.valueOf(team2Last6Games.looses));

        entry.getAttributes().add(String.valueOf(team2Last6GamesAway.wins));
        entry.getAttributes().add(String.valueOf(team2Last6GamesAway.draws));
        entry.getAttributes().add(String.valueOf(team2Last6GamesAway.looses));

        entry.setLabel(outcome);

        return entry;
    }
}
