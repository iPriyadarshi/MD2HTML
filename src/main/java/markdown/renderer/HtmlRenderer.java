package markdown.renderer;

import markdown.parser.nodes.*;
import markdown.parser.visitor.NodeVisitor;

public class HtmlRenderer implements Renderer, NodeVisitor {

    private HtmlBuilder builder;

    @Override
    public String render(Node node) {

        builder = new HtmlBuilder();

        node.accept(this);

        return builder.getHtml();
    }

    // ---------- document ----------

    @Override
    public void visitDocument(DocumentNode node) {

        for (Node child : node.getChildren()) {
            child.accept(this);
        }
    }

    // ---------- block elements ----------

    @Override
    public void visitParagraph(ParagraphNode node) {

        builder.openTag("p");

        for (Node child : node.getChildren()) {
            child.accept(this);
        }

        builder.closeTag("p");
        builder.newLine();
    }

    @Override
    public void visitHeading(HeadingNode node) {

        builder.openTag("h" + node.getLevel());

        for (Node child : node.getChildren()) {
            child.accept(this);
        }

        builder.closeTag("h" + node.getLevel());
        builder.newLine();
    }

    @Override
    public void visitUnorderedList(ListNode node) {

        builder.openTag("ul");
        builder.newLine();

        for (Node child : node.getChildren()) {
            child.accept(this);
        }

        builder.closeTag("ul");
        builder.newLine();
    }

    @Override
    public void visitOrderedList(OrderedListNode node) {

        builder.openTag("ol");
        builder.newLine();

        for (Node child : node.getChildren()) {
            child.accept(this);
        }

        builder.closeTag("ol");
        builder.newLine();
    }

    @Override
    public void visitListItem(ListItemNode node) {

        builder.openTag("li");

        for (Node child : node.getChildren()) {
            child.accept(this);
        }

        builder.closeTag("li");
        builder.newLine();
    }

    @Override
    public void visitCodeBlock(CodeBlockNode node) {

        builder.openTag("pre");

        if (!node.getLanguage().isEmpty()) {

            builder.openTag("code class=\"language-" + node.getLanguage() + "\"");
        } else {

            builder.openTag("code");
        }
        builder.newLine();

        for (Node child : node.getChildren()) {
            child.accept(this);
        }

        builder.closeTag("code");
        builder.closeTag("pre");

        builder.newLine();
    }

    // ---------- inline elements ----------

    @Override
    public void visitText(TextNode node) {

        builder.addText(node.getText());
    }

    @Override
    public void visitBold(BoldNode node) {

        builder.openTag("strong");

        for (Node child : node.getChildren()) {
            child.accept(this);
        }

        builder.closeTag("strong");
    }

    @Override
    public void visitItalic(ItalicNode node) {

        builder.openTag("em");

        for (Node child : node.getChildren()) {
            child.accept(this);
        }

        builder.closeTag("em");
    }

    @Override
    public void visitCode(CodeNode node) {

        builder.openTag("code");

        for (Node child : node.getChildren()) {
            child.accept(this);
        }

        builder.closeTag("code");
    }

    @Override
    public void visitLink(LinkNode node) {

        builder.openTag("a href=\"" + node.getUrl() + "\"");

        for (Node child : node.getChildren()) {
            child.accept(this);
        }

        builder.closeTag("a");
    }
}