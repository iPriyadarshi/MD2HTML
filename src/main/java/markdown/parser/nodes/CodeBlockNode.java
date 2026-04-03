package markdown.parser.nodes;

public class CodeBlockNode extends Node {

    private final String language;

    public CodeBlockNode(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}