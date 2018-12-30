package ivanov.boris.predictor.dataset;

import java.util.ArrayList;
import java.util.List;

public class Dataset<T> {
    private List<DatasetEntry<T>> entries = new ArrayList<>();

    public void addEntry(DatasetEntry<T> entry) {
        entries.add(entry);
    }

    public int size() {
        return entries.size();
    }

    public int getAttributesCount() {
        if (entries.size() == 0) {
            return 0;
        }

        return entries.get(0).getAttributes().size();
    }

    public List<DatasetEntry<T>> getEntries() {
        return entries;
    }

}
