package highlighting.regex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import highlighting.core.HighlightRegion;
import highlighting.presets.MiniJavaColours;
import java.util.List;
import org.junit.jupiter.api.Test;

class ScanningHighlighterTest {

  private final ScanningHighlighter highlighter = new ScanningHighlighter();

  @Test
  void choosesLongestTokenAtSamePosition() {
    String text = "/** public */";
    var regions = highlighter.computeRegions(text);

    assertEquals(1, regions.size());
    assertEquals(
        new HighlightRegion(0, 13, MiniJavaColours.JAVADOC_COMMENT_COLOUR), regions.get(0));
  }

  @Test
  void skipsUnmatchedCharactersAndFindsLaterTokens() {
    String text = "x public";
    var regions = highlighter.computeRegions(text);

    assertEquals(List.of(new HighlightRegion(2, 8, MiniJavaColours.KEYWORD_COLOUR)), regions);
  }

  @Test
  void adjacentMatchesAreKept() {
    String text = "\"hi\"'a'";
    var regions = highlighter.computeRegions(text);

    assertEquals(
        List.of(
            new HighlightRegion(0, 4, MiniJavaColours.STRING_LITERAL_COLOUR),
            new HighlightRegion(4, 7, MiniJavaColours.CHAR_LITERAL_COLOUR)),
        regions);
  }
}
