package markdown.parser;

import markdown.lexer.Token;
import markdown.parser.nodes.DocumentNode;

import java.util.List;

public interface Parser {

    DocumentNode parse(List<Token> tokens);

}