package markdown.parser.nodes;

import markdown.parser.visitor.NodeVisitor;

public class TextNode extends Node {

    private final String text;

    public TextNode(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public void accept(NodeVisitor visitor) {

        visitor.visitText(this);
    }
}