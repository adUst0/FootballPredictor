package ivanov.boris.predictor.classifier.id3;

public class LeafNode extends TreeNode {
    private String label;

    public LeafNode(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
