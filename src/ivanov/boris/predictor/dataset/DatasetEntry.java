package ivanov.boris.predictor.dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatasetEntry {

    private List<String> attributes = new ArrayList<>();
    private String label = null;

    public DatasetEntry() {

    }

    public DatasetEntry(String entry, String delimiter) {
        List<String> attributes = Arrays.asList(entry.split(delimiter));

        this.label = attributes.get(attributes.size() - 1);
        this.attributes.addAll(attributes);
        this.attributes.remove(this.attributes.size() - 1);
    }

    public DatasetEntry(List<String> attributes, String label) {
        this.attributes = attributes;
        this.label = label;
    }

    public DatasetEntry(DatasetEntry other) {
        this.label = other.label;
        attributes = new ArrayList<>(other.attributes);
    }

    public List<String> getAttributes() {
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

        for (String attribute : attributes) {
            sb.append(attribute).append(" ");
        }
        sb.append(label);

        return sb.toString();
    }
}
