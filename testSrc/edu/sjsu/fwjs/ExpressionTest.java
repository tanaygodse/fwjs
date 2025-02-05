package edu.sjsu.fwjs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

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
    aliceGreetMethod.apply(new ArrayList<>()); // Apply with empty args and new environment

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
@Test
public void testFileRead() throws IOException {
    // Setup the test by writing a file to be read
    String filePath = "test_input.txt";
    String fileContent = "This is a test file.";
    Files.write(Paths.get(filePath), fileContent.getBytes());

    // Create an environment and add the FileIOCapability
    Environment env = new Environment();
    Value fileIOCap = new FileIOCapability();
    env.createVar("fileIO", fileIOCap);

    // Create the expression to read the file using FileIOCapability
    Expression fileReadExpr = new FunctionAppExpr(
        new GetPropertyExpr(new VarExpr("fileIO"), "readFile"),
        Arrays.asList(new ValueExpr(new StringVal(filePath))) // Use Arrays.asList()
    );

    // Evaluate the expression
    StringVal result = (StringVal) fileReadExpr.evaluate(env);

    // Check if the result matches the file content
    assertEquals(new StringVal(fileContent), result);

    // Clean up the file
    Files.delete(Paths.get(filePath));
}

@Test
public void testFileWrite() throws IOException {
    // Setup the test file path and content
    String filePath = "test_output.txt";
    String fileContent = "Writing to a test file.";

    // Create an environment and add the FileIOCapability
    Environment env = new Environment();
    Value fileIOCap = new FileIOCapability();
    env.createVar("fileIO", fileIOCap);

    // Create the expression to write to the file using FileIOCapability
    Expression fileWriteExpr = new FunctionAppExpr(
        new GetPropertyExpr(new VarExpr("fileIO"), "writeFile"),
        Arrays.asList(new ValueExpr(new StringVal(filePath)), new ValueExpr(new StringVal(fileContent))) // Use Arrays.asList()
    );

    // Evaluate the expression (write the file)
    fileWriteExpr.evaluate(env);

    // Read the file and check if the content matches
    String result = new String(Files.readAllBytes(Paths.get(filePath)));
    assertEquals(fileContent, result);

    // Clean up the file
    Files.delete(Paths.get(filePath));
}

@Test
public void testFileReadAndWrite() throws IOException {
    // Setup the test file paths and content
    String inputFilePath = "test_input.txt";
    String outputFilePath = "test_output.txt";
    String fileContent = "This is the content to be copied.";

    // Write content to the input file
    Files.write(Paths.get(inputFilePath), fileContent.getBytes());

    // Create an environment and add the FileIOCapability
    Environment env = new Environment();
    Value fileIOCap = new FileIOCapability();
    env.createVar("fileIO", fileIOCap);

    // Create the expression to read the input file and write to the output file
    Expression fileReadExpr = new FunctionAppExpr(
        new GetPropertyExpr(new VarExpr("fileIO"), "readFile"),
        Arrays.asList(new ValueExpr(new StringVal(inputFilePath))) // Use Arrays.asList()
    );

    Expression fileWriteExpr = new FunctionAppExpr(
        new GetPropertyExpr(new VarExpr("fileIO"), "writeFile"),
        Arrays.asList(new ValueExpr(new StringVal(outputFilePath)), fileReadExpr) // Use Arrays.asList()
    );

    // Evaluate the expression (read and then write the file)
    fileWriteExpr.evaluate(env);

    // Check if the output file has the same content as the input file
    String result = new String(Files.readAllBytes(Paths.get(outputFilePath)));
    assertEquals(fileContent, result);

    // Clean up the files
    Files.delete(Paths.get(inputFilePath));
    Files.delete(Paths.get(outputFilePath));
}

@Test(expected = RuntimeException.class)
public void testFileWriteWithoutCapability() throws IOException {
    // Setup the test file path and content
    String filePath = "test_output.txt";
    String fileContent = "This write should not be allowed.";

    // Create an environment **without** adding the FileIOCapability
    Environment env = new Environment();

    // Try to create the expression to write to the file (without the capability)
    Expression fileWriteExpr = new FunctionAppExpr(
        new GetPropertyExpr(new VarExpr("fileIO"), "writeFile"), // Try to access 'fileIO' which does not exist
        Arrays.asList(new ValueExpr(new StringVal(filePath)), new ValueExpr(new StringVal(fileContent))) // Use Arrays.asList()
    );

    // Evaluate the expression (this should fail and throw an exception)
    fileWriteExpr.evaluate(env);

    // The test should expect a RuntimeException because 'fileIO' is not available in the environment
}


@Test
public void testNetworkGet() throws IOException {
    // Mock URL for the GET request
    String mockUrl = "http://httpbin.org/get";

    // Create an environment and add the NetworkIOCapability
    Environment env = new Environment();
    Value networkIOCap = new NetworkIOCapability();
    env.createVar("networkIO", networkIOCap);

    // Create the expression to perform a GET request using NetworkIOCapability
    Expression networkGetExpr = new FunctionAppExpr(
        new GetPropertyExpr(new VarExpr("networkIO"), "get"),
        Arrays.asList(new ValueExpr(new StringVal(mockUrl))) // Use Arrays.asList()
    );

    // Evaluate the expression (perform the GET request)
    StringVal result = (StringVal) networkGetExpr.evaluate(env);

    // Print result for debugging
    System.out.println("GET response: " + result.toString());

    // Assert that the response contains the expected URL field
    assertTrue("Response should contain the requested URL",
            result.toString().contains("\"url\": \"http://httpbin.org/get\""));
}



@Test
public void testNetworkPost() throws IOException {
    // Mock URL and request data for the POST request
    String mockUrl = "http://httpbin.org/post";
    String postData = "{\"name\":\"John\", \"age\":30}"; // Example JSON data

    // Create an environment and add the NetworkIOCapability
    Environment env = new Environment();
    Value networkIOCap = new NetworkIOCapability();
    env.createVar("networkIO", networkIOCap);

    // Create the expression to perform a POST request using NetworkIOCapability
    Expression networkPostExpr = new FunctionAppExpr(
        new GetPropertyExpr(new VarExpr("networkIO"), "post"),
        Arrays.asList(new ValueExpr(new StringVal(mockUrl)), new ValueExpr(new StringVal(postData))) // Use Arrays.asList()
    );

    // Evaluate the expression (perform the POST request)
    StringVal result = (StringVal) networkPostExpr.evaluate(env);

    // Print result for debugging
    System.out.println("POST response: " + result.toString());

    // Assert that the response contains the sent data in the json field
    String response = result.toString();

    // Check that the json section has the expected values
    assertTrue("Response should contain the name field inside json", response.contains("\"name\": \"John\""));
    assertTrue("Response should contain the age field inside json", response.contains("\"age\": 30"));
}



@Test(expected = RuntimeException.class)
public void testNetworkPostWithoutCapability() throws IOException {
    // Setup the test URL and data for the POST request
    String mockUrl = "http://httpbin.org/post";
    String postData = "{\"name\":\"John\", \"age\":30}";

    // Create an environment **without** adding the NetworkIOCapability
    Environment env = new Environment();

    // Try to create the expression to perform a POST request (without the capability)
    Expression networkPostExpr = new FunctionAppExpr(
        new GetPropertyExpr(new VarExpr("networkIO"), "post"), // Try to access 'networkIO' which does not exist
        Arrays.asList(new ValueExpr(new StringVal(mockUrl)), new ValueExpr(new StringVal(postData))) // Use Arrays.asList()
    );

    // Evaluate the expression (this should fail and throw an exception)
    networkPostExpr.evaluate(env);

    // The test should expect a RuntimeException because 'networkIO' is not available in the environment
}


@Test(expected = RuntimeException.class)
public void testNetworkGetInvalidURL() throws IOException {
    // Invalid URL
    String invalidUrl = "http://invalid.url/";

    // Create an environment and add the NetworkIOCapability
    Environment env = new Environment();
    Value networkIOCap = new NetworkIOCapability();
    env.createVar("networkIO", networkIOCap);

    // Create the expression to perform a GET request using the invalid URL
    Expression networkGetExpr = new FunctionAppExpr(
        new GetPropertyExpr(new VarExpr("networkIO"), "get"),
        Arrays.asList(new ValueExpr(new StringVal(invalidUrl))) // Use Arrays.asList()
    );

    // Evaluate the expression (this should fail due to the invalid URL)
    networkGetExpr.evaluate(env);

    // The test should expect a RuntimeException because the URL is invalid
}


// In ExpressionTest.java
@Test
public void testFunctionWithCapabilities() {
    // --- Test 1: Without fileIO capability ---
    {
        // Create an environment that does NOT have fileIO.
        Environment envWithoutFileIO = new Environment();
        
        // Define a sandboxed function that uses fileIO.writeFile.
        // Its outer environment is envWithoutFileIO (which lacks fileIO).
        List<String> params = new ArrayList<>();
        Expression body = new FunctionAppExpr(
            new GetPropertyExpr(new VarExpr("fileIO"), "writeFile"),
            Arrays.asList(
                new ValueExpr(new StringVal("test.txt")),
                new ValueExpr(new StringVal("Hello World"))
            )
        );
        ClosureVal funcVal = new ClosureVal(params, body, envWithoutFileIO, true);
        Expression funcExpr = new ValueExpr(funcVal);
        
        // When called, the function should throw a RuntimeException because fileIO is missing.
        try {
            new FunctionAppExpr(funcExpr, new ArrayList<>()).evaluate(envWithoutFileIO);
            fail("Expected RuntimeException due to missing capability");
        } catch (RuntimeException e) {
            // Expected exception.
        }
    }
    
    // --- Test 2: With fileIO capability ---
    {
        // Create an environment that DOES include fileIO.
        Environment envWithFileIO = new Environment();
        Value fileIOCap = new FileIOCapability();
        envWithFileIO.createVar("fileIO", fileIOCap);
        
        // Define the same sandboxed function; now its outer environment (envWithFileIO)
        // provides the fileIO capability.
        List<String> params = new ArrayList<>();
        Expression body = new FunctionAppExpr(
            new GetPropertyExpr(new VarExpr("fileIO"), "writeFile"),
            Arrays.asList(
                new ValueExpr(new StringVal("test.txt")),
                new ValueExpr(new StringVal("Hello World"))
            )
        );
        ClosureVal funcVal = new ClosureVal(params, body, envWithFileIO, true);
        Expression funcExpr = new ValueExpr(funcVal);
        
        // Call the function. Since fileIO is available in the outer environment, the call should succeed.
        new FunctionAppExpr(funcExpr, new ArrayList<>()).evaluate(envWithFileIO);
        
        // Verify that the file was written with the expected content.
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get("test.txt")));
        } catch (IOException e) {
            fail("Failed to read written file");
        }
        assertEquals("Hello World", content);
        
        // Clean up by deleting the file.
        try {
            Files.delete(Paths.get("test.txt"));
        } catch (IOException e) {
            // Ignore cleanup failures.
        }
    }
}
}
