package ivanov.boris.predictor.footballapi;

import ivanov.boris.predictor.dataset.DatasetEntry;
import ivanov.boris.predictor.footballapi.dto.Fixture;
import ivanov.boris.predictor.footballapi.dto.H2H;
import ivanov.boris.predictor.footballapi.dto.LeagueTeam;

import java.io.*;
import java.net.http.HttpClient;
import java.util.*;

public class DatasetBuilder {
    private static FootballAPIClient footballAPIClient;
    private static String apiKey = "3e265c14f3bac8645bf5c7389dc33c95e426d19ba0499cedb7b75fccc0b0df14";

    private static Map<String, String> countries = new HashMap<>();
    private static List<String> leagueIds;

    public static void main(String[] args) throws IOException, InterruptedException {
//        HttpClient client = HttpClient.newHttpClient();
//        footballAPIClient = new FootballAPIClient(apiKey, client);

//        String from = "2019-01-14";
//        String to = "2019-01-20";
//
//        countries.put("England", "169");
//        countries.put("Spain", "171");
//        countries.put("France", "173");
//        countries.put("Italy", "170");
//        countries.put("Portugal", "176");
//        countries.put("Germany", "172");
//
//
//        leagueIds = Arrays.asList(
//                "62", "63", "64", "65", // En: Premier League, Championship, League 1, League 2
//                "109", "110", // Es: Primera / Segunda division
//                "127", "128", // Fr: League 1/2
//                "79", "81", // It: Serie A/B
//                "150", "151", // Pt: Primeira/Segunda Liga
//                "117", "118" // Germany: Bundesliga, 2nd Bundesliga
//        );
//
////         findGames(from, to);
//        updateOutcomes("2019-01-05", "2019-01-14");

        /*List<Fixture> fixtures = footballAPIClient.getFixtures("2019-01-06", "2019-01-06");
        Map<String, String> game_league = new HashMap<>();
        for (Fixture fixture : fixtures) {
            game_league.put(fixture.getId(), fixture.getLeagueId());
        }

        final String fileName = "Data/football_filtered_input.data";
        final String outFileName = "Data/football_filtered_output.data";

        List<String> file = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String comment = bufferedReader.readLine();
            String game = bufferedReader.readLine();
            String empty = bufferedReader.readLine();


            while (comment != null) {

                String[] tokens = comment.split(", ");
                String gameId = tokens[tokens.length - 1];

                if (leagueIds.contains(game_league.get(gameId))) {
                    file.add(comment);
                    file.add(game);
                    if (empty != null) {
                        file.add(empty);
                    }
                }

                comment = bufferedReader.readLine();
                game = bufferedReader.readLine();
                empty = bufferedReader.readLine();
            }


        } catch (IOException e) {
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
        }*/


    }

    private static void findGames(String from, String to) throws IOException, InterruptedException {
        List<Fixture> fixtures = footballAPIClient.getFixtures(from, to);

        for (Fixture fixture : fixtures) {
            try {
                if (!fixture.getOutcome().equals("?") || !leagueIds.contains(fixture.getLeagueId())) {
                    continue;
                }

                String matchMetaData = getMatchMetaData(fixture);
                String matchData = getMatchData(fixture);

                System.out.println(matchMetaData);
                System.out.println("% " + ">" + fixture.getId() + " " + matchData);
                System.out.println();
            } catch (NullPointerException e) {
                continue;
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }

        System.out.println(fixtures);
    }

    private static void updateOutcomes(String from, String to) throws IOException, InterruptedException {
        final String fileName = "Data/games_week3.data";
        final String outFileName = "Data/games_week3_outCome.data";

        List<Fixture> fixtures = footballAPIClient.getFixtures(from, to);
        Map<String, String> game_outCome = new HashMap<>();
        for (Fixture fixture : fixtures) {
            game_outCome.put(fixture.getId(), fixture.getOutcome());
        }

        List<String> file = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String line = bufferedReader.readLine();

            while (line != null) {
                // check if the current line is Fixture data
                if (line.contains("% >")) {

                    String id = line.split(" ")[1].substring(1);

                    if (!game_outCome.containsKey(id) || game_outCome.get(id).equals("?")) {
                        file.add(line);

                        line = bufferedReader.readLine();
                        continue;
                    }

                    line = line.replace("?", game_outCome.get(id));
                    line = line.substring(3 + id.length() + 1);
                }
                file.add(line);

                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
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

    private static void toBeDeletedASAP() {
        final String fileName = "Data/SelectedLeaguesOnly.data";
        final String outFileName = "Data/SelectedLeaguesOnly_update.data";

        List<String> file = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String comment = bufferedReader.readLine();
            String data = bufferedReader.readLine();
            String newLine = bufferedReader.readLine();

            while (comment != null) {

                file.add(comment);
                file.add(data);
                file.add(newLine);

                comment = bufferedReader.readLine();
            }
        } catch (IOException e) {
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
        String awayTeam1v1 = h2h.get1v1SecondTeam();

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
