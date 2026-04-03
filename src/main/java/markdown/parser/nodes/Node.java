package markdown.parser.nodes;

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

}