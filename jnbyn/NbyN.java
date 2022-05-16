package tech.sfqr.nbyn;

import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

import static tech.sfqr.nbyn.TokenType.*;

public class NbyN {

private static final int ROWLEN = 4;

private static final Interpreter inter = new Interpreter(ROWLEN);
private static final Binder binderObj = new Binder(ROWLEN);

public static void main(String[] args) throws IOException {
	if (args.length != 1) {
		System.out.println("Usage: jlox [script]");
		System.exit(64);
	} else {
		run(args[0]);
	}
}

private static void run(String fileName) throws IOException {
	byte[] bytes = Files.readAllBytes(Paths.get(fileName));
	String byteString = new String(bytes, Charset.defaultCharset());
	List<Token> tokens = scan(byteString); // Gets tokens
	List<Stmt> matrices = parse(tokens); // Makes sure all matrices are legal
	int startI = binderObj.bind(matrices);
	if(matrices.get(0).func <= -1){
		if(matrices.get(0).func == -2){
			System.out.println("Incorrect number of brackets. Check for unmatched matrices (matrices with function values of 0 and 7 must be in equal amounts).");
			return;
		}
		System.out.println("Erroneous matrix at matrix ["+matrices.get(0).val+"]");
		return;
	}
	inter.interpret(matrices, startI, binderObj);
}

private static List<Token> scan(String str){
	List<Token> tokens = new ArrayList<>();
	for(int i = 0; i < str.length(); i++) {
		switch(str.charAt(i)) {
		case '[':
			tokens.add(new Token(OPEN));
			break;
		case '0':
			tokens.add(new Token(ZERO));
			break;
		case '1':
			tokens.add(new Token(ONE));
			break;
		case ']':
			tokens.add(new Token(CLOSE));
			break;
		default:
			continue;
		}
	}
	tokens.add(new Token(EOF));
	return tokens;
}

private static List<Stmt> parse(List<Token> tokens){
	List<Stmt> stmts = new ArrayList<>();
	int mat = 0;
	int i = 0;
	while(tokens.get(i).type != EOF) {
		mat++;
		if(tokens.get(i).type == OPEN) {
			// Get past open token
			i++;
			int a = 0;
			for(int j = 0; j < ROWLEN; j++){
				if(tokens.get(i+j).type == ONE){
					a += 1;
				}
				if(j != ROWLEN-1){
					a = a << 1;
				}
			}
			i+=ROWLEN;
			int f = 0;
			for(int j = 0; j < ROWLEN; j++){
				if(tokens.get(i+j).type == ONE){
					f += 1;
				}
				if(j != ROWLEN-1){
					f = f << 1;
				}
			}
			i+=ROWLEN;
			int v = 0;
			for(int j = 0; j < ROWLEN; j++){
				if(tokens.get(i+j).type == ONE){
					v += 1;
				}
				if(j != ROWLEN-1){
					v = v << 1;
				}
			}
			i+=ROWLEN;
			stmts.add(new Stmt(a,f,v));
			if(tokens.get(i).type != CLOSE){
				stmts.clear();
				stmts.add(new Stmt(0,-1,mat));
				return stmts;
			}
			i++;
		}
		System.out.println("Parsing "+ i);
	}
	int brackEqualizer = 0;
	System.out.println("-----------");
	for(int k = 0; k < stmts.size(); k++){
		System.out.println(stmts.get(k).toString());
		System.out.println("-----------");
		if(stmts.get(k).func == 0){
			brackEqualizer+=1;
		}
		if(stmts.get(k).func == 7){
			brackEqualizer-=1;
		}
	}
	if(brackEqualizer != 0){
		stmts.clear();
		stmts.add(new Stmt(0,-2,0));
	}
	return stmts;
}

}
