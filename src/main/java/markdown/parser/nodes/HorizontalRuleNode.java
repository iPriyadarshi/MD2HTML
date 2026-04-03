package markdown.parser.nodes;

import markdown.parser.visitor.NodeVisitor;

public class HorizontalRuleNode extends Node {

    @Override
    public void accept(NodeVisitor visitor) {

        visitor.visitHorizontalRule(this);
    }
}