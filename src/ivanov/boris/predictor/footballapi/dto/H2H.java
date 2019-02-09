package ivanov.boris.predictor.footballapi.dto;

import com.google.gson.annotations.SerializedName;
import com.sun.jdi.PrimitiveValue;

import java.util.List;

public class H2H {
    private static final int DATASET_MAX_GAMES_IN_GROUP = 6;
    private static final int DATASET_CATEGORIES_IN_GROUP = 3;

    private transient String firstTeamName;
    private transient String secondTeamName;
    private transient String leagueId;

    public String getFirstTeamName() {
        return firstTeamName;
    }

    public void setFirstTeamName(String firstTeamName) {
        this.firstTeamName = firstTeamName;
    }

    public String getSecondTeamName() {
        return secondTeamName;
    }

    public void setSecondTeamName(String secondTeamName) {
        this.secondTeamName = secondTeamName;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    @SerializedName("firstTeam_VS_secondTeam")
    private List<Fixture> firstVSSecond;

    @SerializedName("firstTeam_lastResults")
    private List<Fixture> firstTeamLastResults;

    @SerializedName("secondTeam_lastResults")
    private List<Fixture> secondTeamLastResults;

    public List<Fixture> getFirstVSSecond() {
        return firstVSSecond;
    }

    public void setFirstVSSecond(List<Fixture> firstVSSecond) {
        this.firstVSSecond = firstVSSecond;
    }

    public List<Fixture> getFirstTeamLastResults() {
        return firstTeamLastResults;
    }

    public void setFirstTeamLastResults(List<Fixture> firstTeamLastResults) {
        this.firstTeamLastResults = firstTeamLastResults;
    }

    public List<Fixture> getSecondTeamLastResults() {
        return secondTeamLastResults;
    }

    public void setSecondTeamLastResults(List<Fixture> secondTeamLastResults) {
        this.secondTeamLastResults = secondTeamLastResults;
    }

    public String getFirstTeamLast6Games(boolean onlyHomeTerrain) {
        double wins = 0, draws = 0, looses = 0;

        for (int i = 0; i < firstTeamLastResults.size() && wins + draws + looses < DATASET_MAX_GAMES_IN_GROUP; i++) {
            Fixture fixture = firstTeamLastResults.get(i);
            String outcome = fixture.getOutcome();

            if (onlyHomeTerrain && !fixture.getHomeTeamName().equals(firstTeamName)) {
                continue;
            }

            if (!fixture.getLeagueId().equals(leagueId)) {
                continue;
            }

            if (outcome.equals("X")) {
                draws++;
            }
            else if (fixture.getHomeTeamName().equals(firstTeamName)) {
                if (outcome.equals("1")) {
                    wins++;
                }
                else {
                    looses++;
                }
            }
            else {
                if (outcome.equals("1")) {
                    looses++;
                }
                else {
                    wins++;
                }
            }
        }

        // Normalize results if there are less than DATASET_MAX_GAMES_IN_GROUP games
        if (wins + draws + looses < DATASET_MAX_GAMES_IN_GROUP) {
            double missingGames = DATASET_MAX_GAMES_IN_GROUP - (wins + draws + looses);
            double valueToAdd = Math.floor(missingGames / DATASET_CATEGORIES_IN_GROUP * 100) / 100;

            wins += valueToAdd;
            draws += valueToAdd;
            looses += valueToAdd;
        }

        return wins + " " + draws + " " + looses;
    }

    public String getSecondTeamLast6Games(boolean onlyAwayTerrain) {
        double wins = 0, draws = 0, looses = 0;

        for (int i = 0; i < secondTeamLastResults.size() && wins + draws + looses < DATASET_MAX_GAMES_IN_GROUP; i++) {
            Fixture fixture = secondTeamLastResults.get(i);
            String outcome = fixture.getOutcome();

            if (onlyAwayTerrain && !fixture.getAwayTeamName().equals(secondTeamName)) {
                continue;
            }

            if (!fixture.getLeagueId().equals(leagueId)) {
                continue;
            }

            if (outcome.equals("X")) {
                draws++;
            }
            else if (fixture.getHomeTeamName().equals(secondTeamName)) {
                if (outcome.equals("1")) {
                    wins++;
                }
                else {
                    looses++;
                }
            }
            else {
                if (outcome.equals("1")) {
                    looses++;
                }
                else {
                    wins++;
                }
            }
        }

        // Normalize results if there are less than 6 games
        if (wins + draws + looses < DATASET_MAX_GAMES_IN_GROUP) {
            double missingGames = DATASET_MAX_GAMES_IN_GROUP - (wins + draws + looses);
            double valueToAdd = Math.floor(missingGames / DATASET_CATEGORIES_IN_GROUP * 100) / 100;

            wins += valueToAdd;
            draws += valueToAdd;
            looses += valueToAdd;
        }

        return wins + " " + draws + " " + looses;
    }

    public String get1v1FirstTeam() {
        double wins = 0, draws = 0, looses = 0;

        for (int i = 0; i < DATASET_MAX_GAMES_IN_GROUP && i < firstVSSecond.size() &&
                wins + draws + looses < DATASET_MAX_GAMES_IN_GROUP; i++) {
            Fixture fixture = firstVSSecond.get(i);
            String outcome = fixture.getOutcome();

            if (outcome.equals("X")) {
                draws++;
            }
            else if (fixture.getHomeTeamName().equals(firstTeamName)) {
                if (outcome.equals("1")) {
                    wins++;
                }
                else {
                    looses++;
                }
            }
            else {
                if (outcome.equals("1")) {
                    looses++;
                }
                else {
                    wins++;
                }
            }
        }

        // Normalize results if there are less than 6 games
        if (wins + draws + looses < DATASET_MAX_GAMES_IN_GROUP) {
            double missingGames = DATASET_MAX_GAMES_IN_GROUP - (wins + draws + looses);
            double valueToAdd = Math.floor(missingGames / DATASET_CATEGORIES_IN_GROUP * 100) / 100;

            wins += valueToAdd;
            draws += valueToAdd;
            looses += valueToAdd;
        }

        return wins + " " + draws + " " + looses;
    }

    public String get1v1SecondTeam() {
        StringBuilder sb = new StringBuilder(get1v1FirstTeam());
        return sb.reverse().toString();
    }
}
