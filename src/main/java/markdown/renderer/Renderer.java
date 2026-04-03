package markdown.renderer;

import markdown.parser.nodes.Node;

public interface Renderer {

    String render(Node node);

}