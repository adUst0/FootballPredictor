package ivanov.boris.predictor.dataset;

import java.util.ArrayList;
import java.util.List;


public class DatasetEntry<T> {
    private List<T> attributes = new ArrayList<>();
    private String label = null;

    public DatasetEntry() {}

    public DatasetEntry(List<T> attributes, String label) {
        this.attributes = attributes;
        this.label = label;
    }

    public DatasetEntry(DatasetEntry<T> other) {
        this.label = other.label;
        attributes = new ArrayList<>(other.attributes);
    }

    public List<T> getAttributes() {
        return attributes;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int j = 0; j < attributes.size(); j++) {
            sb.append(attributes.get(j)).append(" ");
            if (j == 11 || j == 20) {
                sb.append("    ");
            }
        }
        sb.append(label);

        return sb.toString();
    }
}
