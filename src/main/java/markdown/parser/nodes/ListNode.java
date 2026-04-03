package markdown.parser.nodes;

import markdown.parser.visitor.NodeVisitor;

public class ListNode extends Node {

    @Override
    public void accept(NodeVisitor visitor) {

        visitor.visitUnorderedList(this);
    }
}