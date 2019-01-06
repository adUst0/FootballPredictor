package ivanov.boris.predictor.footballapi.dto;

import com.google.gson.annotations.SerializedName;

public class LeagueTeam {
    @SerializedName("league_id")
    private String leagueId;

    @SerializedName("league_name")
    private String leagueName;

    @SerializedName("team_name")
    private String teamName;

    @SerializedName("overall_league_PTS")
    private String points;

    @SerializedName("overall_league_W")
    private String wins;

    @SerializedName("overall_league_D")
    private String draws;

    @SerializedName("overall_league_L")
    private String looses;

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getDraws() {
        return draws;
    }

    public void setDraws(String draws) {
        this.draws = draws;
    }

    public String getLooses() {
        return looses;
    }

    public void setLooses(String looses) {
        this.looses = looses;
    }
}
