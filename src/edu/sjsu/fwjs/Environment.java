package edu.sjsu.fwjs;

import java.util.Map;
import java.util.HashMap;

public class Environment {
    private Map<String, Value> env = new HashMap<String, Value>();
    private Environment outerEnv;
    private static final ObjectVal globalPrototype = new ObjectVal(null); // Singleton global prototype for all objects

    /**
     * Constructor for global environment.
     */
    public Environment() {
        initializeDefaultValues();
    }

    /**
     * Constructor for local environment of a function.
     */
    public Environment(Environment outerEnv) {
        this.outerEnv = outerEnv;
    }

    /**
     * Initializes the environment with default values or functions.
     */
    private void initializeDefaultValues() {
        // Here you could add default functions or global variables if necessary
    }

    /**
     * Resolves the value of a variable.
     * Throws a RuntimeException if the variable is not found in any enclosing environment.
     */
    public Value resolveVar(String varName) {
        if (env.containsKey(varName)) {
            return env.get(varName);
        } else if (outerEnv != null) {
            return outerEnv.resolveVar(varName);
        } else {
            throw new RuntimeException("Variable '" + varName + "' is not defined.");
        }
    }

    /**
     * Updates an existing variable or creates it in the global scope if it does not exist.
     */
    public void updateVar(String varName, Value v) {
        if (containsVar(varName)) {
            Environment definingEnv = this;
            while (!definingEnv.env.containsKey(varName)) {
                definingEnv = definingEnv.outerEnv;
            }
            definingEnv.env.put(varName, v);
        } else {
            env.put(varName, v);
        }
    }

    /**
     * Checks if the variable is defined in any scope from local to global.
     */
    public boolean containsVar(String varName) {
        Environment currentEnv = this;
        while (currentEnv != null) {
            if (currentEnv.env.containsKey(varName)) {
                return true;
            }
            currentEnv = currentEnv.outerEnv;
        }
        return false;
    }

    /**
     * Creates a new variable in the local scope.
     */
    public void createVar(String varName, Value v) {
        if (env.containsKey(varName)) {
            throw new RuntimeException("Variable '" + varName + "' already defined in this scope.");
        }
        env.put(varName, v);
    }

    /**
     * Retrieves the global prototype for all objects.
     */
    public static ObjectVal getGlobalPrototype() {
        return globalPrototype;
    }

    /**
     * Registers a prototype for a new object type.
     */
    public static void registerPrototype(String typeName, ObjectVal prototype) {
        globalPrototype.setProperty(typeName, prototype);
    }
}
