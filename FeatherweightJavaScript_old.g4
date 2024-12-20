grammar FeatherweightJavaScript;

@header { package edu.sjsu.fwjs.parser; }

// Reserved words
IF: 'if';
ELSE: 'else';
WHILE: 'while';
FUNCTION: 'function';
VAR: 'var';
PRINT: 'print';
NEW: 'new';
IMPORT: 'import';

// Literals
INT: [1-9][0-9]* | '0';
BOOL: 'true' | 'false';
NULL: 'null';
STRING: '"' ( ~["\\] | '\\' (["\\bfnrt] | 'u' HEX HEX HEX HEX) )* '"';
fragment HEX: [0-9a-fA-F];

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
DOT: '.';
COMMA: ',';
ASSIGN: '=';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
COLON: ':';
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
    importStatement                                      # importExpr
    | expr SEPARATOR                                     # bareExpr
    | IF LPAREN expr RPAREN block ELSE block             # ifThenElse
    | IF LPAREN expr RPAREN block                        # ifThen
    | WHILE LPAREN expr RPAREN block                     # while
    | PRINT LPAREN expr RPAREN SEPARATOR                 # print
    | SEPARATOR                                          # blankExpr
    ;

expr:
    LPAREN expr RPAREN                                   # parens
    | objectLiteral                                      # objectLiteralExpr
    | expr LPAREN arglist? RPAREN                        # functionCall
    | assignmentExpr
    | expr op=('*' | '/' | '%') expr                     # MulDivMod
    | expr op=('+' | '-') expr                           # AddSub
    | expr op=('<' | '<=' | '>' | '>=' | '==') expr      # Comparisons
    | FUNCTION IDENTIFIER LPAREN idlist? RPAREN block    # functionDeclaration
    | FUNCTION LPAREN idlist? RPAREN block               # anonFunctionDeclaration
    | VAR IDENTIFIER ASSIGN assignmentExpr               # variableDeclaration
    | IDENTIFIER ASSIGN assignmentExpr                   # assignmentStatement
    | IDENTIFIER                                         # variableReference
    | NEW IDENTIFIER LPAREN arglist? RPAREN              # objectCreation
    | expr DOT IDENTIFIER                                # objectPropertyAccess
    | expr DOT IDENTIFIER ASSIGN expr                    # objectPropertyAssign
    | expr DOT IDENTIFIER LPAREN arglist? RPAREN         # methodCall
    | INT                                                # int
    | BOOL                                               # boolean
    | NULL                                               # null
    | STRING                                             # string
    ;

assignmentExpr:
    functionExpr
    ;

functionExpr:
    objectExpr
    | functionExpr LPAREN arglist? RPAREN                # functionCall
    ;

objectExpr:
    primary
    | objectExpr DOT IDENTIFIER                          # objectPropertyAccess
    | objectExpr DOT IDENTIFIER LPAREN arglist? RPAREN   # methodCall
    | objectExpr DOT IDENTIFIER ASSIGN assignmentExpr    # objectPropertyAssign
    ;

primary:
    LPAREN expr RPAREN                                   # parens
    | FUNCTION IDENTIFIER LPAREN idlist? RPAREN block    # functionDeclaration
    | FUNCTION LPAREN idlist? RPAREN block               # anonFunctionDeclaration
    | NEW IDENTIFIER LPAREN arglist? RPAREN              # objectCreation
    | IDENTIFIER                                         # variableReference
    | INT                                                # int
    | BOOL                                               # boolean
    | NULL                                               # null
    | STRING                                             # string
    | objectLiteral                                      # objectLiteralExpr
    ;

objectLiteral:
    LBRACE objectPropertyList? RBRACE
    ;

objectPropertyList:
    objectProperty (COMMA objectProperty)*
    ;

objectProperty:
    (IDENTIFIER | STRING) COLON expr
    ;

importStatement:
    IMPORT str=STRING SEPARATOR
    ;

arglist: expr (COMMA expr)*;

idlist: IDENTIFIER (COMMA IDENTIFIER)*;

block:
    LBRACE stat* RBRACE                                  # fullBlock
    | stat                                               # simpBlock
    ;

