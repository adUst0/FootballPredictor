import ivanov.boris.predictor.classifier.knn.KNearestNeighbors;
import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class KNearestNeighborsTest {

    private static final double DELTA = 0.001;
    private static final int K_NEIGHBORS_DEFAULT = 2;

    @Test
    public void testClassify() {
        Dataset dataset = new Dataset();
        dataset.addEntry(new DatasetEntry(Arrays.asList("1.0", "2.0", "3.0"), "YES"));
        dataset.addEntry(new DatasetEntry(Arrays.asList("10.0", "20.0", "30.0"), "NO"));
        dataset.addEntry(new DatasetEntry(Arrays.asList("2.0", "3.0", "4.0"), "YES"));
        dataset.addEntry(new DatasetEntry(Arrays.asList("1.0", "2.0", "3.0"), "YES"));
        dataset.addEntry(new DatasetEntry(Arrays.asList("1.0", "2.0", "3.0"), "YES"));
        dataset.addEntry(new DatasetEntry(Arrays.asList("1.0", "2.0", "3.0"), "YES"));
        dataset.addEntry(new DatasetEntry(Arrays.asList("10.0", "20.0", "30.0"), "YES"));

        KNearestNeighbors knn = new KNearestNeighbors();
        knn.buildModel(dataset);
        knn.setK(K_NEIGHBORS_DEFAULT);

        DatasetEntry toPredict = new DatasetEntry(Arrays.asList("105.0", "22.0", "3.0"), "NO");

        assertEquals("NO", knn.classify(toPredict));
    }

}
