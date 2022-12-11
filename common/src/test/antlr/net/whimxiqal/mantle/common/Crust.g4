grammar Crust;

// crust
crust: CRUST (register | unregister | player);
core: CORE color=identifier;  // just tests autocompletion for this specific case

register: REGISTER user=identifier color=identifier?;
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
identifier: ID | SINGLE_QUOTE ID SINGLE_QUOTE | DOUBLE_QUOTE ID DOUBLE_QUOTE;
ID: [a-zA-Z0-9\-_]+;
SINGLE_QUOTE: '\'';
DOUBLE_QUOTE: '"';
WS : [ \t\r\n]+ -> channel(HIDDEN); // skip spaces, tabs, newlines
// END MANTLE NODES