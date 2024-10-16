package edu.sjsu.fwjs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;


/**
 * Values in FWJS.
 * Evaluating a FWJS expression should return a FWJS value.
 */
public interface Value {}
/**
 * Object value to represent JavaScript objects and support prototypical inheritance.
 */
class ObjectVal implements Value {
    private HashMap<String, Value> properties;
    private ObjectVal prototype;

    public ObjectVal(ObjectVal prototype) {
        this.properties = new HashMap<>();
        this.prototype = prototype;
    }

    public void setProperty(String prop, Value val) {
        properties.put(prop, val);
    }

    public Value getProperty(String prop) {
        Value val = properties.get(prop);
        if (val == null && prototype != null) {
            return prototype.getProperty(prop);
        }
        return val;
    }

    public Value callProperty(String propName, List<Value> args) {
        Value prop = getProperty(propName);
        if (prop instanceof ClosureVal) {
            ClosureVal closure = (ClosureVal) prop;
            return closure.apply(args);
        } else if (prop instanceof NativeFunctionVal) {
            NativeFunctionVal nativeFunc = (NativeFunctionVal) prop;
            return nativeFunc.apply(args);
        } else {
            throw new RuntimeException("Property " + propName + " is not a function.");
        }
    }
    
    /**
     * Create an environment that includes properties of this object.
     * Each property is added as a variable to the environment.
     */
    public Environment createEnvironment(Environment outerEnv) {
        Environment env = new Environment(outerEnv);
        for (Map.Entry<String, Value> entry : properties.entrySet()) {
            env.createVar(entry.getKey(), entry.getValue());
        }
        return env;
    }

    @Override
    public String toString() {
        return "Object with properties: " + properties.keySet();
    }
}


/**
 * Boolean values.
 */
class BoolVal implements Value {
    private boolean boolVal;

    public BoolVal(boolean b) { this.boolVal = b; }

    public boolean toBoolean() { return this.boolVal; }

    @Override
    public boolean equals(Object that) {
        return that instanceof BoolVal && this.boolVal == ((BoolVal) that).boolVal;
    }

    @Override
    public String toString() {
        return Boolean.toString(boolVal);
    }
}

/**
 * Integer values.
 */
class IntVal implements Value {
    private int i;

    public IntVal(int i) { this.i = i; }

    public int toInt() { return this.i; }

    @Override
    public boolean equals(Object that) {
        return that instanceof IntVal && this.i == ((IntVal) that).i;
    }

    @Override
    public String toString() {
        return Integer.toString(i);
    }
}

class NullVal implements Value {
    @Override
    public boolean equals(Object that) {
        return that instanceof NullVal;
    }

    @Override
    public String toString() {
        return "null";
    }
}
/**
 * A closure that remembers its surrounding scope and can be applied to arguments within a specific environment.
 */
class ClosureVal implements Value {
    private List<String> params;
    private Expression body;
    private Environment outerEnv;

    public ClosureVal(List<String> params, Expression body, Environment env) {
        this.params = params;
        this.body = body;
        //Might have to change this for capabilities, make more strict
        this.outerEnv = env;
    }

    /**
     * Apply this closure to given arguments within the specified environment.
     * 
     * @param argVals The arguments to the function.
     * @param env The environment in which to evaluate the function.
     * @return The result of evaluating the function.
     */
    public Value apply(List<Value> argVals) {
        Environment env = new Environment(this.outerEnv);
        for(int i = 0; i < params.size(); i++) {
            env.createVar(params.get(i), argVals.get(i));
        }

        return this.body.evaluate(env);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("function(");
        String sep = "";
        for (String param : params) {
            sb.append(sep).append(param);
            sep = ", ";
        }
        sb.append(") {...};");
        return sb.toString();
    }
}

/**
 * String values for FWJS.
 */
class StringVal implements Value {
    private String strVal;

    public StringVal(String s) {
        this.strVal = s;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringVal && this.strVal.equals(((StringVal) obj).strVal);
    }

    @Override
    public String toString() {
        return this.strVal;
    }
    
}

class NativeFunctionVal implements Value {
    private Function<List<Value>, Value> function;

    public NativeFunctionVal(Function<List<Value>, Value> function) {
        this.function = function;
    }

    public Value apply(List<Value> args) {
        return function.apply(args);
    }

    @Override
    public String toString() {
        return "<native function>";
    }
}

class FileIOCapability extends ObjectVal {
    public FileIOCapability() {
        super(null); // No prototype

        // Add the 'readFile' method
        this.setProperty("readFile", new NativeFunctionVal(args -> {
            if (args.size() != 1 || !(args.get(0) instanceof StringVal)) {
                throw new RuntimeException("readFile expects a single string argument");
            }
            String filePath = ((StringVal) args.get(0)).toString();
            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                return new StringVal(content);
            } catch (IOException e) {
                throw new RuntimeException("Error reading file: " + e.getMessage());
            }
        }));

        // Add the 'writeFile' method
        this.setProperty("writeFile", new NativeFunctionVal(args -> {
            if (args.size() != 2 || !(args.get(0) instanceof StringVal) || !(args.get(1) instanceof StringVal)) {
                throw new RuntimeException("writeFile expects two string arguments");
            }
            String filePath = ((StringVal) args.get(0)).toString();
            String content = ((StringVal) args.get(1)).toString();
            try {
                Files.write(Paths.get(filePath), content.getBytes());
                return new NullVal();
            } catch (IOException e) {
                throw new RuntimeException("Error writing file: " + e.getMessage());
            }
        }));
    }
}


class NetworkIOCapability extends ObjectVal {
    public NetworkIOCapability() {
        super(null); // No prototype

        // Add the 'get' method
        this.setProperty("get", new NativeFunctionVal(args -> {
            if (args.size() != 1 || !(args.get(0) instanceof StringVal)) {
                throw new RuntimeException("get expects a single string argument");
            }
            String urlString = ((StringVal) args.get(0)).toString();
            try {
                java.net.URL url = new java.net.URL(urlString);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                // Get response code and handle redirects or errors
                int responseCode = conn.getResponseCode();
                System.out.println("GET Response Code :: " + responseCode);

                InputStream in;
                if (responseCode >= 200 && responseCode < 300) {
                    in = conn.getInputStream();
                } else {
                    in = conn.getErrorStream();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                reader.close();

                System.out.println("GET Response Content :: " + sb.toString());

                return new StringVal(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error performing GET request: " + e.getMessage());
            }
        }));

        // Add the 'post' method
        this.setProperty("post", new NativeFunctionVal(args -> {
            if (args.size() != 2 || !(args.get(0) instanceof StringVal) || !(args.get(1) instanceof StringVal)) {
                throw new RuntimeException("post expects two string arguments");
            }
            String urlString = ((StringVal) args.get(0)).toString();
            String jsonData = ((StringVal) args.get(1)).toString();
            try {
                java.net.URL url = new java.net.URL(urlString);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json"); // Set Content-Type to application/json

                // Write JSON data to the output stream
                java.io.OutputStream os = conn.getOutputStream();
                os.write(jsonData.getBytes("UTF-8"));
                os.flush();
                os.close();

                // Get response code and handle redirects or errors
                int responseCode = conn.getResponseCode();
                System.out.println("POST Response Code :: " + responseCode);

                InputStream in;
                if (responseCode >= 200 && responseCode < 300) {
                    in = conn.getInputStream();
                } else {
                    in = conn.getErrorStream();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                reader.close();

                System.out.println("POST Response Content :: " + sb.toString());

                return new StringVal(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error performing POST request: " + e.getMessage());
            }
        }));
    }
}

