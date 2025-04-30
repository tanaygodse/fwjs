package edu.sjsu.fwjs;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import edu.sjsu.fwjs.parser.FeatherweightJavaScriptLexer;
import edu.sjsu.fwjs.parser.FeatherweightJavaScriptParser;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * FWJS expressions.
 */
public interface Expression {
	/**
	 * Evaluate the expression in the context of the specified environment.
	 */
	public Value evaluate(Environment env);
}

// NOTE: Using package access so that all implementations of Expression
// can be included in the same file.

/**
 * FWJS constants.
 */
class ValueExpr implements Expression {
	private Value val;
	public ValueExpr(Value v) {
		this.val = v;
	}
	public Value evaluate(Environment env) {
		return this.val;
	}
}

/**
 * Expressions that are a FWJS variable.
 */
class VarExpr implements Expression {
	private String varName;
	public VarExpr(String varName) {
		this.varName = varName;
	}
	public Value evaluate(Environment env) {
		return env.resolveVar(varName);
	}
}

/**
 * A print expression.
 */
class PrintExpr implements Expression {
	private Expression exp;
	public PrintExpr(Expression exp) {
		this.exp = exp;
	}
public Value evaluate(Environment env) {
    Value v = exp.evaluate(env);
    System.out.println(v.toString());
    return v;
}
}
/**
 * Binary operators (+, -, *, etc).
 * Currently only numbers are supported.
 */
class BinOpExpr implements Expression {
	private Op op;
	private List<Expression> exprs;
	public BinOpExpr(Op op, Expression e1, Expression e2) {
		this.op = op;
		this.exprs = new ArrayList<Expression>();
		this.exprs.add(e1);
		this.exprs.add(e2);
	}

    @SuppressWarnings("incomplete-switch")
    public Value evaluate(Environment env) {
        List<Value> vs = exprs.stream()
                               .map(e -> e.evaluate(env))
                               .collect(Collectors.toList());

        // Short-circuit string concatenation for ADD
        if (op == Op.ADD &&
            (vs.get(0) instanceof StringVal || vs.get(1) instanceof StringVal)) {
            String s1 = vs.get(0).toString();
            String s2 = vs.get(1).toString();
            return new StringVal(s1 + s2);
        }

        // Determine numeric values
        List<Long> vals = vs.stream().map(x -> {
            if (x instanceof BoolVal)
                return ((BoolVal)x).toBoolean() ? 1L : 0L;
            else if (x instanceof NullVal)
                return 0L;
            else if (x instanceof ClosureVal)
                return -1L;
            return ((IntVal)x).toLong();
        }).collect(Collectors.toList());
        long x = vals.get(0), y = vals.get(1);

        boolean nullFlag = vs.stream().anyMatch(v -> v == null);
        boolean closureFlag = vs.stream().anyMatch(v -> v instanceof ClosureVal);

        // Handle null/closure for EQ only
        if (nullFlag || closureFlag) {
            if (op == Op.EQ)
                return new BoolVal(vs.get(0).equals(vs.get(1)));
            else if (!nullFlag)
                throw new RuntimeException("Tried numeric operation on a closure.");
        }

        // Arithmetic and comparisons
        switch (op) {
            case ADD:
                return new IntVal(x + y);
            case SUBTRACT:
                return new IntVal(x - y);
            case MULTIPLY:
                return new IntVal(x * y);
            case DIVIDE:
                return new IntVal(x / y);
            case MOD:
                return new IntVal(x % y);
            case GT:
                return new BoolVal(x > y);
            case GE:
                return new BoolVal(x >= y);
            case LT:
                return new BoolVal(x < y);
            case LE:
                return new BoolVal(x <= y);
            case EQ:
                return new BoolVal(x == y);
            case NE:
                return new BoolVal(x != y);
            case AND:
                return new BoolVal((x != 0) && (y != 0));
            case OR:
                return new BoolVal((x != 0) || (y != 0));
            default:
                return new NullVal();
        }
    }
}

/**
 * If-then-else expressions.
 * Unlike JS, if expressions return a value.
 */
class IfExpr implements Expression {
	private Expression cond;
	private Expression thn;
	private Expression els;
	public IfExpr(Expression cond, Expression thn, Expression els) {
		this.cond = cond;
		this.thn = thn;
		this.els = els;
	}
	
public Value evaluate(Environment env) {
    Value v = cond.evaluate(env);
    if (!(v instanceof IntVal) && !(v instanceof BoolVal) && !(v instanceof NullVal)) {
        throw new RuntimeException("If condition must evaluate to a boolean, integer, or null");
    }
    boolean condVal = (v instanceof BoolVal && ((BoolVal)v).toBoolean()) || 
                      (v instanceof IntVal && ((IntVal)v).toInt() != 0);
    if (condVal) {
        return thn.evaluate(env);
    } else if (this.els != null) {
        return els.evaluate(env);
    } else {
        return new NullVal();
    }
}

}

/**
 * While statements (treated as expressions in FWJS, unlike JS).
 */
class WhileExpr implements Expression {
	private Expression cond;
	private Expression body;
	public WhileExpr(Expression cond, Expression body) {
		this.cond = cond;
		this.body = body;
	}
	public Value evaluate(Environment env) {
		while(true) {
			Value v = cond.evaluate(env);
			if(!(v instanceof BoolVal))
				throw new RuntimeException("While condition did not evaluate to a boolean");
			else if (((BoolVal)v).toBoolean()){
				body.evaluate(env);
			} else {
				return new NullVal();
			}
		}
	}
}

/**
 * Sequence expressions (i.e. 2 back-to-back expressions).
 */
class SeqExpr implements Expression {
	private Expression e1;
	private Expression e2;
	public SeqExpr(Expression e1, Expression e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	public Value evaluate(Environment env) {
		if(e1 == null) return new NullVal();
		e1.evaluate(env);
		if(e2 == null) return new NullVal();
		return e2.evaluate(env);
	}
}

/**
 * Declaring a variable in the local scope.
 */
class VarDeclExpr implements Expression {
	private String varName;
	private Expression exp;
	public VarDeclExpr(String varName, Expression exp) {
		this.varName = varName;
		this.exp = exp;
	}
	public Value evaluate(Environment env) {
        Value v = (exp == null) ? new NullVal() : exp.evaluate(env);
        env.createVar(varName, v);
        return v;
    }
}

/**
 * Updating an existing variable.
 * If the variable is not set already, it is added
 * to the global scope.
 */
class AssignExpr implements Expression {
	private String varName;
	private Expression e;
	public AssignExpr(String varName, Expression e) {
		this.varName = varName;
		this.e = e;
	}
	public Value evaluate(Environment env) {
		if(e == null) return null;
		Value v = e.evaluate(env);
		env.updateVar(varName, v);
		return v;
	}
}

/**
 * A function declaration, which evaluates to a closure.
 */
class AnonFunctionDeclExpr implements Expression {
    private List<String> params;
    private Expression body;
    private boolean isSandboxed;

    public AnonFunctionDeclExpr(List<String> params, Expression body, boolean isSandboxed) {
        this.params = params;
        this.body = body;
        this.isSandboxed = isSandboxed;
    }

    public AnonFunctionDeclExpr(List<String> params, Expression body) {
        this(params, body, false);
    }

    public Value evaluate(Environment env) {
        return new ClosureVal(params, body, env, isSandboxed);
    }
}

/**
 * A function declaration, which evaluates to a closure.
 */
class FunctionDeclExpr implements Expression {
    private String name;
    private List<String> params;
    private Expression body;
    private boolean isSandboxed;

    public FunctionDeclExpr(String name, List<String> params, Expression body, boolean isSandboxed) {
        this.name = name;
        this.params = params;
        this.body = body;
        this.isSandboxed = isSandboxed;
    }

    public FunctionDeclExpr(String name, List<String> params, Expression body) {
        this(name, params, body, false);
    }

    public Value evaluate(Environment env) {
        return (new VarDeclExpr(this.name, new ValueExpr(new ClosureVal(params, body, env, isSandboxed)))).evaluate(env);
    }
}


class FunctionAppExpr implements Expression {
    private Expression f;
    private List<Expression> args;

    public FunctionAppExpr(Expression f, List<Expression> args) {
        this.f = f;
        this.args = args;
    }

    public Value evaluate(Environment env) {
        List<Value> argvals = args.stream().map(arg -> arg.evaluate(env)).collect(Collectors.toList());
        Value maybeFunc = f.evaluate(env);
        if (maybeFunc instanceof ClosureVal) {
            ClosureVal func = (ClosureVal) maybeFunc;
            // Always use the closure's outer environment so that sandboxed functions retain their capabilities
            Environment funcEnv = new Environment(func.getOuterEnv());
            
            // Set up parseInt and Math in the function environment as before
            try {
                funcEnv.resolveVar("parseInt");
                funcEnv.updateVar("parseInt", new NativeFunctionVal(args1 -> {
                    if (args1.size() != 1 || !(args1.get(0) instanceof StringVal)) {
                        throw new RuntimeException("parseInt expects a single string argument");
                    }
                    StringVal s = (StringVal) args1.get(0);
                    try {
                        String trimmed = s.toString().trim();
                        long val = Long.parseLong(trimmed);
                        return new IntVal(val);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Invalid integer string for parseInt: " + s.toString());
                    }
                }));
            } catch (RuntimeException e) {
                funcEnv.createVar("parseInt", new NativeFunctionVal(args1 -> {
                    if (args1.size() != 1 || !(args1.get(0) instanceof StringVal)) {
                        throw new RuntimeException("parseInt expects a single string argument");
                    }
                    StringVal s = (StringVal) args1.get(0);
                    try {
                        String trimmed = s.toString().trim();
                        long val = Long.parseLong(trimmed);
                        return new IntVal(val);
                    } catch (NumberFormatException e1) {
                        throw new RuntimeException("Invalid integer string for parseInt: " + s.toString());
                    }
                }));
            }
            ObjectVal mathObj = new ObjectVal(null);
            mathObj.setProperty("floor", new NativeFunctionVal(args2 -> {
                if (args2.size() != 1 || !(args2.get(0) instanceof IntVal)) {
                    throw new RuntimeException("Math.floor(...) expects one integer argument");
                }
                int val = ((IntVal) args2.get(0)).toInt();
                return new IntVal(val);
            }));
            try {
                funcEnv.resolveVar("Math");
                funcEnv.updateVar("Math", mathObj);
            } catch (RuntimeException e) {
                funcEnv.updateVar("Math", mathObj);
            }
            return func.apply(argvals, funcEnv);
        } else if (maybeFunc instanceof NativeFunctionVal) {
            NativeFunctionVal nativeFunc = (NativeFunctionVal) maybeFunc;
            return nativeFunc.apply(argvals);
        } else {
            throw new RuntimeException("The expression does not evaluate to a function and thus cannot be applied.");
        }
    }
}




//Object Get and Set property for Prototype
class GetPropertyExpr implements Expression {
    private Expression objectExpr;
    private String property;

    public GetPropertyExpr(Expression objectExpr, String property) {
        this.objectExpr = objectExpr;
        this.property = property;
    }

    public Value evaluate(Environment env) {
        Value val = objectExpr.evaluate(env);
        /* if (!(val instanceof ObjectVal))
            throw new RuntimeException("Trying to access a property on a non-object.");
        return ((ObjectVal) val).getProperty(property);
        */
        if (val instanceof ObjectVal) {
            return ((ObjectVal) val).getProperty(property);
        } else if (val instanceof StringVal) {
            return ((StringVal) val).getProperty(property);
        } else if (val instanceof IntVal) {
            return ((IntVal) val).getProperty(property);
        } else if (val instanceof BoolVal) {
            return ((BoolVal) val).getProperty(property);
        } else if (val instanceof NullVal) {
            return ((NullVal) val).getProperty(property);
        } else {
            throw new RuntimeException(
              "Trying to access a property on a non-object (and not a recognized type).");
        }
        
    }
}

// Expression to set property on an object
class SetPropertyExpr implements Expression {
    private Expression objectExpr;
    private String property;
    private Expression valueExpr;

    public SetPropertyExpr(Expression objectExpr, String property, Expression valueExpr) {
        this.objectExpr = objectExpr;
        this.property = property;
        this.valueExpr = valueExpr;
    }

    public Value evaluate(Environment env) {
        Value obj = objectExpr.evaluate(env);
        Value val = valueExpr.evaluate(env);
        if (!(obj instanceof ObjectVal))
            throw new RuntimeException("Trying to set a property on a non-object.");
        ((ObjectVal) obj).setProperty(property, val);
        return val;
    }
}

class ObjectLiteralExpr implements Expression {
    private Map<String, Expression> properties;

    public ObjectLiteralExpr(Map<String, Expression> properties) {
        this.properties = properties;
    }

    @Override
    public Value evaluate(Environment env) {
        ObjectVal obj = new ObjectVal(null); // No prototype for object literals
        for (Map.Entry<String, Expression> entry : properties.entrySet()) {
            Value val = entry.getValue().evaluate(env);
            obj.setProperty(entry.getKey(), val);
        }
        return obj;
    }
}

class ObjectCreateExpr implements Expression {
    private String constructorName;
    private List<Expression> args;

    public ObjectCreateExpr(String constructorName, List<Expression> args) {
        this.constructorName = constructorName;
        this.args = args;
    }

    public Value evaluate(Environment env) {
        // Get the constructor function
        Value constructorVal = env.resolveVar(constructorName);
        if (!(constructorVal instanceof ClosureVal)) {
            throw new RuntimeException(constructorName + " is not a function");
        }
        ClosureVal constructor = (ClosureVal) constructorVal;

        // Create a new object with the constructor's prototype
        Value protoVal = env.resolveVar(constructorName + ".prototype");
        ObjectVal prototype;
        if (protoVal instanceof ObjectVal) {
            prototype = (ObjectVal) protoVal;
        } else {
            // If no prototype is defined, use an empty object
            prototype = new ObjectVal(null);
        }
        ObjectVal newObj = new ObjectVal(prototype);

        // Create a new environment with 'this' bound to newObj
        Environment newEnv = new Environment(constructor.getOuterEnv());
        newEnv.createVar("this", newObj);

        // Evaluate the constructor body
        List<Value> argVals = args.stream().map(arg -> arg.evaluate(env)).collect(Collectors.toList());
        constructor.apply(argVals, newEnv);

        return newObj;
    }
}

class ImportExpr implements Expression {
    private static final Set<String> importedFiles = new HashSet<>();
    private final String fileName;
    private final String basePath;
    private final Map<String,String> caps;

    public ImportExpr(String fileName, String basePath, Map<String,String> caps) {
        this.fileName = fileName;
        this.basePath = basePath;
        this.caps     = caps;
    }

    @Override
    public Value evaluate(Environment env) {
        if (importedFiles.contains(fileName)) {
            // File has already been imported, skip to prevent circular import
            return new NullVal();
        }
        importedFiles.add(fileName);
        try {
            String path = fileName;
            if (!path.endsWith(".fwjs")) {
                path += ".fwjs";
            }
            // Construct the full path relative to basePath
            File file = new File(basePath, path);
            InputStream is = new FileInputStream(file);
            ANTLRInputStream input = new ANTLRInputStream(is);
            FeatherweightJavaScriptLexer lexer = new FeatherweightJavaScriptLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            FeatherweightJavaScriptParser parser = new FeatherweightJavaScriptParser(tokens);
            ParseTree tree = parser.prog();  // parse the imported file

            // Update the basePath for the imported file's directory
            String newBasePath = file.getParent();
            ExpressionBuilderVisitor builder = new ExpressionBuilderVisitor(newBasePath, true); // sandboxed
            Expression prog = builder.visit(tree);

            // Create a new (sandboxed) environment for the imported file.
            Environment importEnv = new Environment(null);

            // Add only the requested capabilities from the current environment
            for (java.util.Map.Entry<String,String> e : caps.entrySet()) {
                String alias    = e.getKey();
                String realName = e.getValue();
                Value cap       = env.resolveVar(realName);
                importEnv.createVar(alias, cap);
            }
            

            // *** New: Always add built-in functions to the import environment ***
            if (!importEnv.env.containsKey("parseInt")) {
                importEnv.createVar("parseInt", env.resolveVar("parseInt"));
            }
            if (!importEnv.env.containsKey("Math")) {
                importEnv.createVar("Math", env.resolveVar("Math"));
            }

            // Evaluate the imported program in its sandboxed environment
            prog.evaluate(importEnv);

            // Merge the definitions from the sandbox into the caller's environment.
            mergeEnvironment(importEnv, env);

            return new NullVal();
        } catch (Exception e) {
            throw new RuntimeException("Error importing file: " + fileName + ". Details: " + e.getMessage());
        }
    }

    // Helper method to merge definitions from source into target.
    private void mergeEnvironment(Environment source, Environment target) {
        for (Map.Entry<String, Value> entry : source.env.entrySet()) {
             // Only add definitions that do not already exist in the target.
             if (!target.env.containsKey(entry.getKey())) {
                 target.env.put(entry.getKey(), entry.getValue());
             }
        }
    }
}


class MethodCallExpr implements Expression {
    private Expression objectExpr;
    private String methodName;
    private List<Expression> args;

    public MethodCallExpr(Expression objectExpr, String methodName, List<Expression> args) {
        this.objectExpr = objectExpr;
        this.methodName = methodName;
        this.args = args;
    }

    public Value evaluate(Environment env) {
        Value objVal = objectExpr.evaluate(env);
        if (!(objVal instanceof ObjectVal)) {
            throw new RuntimeException("Trying to call a method on a non-object.");
        }
        ObjectVal obj = (ObjectVal) objVal;

        Value methodVal = obj.getProperty(methodName);
        if (methodVal instanceof ClosureVal) {
            ClosureVal method = (ClosureVal) methodVal;
            List<Value> argVals = args.stream().map(a -> a.evaluate(env)).collect(Collectors.toList());
            Environment methodEnv = new Environment(method.getOuterEnv());
            methodEnv.createVar("this", obj);
            return method.apply(argVals, methodEnv);

        } else if (methodVal instanceof NativeFunctionVal) {
            NativeFunctionVal nativeFunc = (NativeFunctionVal) methodVal;
            List<Value> argVals = args.stream().map(a -> a.evaluate(env)).collect(Collectors.toList());
            return nativeFunc.apply(argVals);

        } else {
            throw new RuntimeException("Property " + methodName + " is not a function.");
        }
    }

}

class NotExpr implements Expression {
    private Expression expr;

    public NotExpr(Expression expr) {
        this.expr = expr;
    }

    @Override
    public Value evaluate(Environment env) {
        Value v = expr.evaluate(env);
        if (v instanceof BoolVal) {
            return new BoolVal(!((BoolVal) v).toBoolean());
        } else {
            throw new RuntimeException("Cannot apply NOT operator to non-boolean value");
        }
    }
}


class ReturnExpr implements Expression {
    private Expression expr;

    public ReturnExpr(Expression expr) {
        this.expr = expr;
    }

    @Override
    public Value evaluate(Environment env) {
        Value returnValue = expr != null ? expr.evaluate(env) : new NullVal();
        throw new ReturnException(returnValue);
    }
}

class ReturnException extends RuntimeException {
    private Value returnValue;

    public ReturnException(Value returnValue) {
        this.returnValue = returnValue;
    }

    public Value getReturnValue() {
        return returnValue;
    }
}
