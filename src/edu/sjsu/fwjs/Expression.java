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
		List<Value> vs = this.exprs.stream().map(x -> x.evaluate(env)).collect(Collectors.toList());
		Boolean nullFlag = vs.stream().anyMatch(x -> (x == null));
		Boolean closureFlag = vs.stream().anyMatch(x -> (x instanceof ClosureVal));

		//Javascript-ish implicit type conversion
		List<Integer> vals = vs.stream().map(
			x -> {
				if (x instanceof BoolVal)
					return ((BoolVal)x).toBoolean() ? 1 : 0;
				else if (x instanceof NullVal)
					return 0;
				else if (x instanceof ClosureVal)
					return -1; //Handled with closureFlag above
				return ((IntVal)x).toInt();
			}
		).collect(Collectors.toList());
		int x = vals.get(0);
		int y = vals.get(1);

		if (nullFlag || closureFlag){
			if (op.equals(Op.EQ)) {
				return new BoolVal(vs.get(0).equals(vs.get(1)));
			}
			else if (!nullFlag)
				throw new RuntimeException("Tried to perform arithmetic or numeric comparison with a closure.");
		}
		if(op.equals(Op.ADD)) {
			return new IntVal(x + y);
		}else if(op.equals(Op.DIVIDE)) {
			return new IntVal(x / y);
		}else if(op.equals(Op.EQ)) {
				return new BoolVal(x == y);
		}else if(op.equals(Op.GE)) {
			return new BoolVal(x >= y);
		}else if(op.equals(Op.GT)) {
			return new BoolVal(x > y);
		}else if(op.equals(Op.LE)) {
			return new BoolVal(x <= y);
		}else if(op.equals(Op.LT)) {
			return new BoolVal(x < y);
		}else if(op.equals(Op.MOD)) {
			return new IntVal(x % y);
		}else if(op.equals(Op.MULTIPLY)) {
			return new IntVal(x * y);
		}else if(op.equals(Op.SUBTRACT)) {
			return new IntVal(x - y);
		}else {return new NullVal();}
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
		Value v = new NullVal();
		if(exp == null) {
			env.createVar(varName, v);
		}	
		try {
			v = exp.evaluate(env);
		}catch(RuntimeException e) {
			System.out.print(e.toString());
		}
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
	public AnonFunctionDeclExpr(List<String> params, Expression body) {
		this.params = params;
		this.body = body;
	}
	public Value evaluate(Environment env) {
		return new ClosureVal(params, body, env);
	}
}

/**
 * A function declaration, which evaluates to a closure.
 */
class FunctionDeclExpr implements Expression {
	private String name;
	private List<String> params;
	private Expression body;
	public FunctionDeclExpr(String name, List<String> params, Expression body) {
		this.name = name;
		this.params = params;
		this.body = body;
	}
	public Value evaluate(Environment env) {
		// function foo(){...} should be syntactic sugar for var foo = function(){...}
		return (new VarDeclExpr(this.name, new ValueExpr(new ClosureVal(params, body, env)))).evaluate(env);
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
        return func.apply(argvals);
    } else if (maybeFunc instanceof NativeFunctionVal) {
        NativeFunctionVal nativeFunc = (NativeFunctionVal) maybeFunc;
        return nativeFunc.apply(argvals);
    } else {
        System.out.println("Failed to apply function, value: " + maybeFunc);
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
        if (!(val instanceof ObjectVal))
            throw new RuntimeException("Trying to access a property on a non-object.");
        return ((ObjectVal) val).getProperty(property);
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
    private Expression prototypeExpr; // This can be null for default global prototype

    public ObjectCreateExpr(Expression prototypeExpr) {
        this.prototypeExpr = prototypeExpr;
    }

    public Value evaluate(Environment env) {
        ObjectVal prototype = Environment.getGlobalPrototype(); // Default prototype
        if (prototypeExpr != null) {
            Value protoVal = prototypeExpr.evaluate(env);
            if (protoVal instanceof ObjectVal) {
                prototype = (ObjectVal) protoVal;
            } else {
                throw new RuntimeException("Prototype expression must evaluate to an ObjectVal");
            }
        }
        return new ObjectVal(prototype);
    }

	
}

class ImportExpr implements Expression {
    private String fileName;
    private String basePath; // New field to store base directory
    private static Set<String> importedFiles = new HashSet<>();

    public ImportExpr(String fileName, String basePath) {
        this.fileName = fileName;
        this.basePath = basePath;
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
        ExpressionBuilderVisitor builder = new ExpressionBuilderVisitor(newBasePath);

        Expression prog = builder.visit(tree);

        // Evaluate the imported file in the same environment
        return prog.evaluate(env);
      } catch (Exception e) {
        throw new RuntimeException("Error importing file: " + fileName + ". Details: " + e.getMessage());
      }
    }
}

