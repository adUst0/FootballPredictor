package ivanov.boris.predictor.footballapi.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class H2H {
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
}
