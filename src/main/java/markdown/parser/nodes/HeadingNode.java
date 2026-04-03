package markdown.parser.nodes;

import markdown.parser.visitor.NodeVisitor;

public class HeadingNode extends Node {

    private final int level;    // Stores heading level

    public HeadingNode(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitHeading(this);
    }
}