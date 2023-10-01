grammar Crust;

// crust
crust: CRUST (register | unregister | player | age);
core: CORE identifier;  // just tests autocompletion for this specific case

register: REGISTER name=identifier (color=identifier (identifier (identifier person=identifier?)?)?)?;
unregister: UNREGISTER name=identifier;
player: PLAYER name=identifier (playerInfo | playerEdit);
playerInfo: INFO;
playerEdit: EDIT (playerEditNickname | playerEditColor);
playerEditNickname: NICKNAME identifier;
playerEditColor: COLOR identifier;

age: AGE identifier;  // tests integer parameter

// tokens
CRUST: 'crust';
CORE: 'core';
REGISTER: 'register';
UNREGISTER: 'unregister';
PLAYER: 'player';
INFO: 'info';
EDIT: 'edit';
NICKNAME: 'nickname';
COLOR: 'color';
AGE: 'age';

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
        | NICKNAME
        | COLOR
        | AGE;
ID: [a-zA-Z0-9\-_&.{}]+;
SINGLE_QUOTE: '\'';
DOUBLE_QUOTE: '"';
WS : [ \t\r\n]+ -> channel(HIDDEN); // skip spaces, tabs, newlines
// END MANTLE NODES