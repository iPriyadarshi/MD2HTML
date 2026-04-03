package markdown.lexer;

public enum TokenType {

    // structure
    HASH,          // #
    DASH,          // -
    NEWLINE,

    // text
    TEXT,

    // inline formatting
    STAR,          // *
    BACKTICK,      // `
    OPEN_BRACKET,  // [
    CLOSE_BRACKET, // ]
    OPEN_PAREN,    // (
    CLOSE_PAREN,   // )

    // ordered list
    NUMBER, DOT,

    // multiline Codeblocks
    TRIPLE_BACKTICK,


    EOF
}