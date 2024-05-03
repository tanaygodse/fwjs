package edu.sjsu.fwjs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

import org.antlr.v4.runtime.tree.*;

import edu.sjsu.fwjs.parser.FeatherweightJavaScriptBaseVisitor;
import edu.sjsu.fwjs.parser.FeatherweightJavaScriptParser;

public class ExpressionBuilderVisitor extends FeatherweightJavaScriptBaseVisitor<Expression> {
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
    public Expression visitWhile(FeatherweightJavaScriptParser.WhileContext ctx) {
        Expression cond = visit(ctx.expr());
        Expression keepDoing = visit(ctx.block());
        return new WhileExpr(cond, keepDoing);
    }

    @Override
    public Expression visitPrint(FeatherweightJavaScriptParser.PrintContext ctx) {
        Expression val = visit(ctx.expr());
        return new PrintExpr(val);
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
    public Expression visitSimpBlock(FeatherweightJavaScriptParser.SimpBlockContext ctx) {
        return visit(ctx.stat());
    }

    @Override
    public Expression visitVariableReference(FeatherweightJavaScriptParser.VariableReferenceContext ctx) {
        return new VarExpr(ctx.IDENTIFIER().getSymbol().getText());
    }

    public Expression visitVariableDeclaration(FeatherweightJavaScriptParser.VariableDeclarationContext ctx) {
        return new VarDeclExpr(ctx.IDENTIFIER().getSymbol().getText(), visit(ctx.expr()));
    }

    public Expression visitAssignmentStatement(FeatherweightJavaScriptParser.AssignmentStatementContext ctx) {
        return new AssignExpr(ctx.IDENTIFIER().getSymbol().getText(), visit(ctx.expr()));
    }

    public Expression visitFunctionCall(FeatherweightJavaScriptParser.FunctionCallContext ctx) {
        List<Expression> args = Collections.emptyList();
        if (ctx.arglist() != null)
            args = ctx.arglist().expr()
                    .stream().map(x -> visit(x)).collect(Collectors.toList());
        return new FunctionAppExpr(visit(ctx.expr()), args);
    }

    public Expression visitArglist(FeatherweightJavaScriptParser.ArglistContext ctx) {
        throw new RuntimeException("Oops! This function should never have been called. Arglists are parsed as part of functions only!");
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
	public Expression visitAnonFunctionDeclaration(FeatherweightJavaScriptParser.AnonFunctionDeclarationContext ctx) {
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
		return new AnonFunctionDeclExpr(params,body);
	}
	@Override
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
		return new FunctionDeclExpr(name,params,body);
	}

}
