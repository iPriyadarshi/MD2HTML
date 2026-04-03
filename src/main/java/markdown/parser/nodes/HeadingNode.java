package markdown.parser.nodes;

public class HeadingNode extends Node {

    private final int level;    // Stores heading level

    public HeadingNode(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

}