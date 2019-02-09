import ivanov.boris.predictor.Fixture;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FixtureTest {
    private static final double DELTA = 0.001;

    @Test
    public void testGetOutcomeProbabilities() {
        Fixture fixture = Fixture.fromString("0 0 0  0 0 0  6 0 0  6 0 0     0 0 0  0 0 0  0 0 6     1");

        assertEquals(1.0, (double) fixture.getOutcomeProbabilities().get("1"), DELTA);
        assertEquals(0, (double) fixture.getOutcomeProbabilities().get("2"), DELTA);
        assertEquals(0, (double) fixture.getOutcomeProbabilities().get("X"), DELTA);
    }

    @Test
    public void testFromDatasetEntry() {
        DatasetEntry entry = new DatasetEntry("0.0 1.0 2.0 3.0 4.0 5.0 6.0 7.0 8.0 9.0 10.0 11.0 12.0 13.0 14.0 15.0 16.0 17.0 18.0 19.0 20.0 1X2", "\\s+");
        Fixture fixture = Fixture.fromDatasetEntry(entry);

        assertEquals(String.valueOf(fixture.team1League.wins), entry.getAttributes().get(0));
        assertEquals(String.valueOf(fixture.team1League.draws), entry.getAttributes().get(1));
        assertEquals(String.valueOf(fixture.team1League.looses), entry.getAttributes().get(2));
        assertEquals(String.valueOf(fixture.team1Last6Games.wins), entry.getAttributes().get(3));
        assertEquals(String.valueOf(fixture.team1Last6Games.draws), entry.getAttributes().get(4));
        assertEquals(String.valueOf(fixture.team1Last6Games.looses), entry.getAttributes().get(5));
        assertEquals(String.valueOf(fixture.team1Last6GamesHome.wins), entry.getAttributes().get(6));
        assertEquals(String.valueOf(fixture.team1Last6GamesHome.draws), entry.getAttributes().get(7));
        assertEquals(String.valueOf(fixture.team1Last6GamesHome.looses), entry.getAttributes().get(8));
        assertEquals(String.valueOf(fixture.team1Last6Games1v1.wins), entry.getAttributes().get(9));
        assertEquals(String.valueOf(fixture.team1Last6Games1v1.draws), entry.getAttributes().get(10));
        assertEquals(String.valueOf(fixture.team1Last6Games1v1.looses), entry.getAttributes().get(11));
        assertEquals(String.valueOf(fixture.team2League.wins), entry.getAttributes().get(12));
        assertEquals(String.valueOf(fixture.team2League.draws), entry.getAttributes().get(13));
        assertEquals(String.valueOf(fixture.team2League.looses), entry.getAttributes().get(14));
        assertEquals(String.valueOf(fixture.team2Last6Games.wins), entry.getAttributes().get(15));
        assertEquals(String.valueOf(fixture.team2Last6Games.draws), entry.getAttributes().get(16));
        assertEquals(String.valueOf(fixture.team2Last6Games.looses), entry.getAttributes().get(17));
        assertEquals(String.valueOf(fixture.team2Last6GamesAway.wins), entry.getAttributes().get(18));
        assertEquals(String.valueOf(fixture.team2Last6GamesAway.draws), entry.getAttributes().get(19));
        assertEquals(String.valueOf(fixture.team2Last6GamesAway.looses), entry.getAttributes().get(20));
        assertEquals(fixture.outcome, entry.getLabel());
    }

    @Test
    public void testToDatasetEntry() {
        final String entry = "0.0 0.0 0.0  0.0 0.0 0.0  6.0 0.0 0.0  6.0 0.0 0.0     0.0 0.0 0.0  0.0 0.0 0.0  0.0 0.0 6.0     1";
        Fixture fixture = Fixture.fromString(entry);
        DatasetEntry datasetEntryFromFixture = fixture.toDatasetEntry();
        DatasetEntry datasetEntryFromString = new DatasetEntry(entry, "\\s+");

        assertEquals(datasetEntryFromFixture.getAttributes(), datasetEntryFromString.getAttributes());
        assertEquals(datasetEntryFromFixture.getLabel(), datasetEntryFromString.getLabel());
    }

}
