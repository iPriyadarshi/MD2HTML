package markdown.parser.nodes;

public class LinkNode extends Node {

    private final String url;

    public LinkNode(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}