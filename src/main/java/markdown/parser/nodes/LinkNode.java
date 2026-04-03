package markdown.parser.nodes;

import markdown.parser.visitor.NodeVisitor;

public class LinkNode extends Node {

    private final String url;

    public LinkNode(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public void accept(NodeVisitor visitor) {

        visitor.visitLink(this);
    }
}