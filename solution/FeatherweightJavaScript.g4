grammar FeatherweightJavaScript;

@header { package edu.sjsu.fwjs.parser; }

// Reserved words
IF: 'if';
ELSE: 'else';
WHILE: 'while';
FUNCTION: 'function';
VAR: 'var';
PRINT: 'print';

// Literals
INT: [1-9][0-9]* | '0';
BOOL: 'true' | 'false';
NULL: 'null';

// Symbols 
MUL: '*';
DIV: '/';
PLUS: '+';
MIN: '-';
MOD: '%';
SEPARATOR: ';';
GT: '>';
LT: '<';
GE: '>=';
LE: '<=';
EQ: '==';
IDENTIFIER: [a-zA-Z_][a-zA-Z_0-9]*;

// Whitespace and comments
NEWLINE: '\r'? '\n' -> skip;
LINE_COMMENT: '//' ~[\n\r]* -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;
WS: [ \t]+ -> skip; // ignore whitespace

// ***Parsing rules ***

/** The start rule */
prog: stat+;

stat:
	expr SEPARATOR											# bareExpr
	| IF '(' expr ')' block ELSE block	# ifThenElse
	| IF '(' expr ')' block							# ifThen
	| WHILE '(' expr ')' block					# while
	| PRINT '(' expr ')' SEPARATOR			# print
	| SEPARATOR													# blankExpr;

expr:
	'(' expr ')'																			# parens
	| expr '(' arglist? ')'														# functionCall
	| expr op = ('*' | '/' | '%') expr								# MulDivMod
	| expr op = ('+' | '-') expr											# AddSub
	| expr op = ('<' | '<=' | '>' | '>=' | '==') expr	# Comparisons
	| FUNCTION IDENTIFIER '(' idlist? ')' block				# functionDeclaration
	| FUNCTION '(' idlist? ')' block									# anonFunctionDeclaration
	| IDENTIFIER																			# variableReference
	| VAR IDENTIFIER '=' expr													# variableDeclaration
	| IDENTIFIER '=' expr															# assignmentStatement
	| INT																							# int
	| BOOL																						# boolean
	| NULL																						# null;

arglist: expr (',' expr)*;

idlist: IDENTIFIER (',' IDENTIFIER)*;

block: '{' stat* '}' # fullBlock | stat # simpBlock;

