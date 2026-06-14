package highlighting.antlr;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import java.awt.*;
import java.util.List;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStreams;
import highlighting.presets.MiniJavaColours;

// TODO Phase III — AntlrTokenCollector (token-based syntax highlighting).

// This highlighter uses the ANTLR-generated MiniJavaLexer to turn the input text into a token
// stream. {@code collectMatches(String)} is the only method you need to implement: extract tokens
// of interest and map them to {@code HighlightRegions} using the colours from {@code
// MiniJavaColours}. Sorting, filtering of invalid regions, and conflict handling are performed by
// the base class {@code SyntaxHighlighter} via the template method {@code computeRegions(...)}.
public class AntlrTokenCollector extends SyntaxHighlighter {

  // TODO (Phase III — implement this method): Use the token stream produced by the ANTLR-generated
  // {@code MiniJavaLexer} to collect highlight regions.
  //
  // Requirements / hints:
  // - Iterate over the lexer tokens (typically via {@code CommonTokenStream}); ignore the EOF
  // token.
  // - For each token type that should be coloured (e.g., keywords, string/char literals, comments),
  // create a {@code HighlightRegion} with the corresponding colour from {@code MiniJavaColours}.
  // - Use {@code Token#getStartIndex()} and {@code Token#getStopIndex()} (inclusive) to compute
  // {@code [start, end)} ranges: {@code start = startIndex, end = stopIndex + 1}.
  // - Do not sort, merge, or resolve overlaps here; return all candidates as you find them.
  // Normalisation and conflict resolution are handled later by the template method.
  // - Annotation highlighting: colour '@' and the immediately following IDENTIFIER token (if
  // present).
  @Override
  public List<HighlightRegion> collectMatches(String text) {
    var regions = new java.util.ArrayList<HighlightRegion>();

    MiniJavaLexer lexer = new MiniJavaLexer(CharStreams.fromString(text));
    java.util.List<? extends Token> tokens = lexer.getAllTokens();

    for (int i = 0; i < tokens.size(); i++) {
      Token t = tokens.get(i);
      if (t.getType() == Token.EOF) continue;

      int start = t.getStartIndex();
      int end = t.getStopIndex() + 1; // inclusive -> exclusive
      Color colour = null;
      int type = t.getType();

      // literals
      if (type == MiniJavaLexer.STRING_LITERAL) colour = MiniJavaColours.STRING_LITERAL_COLOUR;
      else if (type == MiniJavaLexer.CHAR_LITERAL) colour = MiniJavaColours.CHAR_LITERAL_COLOUR;

      // comments
      else if (type == MiniJavaLexer.LINE_COMMENT) colour = MiniJavaColours.LINE_COMMENT_COLOUR;
      else if (type == MiniJavaLexer.BLOCK_COMMENT) colour = MiniJavaColours.BLOCK_COMMENT_COLOUR;
      else if (type == MiniJavaLexer.JAVADOC_COMMENT) colour = MiniJavaColours.JAVADOC_COMMENT_COLOUR;

      // keywords
      else if (type == MiniJavaLexer.PACKAGE
          || type == MiniJavaLexer.IMPORT
          || type == MiniJavaLexer.CLASS
          || type == MiniJavaLexer.PUBLIC
          || type == MiniJavaLexer.PRIVATE
          || type == MiniJavaLexer.FINAL
          || type == MiniJavaLexer.RETURN
          || type == MiniJavaLexer.NULL
          || type == MiniJavaLexer.NEW) {
        colour = MiniJavaColours.KEYWORD_COLOUR;
      }

      // annotation: highlight '@' and following identifier if present
      else if (type == MiniJavaLexer.AT) {
        if (i + 1 < tokens.size()) {
          Token next = tokens.get(i + 1);
          if (next.getType() == MiniJavaLexer.IDENTIFIER) {
            end = next.getStopIndex() + 1;
            i++; // skip the identifier as it is consumed
          }
        }
        colour = MiniJavaColours.ANNOTATION_COLOUR;
      }

      if (colour != null && start < end) {
        regions.add(new HighlightRegion(start, end, colour));
      }
    }

    return regions;
  }
}
