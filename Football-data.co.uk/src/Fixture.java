import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fixture {

    public static class Stats {
        public double wins;
        public double draws;
        public double looses;

        @Override
        public String toString() {
            return String.format("%f %f %f", wins, draws, looses);
        }
    }

    public Stats t1_League = new Stats(); // all games in current league season
    public Stats t1_Last5Games = new Stats();
    public Stats t1_Last5GamesHome = new Stats();
    public Stats t1_Last5Games1v1 = new Stats();
    public double t1_GoalsForwardInLast5Games;
    public double t1_GoalsAgainstInLast5Games;
    public double t1_GoalsForwardInLast5GamesHome;
    public double t1_GoalsAgainstInLast5GamesHome;

    public Stats t2_League = new Stats(); // all games in current league season
    public Stats t2_Last5Games = new Stats();
    public Stats t2_Last5GamesAway = new Stats();
    public double t2_GoalsForwardInLast5Games;
    public double t2_GoalsAgainstInLast5Games;
    public double t2_GoalsForwardInLast5GamesAway;
    public double t2_GoalsAgainstInLast5GamesAway;

    public String outcome;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(t1_League).append(" ");
        sb.append(t1_Last5Games).append(" ");
        sb.append(t1_Last5GamesHome).append(" ");
        sb.append(t1_Last5Games1v1).append(" ");
        sb.append(t1_GoalsForwardInLast5Games).append(" ");
        sb.append(t1_GoalsAgainstInLast5Games).append(" ");
        sb.append(t1_GoalsForwardInLast5GamesHome).append(" ");
        sb.append(t1_GoalsAgainstInLast5GamesHome).append(" ");
        sb.append("     ");

        sb.append(t2_League).append(" ");
        sb.append(t2_Last5Games).append(" ");
        sb.append(t2_Last5GamesAway).append(" ");
        sb.append(t2_GoalsForwardInLast5Games).append(" ");
        sb.append(t2_GoalsAgainstInLast5Games).append(" ");
        sb.append(t2_GoalsForwardInLast5GamesAway).append(" ");
        sb.append(t2_GoalsAgainstInLast5GamesAway).append(" ");
        sb.append("     ");

        sb.append(outcome);
        return sb.toString();
    }
}
