lexer grammar GraafvisVocab;

ARROW: '->';
COLON: ':';

EOL: '.';
NEWLINE: '\n';

PAR_OPEN: '(';
PAR_CLOSE: ')';
BRACE_OPEN: '{';
BRACE_CLOSE: '}';

/* Eq operators */
EQ: '==';
NQ: '!=';
GT: '>';
ST: '<';
GE: '>=';
LE: '<=';

/* Booloperators */

COMMA: ',';
OR: 'or';

UNDERSCORE: '_';

TRUE: 'true';
FALSE: 'false';

IMPORT_TOKEN: 'consult';
NODE_LABEL_TOKEN: 'node labels';
EDGE_LABEL_TOKEN: 'edge labels';
RENAME_TOKEN: 'as';

STRING: '"' (~'"')* '"';
NUMBER:         [0-9]+;

NAME_LO: LETTER_LO (LETTER_LO | LETTER_HI | NUMBER)*;
NAME_HI: LETTER_HI (LETTER_LO | LETTER_HI | NUMBER)*;
NAME:    NAME_LO | NAME_HI;

fragment LETTER_LO: [a-z];
fragment LETTER_HI: [A-Z];
fragment BEGIN_COMMENT: '%';

WS:             [ \n\r\t] -> skip;
BLOCKCOMMENT:   '/*' .*? '*/' -> skip;
LINECOMMENT: BEGIN_COMMENT .*? NEWLINE -> skip;
