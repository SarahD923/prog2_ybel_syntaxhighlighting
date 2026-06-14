package highlighting.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrettyPrinterVisitorTest {

  @Test
  void prettyPrinterFormatsClassWithMethodBody() {
    String source = "public class Hello{public void greet(){System.out.println(\"hi\");}}";
    MiniJavaLexer lexer = new MiniJavaLexer(CharStreams.fromString(source));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MiniJavaParser parser = new MiniJavaParser(tokens);
    var tree = parser.compilationUnit();

    PrettyPrinterVisitor printer = new PrettyPrinterVisitor();
    String result = printer.print(tree);

    assertTrue(result.contains("public class Hello"));
    assertTrue(result.contains("public void greet()"));
    assertTrue(result.contains("System.out.println(\"hi\");"));
    assertTrue(result.contains("}"));
    assertFalse(result.contains("}}"));
  }
}
