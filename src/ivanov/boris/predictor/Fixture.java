package ivanov.boris.predictor;

import ivanov.boris.predictor.dataset.DatasetEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * This class has strong dependency with the Dataset format and it has to be updated accordingly on
 * every format change.
 */

public class Fixture {

    public class Stats {
        public double wins;
        public double draws;
        public double looses;
    }

    private Stats team1League = new Stats(); // all games in current league season
    private Stats team1Last6Games = new Stats();
    private Stats team1Last6GamesHome = new Stats();
    private Stats team1Last6Games1v1 = new Stats();

    private Stats team2League = new Stats(); // all games in current league season
    private Stats team2Last6Games = new Stats();
    private Stats team2Last6GamesAway = new Stats();

    private String outcome;

    public Stats getTeam1League() {
        return team1League;
    }

    public Stats getTeam1Last6Games() {
        return team1Last6Games;
    }

    public Stats getTeam1Last6GamesHome() {
        return team1Last6GamesHome;
    }

    public Stats getTeam1Last6Games1v1() {
        return team1Last6Games1v1;
    }

    public Stats getTeam2League() {
        return team2League;
    }

    public Stats getTeam2Last6Games() {
        return team2Last6Games;
    }

    public Stats getTeam2Last6GamesAway() {
        return team2Last6GamesAway;
    }

    public String getOutcome() {
        return outcome;
    }

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

    public static Fixture fromDatasetEntry(DatasetEntry<Double> entry) {
        Fixture stats = new Fixture();
        stats.team1League.wins = entry.getAttributes().get(0);
        stats.team1League.draws = entry.getAttributes().get(1);
        stats.team1League.looses = entry.getAttributes().get(2);

        stats.team1Last6Games.wins = entry.getAttributes().get(3);
        stats.team1Last6Games.draws = entry.getAttributes().get(4);
        stats.team1Last6Games.looses = entry.getAttributes().get(5);

        stats.team1Last6GamesHome.wins = entry.getAttributes().get(6);
        stats.team1Last6GamesHome.draws = entry.getAttributes().get(7);
        stats.team1Last6GamesHome.looses = entry.getAttributes().get(8);

        stats.team1Last6Games1v1.wins = entry.getAttributes().get(9);
        stats.team1Last6Games1v1.draws = entry.getAttributes().get(10);
        stats.team1Last6Games1v1.looses = entry.getAttributes().get(11);

        stats.team2League.wins = entry.getAttributes().get(12);
        stats.team2League.draws = entry.getAttributes().get(13);
        stats.team2League.looses = entry.getAttributes().get(14);

        stats.team2Last6Games.wins = entry.getAttributes().get(15);
        stats.team2Last6Games.draws = entry.getAttributes().get(16);
        stats.team2Last6Games.looses = entry.getAttributes().get(17);

        stats.team2Last6GamesAway.wins = entry.getAttributes().get(18);
        stats.team2Last6GamesAway.draws = entry.getAttributes().get(19);
        stats.team2Last6GamesAway.looses = entry.getAttributes().get(20);

        stats.outcome = entry.getLabel();

        return stats;
    }

    public DatasetEntry<Double> toDatasetEntry() {
        DatasetEntry<Double> entry = new DatasetEntry<>();
        entry.getAttributes().add(team1League.wins);
        entry.getAttributes().add(team1League.draws);
        entry.getAttributes().add(team1League.looses);

        entry.getAttributes().add(team1Last6Games.wins);
        entry.getAttributes().add(team1Last6Games.draws);
        entry.getAttributes().add(team1Last6Games.looses);

        entry.getAttributes().add(team1Last6GamesHome.wins);
        entry.getAttributes().add(team1Last6GamesHome.draws);
        entry.getAttributes().add(team1Last6GamesHome.looses);

        entry.getAttributes().add(team1Last6Games1v1.wins);
        entry.getAttributes().add(team1Last6Games1v1.draws);
        entry.getAttributes().add(team1Last6Games1v1.looses);

        entry.getAttributes().add(team2League.wins);
        entry.getAttributes().add(team2League.draws);
        entry.getAttributes().add(team2League.looses);

        entry.getAttributes().add(team2Last6Games.wins);
        entry.getAttributes().add(team2Last6Games.draws);
        entry.getAttributes().add(team2Last6Games.looses);

        entry.getAttributes().add(team2Last6GamesAway.wins);
        entry.getAttributes().add(team2Last6GamesAway.draws);
        entry.getAttributes().add(team2Last6GamesAway.looses);

        entry.setLabel(outcome);

        return entry;
    }
}
