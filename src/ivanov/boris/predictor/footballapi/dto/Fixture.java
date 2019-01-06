package ivanov.boris.predictor.footballapi.dto;

import com.google.gson.annotations.SerializedName;

public class Fixture {

    @SerializedName("match_id")
    private String id;

    @SerializedName("country_id")
    private String countryId;

    @SerializedName("country_name")
    private String countryName;

    @SerializedName("league_name")
    private String leagueName;

    @SerializedName("league_id")
    private String leagueId;

    @SerializedName("match_date")
    private String date;

    @SerializedName("match_time")
    private String time;

    @SerializedName("match_hometeam_name")
    private String homeTeamName;

    @SerializedName("match_awayteam_name")
    private String awayTeamName;

    @SerializedName("match_hometeam_score")
    private String homeTeamScore;

    @SerializedName("match_awayteam_score")
    private String awayTeamScore;

    @SerializedName("match_status")
    private String matchStatus;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public String getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(String homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public String getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(String awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public String getOutcome() {
        if (!matchStatus.equals("FT") || homeTeamScore == null || awayTeamScore == null || homeTeamScore.equals("") || awayTeamScore.equals("")) {
            return "?";
        }

        int homeScore = Integer.parseInt(homeTeamScore);
        int awayScore = Integer.parseInt(awayTeamScore);

        String result;

        if (homeScore == awayScore) {
            result = "X";
        }
        else if (homeScore > awayScore) {
            result = "1";
        }
        else {
            result = "2";
        }

        return result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
