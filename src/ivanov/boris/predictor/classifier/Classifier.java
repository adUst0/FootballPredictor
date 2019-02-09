package ivanov.boris.predictor.classifier;

import ivanov.boris.predictor.dataset.Dataset;
import ivanov.boris.predictor.dataset.DatasetEntry;

public interface Classifier {
    void buildModel(Dataset dataset);
    String classify(DatasetEntry entry);
}