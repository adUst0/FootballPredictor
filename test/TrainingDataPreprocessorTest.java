import ivanov.boris.predictor.TrainingDataPreprocessor;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrainingDataPreprocessorTest {

    @Test
    public void testRemoveOutliers() {
        Dataset dataset = new Dataset();
        dataset.addEntry(
                new DatasetEntry("0 0 0  0 0 0  6 0 0  6 0 0     0 0 0  0 0 0  0 0 6     X", "\\s+"));

        TrainingDataPreprocessor.prepare(dataset);

        assertEquals(dataset.size(), 0);
    }

    @Test
    public void testRemoveNoiseData() {
        Dataset dataset = new Dataset();
        dataset.addEntry(
                new DatasetEntry("0 0 0  0 0 0  6 0 0  2 2 2     0 0 0  0 0 0  0 0 6     1", "\\s+"));
        TrainingDataPreprocessor.prepare(dataset);

        assertEquals(dataset.size(), 0);
    }
}
