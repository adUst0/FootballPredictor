import ivanov.boris.predictor.Fixture;
import ivanov.boris.predictor.dataset.Dataset;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FixtureTest {
    private static final double DELTA = 0.001;
    private static final double MAX_GAMES = 6;

    @Test
    public void testGetOutcomeProbabilities() {
        Fixture fixture = new Fixture();

        fixture.team1Last6Games1v1.wins = MAX_GAMES;
        fixture.team1Last6Games1v1.draws = 0;
        fixture.team1Last6Games1v1.looses = 0;

        fixture.team1Last6GamesHome.wins = MAX_GAMES;
        fixture.team1Last6GamesHome.draws = 0;
        fixture.team1Last6GamesHome.looses = 0;

        fixture.team2Last6GamesAway.wins = 0;
        fixture.team2Last6GamesAway.draws = 0;
        fixture.team2Last6GamesAway.looses = MAX_GAMES;

        assertEquals(1.0, (double) fixture.getOutcomeProbabilities().get("1"), DELTA);
        assertEquals(0, (double) fixture.getOutcomeProbabilities().get("2"), DELTA);
        assertEquals(0, (double) fixture.getOutcomeProbabilities().get("X"), DELTA);
    }

    @Test
    public void testFromDatasetEntry() {
//        DatasetParserrrrr<Double> datasetParser = new DoubleDatasetParser();
//        Dataset dataset = datasetParser.fromFile("Data/SelectedLeaguesOnly.data", "\\s+");
//
//        var entry = dataset.getEntries().get(0);
//        Fixture fixture = Fixture.fromDatasetEntry(entry);
//
//        assertEquals(fixture.team1League.wins, entry.getAttributes().get(0), DELTA);
//        assertEquals(fixture.team1League.draws, entry.getAttributes().get(1), DELTA);
//        assertEquals(fixture.team1League.looses, entry.getAttributes().get(2), DELTA);
//        assertEquals(fixture.team1Last6Games.wins, entry.getAttributes().get(3), DELTA);
//        assertEquals(fixture.team1Last6Games.draws, entry.getAttributes().get(4), DELTA);
//        assertEquals(fixture.team1Last6Games.looses, entry.getAttributes().get(5), DELTA);
//        assertEquals(fixture.team1Last6GamesHome.wins, entry.getAttributes().get(6), DELTA);
//        assertEquals(fixture.team1Last6GamesHome.draws, entry.getAttributes().get(7), DELTA);
//        assertEquals(fixture.team1Last6GamesHome.looses, entry.getAttributes().get(8), DELTA);
//        assertEquals(fixture.team1Last6Games1v1.wins, entry.getAttributes().get(9), DELTA);
//        assertEquals(fixture.team1Last6Games1v1.draws, entry.getAttributes().get(10), DELTA);
//        assertEquals(fixture.team1Last6Games1v1.looses, entry.getAttributes().get(11), DELTA);
//        assertEquals(fixture.team2League.wins, entry.getAttributes().get(12), DELTA);
//        assertEquals(fixture.team2League.draws, entry.getAttributes().get(13), DELTA);
//        assertEquals(fixture.team2League.looses, entry.getAttributes().get(14), DELTA);
//        assertEquals(fixture.team2Last6Games.wins, entry.getAttributes().get(15), DELTA);
//        assertEquals(fixture.team2Last6Games.draws, entry.getAttributes().get(16), DELTA);
//        assertEquals(fixture.team2Last6Games.looses, entry.getAttributes().get(17), DELTA);
//        assertEquals(fixture.team2Last6GamesAway.wins, entry.getAttributes().get(18), DELTA);
//        assertEquals(fixture.team2Last6GamesAway.draws, entry.getAttributes().get(19), DELTA);
//        assertEquals(fixture.team2Last6GamesAway.looses, entry.getAttributes().get(20), DELTA);
//        assertEquals(fixture.outcome, entry.getLabel());
    }

    @Test
    public void testFromToDatasetEntry() {
//        Fixture fixture = new Fixture();
//
//        var entry = fixture.toDatasetEntry();
//
//        assertEquals(entry.getAttributes().get(0), 0, DELTA);
//        assertEquals(entry.getAttributes().get(1), 0, DELTA);
//        assertEquals(entry.getAttributes().get(2), 0, DELTA);
//        assertEquals(entry.getAttributes().get(3), 0, DELTA);
//        assertEquals(entry.getAttributes().get(4), 0, DELTA);
//        assertEquals(entry.getAttributes().get(5), 0, DELTA);
//        assertEquals(entry.getAttributes().get(6), 0, DELTA);
//        assertEquals(entry.getAttributes().get(7), 0, DELTA);
//        assertEquals(entry.getAttributes().get(8), 0, DELTA);
//        assertEquals(entry.getAttributes().get(9), 0, DELTA);
//        assertEquals(entry.getAttributes().get(10), 0, DELTA);
//        assertEquals(entry.getAttributes().get(11), 0, DELTA);
//        assertEquals(entry.getAttributes().get(12), 0, DELTA);
//        assertEquals(entry.getAttributes().get(13), 0, DELTA);
//        assertEquals(entry.getAttributes().get(14), 0, DELTA);
//        assertEquals(entry.getAttributes().get(15), 0, DELTA);
//        assertEquals(entry.getAttributes().get(16), 0, DELTA);
//        assertEquals(entry.getAttributes().get(17), 0, DELTA);
//        assertEquals(entry.getAttributes().get(18), 0, DELTA);
//        assertEquals(entry.getAttributes().get(19), 0, DELTA);
//        assertEquals(entry.getAttributes().get(20), 0, DELTA);
//        assertNull(entry.getLabel());

    }

}
