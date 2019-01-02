package ivanov.boris.predictor.dataset.parser;

import ivanov.boris.predictor.dataset.Dataset;

public interface DatasetParser<T> {

    Dataset<T> fromFile(String fileName, String delimiter);
}
