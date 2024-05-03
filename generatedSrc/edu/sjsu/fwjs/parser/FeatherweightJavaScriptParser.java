// Generated from FeatherweightJavaScript.g4 by ANTLR 4.4
 package edu.sjsu.fwjs.parser; 
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FeatherweightJavaScriptParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__5=1, T__4=2, T__3=3, T__2=4, T__1=5, T__0=6, IF=7, ELSE=8, WHILE=9, 
		FUNCTION=10, VAR=11, PRINT=12, INT=13, BOOL=14, NULL=15, MUL=16, DIV=17, 
		PLUS=18, MIN=19, MOD=20, SEPARATOR=21, GT=22, LT=23, GE=24, LE=25, EQ=26, 
		IDENTIFIER=27, NEWLINE=28, LINE_COMMENT=29, BLOCK_COMMENT=30, WS=31;
	public static final String[] tokenNames = {
		"<INVALID>", "'{'", "'}'", "'='", "'('", "')'", "','", "'if'", "'else'", 
		"'while'", "'function'", "'var'", "'print'", "INT", "BOOL", "'null'", 
		"'*'", "'/'", "'+'", "'-'", "'%'", "';'", "'>'", "'<'", "'>='", "'<='", 
		"'=='", "IDENTIFIER", "NEWLINE", "LINE_COMMENT", "BLOCK_COMMENT", "WS"
	};
	public static final int
		RULE_prog = 0, RULE_stat = 1, RULE_expr = 2, RULE_arglist = 3, RULE_idlist = 4, 
		RULE_block = 5;
	public static final String[] ruleNames = {
		"prog", "stat", "expr", "arglist", "idlist", "block"
	};

	@Override
	public String getGrammarFileName() { return "FeatherweightJavaScript.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public FeatherweightJavaScriptParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgContext extends ParserRuleContext {
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(13); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(12); stat();
				}
				}
				setState(15); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << IF) | (1L << WHILE) | (1L << FUNCTION) | (1L << VAR) | (1L << PRINT) | (1L << INT) | (1L << BOOL) | (1L << NULL) | (1L << SEPARATOR) | (1L << IDENTIFIER))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatContext extends ParserRuleContext {
		public StatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stat; }
	 
		public StatContext() { }
		public void copyFrom(StatContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PrintContext extends StatContext {
		public TerminalNode PRINT() { return getToken(FeatherweightJavaScriptParser.PRINT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SEPARATOR() { return getToken(FeatherweightJavaScriptParser.SEPARATOR, 0); }
		public PrintContext(StatContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitPrint(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfThenElseContext extends StatContext {
		public TerminalNode ELSE() { return getToken(FeatherweightJavaScriptParser.ELSE, 0); }
		public TerminalNode IF() { return getToken(FeatherweightJavaScriptParser.IF, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public IfThenElseContext(StatContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitIfThenElse(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfThenContext extends StatContext {
		public TerminalNode IF() { return getToken(FeatherweightJavaScriptParser.IF, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public IfThenContext(StatContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitIfThen(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WhileContext extends StatContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode WHILE() { return getToken(FeatherweightJavaScriptParser.WHILE, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public WhileContext(StatContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitWhile(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BlankExprContext extends StatContext {
		public TerminalNode SEPARATOR() { return getToken(FeatherweightJavaScriptParser.SEPARATOR, 0); }
		public BlankExprContext(StatContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitBlankExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BareExprContext extends StatContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SEPARATOR() { return getToken(FeatherweightJavaScriptParser.SEPARATOR, 0); }
		public BareExprContext(StatContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitBareExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatContext stat() throws RecognitionException {
		StatContext _localctx = new StatContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stat);
		try {
			setState(47);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new BareExprContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(17); expr(0);
				setState(18); match(SEPARATOR);
				}
				break;
			case 2:
				_localctx = new IfThenElseContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(20); match(IF);
				setState(21); match(T__2);
				setState(22); expr(0);
				setState(23); match(T__1);
				setState(24); block();
				setState(25); match(ELSE);
				setState(26); block();
				}
				break;
			case 3:
				_localctx = new IfThenContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(28); match(IF);
				setState(29); match(T__2);
				setState(30); expr(0);
				setState(31); match(T__1);
				setState(32); block();
				}
				break;
			case 4:
				_localctx = new WhileContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(34); match(WHILE);
				setState(35); match(T__2);
				setState(36); expr(0);
				setState(37); match(T__1);
				setState(38); block();
				}
				break;
			case 5:
				_localctx = new PrintContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(40); match(PRINT);
				setState(41); match(T__2);
				setState(42); expr(0);
				setState(43); match(T__1);
				setState(44); match(SEPARATOR);
				}
				break;
			case 6:
				_localctx = new BlankExprContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(46); match(SEPARATOR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ParensContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParensContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitParens(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AddSubContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public AddSubContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitAddSub(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AssignmentStatementContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(FeatherweightJavaScriptParser.IDENTIFIER, 0); }
		public AssignmentStatementContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitAssignmentStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AnonFunctionDeclarationContext extends ExprContext {
		public TerminalNode FUNCTION() { return getToken(FeatherweightJavaScriptParser.FUNCTION, 0); }
		public IdlistContext idlist() {
			return getRuleContext(IdlistContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public AnonFunctionDeclarationContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitAnonFunctionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VariableReferenceContext extends ExprContext {
		public TerminalNode IDENTIFIER() { return getToken(FeatherweightJavaScriptParser.IDENTIFIER, 0); }
		public VariableReferenceContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitVariableReference(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntContext extends ExprContext {
		public TerminalNode INT() { return getToken(FeatherweightJavaScriptParser.INT, 0); }
		public IntContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitInt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VariableDeclarationContext extends ExprContext {
		public TerminalNode VAR() { return getToken(FeatherweightJavaScriptParser.VAR, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(FeatherweightJavaScriptParser.IDENTIFIER, 0); }
		public VariableDeclarationContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MulDivModContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public MulDivModContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitMulDivMod(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanContext extends ExprContext {
		public TerminalNode BOOL() { return getToken(FeatherweightJavaScriptParser.BOOL, 0); }
		public BooleanContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitBoolean(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullContext extends ExprContext {
		public TerminalNode NULL() { return getToken(FeatherweightJavaScriptParser.NULL, 0); }
		public NullContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitNull(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ComparisonsContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ComparisonsContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitComparisons(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionCallContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ArglistContext arglist() {
			return getRuleContext(ArglistContext.class,0);
		}
		public FunctionCallContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionDeclarationContext extends ExprContext {
		public TerminalNode FUNCTION() { return getToken(FeatherweightJavaScriptParser.FUNCTION, 0); }
		public IdlistContext idlist() {
			return getRuleContext(IdlistContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(FeatherweightJavaScriptParser.IDENTIFIER, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public FunctionDeclarationContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitFunctionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				_localctx = new VariableDeclarationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(50); match(VAR);
				setState(51); match(IDENTIFIER);
				setState(52); match(T__3);
				setState(53); expr(5);
				}
				break;
			case 2:
				{
				_localctx = new AssignmentStatementContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(54); match(IDENTIFIER);
				setState(55); match(T__3);
				setState(56); expr(4);
				}
				break;
			case 3:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(57); match(T__2);
				setState(58); expr(0);
				setState(59); match(T__1);
				}
				break;
			case 4:
				{
				_localctx = new FunctionDeclarationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(61); match(FUNCTION);
				setState(62); match(IDENTIFIER);
				setState(63); match(T__2);
				setState(65);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(64); idlist();
					}
				}

				setState(67); match(T__1);
				setState(68); block();
				}
				break;
			case 5:
				{
				_localctx = new AnonFunctionDeclarationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(69); match(FUNCTION);
				setState(70); match(T__2);
				setState(72);
				_la = _input.LA(1);
				if (_la==IDENTIFIER) {
					{
					setState(71); idlist();
					}
				}

				setState(74); match(T__1);
				setState(75); block();
				}
				break;
			case 6:
				{
				_localctx = new VariableReferenceContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(76); match(IDENTIFIER);
				}
				break;
			case 7:
				{
				_localctx = new IntContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(77); match(INT);
				}
				break;
			case 8:
				{
				_localctx = new BooleanContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(78); match(BOOL);
				}
				break;
			case 9:
				{
				_localctx = new NullContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(79); match(NULL);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(99);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(97);
					switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
					case 1:
						{
						_localctx = new MulDivModContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(82);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(83);
						((MulDivModContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUL) | (1L << DIV) | (1L << MOD))) != 0)) ) {
							((MulDivModContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(84); expr(12);
						}
						break;
					case 2:
						{
						_localctx = new AddSubContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(85);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(86);
						((AddSubContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MIN) ) {
							((AddSubContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(87); expr(11);
						}
						break;
					case 3:
						{
						_localctx = new ComparisonsContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(88);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(89);
						((ComparisonsContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GT) | (1L << LT) | (1L << GE) | (1L << LE) | (1L << EQ))) != 0)) ) {
							((ComparisonsContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(90); expr(10);
						}
						break;
					case 4:
						{
						_localctx = new FunctionCallContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(91);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(92); match(T__2);
						setState(94);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << FUNCTION) | (1L << VAR) | (1L << INT) | (1L << BOOL) | (1L << NULL) | (1L << IDENTIFIER))) != 0)) {
							{
							setState(93); arglist();
							}
						}

						setState(96); match(T__1);
						}
						break;
					}
					} 
				}
				setState(101);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ArglistContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ArglistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arglist; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitArglist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArglistContext arglist() throws RecognitionException {
		ArglistContext _localctx = new ArglistContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_arglist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(102); expr(0);
			setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(103); match(T__0);
				setState(104); expr(0);
				}
				}
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdlistContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER(int i) {
			return getToken(FeatherweightJavaScriptParser.IDENTIFIER, i);
		}
		public List<TerminalNode> IDENTIFIER() { return getTokens(FeatherweightJavaScriptParser.IDENTIFIER); }
		public IdlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idlist; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitIdlist(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdlistContext idlist() throws RecognitionException {
		IdlistContext _localctx = new IdlistContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_idlist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110); match(IDENTIFIER);
			setState(115);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(111); match(T__0);
				setState(112); match(IDENTIFIER);
				}
				}
				setState(117);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
	 
		public BlockContext() { }
		public void copyFrom(BlockContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FullBlockContext extends BlockContext {
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public FullBlockContext(BlockContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitFullBlock(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SimpBlockContext extends BlockContext {
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public SimpBlockContext(BlockContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FeatherweightJavaScriptVisitor ) return ((FeatherweightJavaScriptVisitor<? extends T>)visitor).visitSimpBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_block);
		int _la;
		try {
			setState(127);
			switch (_input.LA(1)) {
			case T__5:
				_localctx = new FullBlockContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(118); match(T__5);
				setState(122);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << IF) | (1L << WHILE) | (1L << FUNCTION) | (1L << VAR) | (1L << PRINT) | (1L << INT) | (1L << BOOL) | (1L << NULL) | (1L << SEPARATOR) | (1L << IDENTIFIER))) != 0)) {
					{
					{
					setState(119); stat();
					}
					}
					setState(124);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(125); match(T__4);
				}
				break;
			case T__2:
			case IF:
			case WHILE:
			case FUNCTION:
			case VAR:
			case PRINT:
			case INT:
			case BOOL:
			case NULL:
			case SEPARATOR:
			case IDENTIFIER:
				_localctx = new SimpBlockContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(126); stat();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2: return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 11);
		case 1: return precpred(_ctx, 10);
		case 2: return precpred(_ctx, 9);
		case 3: return precpred(_ctx, 12);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3!\u0084\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\6\2\20\n\2\r\2\16\2\21\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\62\n\3\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4D\n\4\3\4\3\4"+
		"\3\4\3\4\3\4\5\4K\n\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4S\n\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4a\n\4\3\4\7\4d\n\4\f\4\16\4g\13\4"+
		"\3\5\3\5\3\5\7\5l\n\5\f\5\16\5o\13\5\3\6\3\6\3\6\7\6t\n\6\f\6\16\6w\13"+
		"\6\3\7\3\7\7\7{\n\7\f\7\16\7~\13\7\3\7\3\7\5\7\u0082\n\7\3\7\2\3\6\b\2"+
		"\4\6\b\n\f\2\5\4\2\22\23\26\26\3\2\24\25\3\2\30\34\u0096\2\17\3\2\2\2"+
		"\4\61\3\2\2\2\6R\3\2\2\2\bh\3\2\2\2\np\3\2\2\2\f\u0081\3\2\2\2\16\20\5"+
		"\4\3\2\17\16\3\2\2\2\20\21\3\2\2\2\21\17\3\2\2\2\21\22\3\2\2\2\22\3\3"+
		"\2\2\2\23\24\5\6\4\2\24\25\7\27\2\2\25\62\3\2\2\2\26\27\7\t\2\2\27\30"+
		"\7\6\2\2\30\31\5\6\4\2\31\32\7\7\2\2\32\33\5\f\7\2\33\34\7\n\2\2\34\35"+
		"\5\f\7\2\35\62\3\2\2\2\36\37\7\t\2\2\37 \7\6\2\2 !\5\6\4\2!\"\7\7\2\2"+
		"\"#\5\f\7\2#\62\3\2\2\2$%\7\13\2\2%&\7\6\2\2&\'\5\6\4\2\'(\7\7\2\2()\5"+
		"\f\7\2)\62\3\2\2\2*+\7\16\2\2+,\7\6\2\2,-\5\6\4\2-.\7\7\2\2./\7\27\2\2"+
		"/\62\3\2\2\2\60\62\7\27\2\2\61\23\3\2\2\2\61\26\3\2\2\2\61\36\3\2\2\2"+
		"\61$\3\2\2\2\61*\3\2\2\2\61\60\3\2\2\2\62\5\3\2\2\2\63\64\b\4\1\2\64\65"+
		"\7\r\2\2\65\66\7\35\2\2\66\67\7\5\2\2\67S\5\6\4\789\7\35\2\29:\7\5\2\2"+
		":S\5\6\4\6;<\7\6\2\2<=\5\6\4\2=>\7\7\2\2>S\3\2\2\2?@\7\f\2\2@A\7\35\2"+
		"\2AC\7\6\2\2BD\5\n\6\2CB\3\2\2\2CD\3\2\2\2DE\3\2\2\2EF\7\7\2\2FS\5\f\7"+
		"\2GH\7\f\2\2HJ\7\6\2\2IK\5\n\6\2JI\3\2\2\2JK\3\2\2\2KL\3\2\2\2LM\7\7\2"+
		"\2MS\5\f\7\2NS\7\35\2\2OS\7\17\2\2PS\7\20\2\2QS\7\21\2\2R\63\3\2\2\2R"+
		"8\3\2\2\2R;\3\2\2\2R?\3\2\2\2RG\3\2\2\2RN\3\2\2\2RO\3\2\2\2RP\3\2\2\2"+
		"RQ\3\2\2\2Se\3\2\2\2TU\f\r\2\2UV\t\2\2\2Vd\5\6\4\16WX\f\f\2\2XY\t\3\2"+
		"\2Yd\5\6\4\rZ[\f\13\2\2[\\\t\4\2\2\\d\5\6\4\f]^\f\16\2\2^`\7\6\2\2_a\5"+
		"\b\5\2`_\3\2\2\2`a\3\2\2\2ab\3\2\2\2bd\7\7\2\2cT\3\2\2\2cW\3\2\2\2cZ\3"+
		"\2\2\2c]\3\2\2\2dg\3\2\2\2ec\3\2\2\2ef\3\2\2\2f\7\3\2\2\2ge\3\2\2\2hm"+
		"\5\6\4\2ij\7\b\2\2jl\5\6\4\2ki\3\2\2\2lo\3\2\2\2mk\3\2\2\2mn\3\2\2\2n"+
		"\t\3\2\2\2om\3\2\2\2pu\7\35\2\2qr\7\b\2\2rt\7\35\2\2sq\3\2\2\2tw\3\2\2"+
		"\2us\3\2\2\2uv\3\2\2\2v\13\3\2\2\2wu\3\2\2\2x|\7\3\2\2y{\5\4\3\2zy\3\2"+
		"\2\2{~\3\2\2\2|z\3\2\2\2|}\3\2\2\2}\177\3\2\2\2~|\3\2\2\2\177\u0082\7"+
		"\4\2\2\u0080\u0082\5\4\3\2\u0081x\3\2\2\2\u0081\u0080\3\2\2\2\u0082\r"+
		"\3\2\2\2\16\21\61CJR`cemu|\u0081";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}