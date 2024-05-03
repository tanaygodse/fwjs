// Generated from FeatherweightJavaScript.g4 by ANTLR 4.4
 package edu.sjsu.fwjs.parser; 
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FeatherweightJavaScriptParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FeatherweightJavaScriptVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code simpBlock}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpBlock(@NotNull FeatherweightJavaScriptParser.SimpBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(@NotNull FeatherweightJavaScriptParser.ParensContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifThenElse}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfThenElse(@NotNull FeatherweightJavaScriptParser.IfThenElseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(@NotNull FeatherweightJavaScriptParser.AddSubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignmentStatement}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentStatement(@NotNull FeatherweightJavaScriptParser.AssignmentStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code anonFunctionDeclaration}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnonFunctionDeclaration(@NotNull FeatherweightJavaScriptParser.AnonFunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variableReference}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableReference(@NotNull FeatherweightJavaScriptParser.VariableReferenceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifThen}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfThen(@NotNull FeatherweightJavaScriptParser.IfThenContext ctx);
	/**
	 * Visit a parse tree produced by the {@code while}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile(@NotNull FeatherweightJavaScriptParser.WhileContext ctx);
	/**
	 * Visit a parse tree produced by {@link FeatherweightJavaScriptParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(@NotNull FeatherweightJavaScriptParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code blankExpr}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlankExpr(@NotNull FeatherweightJavaScriptParser.BlankExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code int}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt(@NotNull FeatherweightJavaScriptParser.IntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bareExpr}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBareExpr(@NotNull FeatherweightJavaScriptParser.BareExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variableDeclaration}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(@NotNull FeatherweightJavaScriptParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fullBlock}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullBlock(@NotNull FeatherweightJavaScriptParser.FullBlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code print}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrint(@NotNull FeatherweightJavaScriptParser.PrintContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulDivMod}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDivMod(@NotNull FeatherweightJavaScriptParser.MulDivModContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(@NotNull FeatherweightJavaScriptParser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code null}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull(@NotNull FeatherweightJavaScriptParser.NullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Comparisons}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisons(@NotNull FeatherweightJavaScriptParser.ComparisonsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(@NotNull FeatherweightJavaScriptParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link FeatherweightJavaScriptParser#arglist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArglist(@NotNull FeatherweightJavaScriptParser.ArglistContext ctx);
	/**
	 * Visit a parse tree produced by {@link FeatherweightJavaScriptParser#idlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdlist(@NotNull FeatherweightJavaScriptParser.IdlistContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionDeclaration}
	 * labeled alternative in {@link FeatherweightJavaScriptParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclaration(@NotNull FeatherweightJavaScriptParser.FunctionDeclarationContext ctx);
}