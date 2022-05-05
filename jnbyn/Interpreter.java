package tech.sfqr.nbyn;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

public class Interpreter {
private char[] tape;
private static int head;
private Scanner sc = new Scanner(System.in);
public Interpreter(int rowlen){
		tape = new char[32768];
		head = 0;
}

private static void moveHead(int modVal){
	head += modVal;
	head %= 32768;
}
public void interpret(List<Stmt> matrices, int startI, Binder binderObj){
	boolean err = false;
	for(int i = startI; i < matrices.size(); i++) {
		if(err){
			System.out.printf("Undeclared function at Matrix: [%d]%n", i+1);
			break;
		}
		Stmt matrix = matrices.get(i);
		switch(matrix.func) {
		case 0:
			// [ -- advance past matching ']' if 0, else continue
			if(tape[head] == 0){
				int brackMatch = 0;
				i++;
				while((matrices.get(i).func != 7) && (brackMatch == 0)){
					if(matrices.get(i).func == 0){
						brackMatch++;
					}
					else if(matrices.get(i).func == 7){
						brackMatch--;
					}
					i++;
				}
			}
			break;
		case 1:
			moveHead(matrix.index);
			tape[head] += matrix.val;
			moveHead(-matrix.index);
			break;
		case 2:
			moveHead(matrix.index);
			tape[head] = (char)sc.nextInt();
			moveHead(-matrix.index);
			break;
		case 3:
			moveHead(matrix.index);
			System.out.println(tape[head]);
			moveHead(-matrix.index);
			break;
		case 4:
			moveHead(matrix.val);
			break;
		case 5:
			moveHead(-matrix.val);
			break;
		case 6:
			moveHead(matrix.index);
			tape[head] -= matrix.val;
			moveHead(-matrix.index);
			break;
		case 7:
			// ] -- unconditional jump back to matching '['
			int brackMatch = 0;
			i--;
			while((matrices.get(i).func != 0) && (brackMatch == 0)){
				if(matrices.get(i).func == 0){
					brackMatch++;
				}
				else if(matrices.get(i).func == 7){
					brackMatch--;
				}
				i--;
			}
			i--;
			break;
		default:
			// Functions lie here, associated with matrix.func-8 in hashmap, with [val, func, 0]
			List<Stmt> funcStmts = binderObj.getFunc(matrix.func - 8);
			if(funcStmts.isEmpty()){
				err = true;
				i--;
				break;
			}
			else{
				this.interpret(funcStmts, 0, binderObj);
			}
			break;
		}
	}
}
}
