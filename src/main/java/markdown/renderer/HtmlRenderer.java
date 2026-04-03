package markdown.renderer;

import markdown.parser.nodes.*;

public class HtmlRenderer implements Renderer {

    @Override
    public String render(Node node) {

        HtmlBuilder builder = new HtmlBuilder();

        renderNode(node, builder);

        return builder.getHtml();
    }

    private void renderNode(Node node, HtmlBuilder builder) {

        switch (node) {

            case DocumentNode document -> {

                for (Node child : document.getChildren()) {
                    renderNode(child, builder);
                }
            }

            case HeadingNode heading -> {

                String tag = "h" + heading.getLevel();

                builder.openTag(tag);

                for (Node child : heading.getChildren()) {
                    renderNode(child, builder);
                }

                builder.closeTag(tag);
                builder.newLine();
            }

            case ParagraphNode paragraph -> {

                builder.openTag("p");

                for (Node child : paragraph.getChildren()) {
                    renderNode(child, builder);
                }

                builder.closeTag("p");
                builder.newLine();
            }

            case TextNode textNode -> builder.addText(textNode.getText());

            case BoldNode bold -> {

                builder.openTag("strong");

                for (Node child : bold.getChildren()) {
                    renderNode(child, builder);
                }

                builder.closeTag("strong");
            }

            case ItalicNode italic -> {

                builder.openTag("em");

                for (Node child : italic.getChildren()) {
                    renderNode(child, builder);
                }

                builder.closeTag("em");
            }

            case CodeNode code -> {

                builder.openTag("code");

                for (Node child : code.getChildren()) {
                    renderNode(child, builder);
                }

                builder.closeTag("code");
            }

            case ListNode list -> {

                builder.openTag("ul");
                builder.newLine();

                for (Node child : list.getChildren()) {
                    renderNode(child, builder);
                }

                builder.closeTag("ul");
                builder.newLine();
            }

            case ListItemNode item -> {

                builder.openTag("li");

                for (Node child : item.getChildren()) {
                    renderNode(child, builder);
                }

                builder.closeTag("li");
                builder.newLine();
            }

            case LinkNode link -> {

                builder.openTag("a href=\"" + link.getUrl() + "\"");

                for (Node child : link.getChildren()) {
                    renderNode(child, builder);
                }

                builder.closeTag("a");
            }

            default -> throw new RuntimeException("Unknown node type: " + node.getClass());
        }
    }
}