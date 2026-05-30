package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaTokens;
import java.util.ArrayList;
import java.util.List;

public class RegexHighlighter extends SyntaxHighlighter {

  @Override
  public List<HighlightRegion> collectMatches(String text) {
    return MiniJavaTokens.defaultTokens().stream()
        .flatMap(token -> token.test(text).stream())
        .toList();
  }

  @Override
  public List<HighlightRegion> resolveConflicts(List<HighlightRegion> regions) {
    var resolved = new ArrayList<HighlightRegion>();
    int currentEnd = -1;
    for (HighlightRegion region : regions) {
      if (currentEnd <= region.start()) {
        resolved.add(region);
        currentEnd = region.end();
      }
    }
    return resolved;
  }
}
