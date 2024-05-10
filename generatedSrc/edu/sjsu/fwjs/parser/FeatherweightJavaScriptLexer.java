// Generated from FeatherweightJavaScript.g4 by ANTLR 4.4
 package edu.sjsu.fwjs.parser; 
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FeatherweightJavaScriptLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__5=1, T__4=2, T__3=3, T__2=4, T__1=5, T__0=6, IF=7, ELSE=8, WHILE=9, 
		FUNCTION=10, VAR=11, PRINT=12, NEW=13, INT=14, BOOL=15, NULL=16, STRING=17, 
		MUL=18, DIV=19, PLUS=20, MIN=21, MOD=22, SEPARATOR=23, GT=24, LT=25, GE=26, 
		LE=27, EQ=28, DOT=29, IDENTIFIER=30, NEWLINE=31, LINE_COMMENT=32, BLOCK_COMMENT=33, 
		WS=34;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'", "'\\u0018'", 
		"'\\u0019'", "'\\u001A'", "'\\u001B'", "'\\u001C'", "'\\u001D'", "'\\u001E'", 
		"'\\u001F'", "' '", "'!'", "'\"'"
	};
	public static final String[] ruleNames = {
		"T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "IF", "ELSE", "WHILE", 
		"FUNCTION", "VAR", "PRINT", "NEW", "INT", "BOOL", "NULL", "STRING", "HEX", 
		"MUL", "DIV", "PLUS", "MIN", "MOD", "SEPARATOR", "GT", "LT", "GE", "LE", 
		"EQ", "DOT", "IDENTIFIER", "NEWLINE", "LINE_COMMENT", "BLOCK_COMMENT", 
		"WS"
	};


	public FeatherweightJavaScriptLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "FeatherweightJavaScript.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2$\u00f2\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3"+
		"\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r"+
		"\3\r\3\16\3\16\3\16\3\16\3\17\3\17\7\17}\n\17\f\17\16\17\u0080\13\17\3"+
		"\17\5\17\u0083\n\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20"+
		"\u008e\n\20\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\5\22\u009f\n\22\7\22\u00a1\n\22\f\22\16\22\u00a4\13\22"+
		"\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30"+
		"\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36"+
		"\3\36\3\37\3\37\3 \3 \7 \u00c7\n \f \16 \u00ca\13 \3!\5!\u00cd\n!\3!\3"+
		"!\3!\3!\3\"\3\"\3\"\3\"\7\"\u00d7\n\"\f\"\16\"\u00da\13\"\3\"\3\"\3#\3"+
		"#\3#\3#\7#\u00e2\n#\f#\16#\u00e5\13#\3#\3#\3#\3#\3#\3$\6$\u00ed\n$\r$"+
		"\16$\u00ee\3$\3$\3\u00e3\2%\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"+
		"\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\2\'\24)\25+\26-\27/\30\61\31"+
		"\63\32\65\33\67\349\35;\36=\37? A!C\"E#G$\3\2\13\3\2\63;\3\2\62;\4\2$"+
		"$^^\t\2$$^^ddhhppttvv\5\2\62;CHch\5\2C\\aac|\6\2\62;C\\aac|\4\2\f\f\17"+
		"\17\4\2\13\13\"\"\u00fb\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2"+
		"\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2!\3\2\2\2\2#\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2"+
		"\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3"+
		"\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2"+
		"\2\2G\3\2\2\2\3I\3\2\2\2\5K\3\2\2\2\7M\3\2\2\2\tO\3\2\2\2\13Q\3\2\2\2"+
		"\rS\3\2\2\2\17U\3\2\2\2\21X\3\2\2\2\23]\3\2\2\2\25c\3\2\2\2\27l\3\2\2"+
		"\2\31p\3\2\2\2\33v\3\2\2\2\35\u0082\3\2\2\2\37\u008d\3\2\2\2!\u008f\3"+
		"\2\2\2#\u0094\3\2\2\2%\u00a7\3\2\2\2\'\u00a9\3\2\2\2)\u00ab\3\2\2\2+\u00ad"+
		"\3\2\2\2-\u00af\3\2\2\2/\u00b1\3\2\2\2\61\u00b3\3\2\2\2\63\u00b5\3\2\2"+
		"\2\65\u00b7\3\2\2\2\67\u00b9\3\2\2\29\u00bc\3\2\2\2;\u00bf\3\2\2\2=\u00c2"+
		"\3\2\2\2?\u00c4\3\2\2\2A\u00cc\3\2\2\2C\u00d2\3\2\2\2E\u00dd\3\2\2\2G"+
		"\u00ec\3\2\2\2IJ\7}\2\2J\4\3\2\2\2KL\7\177\2\2L\6\3\2\2\2MN\7?\2\2N\b"+
		"\3\2\2\2OP\7*\2\2P\n\3\2\2\2QR\7+\2\2R\f\3\2\2\2ST\7.\2\2T\16\3\2\2\2"+
		"UV\7k\2\2VW\7h\2\2W\20\3\2\2\2XY\7g\2\2YZ\7n\2\2Z[\7u\2\2[\\\7g\2\2\\"+
		"\22\3\2\2\2]^\7y\2\2^_\7j\2\2_`\7k\2\2`a\7n\2\2ab\7g\2\2b\24\3\2\2\2c"+
		"d\7h\2\2de\7w\2\2ef\7p\2\2fg\7e\2\2gh\7v\2\2hi\7k\2\2ij\7q\2\2jk\7p\2"+
		"\2k\26\3\2\2\2lm\7x\2\2mn\7c\2\2no\7t\2\2o\30\3\2\2\2pq\7r\2\2qr\7t\2"+
		"\2rs\7k\2\2st\7p\2\2tu\7v\2\2u\32\3\2\2\2vw\7p\2\2wx\7g\2\2xy\7y\2\2y"+
		"\34\3\2\2\2z~\t\2\2\2{}\t\3\2\2|{\3\2\2\2}\u0080\3\2\2\2~|\3\2\2\2~\177"+
		"\3\2\2\2\177\u0083\3\2\2\2\u0080~\3\2\2\2\u0081\u0083\7\62\2\2\u0082z"+
		"\3\2\2\2\u0082\u0081\3\2\2\2\u0083\36\3\2\2\2\u0084\u0085\7v\2\2\u0085"+
		"\u0086\7t\2\2\u0086\u0087\7w\2\2\u0087\u008e\7g\2\2\u0088\u0089\7h\2\2"+
		"\u0089\u008a\7c\2\2\u008a\u008b\7n\2\2\u008b\u008c\7u\2\2\u008c\u008e"+
		"\7g\2\2\u008d\u0084\3\2\2\2\u008d\u0088\3\2\2\2\u008e \3\2\2\2\u008f\u0090"+
		"\7p\2\2\u0090\u0091\7w\2\2\u0091\u0092\7n\2\2\u0092\u0093\7n\2\2\u0093"+
		"\"\3\2\2\2\u0094\u00a2\7$\2\2\u0095\u00a1\n\4\2\2\u0096\u009e\7^\2\2\u0097"+
		"\u009f\t\5\2\2\u0098\u0099\7w\2\2\u0099\u009a\5%\23\2\u009a\u009b\5%\23"+
		"\2\u009b\u009c\5%\23\2\u009c\u009d\5%\23\2\u009d\u009f\3\2\2\2\u009e\u0097"+
		"\3\2\2\2\u009e\u0098\3\2\2\2\u009f\u00a1\3\2\2\2\u00a0\u0095\3\2\2\2\u00a0"+
		"\u0096\3\2\2\2\u00a1\u00a4\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a2\u00a3\3\2"+
		"\2\2\u00a3\u00a5\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a5\u00a6\7$\2\2\u00a6"+
		"$\3\2\2\2\u00a7\u00a8\t\6\2\2\u00a8&\3\2\2\2\u00a9\u00aa\7,\2\2\u00aa"+
		"(\3\2\2\2\u00ab\u00ac\7\61\2\2\u00ac*\3\2\2\2\u00ad\u00ae\7-\2\2\u00ae"+
		",\3\2\2\2\u00af\u00b0\7/\2\2\u00b0.\3\2\2\2\u00b1\u00b2\7\'\2\2\u00b2"+
		"\60\3\2\2\2\u00b3\u00b4\7=\2\2\u00b4\62\3\2\2\2\u00b5\u00b6\7@\2\2\u00b6"+
		"\64\3\2\2\2\u00b7\u00b8\7>\2\2\u00b8\66\3\2\2\2\u00b9\u00ba\7@\2\2\u00ba"+
		"\u00bb\7?\2\2\u00bb8\3\2\2\2\u00bc\u00bd\7>\2\2\u00bd\u00be\7?\2\2\u00be"+
		":\3\2\2\2\u00bf\u00c0\7?\2\2\u00c0\u00c1\7?\2\2\u00c1<\3\2\2\2\u00c2\u00c3"+
		"\7\60\2\2\u00c3>\3\2\2\2\u00c4\u00c8\t\7\2\2\u00c5\u00c7\t\b\2\2\u00c6"+
		"\u00c5\3\2\2\2\u00c7\u00ca\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9\3\2"+
		"\2\2\u00c9@\3\2\2\2\u00ca\u00c8\3\2\2\2\u00cb\u00cd\7\17\2\2\u00cc\u00cb"+
		"\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00cf\7\f\2\2\u00cf"+
		"\u00d0\3\2\2\2\u00d0\u00d1\b!\2\2\u00d1B\3\2\2\2\u00d2\u00d3\7\61\2\2"+
		"\u00d3\u00d4\7\61\2\2\u00d4\u00d8\3\2\2\2\u00d5\u00d7\n\t\2\2\u00d6\u00d5"+
		"\3\2\2\2\u00d7\u00da\3\2\2\2\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9"+
		"\u00db\3\2\2\2\u00da\u00d8\3\2\2\2\u00db\u00dc\b\"\2\2\u00dcD\3\2\2\2"+
		"\u00dd\u00de\7\61\2\2\u00de\u00df\7,\2\2\u00df\u00e3\3\2\2\2\u00e0\u00e2"+
		"\13\2\2\2\u00e1\u00e0\3\2\2\2\u00e2\u00e5\3\2\2\2\u00e3\u00e4\3\2\2\2"+
		"\u00e3\u00e1\3\2\2\2\u00e4\u00e6\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e6\u00e7"+
		"\7,\2\2\u00e7\u00e8\7\61\2\2\u00e8\u00e9\3\2\2\2\u00e9\u00ea\b#\2\2\u00ea"+
		"F\3\2\2\2\u00eb\u00ed\t\n\2\2\u00ec\u00eb\3\2\2\2\u00ed\u00ee\3\2\2\2"+
		"\u00ee\u00ec\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00f1"+
		"\b$\2\2\u00f1H\3\2\2\2\16\2~\u0082\u008d\u009e\u00a0\u00a2\u00c8\u00cc"+
		"\u00d8\u00e3\u00ee\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}