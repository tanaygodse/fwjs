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
USING: 'using';
RETURN: 'return';

// Literals
INT: [1-9][0-9]* | '0';
BOOL: 'true' | 'false';
NULL: 'null';
STRING: '"' ( ~["\\] | '\\' (["\\bfnrt] | 'u' HEX HEX HEX HEX) )* '"';
fragment HEX: [0-9a-fA-F];

// Symbols 
AND: '&&';
OR: '||';
NOT: '!';
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
NE: '!=';
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
    | functionDeclaration                                # funcDeclStat
    | variableDeclaration SEPARATOR                      # varDeclStat
    | assignmentStatement SEPARATOR                      # assignStat
    | returnStatement SEPARATOR                          # returnStat
    | expr SEPARATOR                                     # bareExpr
    | IF LPAREN expr RPAREN block ELSE block             # ifThenElse
    | IF LPAREN expr RPAREN block                        # ifThen
    | WHILE LPAREN expr RPAREN block                     # whileStat
    | PRINT LPAREN expr RPAREN SEPARATOR                 # printStat
    | SEPARATOR                                          # blankStat
    ;

expr:
    expr AND expr                                        # logicalAnd
    | expr OR expr                                       # logicalOr
    | LPAREN expr RPAREN                                 # parens
    | objectLiteral                                      # objectLiteralExpr
    | expr LPAREN arglist? RPAREN capabilityClause?      # functionCall
    | NOT expr                                           # notExpr
    | expr op=('*' | '/' | '%') expr                     # MulDivMod
    | expr op=('+' | '-') expr                           # AddSub
    | expr op=('<' | '<=' | '>' | '>=' | '==' | '!=') expr # Comparisons
    | expr DOT IDENTIFIER                                # objectPropertyAccess
    | expr DOT IDENTIFIER ASSIGN expr                    # objectPropertyAssign
    | expr DOT IDENTIFIER LPAREN arglist? RPAREN         # methodCall
    | NEW IDENTIFIER LPAREN arglist? RPAREN              # objectCreation
    | IDENTIFIER                                         # variableReference
    | INT                                                # int
    | BOOL                                               # boolean
    | NULL                                               # null
    | STRING                                             # string
    ;

functionDeclaration:
      FUNCTION IDENTIFIER LPAREN idlist? RPAREN block    # funcDeclWithName
    | FUNCTION LPAREN idlist? RPAREN block               # anonFuncDecl
    ;



variableDeclaration:
    VAR IDENTIFIER ASSIGN expr                           # varDecl
    ;


assignmentStatement:
    IDENTIFIER ASSIGN expr                               # assignmentExpr
    ;


returnStatement:
    RETURN expr?                                         # returnStatRule
    ;


capabilityClause: USING capabilityList;

capabilityList: IDENTIFIER (COMMA IDENTIFIER)*;

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
    | stat                                               # simpleBlock
    ;


