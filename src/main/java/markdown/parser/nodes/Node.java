package markdown.parser.nodes;

import markdown.parser.visitor.NodeVisitor;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {

    protected List<Node> children = new ArrayList<>();

    public void addChild(Node node) {
        children.add(node);
    }

    public List<Node> getChildren() {
        return children;
    }

    public abstract void accept(NodeVisitor visitor);
}