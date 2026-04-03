package markdown.parser.nodes;

import markdown.parser.visitor.NodeVisitor;

public class ItalicNode extends Node {

    @Override
    public void accept(NodeVisitor visitor) {

        visitor.visitItalic(this);
    }
}