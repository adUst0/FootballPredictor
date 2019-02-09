package ivanov.boris.predictor.classifier.knn;

import ivanov.boris.predictor.dataset.DatasetEntry;

import java.util.ArrayList;
import java.util.List;

class Entry {
    List<Double> attributes = new ArrayList<>();
    String label;

    Entry(DatasetEntry entry) {
        this.label = entry.getLabel();

        for (String attribute : entry.getAttributes()) {
            this.attributes.add(Double.parseDouble(attribute));
        }
    }

    Entry(Entry entry) {
        attributes = new ArrayList<>(entry.attributes);
        label = entry.label;
    }
}
