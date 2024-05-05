package edu.sjsu.fwjs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExpressionTest {

    @Test
    public void testValueExpr() {
        Environment env = new Environment();
        ValueExpr ve = new ValueExpr(new IntVal(3));
        IntVal i = (IntVal) ve.evaluate(env);
        assertEquals(i.toInt(), 3);
    }
    
    @Test
    public void testVarExpr() {
        Environment env = new Environment();
        Value v = new IntVal(3);
        env.updateVar("x", v);
        Expression e = new VarExpr("x");
        assertEquals(e.evaluate(env), v);
    }
    
    @Test(expected = RuntimeException.class)
    public void testVarNotFoundExpr() {
        Environment env = new Environment();
        Value v = new IntVal(3);
        env.updateVar("x", v);
        Expression e = new VarExpr("y");
        assertEquals(e.evaluate(env), new NullVal());
    }
    
    @Test
    public void testIfTrueExpr() {
        Environment env = new Environment();
        IfExpr ife = new IfExpr(new ValueExpr(new BoolVal(true)),
                new ValueExpr(new IntVal(1)),
                new ValueExpr(new IntVal(2)));
        IntVal iv = (IntVal) ife.evaluate(env);
        assertEquals(iv.toInt(), 1);
    }
    
    @Test
    public void testIfFalseExpr() {
        Environment env = new Environment();
        IfExpr ife = new IfExpr(new ValueExpr(new BoolVal(false)),
                new ValueExpr(new IntVal(1)),
                new ValueExpr(new IntVal(2)));
        IntVal iv = (IntVal) ife.evaluate(env);
        assertEquals(iv.toInt(), 2);
    }
    
    
@Test(expected = RuntimeException.class)
public void testBadIfExpr() {
    Environment env = new Environment();
    IfExpr ife = new IfExpr(new ValueExpr(new StringVal("xyz")),
            new ValueExpr(new IntVal(1)),
            new ValueExpr(new IntVal(2)));
    ife.evaluate(env); // This should throw RuntimeException
}

    
    @Test
    public void testAssignExpr() {
        Environment env = new Environment();
        IntVal inVal = new IntVal(42);
        AssignExpr ae = new AssignExpr("x", new ValueExpr(inVal));
        IntVal iv = (IntVal) ae.evaluate(env);
        assertEquals(iv, inVal);
        assertEquals(env.resolveVar("x"), inVal);
    }
    
    @Test
    public void testBinOpExpr() {
        Environment env = new Environment();
        BinOpExpr boe = new BinOpExpr(Op.ADD,
                new ValueExpr(new IntVal(1)),
                new ValueExpr(new IntVal(2)));
        IntVal iv = (IntVal) boe.evaluate(env);
        assertEquals(iv, new IntVal(3));
    }
    
    @Test
    public void testSeqExpr() {
        Environment env = new Environment();
        SeqExpr se = new SeqExpr(new AssignExpr("x", new ValueExpr(new IntVal(2))),
                new BinOpExpr(Op.MULTIPLY,
                        new VarExpr("x"),
                        new ValueExpr(new IntVal(3))));
        assertEquals(se.evaluate(env), new IntVal(6));
    }
    
    @Test
    public void testWhileExpr() {
        Environment env = new Environment();
        env.updateVar("x", new IntVal(10));
        WhileExpr we = new WhileExpr(new BinOpExpr(Op.GT,
                    new VarExpr("x"),
                    new ValueExpr(new IntVal(0))),
                new AssignExpr("x",
                        new BinOpExpr(Op.SUBTRACT,
                                new VarExpr("x"),
                                new ValueExpr(new IntVal(1)))));
        we.evaluate(env);
        assertEquals(new IntVal(0), env.resolveVar("x"));
    }
    
    @Test
    // (function(x) { x; })(321);
    public void testIdFunction() {
        Environment env = new Environment();
        List<String> params = new ArrayList<String>();
        params.add("x");
        AnonFunctionDeclExpr f = new AnonFunctionDeclExpr(params, new VarExpr("x"));
        List<Expression> args = new ArrayList<Expression>();
        args.add(new ValueExpr(new IntVal(321)));
        FunctionAppExpr app = new FunctionAppExpr(f,args);
        assertEquals(new IntVal(321), app.evaluate(env));
    }
    
    @Test
    // (function(x,y) { x / y; })(8,2);
    public void testDivFunction() {
        Environment env = new Environment();
        List<String> params = new ArrayList<String>();
        params.add("x");
        params.add("y");
        AnonFunctionDeclExpr f = new AnonFunctionDeclExpr(params,
                new BinOpExpr(Op.DIVIDE,
                        new VarExpr("x"),
                        new VarExpr("y")));
        List<Expression> args = new ArrayList<Expression>();
        args.add(new ValueExpr(new IntVal(8)));
        args.add(new ValueExpr(new IntVal(2)));
        FunctionAppExpr app = new FunctionAppExpr(f,args);
        assertEquals(new IntVal(4), app.evaluate(env));
    }
    
    @Test
    // x=112358; (function() { x; })();
    public void testOuterScope() {
        Environment env = new Environment();
        VarDeclExpr newVar = new VarDeclExpr("x", new ValueExpr(new IntVal(112358)));
        AnonFunctionDeclExpr f = new AnonFunctionDeclExpr(new ArrayList<String>(),
                new VarExpr("x"));
        SeqExpr seq = new SeqExpr(newVar, new FunctionAppExpr(f, new ArrayList<Expression>()));
        Value v = seq.evaluate(env);
        assertEquals(new IntVal(112358), v);
    }
    
    @Test
    // x=112358; (function() { var x=42; x; })();
    public void testScope() {
        Environment env = new Environment();
        VarDeclExpr newVar = new VarDeclExpr("x", new ValueExpr(new IntVal(112358)));
        AnonFunctionDeclExpr f = new AnonFunctionDeclExpr(new ArrayList<String>(),
                new SeqExpr(new VarDeclExpr("x", new ValueExpr(new IntVal(42))),
                        new VarExpr("x")));
        SeqExpr seq = new SeqExpr(newVar, new FunctionAppExpr(f, new ArrayList<Expression>()));
        Value v = seq.evaluate(env);
        assertEquals(new IntVal(42), v);
    }
    
    @Test
    // x=112358; (function() { var x=42; x; })(); x;
    public void testScope2() {
        Environment env = new Environment();
        VarDeclExpr newVar = new VarDeclExpr("x", new ValueExpr(new IntVal(112358)));
        AnonFunctionDeclExpr f = new AnonFunctionDeclExpr(new ArrayList<String>(),
                new SeqExpr(new VarDeclExpr("x", new ValueExpr(new IntVal(42))),
                        new VarExpr("x")));
        SeqExpr seq = new SeqExpr(new SeqExpr(newVar,
                new FunctionAppExpr(f, new ArrayList<Expression>())),
                new VarExpr("x"));
        Value v = seq.evaluate(env);
        assertEquals(new IntVal(112358), v);
    }
    
    @Test
    // x=112358; (function() { x=42; x; })(); x;
    public void testScope3() {
        Environment env = new Environment();
        VarDeclExpr newVar = new VarDeclExpr("x", new ValueExpr(new IntVal(112358)));
        AnonFunctionDeclExpr f = new AnonFunctionDeclExpr(new ArrayList<String>(),
                new SeqExpr(new AssignExpr("x", new ValueExpr(new IntVal(42))),
                        new VarExpr("x")));
        SeqExpr seq = new SeqExpr(new SeqExpr(newVar,
                new FunctionAppExpr(f, new ArrayList<Expression>())),
                new VarExpr("x"));
        Value v = seq.evaluate(env);
        assertEquals(new IntVal(42), v);
    }
    
    @Test
    // var x=99; var x=99;  /* should throw an error */
    public void testVarDecl() {
        Environment env = new Environment();
        VarDeclExpr newVar = new VarDeclExpr("x", new ValueExpr(new IntVal(99)));
        try {
            (new SeqExpr(newVar, newVar)).evaluate(env);
            fail();
        } catch (Exception e) {}
    }
  
@Test
public void testObjectMethod() {
    Environment globalEnv = new Environment();
    ObjectVal alice = new ObjectVal(null); // The parent prototype is null, which is fine.

    // Set the property 'name' to a new StringVal
    alice.setProperty("name", new StringVal("Alice"));

    // Retrieve the property 'name' and verify it is "Alice"
    Value name= alice.getProperty("name");

    // Assert that the retrieved value is a StringVal and equals "Alice"
    assertNotNull(name); // Ensure that the property was indeed set and retrieved
    assertTrue(name instanceof StringVal); // Check that it is of type StringVal
    assertEquals(new StringVal("Alice"), ((StringVal) name)); // Check that the value is "Alice"
}


  

@Test
public void testPrototypeInheritanceWithSpecificProperty() {
    Environment env = new Environment();

    // Create the prototype object 'human' with a 'greet' property.
    ObjectVal human = new ObjectVal(null);
    List<String> params = new ArrayList<>();
    Expression greetBody = new PrintExpr(new ValueExpr(new StringVal("Hello, I am a human")));
    ClosureVal greetMethod = new ClosureVal(params, greetBody, env);
    human.setProperty("greet", greetMethod);
    human.setProperty("kind", new StringVal("Mankind"));

    // Create 'alice' using 'human' as the prototype and set her specific 'name' property.
    ObjectVal alice = new ObjectVal(human);
    alice.setProperty("name", new StringVal("Alice"));

    // Accessing 'greet' property from 'alice', which should be inherited from 'human'.
    ClosureVal aliceGreetMethod = (ClosureVal) alice.getProperty("greet");

    // Setup to capture output from System.out
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out; // Keep the original System.out
    System.setOut(new PrintStream(outContent)); // Set the new stream

    // Print the method name before executing the method that produces output
    System.out.println("------Executing testPrototypeInheritanceWithSpecificProperty------");

    // Execute the inherited greet method
    aliceGreetMethod.apply(new ArrayList<>(), new Environment()); // Apply with empty args and new environment

    // Reset System.out back to its original setting
    System.out.println("originalOut: ");
    System.setOut(originalOut);

    // Print captured output to the original System.out for visibility in console
    System.out.println("outContent:");
    System.out.println(outContent.toString());

    System.out.println("outContent Alice:");
    System.out.println(alice.getProperty("kind").toString());

    assertEquals(new StringVal("Mankind"), ((StringVal)alice.getProperty("kind")));

    // Asserts to ensure captured output contains expected texts
    String output = outContent.toString();
    assertTrue("Output should contain the greeting 'Hello, I am a human'", output.contains("Hello, I am a human"));

}


    @Test
    public void testPrintString() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Create environment and expression to print
        Environment env = new Environment(); // Assuming you have a constructor like this
        Expression stringExpr = new ValueExpr(new StringVal("Hello, world!")); // Assuming ValueExpr wraps a Value
        Expression printExpr = new PrintExpr(stringExpr);

        // Evaluate the expression
        printExpr.evaluate(env);

        // Check output
        String expectedOutput = "Hello, world!\n"; // Expecting println to add a newline
        assertEquals(expectedOutput, outContent.toString());

        // Clean up by resetting System.out
        System.setOut(System.out);
    }

}
