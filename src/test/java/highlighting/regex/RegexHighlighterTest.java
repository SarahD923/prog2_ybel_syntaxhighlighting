package highlighting.regex;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import highlighting.core.HighlightRegion;
import highlighting.presets.MiniJavaColours;

class RegexHighlighterTest {

  private final RegexHighlighter highlighter = new RegexHighlighter();

  @Test
  void simpleTextWithoutOverlapReturnsMultipleRegions() {
    String text = "public class Test";
    var regions = highlighter.computeRegions(text);

    assertEquals(List.of(
        new HighlightRegion(0, 6, MiniJavaColours.KEYWORD_COLOUR),
        new HighlightRegion(7, 12, MiniJavaColours.KEYWORD_COLOUR)
    ), regions);
  }

  @Test
  void keywordInsideCommentIsDiscarded() {
    String text = "/* public */";
    var regions = highlighter.computeRegions(text);

    assertEquals(1, regions.size());
    assertEquals(new HighlightRegion(0, 12, MiniJavaColours.BLOCK_COMMENT_COLOUR), regions.get(0));
  }

  @Test
  void javadocCommentIsPreferredOverBlockComment() {
    String text = "/** public */";
    var regions = highlighter.computeRegions(text);

    assertEquals(1, regions.size());
    assertEquals(new HighlightRegion(0, 13, MiniJavaColours.JAVADOC_COMMENT_COLOUR), regions.get(0));
  }

  @Test
  void adjacentRegionsDoNotConflict() {
    String text = "\"hi\"'a'";
    var regions = highlighter.computeRegions(text);

    assertEquals(List.of(
        new HighlightRegion(0, 4, MiniJavaColours.STRING_LITERAL_COLOUR),
        new HighlightRegion(4, 7, MiniJavaColours.CHAR_LITERAL_COLOUR)
    ), regions);
  }

  @Test
  void emptyTextProducesNoRegions() {
    assertTrue(highlighter.computeRegions("").isEmpty());
  }
}
