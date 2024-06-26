package edu.sjsu.fwjs;

import java.util.Map;

import java.util.HashMap;

public class Environment {
    public Map<String, Value> env = new HashMap<String, Value>();
    private Environment outerEnv;
    private static final ObjectVal globalPrototype = new ObjectVal(null); // Singleton global prototype for all objects

    /**
     * Constructor for global environment.
     */
    public Environment() {}

    /**
	 * Constructor for local environment of a function
	 */
    public Environment(Environment outerEnv) {
        this.outerEnv = outerEnv;
    }

    /**
	 * Handles the logic of resolving a variable.
	 * If the variable name is in the current scope, it is returned.
	 * Otherwise, search for the variable in the outer scope.
	 * If we are at the outermost scope (AKA the global scope)
	 * null is returned (similar to how JS returns undefined.
	 */
    public Value resolveVar(String varName) {
        if (env.containsKey(varName)) {
            return env.get(varName);
        }else if(outerEnv != null) {
            return outerEnv.resolveVar(varName);
        }else{
            throw new RuntimeException("Variable '" + varName + "' is not defined.");
        }
    }

    /**
	 * Used for updating existing variables.
	 * If a variable has not been defined previously in the current scope,
	 * or any of the function's outer scopes, the var is stored in the global scope.
	 */
    public void updateVar(String varName, Value v) {
    	if (env.containsKey(varName)){
    		env.put(varName, v);
    	}else if (outerEnv != null) {
    		outerEnv.updateVar(varName, v);
    	}else
    		env.put(varName, v);
    }

    /**
     * Creates a new variable in the local scope.
     * If the variable has been defined in the current scope previously,
     * a RuntimeException is thrown.
     */
    public void createVar(String varName, Value v) throws RuntimeException {
        if(!env.containsKey(varName)) {
            env.put(varName, v);
        } else {
            throw new RuntimeException("Variable '" + varName + "' has already been defined in this scope.");
        }
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
