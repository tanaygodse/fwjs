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
        globalEnv.createVar("parseInt", new NativeFunctionVal(args1 -> {
            if (args1.size() != 1 || !(args1.get(0) instanceof StringVal)) {
                throw new RuntimeException("parseInt expects a single string argument");
            }
            StringVal s = (StringVal) args1.get(0);
            try {
                String trimmed = s.toString().trim();
                // Parse as long, store in the new IntVal constructor
                long val = Long.parseLong(trimmed);
                return new IntVal(val);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid integer string for parseInt: " + s.toString());
            }
        }));
        

        Value fileIOCap = new FileIOCapability();
        globalEnv.createVar("fileIO", fileIOCap);
        Value networkIOCap = new NetworkIOCapability();
        globalEnv.createVar("networkIO", networkIOCap);
        Value fakeFileIOCap  = new FakeFileIOCapability();
        globalEnv.createVar("fakeFileIO", fakeFileIOCap);
        Value fakeNetworkIOCap     = new FakeNetworkIOCapability();
        globalEnv.createVar("fakeNetworkIO", fakeNetworkIOCap);
        Value logFileIOCap = new LogFileIOCapability();
        globalEnv.createVar("logFileIO", logFileIOCap);
        Value logNetworkIOCap = new LogNetworkIOCapability();
        globalEnv.createVar("logNetworkIO", logNetworkIOCap);
        Value cryptoIOCap = new CryptoIOCapability();
        globalEnv.createVar("cryptoIO", cryptoIOCap);
        Value fakeCryptoIOCap = new FakeCryptoIOCapability();
        globalEnv.createVar("fakeCryptoIO", fakeCryptoIOCap);
        Value logCryptoIOCap = new LogCryptoIOCapability();
        globalEnv.createVar("logCryptoIO", logCryptoIOCap);
        // Create a 'Math' object with a 'floor' method (no floating-point, but sim for integer)
        ObjectVal mathObj = new ObjectVal(null);
        mathObj.setProperty("floor", new NativeFunctionVal(args2 -> {
            // For this interpreter, we assume integer division, so 'floor' is effectively a no-op 
            if (args2.size() != 1 || !(args2.get(0) instanceof IntVal)) {
                throw new RuntimeException("Math.floor(...) expects one integer argument");
            }
            int val = ((IntVal) args2.get(0)).toInt();
            return new IntVal(val); // Already "floored" as int
        }));

        globalEnv.createVar("Math", mathObj);
        prog.evaluate(globalEnv);
    }

}
