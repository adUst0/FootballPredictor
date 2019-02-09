import ivanov.boris.predictor.classifier.other.RandomGuess;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RandomGuessTest {

    @Test
    public void testRandomGuess() {
        Dataset dataset = new Dataset();

        dataset.addEntry(new DatasetEntry("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 YES", "\\s+"));
        dataset.addEntry(new DatasetEntry("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 NO", "\\s+"));
        dataset.addEntry(new DatasetEntry("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 MAYBE", "\\s+"));

        RandomGuess randomGuess = new RandomGuess();
        randomGuess.buildModel(dataset);

        DatasetEntry toPredict = new DatasetEntry("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 MAYBE", "\\s+");

        assertTrue(Arrays.asList("YES", "NO", "MAYBE").contains(randomGuess.classify(toPredict)));
    }
}
