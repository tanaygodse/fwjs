package edu.sjsu.fwjs;

import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        runner.runTests();
    }

    public void runTests() {
        try {
            testPrototypeInheritanceWithSpecificProperty();
            System.out.println("Test 'testPrototypeInheritanceWithSpecificProperty' passed.");
        } catch (Exception e) {
            System.out.println("Test 'testPrototypeInheritanceWithSpecificProperty' failed with exception: " + e.getMessage());
        }
    }

    private void testPrototypeInheritanceWithSpecificProperty() throws Exception {
        Environment env = new Environment();

        ObjectVal human = new ObjectVal(null);
        List<String> params = new ArrayList<>();
        Expression greetBody = new PrintExpr(new ValueExpr(new StringVal("Hello, I am a human")));
        ClosureVal greetMethod = new ClosureVal(params, greetBody, env);
        human.setProperty("greet", greetMethod);

        ObjectVal alice = new ObjectVal(human);
        alice.setProperty("name", new StringVal("Alice"));

        StringVal aliceName = (StringVal) alice.getProperty("name");
        if (aliceName == null || !aliceName.toString().equals("Alice")) {
            throw new Exception("Alice's name property did not return correctly.");
        }

        ClosureVal aliceGreetMethod = (ClosureVal) alice.getProperty("greet");
        if (aliceGreetMethod == null) {
            throw new Exception("Greet method should be inherited from human but was not found.");
        }

        // Simulating method invocation and checking output (you would need to implement this part based on your output needs)
        System.out.println("Simulated output: " + ((PrintExpr) greetBody).evaluate(env));
    }
}

