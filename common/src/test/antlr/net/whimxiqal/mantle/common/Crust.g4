grammar Crust;

// crust
crust: CRUST (register | unregister | player);
core: CORE identifier;  // just tests autocompletion for this specific case

register: REGISTER identifier identifier?;
unregister: UNREGISTER identifier;
player: PLAYER identifier (playerInfo | playerEdit);
playerInfo: INFO;
playerEdit: EDIT (playerEditNickname);
playerEditNickname: NICKNAME identifier;

// tokens
CRUST: 'crust';
CORE: 'core';
REGISTER: 'register';
UNREGISTER: 'unregister';
PLAYER: 'player';
INFO: 'info';
EDIT: 'edit';
NICKNAME: 'nickname';

// MANTLE NODES
identifier: ident | (SINGLE_QUOTE ident+ SINGLE_QUOTE) | (DOUBLE_QUOTE ident+ DOUBLE_QUOTE);
ident: ID
        | CRUST
        | CORE
        | REGISTER
        | UNREGISTER
        | PLAYER
        | INFO
        | EDIT
        | NICKNAME;
ID: [a-zA-Z0-9\-_]+;
ID_SET: ID (COMMA ID)+;
COMMA: ',';
SINGLE_QUOTE: '\'';
DOUBLE_QUOTE: '"';
WS : [ \t\r\n]+ -> channel(HIDDEN); // skip spaces, tabs, newlines
// END MANTLE NODES