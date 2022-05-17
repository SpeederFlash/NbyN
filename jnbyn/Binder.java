package tech.sfqr.nbyn;

import java.lang.Math;

import java.util.ArrayList;
import java.util.List;

public class Binder{
private static int bindingHelper = 0;
private ArrayList<ArrayList<Stmt>> funcs;
public Binder(int n){
  int maxFuncs = ((int) Math.pow(2, n)) - 8;
  funcs = new ArrayList<ArrayList<Stmt>>();
  for(int i = 0; i < maxFuncs; i++){
    funcs.add(new ArrayList<Stmt>());
  }
}

private ArrayList<Stmt> bindFunc(int i, int f, List<Stmt> matrices){
  ArrayList<Stmt> output = new ArrayList<Stmt>();
  for(int j = i; j < matrices.size(); j++){
    Stmt matrix = matrices.get(j);
    if(matrix.func == f){
      bindingHelper = j;
      return output;
    }
    else{
      output.add(matrix);
    }
  }
  return output;
}

public List<Stmt> getFunc(int n){
  return funcs.get(n);
}

public int bind(List<Stmt> matrices){
  for(int i = 0; i < matrices.size(); i++){
    Stmt matrix = matrices.get(i);
    if((matrix.func < 8) || (!funcs.get(matrix.func-8).isEmpty())){
      return i;
    }
    else{
      funcs.set(matrix.func-8, bindFunc(i+1, matrix.func, matrices));
      i = bindingHelper;
    }
  }
  return 0;
}
}
