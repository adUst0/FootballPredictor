package ivanov.boris.predictor;

import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

public interface Classifier<T> {
    void buildModel(Dataset<T> dataset);
    String classify(DatasetEntry<T> entry);
}
