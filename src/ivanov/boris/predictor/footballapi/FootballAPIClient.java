package ivanov.boris.predictor.footballapi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ivanov.boris.predictor.footballapi.dto.Fixture;
import ivanov.boris.predictor.footballapi.dto.H2H;
import ivanov.boris.predictor.footballapi.dto.LeagueTeam;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FootballAPIClient {

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

    public List<Fixture> getFixtures(String date, String leagueId) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder().uri(
                URI.create(API_URL + "?action=get_events&from=" + date + "&to=" + date +
                        "&league_id=" + leagueId + "&APIkey=" + apiKey)).build();

        return getFixtures(request);
    }

    public Fixture getFixture(String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(
                URI.create(API_URL + "?action=get_events&match_id=" + id + "&APIkey=" + apiKey)).build();

        return getFixtures(request).get(0);
    }

    public H2H getH2H(String firstTeam, String secondTeam, String leagueId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(
                URI.create(API_URL + "?action=get_H2H&firstTeam=" + URLEncoder.encode(firstTeam, StandardCharsets.UTF_8)
                        + "&secondTeam=" + URLEncoder.encode(secondTeam, StandardCharsets.UTF_8)
                        + "&APIkey=" + apiKey)).build();


        Gson gson = new Gson();
        String json = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

        H2H h2h = gson.fromJson(json, H2H.class);
        h2h.setFirstTeamName(firstTeam);
        h2h.setSecondTeamName(secondTeam);
        h2h.setLeagueId(leagueId);

        return h2h;
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
