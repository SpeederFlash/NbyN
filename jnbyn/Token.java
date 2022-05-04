package tech.sfqr.nbyn;

import static tech.sfqr.nbyn.TokenType.*;

class Token {
final TokenType type;

Token(TokenType type) {
	this.type = type;
}

// Return the token as a string literal
public String toString() {
	return type.toString();
}
}
