package markdown.parser.nodes;

import markdown.parser.visitor.NodeVisitor;

public class ListItemNode extends Node {

    @Override
    public void accept(NodeVisitor visitor) {

        visitor.visitListItem(this);
    }
}