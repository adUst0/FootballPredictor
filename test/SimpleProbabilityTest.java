import ivanov.boris.predictor.classifier.other.RandomGuess;
import ivanov.boris.predictor.classifier.other.SimpleProbability;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleProbabilityTest {
    @Test
    public void testClassify() {
        SimpleProbability simpleProbability = new SimpleProbability();
        Dataset dataset = new Dataset();
        dataset.addEntry(new DatasetEntry("0 0 0  0 0 0  6 0 0  6 0 0     0 0 0  0 0 0  0 0 6     1", "\\s+"));
        simpleProbability.buildModel(dataset);

        assertEquals(simpleProbability.classify(
                new DatasetEntry("0 0 0  0 0 0  6 0 0  6 0 0     0 0 0  0 0 0  0 0 6     1", "\\s+")), "1");

        dataset = new Dataset();
        dataset.addEntry(new DatasetEntry("0 0 0  0 0 0  0 6 0  0 6 0     0 0 0  0 0 0  0 6 0     1", "\\s+"));
        simpleProbability.buildModel(dataset);

        assertEquals(simpleProbability.classify(
                new DatasetEntry("0 0 0  0 0 0  0 6 0  0 6 0     0 0 0  0 0 0  0 6 0     1", "\\s+")), "X");

        dataset = new Dataset();
        dataset.addEntry(new DatasetEntry("0 0 0  0 0 0  0 0 6  0 0 6     0 0 0  0 0 0  6 0 0     1", "\\s+"));
        simpleProbability.buildModel(dataset);

        assertEquals(simpleProbability.classify(
                new DatasetEntry("0 0 0  0 0 0  0 0 6  0 0 6     0 0 0  0 0 0  6 0 0     1", "\\s+")), "2");
    }
}
