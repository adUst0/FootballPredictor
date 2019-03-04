import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DatasetBuilder {
    private List<Map<String, String>> entries;

    public DatasetBuilder(List<Map<String, String>> entries) {
        this.entries = entries;
    }

    public List<Fixture> createDataset() {
        sortEntriesByDateDescending();

        List<Fixture> fixtures = new ArrayList<>();

        for (int i = 0; i < entries.size(); i++) {
            Fixture fixture = new Fixture();
            String homeTeam = entries.get(i).get("HomeTeam");
            String awayTeam = entries.get(i).get("AwayTeam");

            fixture.t1_Last5Games = getLast5GamesStats(i + 1, homeTeam, Terrain.ALL);
            fixture.t1_Last5GamesHome = getLast5GamesStats(i + 1, homeTeam, Terrain.HOME);
            fixture.t1_Last5Games1v1 = getLast5Games1v1StatsTeam1(i + 1, homeTeam, awayTeam);
            fixture.t1_GoalsForwardInLast5Games = getGoalsForwardInLast5Games(i + 1, homeTeam, Terrain.ALL);
            fixture.t1_GoalsAgainstInLast5Games = getGoalsAgainstInLast5Games(i + 1, homeTeam, Terrain.ALL);
            fixture.t1_GoalsForwardInLast5GamesHome = getGoalsForwardInLast5Games(i + 1, homeTeam, Terrain.HOME);
            fixture.t1_GoalsAgainstInLast5GamesHome = getGoalsAgainstInLast5Games(i + 1, homeTeam, Terrain.HOME);

            fixture.t2_Last5Games = getLast5GamesStats(i + 1, awayTeam, Terrain.ALL);
            fixture.t2_Last5GamesAway = getLast5GamesStats(i + 1, awayTeam, Terrain.AWAY);
            fixture.t2_GoalsForwardInLast5Games = getGoalsForwardInLast5Games(i + 1, awayTeam, Terrain.ALL);
            fixture.t2_GoalsAgainstInLast5Games = getGoalsAgainstInLast5Games(i + 1, awayTeam, Terrain.ALL);
            fixture.t2_GoalsForwardInLast5GamesAway = getGoalsForwardInLast5Games(i + 1, awayTeam, Terrain.AWAY);
            fixture.t2_GoalsAgainstInLast5GamesAway = getGoalsAgainstInLast5Games(i + 1, awayTeam, Terrain.AWAY);

            if (entries.get(i).containsKey("FTR")) {
                fixture.outcome = entries.get(i).get("FTR");
            } else {
                fixture.outcome = entries.get(i).get("Res");
            }

            fixtures.add(fixture);
        }
        return fixtures;
    }

    /**
     * Return the last games of a given team.
     * It is expected that this.entries is sorted by Date in descending order.
     *
     * @param teamName      last games of given team
     * @param numberOfGames find at most "numberOfGames" entries
     * @param terrain       Specify current terrain for the given team: HOME, AWAY, ALL
     * @param startIndex    offset for this.entries
     * @return list with the last games of the specified team
     */
    private List<Map<String, String>> getLastGames(String teamName, int numberOfGames, Terrain terrain, int startIndex) {

        List<Map<String, String>> lastGames = new ArrayList<>();

        for (int i = startIndex; i < entries.size(); i++) {
            switch (terrain) {
                case ALL:
                    if (entries.get(i).get("HomeTeam").equals(teamName) || entries.get(i).get("AwayTeam").equals(teamName)) {
                        lastGames.add(entries.get(i));
                    }
                    break;
                case HOME:
                    if (entries.get(i).get("HomeTeam").equals(teamName)) {
                        lastGames.add(entries.get(i));
                    }
                    break;
                case AWAY:
                    if (entries.get(i).get("AwayTeam").equals(teamName)) {
                        lastGames.add(entries.get(i));
                    }
                    break;
            }

            if (lastGames.size() == numberOfGames) {
                break;
            }
        }

        return lastGames;
    }

    /**
     * Return the last games 1v1 of given teams
     * It is expected that this.entries is sorted by Date in descending order.
     *
     * @param numberOfGames find at most "numberOfGames" entries
     * @param startIndex    offset for this.entries
     * @return list with the last games 1v1 of the specified teams
     */
    private List<Map<String, String>> getLastGames1v1(String team1, String team2, int numberOfGames, int startIndex) {

        List<Map<String, String>> lastGames = new ArrayList<>();

        for (int i = startIndex; i < entries.size(); i++) {

            Set<String> teamsName = new HashSet<>();
            teamsName.add(entries.get(i).get("HomeTeam"));
            teamsName.add(entries.get(i).get("AwayTeam"));

            if (teamsName.contains(team1) && teamsName.contains(team2)) {
                lastGames.add(entries.get(i));
            }

            if (lastGames.size() == numberOfGames) {
                break;
            }
        }

        return lastGames;
    }

    private void sortEntriesByDateDescending() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        entries.sort((entry1, entry2) -> {
            Date date1 = null, date2 = null;
            try {
                date1 = dateFormatter.parse(entry1.get("Date"));
                date2 = dateFormatter.parse(entry2.get("Date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return date1.compareTo(date2) * -1;
        });
    }

    private Fixture.Stats calculateTeamStats(List<Map<String, String>> games, String teamName) {
        Fixture.Stats stats = new Fixture.Stats();

        for (var entry : games) {
            String result = (entry.containsKey("FTR")) ? entry.get("FTR") : entry.get("Res");

            switch (result) {
                case "H":
                    if (entry.get("HomeTeam").equals(teamName)) {
                        stats.wins++;
                    } else {
                        stats.looses++;
                    }
                    break;
                case "D":
                    stats.draws++;
                    break;
                case "A":
                    if (entry.get("HomeTeam").equals(teamName)) {
                        stats.looses++;
                    } else {
                        stats.wins++;
                    }
                    break;
            }
        }

        return stats;
    }

    private double calculateGoalsAgainst(List<Map<String, String>> games, String teamName) {
        double goalsAgainst = 0;

        for (var entry : games) {
            if (entry.get("HomeTeam").equals(teamName)) {
                if (entry.containsKey("FTAG")) {
                    goalsAgainst += Double.valueOf(entry.get("FTAG"));
                } else if (entry.containsKey("AG")) {
                    goalsAgainst += Double.valueOf(entry.get("AG"));
                }
            } else if (entry.get("AwayTeam").equals(teamName)) {
                if (entry.containsKey("FTHG")) {
                    goalsAgainst += Double.valueOf(entry.get("FTHG"));
                } else if (entry.containsKey("HG")) {
                    goalsAgainst += Double.valueOf(entry.get("HG"));
                }
            }
        }

        return goalsAgainst;
    }

    private double calculateGoalsForward(List<Map<String, String>> games, String teamName) {
        double goalsForward = 0;

        for (var entry : games) {
            if (entry.get("HomeTeam").equals(teamName)) {
                if (entry.containsKey("FTHG")) {
                    goalsForward += Double.valueOf(entry.get("FTHG"));
                } else if (entry.containsKey("HG")) {
                    goalsForward += Double.valueOf(entry.get("HG"));
                }
            } else if (entry.get("AwayTeam").equals(teamName)) {
                if (entry.containsKey("FTAG")) {
                    goalsForward += Double.valueOf(entry.get("FTAG"));
                } else if (entry.containsKey("AG")) {
                    goalsForward += Double.valueOf(entry.get("AG"));
                }
            }
        }

        return goalsForward;
    }

    private Fixture.Stats getLast5GamesStats(int startIndex, String teamName, Terrain terrain) {
        List<Map<String, String>> last5Games = getLastGames(teamName, 5, terrain, startIndex);

        return calculateTeamStats(last5Games, teamName);
    }

    private Fixture.Stats getLast5Games1v1StatsTeam1(int startIndex, String homeTeamName, String awayTeamName) {
        List<Map<String, String>> last5Games1v1 = getLastGames1v1(homeTeamName, awayTeamName, 5, startIndex);


        return calculateTeamStats(last5Games1v1, homeTeamName);
    }

    private double getGoalsAgainstInLast5Games(int startIndex, String teamName, Terrain terrain) {
        List<Map<String, String>> last5Games = getLastGames(teamName, 5, terrain, startIndex);

        return calculateGoalsAgainst(last5Games, teamName);
    }

    private double getGoalsForwardInLast5Games(int startIndex, String teamName, Terrain terrain) {
        List<Map<String, String>> last5Games = getLastGames(teamName, 5, terrain, startIndex);

        return calculateGoalsForward(last5Games, teamName);
    }
}
