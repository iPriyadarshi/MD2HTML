package markdown.core;

import markdown.lexer.Lexer;
import markdown.lexer.MarkdownLexer;
import markdown.lexer.Token;
import markdown.parser.MarkdownParser;
import markdown.parser.Parser;
import markdown.parser.nodes.DocumentNode;
import markdown.renderer.HtmlRenderer;
import markdown.renderer.Renderer;

import java.util.List;

public class MarkdownProcessor {

    private final Lexer lexer;
    private final Parser parser;
    private final Renderer renderer;

    public MarkdownProcessor() {

        this.lexer = new MarkdownLexer();
        this.parser = new MarkdownParser();
        this.renderer = new HtmlRenderer();
    }

    public String process(String markdown) {

        // Step 1: tokenize
        List<Token> tokens = lexer.tokenize(markdown);

        // Step 2: parse tokens into AST
        DocumentNode document = parser.parse(tokens);

        // Step 3: render AST to HTML
        return renderer.render(document);
    }
}