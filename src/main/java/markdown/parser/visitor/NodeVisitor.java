package markdown.parser.visitor;

import markdown.parser.nodes.*;

public interface NodeVisitor {

    void visitDocument(DocumentNode node);

    void visitParagraph(ParagraphNode node);

    void visitHeading(HeadingNode node);

    void visitText(TextNode node);

    void visitBold(BoldNode node);

    void visitItalic(ItalicNode node);

    void visitCode(CodeNode node);

    void visitCodeBlock(CodeBlockNode node);

    void visitUnorderedList(ListNode node);

    void visitOrderedList(OrderedListNode node);

    void visitListItem(ListItemNode node);

    void visitLink(LinkNode node);

    void visitHorizontalRule(HorizontalRuleNode node);
}