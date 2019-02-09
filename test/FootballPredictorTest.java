import ivanov.boris.predictor.Fixture;
import ivanov.boris.predictor.TrainingDataPreprocessor;
import ivanov.boris.predictor.classifier.other.FootballPredictor;
import ivanov.boris.predictor.classifier.other.SimpleProbability;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FootballPredictorTest {
    @Test
    public void testClassifyWithVeryProbableOutcome() {
        FootballPredictor footballPredictor = new FootballPredictor();

        assertEquals(footballPredictor.classify(
                new DatasetEntry("0 0 0  0 0 0  6 0 0  6 0 0     0 0 0  0 0 0  0 0 6     1", "\\s+")), "1");

    }

    @Test
    public void testClassifyWithLessProbableOutcome() {
        Dataset dataset = new Dataset();
        dataset.addEntry(
                new DatasetEntry("0 0 0  0 0 0  1 2 3  1 2 3     0 0 0  0 0 0  1 2 3     1", "\\s+"));
        dataset.addEntry(
                new DatasetEntry("0 0 0  0 0 0  1 2 3  1 2 3     0 0 0  0 0 0  1 2 3     2", "\\s+"));
        dataset.addEntry(
                new DatasetEntry("0 0 0  0 0 0  1 2 3  1 2 3     0 0 0  0 0 0  1 2 3     2", "\\s+"));
        FootballPredictor footballPredictor = new FootballPredictor();
        footballPredictor.buildModel(dataset);

        DatasetEntry toPredict = new DatasetEntry("0 0 0  0 0 0  1 2 3  1 2 3     0 0 0  0 0 0  1 2 3     X", "\\s+");

        assertEquals(footballPredictor.classify(toPredict), "2");
    }
}
