package ivanov.boris.predictor.footballapi;

import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.footballapi.dto.Fixture;
import ivanov.boris.predictor.footballapi.dto.H2H;
import ivanov.boris.predictor.footballapi.dto.LeagueTeam;

import java.io.*;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatasetBuilder {
    private static FootballAPIClient footballAPIClient;
    private static String apiKey = "3e265c14f3bac8645bf5c7389dc33c95e426d19ba0499cedb7b75fccc0b0df14";

    private static String ES_SegundaDivision_ID = "110";
    private static String ES_PrimeraDivision_ID = "109";

    private static void updateOutcomes() {
        final String fileName = "Data/new_01.06.data";
        final String outFileName = "Data/new_01.06_update3.data";

        List<String> file = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String line = bufferedReader.readLine();

            while (line != null) {
                // check if the current line is Fixture data
                if (line.contains("% >")) {

                    String id = line.split(" ")[1].substring(1);

                    Fixture fixture = footballAPIClient.getFixture(id);

                    if (fixture.getOutcome().equals("?")) {
                        file.add(line);

                        line = bufferedReader.readLine();
                        continue;
                    }

                    String res = "X";
                    line = line.replace("?", res);
//                    line = line.replace("?", fixture.getOutcome());
                    line = line.substring(3 + id.length() + 1);
                }
                file.add(line);

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileName)))) {
            for (String line : file) {
                bw.write(line);
                bw.newLine();
                bw.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String date = "2019-01-06";

        HttpClient client = HttpClient.newHttpClient();
        footballAPIClient = new FootballAPIClient(apiKey, client);

        updateOutcomes();

//        List<Fixture> fixtures = footballAPIClient.getFixtures(date);
//
//        for (Fixture fixture : fixtures) {
//            try {
//                if (!fixture.getOutcome().equals("?")) {
//                    continue;
//                }
//
//                String matchMetaData = getMatchMetaData(fixture);
//                String matchData = getMatchData(fixture);
//
//                System.out.println(matchMetaData);
//                System.out.println("% " + ">" + fixture.getId() + " " + matchData);
//                System.out.println();
//            }
//            catch (NullPointerException e) {
//                continue;
//            }
//            catch (Exception e) {
//                // e.printStackTrace();
//            }
//        }
//
//        System.out.println(fixtures);
    }

    private static String getMatchData(Fixture fixture) throws IOException, InterruptedException {
        StringBuilder sb = new StringBuilder();

        String homeTeamLeagueStats = getLeagueStats(fixture.getLeagueId(), fixture.getHomeTeamName());
        String awayTeamLeagueStats = getLeagueStats(fixture.getLeagueId(), fixture.getAwayTeamName());

        H2H h2h = footballAPIClient.getH2H(fixture.getHomeTeamName(), fixture.getAwayTeamName(), fixture.getLeagueId());
        String homeTeamLast6 = h2h.getFirstTeamLast6Games(false);
        String homeTeamLast6Home = h2h.getFirstTeamLast6Games(true);
        String awayTeamLast6 = h2h.getSecondTeamLast6Games(false);
        String awayTeamLast6Away = h2h.getSecondTeamLast6Games(true);
        String homeTeam1v1 = h2h.get1v1FirstTeam();
        String awayTeam1v1 = h2h.get1v1FirstTeam();

        sb.append(homeTeamLeagueStats).append(" ").append(homeTeamLast6).append(" ").append(homeTeamLast6Home).append(" ").append(homeTeam1v1).append("     ").
           append(awayTeamLeagueStats).append(" ").append(awayTeamLast6).append(" ").append(awayTeamLast6Away).append(" ").append(awayTeam1v1).append("     ").
           append("?");

        return sb.toString();
    }

    private static String getLeagueStats(String leagueId, String teamName) throws IOException, InterruptedException {
        LeagueTeam lg = footballAPIClient.getTeamStatsInLeague(leagueId, teamName);
        StringBuilder sb = new StringBuilder();
        sb.append(lg.getPoints()).append(" ").append(lg.getWins()).append(" ").append(lg.getDraws()).append(" ").
                append(lg.getLooses());
        return sb.toString();
    }


    private static String getMatchMetaData(Fixture fixture) {
        StringBuilder sb = new StringBuilder();
        sb.append("% ").append(fixture.getHomeTeamName()).append(" - ").append(fixture.getAwayTeamName()).append(", ").
                append(fixture.getDate()).append(", ").append(fixture.getLeagueName()).append(", ").
                append(fixture.getId());
        return sb.toString();
    }
}
