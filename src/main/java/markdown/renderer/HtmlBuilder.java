package markdown.renderer;

public class HtmlBuilder {

    private final StringBuilder html = new StringBuilder();

    public void openTag(String tag) {
        html.append("<").append(tag).append(">");
    }

    public void closeTag(String tag) {
        html.append("</").append(tag).append(">");
    }

    public void addText(String text) {
        html.append(text);
    }

    public void newLine() {
        html.append("\n");
    }

    public String getHtml() {
        return html.toString();
    }
}