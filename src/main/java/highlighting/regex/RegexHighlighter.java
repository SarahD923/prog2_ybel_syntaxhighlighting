package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaTokens;
import java.util.ArrayList;
import java.util.List;

public class RegexHighlighter extends SyntaxHighlighter {

  @Override
  public List<HighlightRegion> collectMatches(String text) {
    var regions = new ArrayList<HighlightRegion>();
    for (var token : MiniJavaTokens.defaultTokens()) {
      regions.addAll(token.test(text));
    }
    return regions;
  }

  @Override
  public List<HighlightRegion> resolveConflicts(List<HighlightRegion> regions) {
    var resolved = new ArrayList<HighlightRegion>();
    int currentEnd = 0;
    for (var region : regions) {
      if (region.start() >= currentEnd) {
        resolved.add(region);
        currentEnd = region.end();
      }
    }
    return resolved;
  }
}
