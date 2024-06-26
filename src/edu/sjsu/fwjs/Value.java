package edu.sjsu.fwjs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
