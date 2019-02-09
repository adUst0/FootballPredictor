import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatasetTest {

    @Test
    public void testGetAttributesCountOnEmptyDataset() {
        Dataset dataset = new Dataset();

        assertEquals(0, dataset.getAttributesCount());
    }

    @Test
    public void testGetAttributesCountOnNonEmptyDataset() {
        Dataset dataset = new Dataset();
        DatasetEntry entry = new DatasetEntry();
        entry.getAttributes().add("Some attribute value");
        dataset.addEntry(entry);

        assertEquals(entry.getAttributes().size(), dataset.getAttributesCount());
    }
}
