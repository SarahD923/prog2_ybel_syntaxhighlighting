package highlighting.antlr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import highlighting.core.HighlightRegion;
import highlighting.presets.MiniJavaColours;

class AntlrTokenCollectorTest {

  private final AntlrTokenCollector highlighter = new AntlrTokenCollector();

  @Test
  void lexerHighlightsStringAndKeywordAndComment() {
    String text = "/* comment */ public class C { String s = \"hi\"; }";
    var regions = highlighter.computeRegions(text);

    assertTrue(regions.contains(new HighlightRegion(0, 13, MiniJavaColours.BLOCK_COMMENT_COLOUR)));
    assertTrue(regions.contains(new HighlightRegion(14, 20, MiniJavaColours.KEYWORD_COLOUR)));
    assertTrue(regions.contains(new HighlightRegion(42, 46, MiniJavaColours.STRING_LITERAL_COLOUR)));
  }

  @Test
  void lexerHighlightsAnnotationWithIdentifier() {
    String text = "public class C { @Override void m() { } }";
    var regions = highlighter.computeRegions(text);

    assertEquals(1, regions.stream().filter(r -> r.colour().equals(MiniJavaColours.ANNOTATION_COLOUR)).count());
    assertTrue(regions.stream().anyMatch(r -> r.start() == 17 && r.end() == 26));
  }
}
