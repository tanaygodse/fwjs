package edu.sjsu.fwjs;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import edu.sjsu.fwjs.parser.FeatherweightJavaScriptLexer;
import edu.sjsu.fwjs.parser.FeatherweightJavaScriptParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Interpreter {

    public static void main(String[] args) throws Exception {
        String inputFile = null;
        if (args.length > 0) inputFile = args[0];
        InputStream is = System.in;
        String basePath = "."; // Default to current directory
        if (inputFile != null) {
            is = new FileInputStream(inputFile);
            File scriptFile = new File(inputFile);
            basePath = scriptFile.getParent();
            if (basePath == null) {
                basePath = "."; // If no parent, use current directory
            }
        }

        ANTLRInputStream input = new ANTLRInputStream(is);
        FeatherweightJavaScriptLexer lexer = new FeatherweightJavaScriptLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FeatherweightJavaScriptParser parser = new FeatherweightJavaScriptParser(tokens);
        ParseTree tree = parser.prog(); // parse
        ExpressionBuilderVisitor builder = new ExpressionBuilderVisitor(basePath);
        Expression prog = builder.visit(tree);
        Environment globalEnv = new Environment();
        Value fileIOCap = new FileIOCapability();
        globalEnv.createVar("fileIO", fileIOCap);
        Value networkIOCap = new NetworkIOCapability();
        globalEnv.createVar("networkIO", networkIOCap);
        prog.evaluate(globalEnv);
    }

}
