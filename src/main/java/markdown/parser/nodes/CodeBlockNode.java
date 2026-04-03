package markdown.parser.nodes;

import markdown.parser.visitor.NodeVisitor;

public class CodeBlockNode extends Node {

    private final String language;

    public CodeBlockNode(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public void accept(NodeVisitor visitor) {

        visitor.visitCodeBlock(this);
    }
}