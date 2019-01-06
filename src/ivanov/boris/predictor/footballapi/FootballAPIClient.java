package ivanov.boris.predictor.footballapi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ivanov.boris.predictor.footballapi.dto.Fixture;
import ivanov.boris.predictor.footballapi.dto.H2H;
import ivanov.boris.predictor.footballapi.dto.LeagueTeam;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class FootballAPIClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        String apiKey = "3e265c14f3bac8645bf5c7389dc33c95e426d19ba0499cedb7b75fccc0b0df14";

        HttpClient client = HttpClient.newHttpClient();
        FootballAPIClient footballAPIClient = new FootballAPIClient(apiKey, client);

        List<Fixture> fixtures = footballAPIClient.getFixtures("2019-01-06");

        Fixture fixture = footballAPIClient.getFixture("377802");

        H2H h2h = footballAPIClient.getH2H("Chelsea", "Arsenal");
        LeagueTeam stats = footballAPIClient.getTeamStatsInLeague("62", "Liverpool");

        System.out.println(fixtures);

    }

    private final String API_URL = "https://apifootball.com/api/";

    private String apiKey;
    private HttpClient client;

    private List<Fixture> getFixtures(HttpRequest request) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String json = client.send(request,
                HttpResponse.BodyHandlers.ofString()).body();

        Type type = new TypeToken<List<Fixture>>(){}.getType();
        List<Fixture> fixtures = gson.fromJson(json, type);

        return fixtures;
    }

    public FootballAPIClient(String apiKey, HttpClient client) {
        this.apiKey = apiKey;
        this.client = client;
    }

    public List<Fixture> getFixtures(String date) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder().uri(
                URI.create(API_URL + "?action=get_events&from=" + date + "&to=" + date + "&APIkey=" + apiKey)).build();

        return getFixtures(request);
    }

    public Fixture getFixture(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(
                URI.create(API_URL + "?action=get_events&match_id=" + id + "&APIkey=" + apiKey)).build();

        return getFixtures(request).get(0);
    }

    public H2H getH2H(String firstTeam, String secondTeam) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(
                URI.create(API_URL + "?action=get_H2H&firstTeam=" + firstTeam + "&secondTeam=" + secondTeam
                        + "&APIkey=" + apiKey)).build();

        Gson gson = new Gson();
        String json = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

        return gson.fromJson(json, H2H.class);
    }

    public LeagueTeam getTeamStatsInLeague(String leagueId, String teamName) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(
                URI.create(API_URL + "?action=get_standings&league_id=" + leagueId + "&APIkey=" + apiKey)).build();

        Gson gson = new Gson();
        String json = client.send(request,
                HttpResponse.BodyHandlers.ofString()).body();

        Type type = new TypeToken<List<LeagueTeam>>(){}.getType();
        List<LeagueTeam> stats = gson.fromJson(json, type);

        for (LeagueTeam stat : stats) {
            if (stat.getTeamName().equals(teamName)) {
                return stat;
            }
        }

        return null;
    }
}
