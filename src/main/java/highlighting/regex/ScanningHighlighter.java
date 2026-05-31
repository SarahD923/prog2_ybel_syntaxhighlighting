package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaTokens;
import highlighting.regex.Token;
import java.util.ArrayList;
import java.util.List;

public class ScanningHighlighter extends SyntaxHighlighter {

  @Override
  public List<HighlightRegion> collectMatches(String text) {
    var tokens = MiniJavaTokens.defaultTokens();
    var regions = new ArrayList<HighlightRegion>();
    int index = 0;

    while (index < text.length()) {
      HighlightRegion bestMatch = null;
      int bestLength = 0;

      for (Token token : tokens) {
        var matcher = token.pattern().matcher(text);
        matcher.region(index, text.length());
        if (!matcher.lookingAt()) {
          continue;
        }

        int start = matcher.start(token.matchingGroup());
        int end = matcher.end(token.matchingGroup());
        int length = end - start;

        if (length <= 0) {
          continue;
        }

        if (length > bestLength) {
          bestLength = length;
          bestMatch = new HighlightRegion(start, end, token.colour());
        }
      }

      if (bestMatch == null) {
        index++;
      } else {
        regions.add(bestMatch);
        index = bestMatch.end();
      }
    }

    return regions;
  }

  @Override
  public List<HighlightRegion> normalize(List<HighlightRegion> candidates) {
    return candidates;
  }
}
