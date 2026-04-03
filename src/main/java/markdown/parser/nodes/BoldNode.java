package markdown.parser.nodes;

import markdown.parser.visitor.NodeVisitor;

public class BoldNode extends Node {

    @Override
    public void accept(NodeVisitor visitor) {

        visitor.visitBold(this);
    }
}