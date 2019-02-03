package ivanov.boris.predictor.footballapi;

import ivanov.boris.predictor.footballapi.dto.Fixture;
import ivanov.boris.predictor.footballapi.dto.H2H;
import ivanov.boris.predictor.footballapi.dto.LeagueTeam;

import java.io.*;
import java.net.http.HttpClient;
import java.util.*;

public class DatasetBuilder {
    private static final String API_KEY = "49f38e957674ce1d1d840e4d500c7fea429d8e624f504002075ea24a9ca6e2fa";
    private static final String ALLOWED_COUNTRIES_FILE_NAME = "Data/AllowedCountries.txt";
    private static final String ALLOWED_LEAGUES_FILE_NAME = "Data/AllowedLeagues.txt";

    private Map<String, String> allowedCountries = new HashMap<>();
    private List<String> allowedLeaguesId = new ArrayList<>();
    private FootballAPIClient footballAPIClient;

    public DatasetBuilder(String allowedCountriesFileName, String allowedLeaguesFileName, String apiKey) {
        footballAPIClient = new FootballAPIClient(apiKey, HttpClient.newHttpClient());

        readAllowedCountries(allowedCountriesFileName);
        readAllowedLeagues(allowedLeaguesFileName);
    }

    public void findGames(String fromDate, String toDate) throws IOException, InterruptedException {
        List<Fixture> fixtures = footballAPIClient.getFixtures(fromDate, toDate);

        for (Fixture fixture : fixtures) {
            try {
                if (!fixture.getOutcome().equals("?") ||
                        !allowedLeaguesId.contains(fixture.getLeagueId()) ||
                        !allowedCountries.keySet().contains(fixture.getCountryName())) {
                    continue;
                }

                String matchMetaData = getFixtureMetaData(fixture);
                String matchData = getFixtureData(fixture);

                System.out.println(matchMetaData);
                System.out.println("% " + ">" + fixture.getId() + " " + matchData);
                System.out.println();
            } catch (NullPointerException e) {
                // Do nothing. Just skip this entry!
            }
        }

        System.out.println(fixtures);
    }

    public void updateOutcomesFromFile(String fromDate, String toDate, String inputFileName,
                                       String outputFileName) throws IOException, InterruptedException {

        List<Fixture> fixtures = footballAPIClient.getFixtures(fromDate, toDate);
        Map<String, String> gamesOutCome = new HashMap<>();
        for (Fixture fixture : fixtures) {
            gamesOutCome.put(fixture.getId(), fixture.getOutcome());
        }

        List<String> file = new ArrayList<>();

        try (BufferedReader bufferedReader =
                     new BufferedReader(new InputStreamReader(new FileInputStream(inputFileName)))) {
            String line = bufferedReader.readLine();

            while (line != null) {
                // check if the current line is Fixture data
                if (line.contains("% >")) {

                    String currentFixtureId = line.split(" ")[1].substring(1);

                    if (!gamesOutCome.containsKey(currentFixtureId) || gamesOutCome.get(currentFixtureId).equals("?")) {
                        // Don't update the entry and leave it for future update.
                        file.add(line);
                        line = bufferedReader.readLine();
                        continue;
                    }

                    // Update the outcome and remove the prefix with the Fixture id
                    line = line.replace("?", gamesOutCome.get(currentFixtureId));
                    line = line.substring(3 + currentFixtureId.length() + 1);
                }
                file.add(line);

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)))) {
            for (String line : file) {
                bw.write(line);
                bw.newLine();
                bw.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAllowedLeagues(String allowedLeaguesFileName) {
        try (BufferedReader bufferedReader =
                     new BufferedReader(new InputStreamReader(new FileInputStream(allowedLeaguesFileName)))) {
            String line = bufferedReader.readLine();

            while (line != null) {

                if (line.isEmpty() || line.charAt(0) == '@' || line.charAt(0) == '%' || line.charAt(0) == '#') {

                    line = bufferedReader.readLine();
                    continue;
                }

                allowedLeaguesId.addAll(Arrays.asList(line.split("\\s+")));

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readAllowedCountries(String allowedCountriesFileName) {
        try (BufferedReader bufferedReader =
                     new BufferedReader(new InputStreamReader(new FileInputStream(allowedCountriesFileName)))) {
            String line = bufferedReader.readLine();

            while (line != null) {

                if (line.isEmpty() || line.charAt(0) == '@' || line.charAt(0) == '%' || line.charAt(0) == '#') {

                    line = bufferedReader.readLine();
                    continue;
                }

                String[] entry = line.split("\\s+");
                allowedCountries.put(entry[0], entry[1]);

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFixtureData(Fixture fixture) throws IOException, InterruptedException {
        StringBuilder sb = new StringBuilder();

        String homeTeamLeagueStats = getTeamStatsInLeague(fixture.getLeagueId(), fixture.getHomeTeamName());
        String awayTeamLeagueStats = getTeamStatsInLeague(fixture.getLeagueId(), fixture.getAwayTeamName());

        H2H h2h = footballAPIClient.getH2H(fixture.getHomeTeamName(), fixture.getAwayTeamName(), fixture.getLeagueId());
        String homeTeamLast6 = h2h.getFirstTeamLast6Games(false);
        String homeTeamLast6Home = h2h.getFirstTeamLast6Games(true);
        String awayTeamLast6 = h2h.getSecondTeamLast6Games(false);
        String awayTeamLast6Away = h2h.getSecondTeamLast6Games(true);
        String homeTeam1v1 = h2h.get1v1FirstTeam();
        String awayTeam1v1 = h2h.get1v1SecondTeam();

        sb.append(homeTeamLeagueStats).append(" ").append(homeTeamLast6).append(" ").append(homeTeamLast6Home).append(" ").append(homeTeam1v1).append("     ").
                append(awayTeamLeagueStats).append(" ").append(awayTeamLast6).append(" ").append(awayTeamLast6Away).append(" ").append(awayTeam1v1).append("     ").
                append("?");

        return sb.toString();
    }

    private String getFixtureMetaData(Fixture fixture) {
        StringBuilder sb = new StringBuilder();
        sb.append("% ").append(fixture.getHomeTeamName()).append(" - ").append(fixture.getAwayTeamName()).append(", ").
                append(fixture.getDate()).append(", ").append(fixture.getLeagueName()).append(", ").
                append(fixture.getId());
        return sb.toString();
    }

    private String getTeamStatsInLeague(String leagueId, String teamName)
            throws IOException, InterruptedException {
        LeagueTeam lg = footballAPIClient.getTeamStatsInLeague(leagueId, teamName);
        StringBuilder sb = new StringBuilder();
        sb.append(lg.getPoints()).append(" ").append(lg.getWins()).append(" ").append(lg.getDraws()).append(" ").
                append(lg.getLooses());
        return sb.toString();
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        String fromDate = "2019-02-02";
        String toDate = "2019-02-02";

        DatasetBuilder datasetBuilder = new DatasetBuilder(ALLOWED_COUNTRIES_FILE_NAME,
                ALLOWED_LEAGUES_FILE_NAME,
                API_KEY);

//        datasetBuilder.findGames(fromDate, toDate);

        // For demo: comment !fixture.getOutcome().equals("?") || in findGames()

//        datasetBuilder.updateOutcomesFromFile(fromDate, toDate, "Data/inFile", "Data/outFile");

    }
}
