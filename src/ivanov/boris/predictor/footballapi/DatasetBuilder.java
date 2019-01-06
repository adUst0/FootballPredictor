package ivanov.boris.predictor.footballapi;

import ivanov.boris.predictor.footballapi.dto.Fixture;
import ivanov.boris.predictor.footballapi.dto.LeagueTeam;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;

public class DatasetBuilder {
    private static FootballAPIClient footballAPIClient;
    private static String apiKey = "3e265c14f3bac8645bf5c7389dc33c95e426d19ba0499cedb7b75fccc0b0df14";

    public static void main(String[] args) throws IOException, InterruptedException {
        String date = "2019-01-06";

        HttpClient client = HttpClient.newHttpClient();
        footballAPIClient = new FootballAPIClient(apiKey, client);

        List<Fixture> fixtures = footballAPIClient.getFixtures(date);

        for (Fixture fixture : fixtures) {
            String matchMetaData = getMatchMetaData(fixture);
            String matchData = getMatchData(fixture);

//            TODO
            footballAPIClient.getH2H(fixture.getHomeTeamName(), fixture.getAwayTeamName());
            footballAPIClient.getTeamStatsInLeague(fixture.getLeagueId(), fixture.getHomeTeamName());
        }

        System.out.println(fixtures);
    }

    private static String getMatchData(Fixture fixture) throws IOException, InterruptedException {
        StringBuilder sb = new StringBuilder();

        String homeTeamLeagueStats = getLeagueStats(fixture.getLeagueId(), fixture.getHomeTeamName());
        String awayTeamLeagueStats = getLeagueStats(fixture.getLeagueId(), fixture.getAwayTeamName());


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
                append(fixture.getDate()).append(", ").append(fixture.getLeagueName()).append(fixture.getId());
        return sb.toString();
    }
}
