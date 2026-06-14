package highlighting.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class PrettyPrinterDemo {

  public static void main(String[] args) {
    String source = "public class Demo{public static void main(String[] args){System.out.println(\"Hello\");}}";
    MiniJavaLexer lexer = new MiniJavaLexer(CharStreams.fromString(source));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    MiniJavaParser parser = new MiniJavaParser(tokens);

    PrettyPrinterVisitor printer = new PrettyPrinterVisitor();
    String output = printer.print(parser.compilationUnit());

    System.out.println(output);
  }
}
