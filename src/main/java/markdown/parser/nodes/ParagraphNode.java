package markdown.parser.nodes;

import markdown.parser.visitor.NodeVisitor;

public class ParagraphNode extends Node {

    @Override
    public void accept(NodeVisitor visitor) {

        visitor.visitParagraph(this);
    }
}