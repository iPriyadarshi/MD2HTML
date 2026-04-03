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

            default -> throw new RuntimeException("Unknown node type: " + node.getClass());
        }
    }
}