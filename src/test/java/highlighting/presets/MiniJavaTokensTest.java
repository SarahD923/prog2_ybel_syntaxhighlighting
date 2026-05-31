package highlighting.presets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import highlighting.core.HighlightRegion;
import highlighting.regex.Token;
import java.awt.Color;
import java.util.List;
import org.junit.jupiter.api.Test;

class MiniJavaTokensTest {

  private static Token tokenByColour(Color colour) {
    return MiniJavaTokens.defaultTokens().stream()
        .filter(token -> token.colour().equals(colour))
        .findFirst()
        .orElseThrow();
  }

  @Test
  void stringLiteralMatchesText() {
    Token token = tokenByColour(MiniJavaColours.STRING_LITERAL_COLOUR);
    String text = "var s = \"hello\";";
    var regions = token.test(text);

    assertEquals(1, regions.size());
    assertEquals(new HighlightRegion(8, 15, token.colour()), regions.get(0));
  }

  @Test
  void charLiteralMatchesSingleCharacter() {
    Token token = tokenByColour(MiniJavaColours.CHAR_LITERAL_COLOUR);
    String text = "char c = '\\n';";
    var regions = token.test(text);

    assertEquals(1, regions.size());
    assertEquals(new HighlightRegion(9, 13, token.colour()), regions.get(0));
  }

  @Test
  void keywordMatchesWholeWordOnly() {
    Token token = tokenByColour(MiniJavaColours.KEYWORD_COLOUR);
    String text = "public class packageable final";
    var regions = token.test(text);

    assertEquals(
        List.of(
            new HighlightRegion(0, 6, token.colour()),
            new HighlightRegion(7, 12, token.colour()),
            new HighlightRegion(25, 30, token.colour())),
        regions);
  }

  @Test
  void annotationMatchesWithHyphen() {
    Token token = tokenByColour(MiniJavaColours.ANNOTATION_COLOUR);
    String text = "  @Over-ride\n";
    var regions = token.test(text);

    assertEquals(1, regions.size());
    assertEquals(new HighlightRegion(2, 12, token.colour()), regions.get(0));
  }

  @Test
  void lineCommentMatchesUntilEol() {
    Token token = tokenByColour(MiniJavaColours.LINE_COMMENT_COLOUR);
    String text = "// comment text\nnext line";
    var regions = token.test(text);

    assertEquals(1, regions.size());
    assertEquals(new HighlightRegion(0, 15, token.colour()), regions.get(0));
  }

  @Test
  void javadocCommentMatchesMultiline() {
    Token token = tokenByColour(MiniJavaColours.JAVADOC_COMMENT_COLOUR);
    String text = "/**\n * docs\n */\npublic";
    var regions = token.test(text);

    assertEquals(1, regions.size());
    assertEquals(new HighlightRegion(0, 15, token.colour()), regions.get(0));
  }

  @Test
  void stringContainsCommentMarkers() {
    Token stringToken = tokenByColour(MiniJavaColours.STRING_LITERAL_COLOUR);
    String text = "String text = \"Hello // world\";";
    var regions = stringToken.test(text);

    assertEquals(1, regions.size());
    assertEquals(new HighlightRegion(14, 30, stringToken.colour()), regions.get(0));
  }
}
