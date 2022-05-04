package tech.sfqr.nbyn;

class Stmt {
final int index;
final int func;
final int val;

Stmt(int index, int func, int val) {
	this.index = index;
  this.func = func;
  this.val = val;
}

// Return the token as a string literal
public String toString() {
	return "IND:\t" + index +"\nFUNC:\t"+func+"\nVAL:\t"+val;
}
}
