package markdown.parser.nodes;

import markdown.parser.visitor.NodeVisitor;

public class CodeNode extends Node {

    @Override
    public void accept(NodeVisitor visitor) {

        visitor.visitCode(this);
    }
}