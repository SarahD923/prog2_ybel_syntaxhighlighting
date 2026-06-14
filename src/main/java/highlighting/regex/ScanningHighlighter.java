package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaTokens;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class ScanningHighlighter extends SyntaxHighlighter {

  @Override
  public List<HighlightRegion> collectMatches(String text) {
    var candidates = new ArrayList<HighlightRegion>();
    int index = 0;
    while (index < text.length()) {
      HighlightRegion best = null;
      for (var token : MiniJavaTokens.defaultTokens()) {
        Matcher matcher = token.pattern().matcher(text);
        matcher.region(index, text.length());
        if (matcher.lookingAt()) {
          int start = matcher.start(token.matchingGroup());
          int end = matcher.end(token.matchingGroup());
          if (start == index && end > start) {
            var current = new HighlightRegion(start, end, token.colour());
            if (best == null || current.end() > best.end()) {
              best = current;
            }
          }
        }
      }

      if (best != null) {
        candidates.add(best);
        index = best.end();
      } else {
        index++;
      }
    }
    return candidates;
  }

  @Override
  public List<HighlightRegion> normalize(List<HighlightRegion> candidates) {
    return candidates;
  }
}
