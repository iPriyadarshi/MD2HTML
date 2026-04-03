package markdown.parser.nodes;

import markdown.parser.visitor.NodeVisitor;

public class DocumentNode extends Node {

    @Override
    public void accept(NodeVisitor visitor) {

        visitor.visitDocument(this);
    }
}