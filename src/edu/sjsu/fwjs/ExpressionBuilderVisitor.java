package edu.sjsu.fwjs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import edu.sjsu.fwjs.parser.FeatherweightJavaScriptBaseVisitor;
import edu.sjsu.fwjs.parser.FeatherweightJavaScriptParser;

public class ExpressionBuilderVisitor extends FeatherweightJavaScriptBaseVisitor<Expression> {
    private String currentBasePath;
    private boolean isSandboxed;

    public ExpressionBuilderVisitor(String basePath, boolean isSandboxed) {
        this.currentBasePath = basePath;
        this.isSandboxed = isSandboxed;
    }

    public ExpressionBuilderVisitor(String basePath) {
        this(basePath, false);
    }
  @Override
  public Expression visitImportExpr(FeatherweightJavaScriptParser.ImportExprContext ctx) {
    String fileName = ctx.importStatement().str.getText();
    // Remove the quotes around the filename
    fileName = fileName.substring(1, fileName.length() - 1);

    // Get the base directory from the current script
    // Assuming we can access the base directory from the environment or a global variable
    String basePath = currentBasePath; // You need to define how to get the current base path

    return new ImportExpr(fileName, basePath);
  }
  @Override
  public Expression visitProg(FeatherweightJavaScriptParser.ProgContext ctx) {
    List<Expression> stmts = new ArrayList<Expression>();
    for (int i = 0; i < ctx.stat().size(); i++) {
      Expression exp = visit(ctx.stat(i));
      if (exp != null)
        stmts.add(exp);
    }
    return listToSeqExp(stmts);
  }

  @Override
  public Expression visitBareExpr(FeatherweightJavaScriptParser.BareExprContext ctx) {
    return visit(ctx.expr());
  }


  @Override
  public Expression visitIfThenElse(FeatherweightJavaScriptParser.IfThenElseContext ctx) {
    Expression cond = visit(ctx.expr());
    Expression thn = visit(ctx.block(0));
    Expression els = visit(ctx.block(1));
    return new IfExpr(cond, thn, els);
  }


  @Override
  public Expression visitIfThen(FeatherweightJavaScriptParser.IfThenContext ctx) {
    Expression cond = visit(ctx.expr());
    Expression thn = visit(ctx.block());
    return new IfExpr(cond, thn, null);
  }

  @Override
  public Expression visitWhileStat(FeatherweightJavaScriptParser.WhileStatContext ctx) {
    Expression cond = visit(ctx.expr());
    Expression body = visit(ctx.block());
    return new WhileExpr(cond, body);
  }


  @Override
  public Expression visitPrintStat(FeatherweightJavaScriptParser.PrintStatContext ctx) {
    Expression expr = visit(ctx.expr());
    return new PrintExpr(expr);
  }

  @Override
  public Expression visitInt(FeatherweightJavaScriptParser.IntContext ctx) {
    int val = Integer.valueOf(ctx.INT().getText());
    return new ValueExpr(new IntVal(val));
  }

  public Expression visitBoolean(FeatherweightJavaScriptParser.BooleanContext ctx) {
    Boolean bol = Boolean.valueOf(ctx.BOOL().getText());
    return new ValueExpr(new BoolVal(bol));
  }

  public Expression visitNull(FeatherweightJavaScriptParser.NullContext ctx) {
    return new ValueExpr(new NullVal());
  }

  @Override
  public Expression visitParens(FeatherweightJavaScriptParser.ParensContext ctx) {
    return visit(ctx.expr());
  }


  @Override
  public Expression visitString(FeatherweightJavaScriptParser.StringContext ctx) {
    String str = ctx.STRING().getText();
    // Remove the leading and trailing quotes
    if (str.length() >= 2 && str.startsWith("\"") && str.endsWith("\"")) {
        str = str.substring(1, str.length() - 1);
    }
    return new ValueExpr(new StringVal(str));
  }


  @Override
  public Expression visitFullBlock(FeatherweightJavaScriptParser.FullBlockContext ctx) {
    List<Expression> stmts = new ArrayList<Expression>();
    for (int i = 1; i < ctx.getChildCount() - 1; i++) {
      Expression exp = visit(ctx.getChild(i));
      stmts.add(exp);
    }
    return listToSeqExp(stmts);
  }

  /**
   * Converts a list of expressions to one sequence expression, if the list
   * contained more than one expression.
   */
  private Expression listToSeqExp(List<Expression> stmts) {
    if (stmts.isEmpty())
      return null;
    Expression exp = stmts.get(0);
    for (int i = 1; i < stmts.size(); i++) {
      exp = new SeqExpr(exp, stmts.get(i));
    }
    return exp;
  }

  @Override
  public Expression visitSimpleBlock(FeatherweightJavaScriptParser.SimpleBlockContext ctx) {
    return visit(ctx.stat());
  }


  @Override
  public Expression visitVariableReference(FeatherweightJavaScriptParser.VariableReferenceContext ctx) {
    return new VarExpr(ctx.IDENTIFIER().getSymbol().getText());
  }


  @Override
  public Expression visitVarDecl(FeatherweightJavaScriptParser.VarDeclContext ctx) {
    String varName = ctx.IDENTIFIER().getText();
    Expression expr = visit(ctx.expr());
    return new VarDeclExpr(varName, expr);
  }



  @Override
  public Expression visitAssignmentExpr(FeatherweightJavaScriptParser.AssignmentExprContext ctx) {
    String varName = ctx.IDENTIFIER().getText();
    Expression expr = visit(ctx.expr());
    return new AssignExpr(varName, expr);
  }



  @Override
  public Expression visitFunctionCall(FeatherweightJavaScriptParser.FunctionCallContext ctx) {
    Expression functionExpr = visit(ctx.expr());
    List<Expression> args = ctx.arglist() != null
            ? ctx.arglist().expr().stream().map(this::visit).collect(Collectors.toList())
            : Collections.emptyList();

    // Handle capabilities
    List<String> capabilities = new ArrayList<>();
    if (ctx.capabilityClause() != null && ctx.capabilityClause().capabilityList() != null) {
        capabilities = ctx.capabilityClause().capabilityList().IDENTIFIER().stream()
                .map(TerminalNode::getText).collect(Collectors.toList());
    }
    return new FunctionAppExpr(functionExpr, args, capabilities);
  }

  public Expression visitArglist(FeatherweightJavaScriptParser.ArglistContext ctx) {
    throw new RuntimeException("Oops! This function should never have been called. Arglists are parsed as part of functions only!");
  }

  @Override
  public Expression visitLogicalAnd(FeatherweightJavaScriptParser.LogicalAndContext ctx) {
      Expression left = visit(ctx.expr(0));
      Expression right = visit(ctx.expr(1));
      // We'll interpret && as a new kind of BinOp, or just evaluate it directly:
      return new BinOpExpr(Op.AND, left, right);
  }

  @Override
  public Expression visitLogicalOr(FeatherweightJavaScriptParser.LogicalOrContext ctx) {
      Expression left = visit(ctx.expr(0));
      Expression right = visit(ctx.expr(1));
      return new BinOpExpr(Op.OR, left, right);
  }

  @Override
  public Expression visitMulDivMod(FeatherweightJavaScriptParser.MulDivModContext ctx) {
    Expression left = visit(ctx.expr(0));
    Expression right = visit(ctx.expr(1));
    int op = ctx.op.getType();
    if (op == FeatherweightJavaScriptParser.MUL)
      return new BinOpExpr(Op.MULTIPLY,left, right);
    else if (op == FeatherweightJavaScriptParser.MOD)
      return new BinOpExpr(Op.MOD,left, right);
    else
      return new BinOpExpr(Op.DIVIDE,left, right);
  }
  @Override
  public Expression visitComparisons(FeatherweightJavaScriptParser.ComparisonsContext ctx) {
    Expression left = visit(ctx.expr(0));
    Expression right = visit(ctx.expr(1));
    int op = ctx.op.getType();
    if (op == FeatherweightJavaScriptParser.GT)
      return new BinOpExpr(Op.GT,left, right);
    else if (op == FeatherweightJavaScriptParser.GE)
      return new BinOpExpr(Op.GE,left, right);
    else if (op == FeatherweightJavaScriptParser.LT)
      return new BinOpExpr(Op.LT,left, right);
    else if (op == FeatherweightJavaScriptParser.LE)
      return new BinOpExpr(Op.LE,left, right);
    else 
      return new BinOpExpr(Op.EQ,left, right);
  }
  @Override
  public Expression visitAddSub(FeatherweightJavaScriptParser.AddSubContext ctx) {
    Expression left = visit(ctx.expr(0));
    Expression right = visit(ctx.expr(1));
    int op = ctx.op.getType();
    if (op == FeatherweightJavaScriptParser.PLUS)
      return new BinOpExpr(Op.ADD,left, right);
    else
      return new BinOpExpr(Op.SUBTRACT,left, right);
  }

@Override
  public Expression visitAnonFuncDecl(FeatherweightJavaScriptParser.AnonFuncDeclContext ctx) {
    List<String> params = ctx.idlist() != null
            ? ctx.idlist().IDENTIFIER().stream().map(TerminalNode::getText).collect(Collectors.toList())
            : new ArrayList<>();
    Expression body = visit(ctx.block());
    return new AnonFunctionDeclExpr(params, body, isSandboxed);
  }


/*  @Override
  public Expression visitFunctionDeclaration(FeatherweightJavaScriptParser.FunctionDeclarationContext ctx) {
    String name = ctx.IDENTIFIER().getSymbol().getText();
    List<TerminalNode> tnodes;
    if (ctx.idlist() != null)
      tnodes = ctx.idlist().IDENTIFIER();
    else
      tnodes = Collections.emptyList();
    List<String> params = new ArrayList<String>();
    for(TerminalNode tn : tnodes){
      params.add(tn.getSymbol().getText());
    }
    Expression body = visit(ctx.block());
    return new FunctionDeclExpr(name, params, body, isSandboxed);
  }
*/

  @Override
  public Expression visitObjectLiteralExpr(FeatherweightJavaScriptParser.ObjectLiteralExprContext ctx) {
    List<FeatherweightJavaScriptParser.ObjectPropertyContext> properties = ctx.objectLiteral().objectPropertyList() != null
            ? ctx.objectLiteral().objectPropertyList().objectProperty()
            : Collections.emptyList();

    Map<String, Expression> propertyExprs = new HashMap<>();
    for (FeatherweightJavaScriptParser.ObjectPropertyContext propCtx : properties) {
        String key;
        if (propCtx.IDENTIFIER() != null) {
            key = propCtx.IDENTIFIER().getText();
        } else {
            // Remove quotes from string keys
            key = propCtx.STRING().getText();
            if (key.length() >= 2 && key.startsWith("\"") && key.endsWith("\"")) {
                key = key.substring(1, key.length() - 1);
            }
        }
        Expression valueExpr = visit(propCtx.expr());
        propertyExprs.put(key, valueExpr);
    }
    return new ObjectLiteralExpr(propertyExprs);
  }

  @Override
  public Expression visitObjectPropertyAccess(FeatherweightJavaScriptParser.ObjectPropertyAccessContext ctx) {
    Expression obj = visit(ctx.expr());
    String prop = ctx.IDENTIFIER().getText();
    return new GetPropertyExpr(obj, prop);
  }

  @Override
  public Expression visitObjectCreation(FeatherweightJavaScriptParser.ObjectCreationContext ctx) {
    String constructorName = ctx.IDENTIFIER().getText();
    List<Expression> args = ctx.arglist() != null
            ? ctx.arglist().expr().stream().map(this::visit).collect(Collectors.toList())
            : Collections.emptyList();
    return new ObjectCreateExpr(constructorName, args);
  }


  @Override
  public Expression visitObjectPropertyAssign(FeatherweightJavaScriptParser.ObjectPropertyAssignContext ctx) {
    Expression obj = visit(ctx.expr(0));
    String prop = ctx.IDENTIFIER().getText();
    Expression value = visit(ctx.expr(1));
    return new SetPropertyExpr(obj, prop, value);
  }
  
  
@Override
public Expression visitMethodCall(FeatherweightJavaScriptParser.MethodCallContext ctx) {
    Expression objExpr = visit(ctx.expr());
    String methodName = ctx.IDENTIFIER().getText();
    List<Expression> args = ctx.arglist() != null
            ? ctx.arglist().expr().stream().map(this::visit).collect(Collectors.toList())
            : Collections.emptyList();
    return new MethodCallExpr(objExpr, methodName, args);
}


@Override
public Expression visitNotExpr(FeatherweightJavaScriptParser.NotExprContext ctx) {
    Expression expr = visit(ctx.expr());
    return new NotExpr(expr);
}


@Override
public Expression visitFuncDeclStat(FeatherweightJavaScriptParser.FuncDeclStatContext ctx) {
    return visit(ctx.functionDeclaration());
}


@Override
public Expression visitFuncDeclWithName(FeatherweightJavaScriptParser.FuncDeclWithNameContext ctx) {
    String name = ctx.IDENTIFIER().getText();
    List<String> params = new ArrayList<>();
    if (ctx.idlist() != null) {
        for (TerminalNode idNode : ctx.idlist().IDENTIFIER()) {
            params.add(idNode.getText());
        }
    }
    Expression body = visit(ctx.block());
    return new FunctionDeclExpr(name, params, body, isSandboxed);
}



@Override
public Expression visitVarDeclStat(FeatherweightJavaScriptParser.VarDeclStatContext ctx) {
    return visit(ctx.variableDeclaration());
}

@Override
public Expression visitAssignStat(FeatherweightJavaScriptParser.AssignStatContext ctx) {
    return visit(ctx.assignmentStatement());
}

@Override
public Expression visitReturnStat(FeatherweightJavaScriptParser.ReturnStatContext ctx) {
    return visit(ctx.returnStatement());
}

@Override
public Expression visitReturnStatRule(FeatherweightJavaScriptParser.ReturnStatRuleContext ctx) {
    Expression expr = ctx.expr() != null ? visit(ctx.expr()) : null;
    return new ReturnExpr(expr);
}


}

