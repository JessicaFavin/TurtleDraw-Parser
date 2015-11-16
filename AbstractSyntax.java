import java.util.*;

abstract class Expression {
    abstract double eval(ValueEnvironment env, DrawPanel dp)
    throws Exception; 
}
class Int extends Expression {
	private double value;
	public Int(double i) {
		value = i;
	}
	public double eval(ValueEnvironment env, DrawPanel dp) throws Exception {
		return value;
	}
}
class Var extends Expression {
	private String name;
	public Var(String s) {
		name = s;
	}
	public double eval(ValueEnvironment env, DrawPanel dp) throws Exception {
		return env.getValue(name);
	}
}
class Sum extends Expression {
	private Expression left, right;
	public Sum(Expression l, Expression r) {
		left = l;
		right = r;
	}
	public double eval(ValueEnvironment env, DrawPanel dp) throws Exception {
		return left.eval(env, dp)+right.eval(env, dp);
	}
}
class Difference extends Expression {
	private Expression left, right;
	public Difference(Expression l, Expression r) throws Exception {
		left = l;
		right = r;
	}
	public double eval(ValueEnvironment env, DrawPanel dp) throws Exception{
		return left.eval(env, dp)-right.eval(env, dp);
	}
}
class Product extends Expression {
	private Expression left, right;
	public Product(Expression l, Expression r) {
		left = l;
		right = r;
	}
	public double eval(ValueEnvironment env, DrawPanel dp) throws Exception {
		return left.eval(env, dp)*right.eval(env, dp);
	}
}
class Division extends Expression {
	private Expression left, right;
	public Division(Expression l, Expression r) {
		left = l;
		right = r;
	}
	public double eval(ValueEnvironment env, DrawPanel dp) throws Exception {
		//Division par 0
		if(right.eval(env, dp)==0){
			dp.p_cp.wrongMessage("Erreur division par 0!!");
			throw new Exception("Erreur division par 0!!");
			
		} else {
			return left.eval(env, dp)/right.eval(env, dp);
		}
	}
}

class Program {
	private BlocDecl first;
	private Instruction rest;
	public Program(BlocDecl d, Instruction i) {
		first = d;
		rest = i;
	}
	public void run(ValueEnvironment env, DrawPanel dp)
	throws Exception {
		if (first != null) {
			first.exec(env, dp);
		}
		if(rest != null){
				rest.exec(env, dp);
		}
	} 
}

abstract class Instruction {
	abstract void exec(ValueEnvironment env, DrawPanel dp)
	throws Exception;
}
class Declaration extends Instruction {
	private String varName;
	public Declaration(String s) {
		varName = s;
	}
	public void exec(ValueEnvironment env, DrawPanel dp) 
	throws Exception {
		if(env.get(varName)!=null){
			dp.p_cp.wrongMessage("Variable déjà déclarée");
		} else {
			try{
				env.addVariable(varName);
				dp.p_cp.correctMessage("Variable declarée");
			} catch(Exception e) {
				throw new Exception();
			}
		}
	} 
}
class Assignment extends Instruction {
	private String varName;
	private Expression exp;
	public Assignment(String s, Expression e) {
		varName = s;
		exp = e;
	}
	public void exec(ValueEnvironment env, DrawPanel dp)
	throws Exception {
		try{
			env.setVariable(varName, exp.eval(env, dp));
			dp.p_cp.correctMessage("Assignement effectuée");
		} catch(Exception e) {
			throw new Exception();
		}
	}
}

class Move extends Instruction {
	private Expression exp;
	public Move(Expression e){
		exp = e;
	}
	public void exec(ValueEnvironment env, DrawPanel dp) throws Exception {
		dp.move(exp.eval(env, dp));
		//dp.p_cp.correctMessage("Avancé");
	}
}

class Mode extends Instruction {
	private Expression exp;
	public Mode(Expression e){
		exp = e;
	}
	public void exec(ValueEnvironment env, DrawPanel dp) throws Exception {
		if(exp.eval(env, dp)==90){
			dp.setMode(true);
			dp.p_cp.correctMessage("Mode 90° activé");
		} else {
			dp.setMode(false);
			dp.p_cp.correctMessage("Mode 90° désactivé");
		}
	}
}

class Save extends Instruction {
	public Save(){
	}
	public void exec(ValueEnvironment env, DrawPanel dp) throws Exception {
		dp.takePicture(dp);
	}
}

class Turn extends Instruction {
	private Expression exp;
	public Turn(Expression e){
		exp = e;
	}
	public void exec(ValueEnvironment env, DrawPanel dp) throws Exception {
		//dp.setAngle();
		dp.turnAngle(exp.eval(env, dp));
	}
}

class Color extends Instruction {
	private Expression exp;
	public Color(Expression e){
		exp = e;
	}
	public void exec(ValueEnvironment env, DrawPanel dp) throws Exception {
		//setColor pas encore définie
		dp.setColor((int)exp.eval(env, dp));
		dp.p_cp.correctMessage("Couleur changée");
	}
}

class Size extends Instruction {
	private Expression exp;
	public Size(Expression e){
		exp = e;
	}
	public void exec(ValueEnvironment env, DrawPanel dp) throws Exception {
		dp.setSize((int)exp.eval(env, dp));
		dp.p_cp.correctMessage("Epaisseur changée");
	}
}

class If extends Instruction {
	private Expression exp;
	private Instruction inst;
	public If(Expression e, Instruction i){
		exp=e;
		inst=i;
	}
	public void exec(ValueEnvironment env, DrawPanel dp) throws Exception {
		if(exp.eval(env, dp)==0){
			inst.exec(env, dp);
		}
	}
}

class IfElse extends Instruction {
	private Expression exp;
	private Instruction inst;
	private Instruction alt;
	public IfElse(Expression e, Instruction i, Instruction a){
		exp=e;
		inst=i;
		alt=a;
	}
	public void exec(ValueEnvironment env, DrawPanel dp) throws Exception {
		if(exp.eval(env, dp)==0){
			inst.exec(env, dp);
		} else {
			alt.exec(env, dp);
		}
	}
}

class For extends Instruction {
	private Expression exp;
	private Instruction inst;
	public For(Expression e, Instruction i){
		exp=e;
		inst=i;
	}
	public void exec(ValueEnvironment env, DrawPanel dp) throws Exception {
		if(exp.eval(env, dp)>0){
			for(int i=0; i<exp.eval(env, dp); i++){
				inst.exec(env, dp);
			}
		} else {
			dp.p_cp.wrongMessage("Condition de boucle POUR non valide");
		}
	}
}

class While extends Instruction {
	private Expression exp;
	private Instruction inst;
	public While(Expression e, Instruction i){
		exp=e;
		inst=i;
	}
	public void exec(ValueEnvironment env, DrawPanel dp) throws Exception {
		while(exp.eval(env, dp)!=0){
			inst.exec(env, dp);
		}
	}
}

class Position extends Instruction {
	private boolean canDraw;
	public Position(boolean b){
		canDraw = b;
	}
	public void exec(ValueEnvironment env, DrawPanel dp) throws Exception {
		dp.setCanDraw(this.canDraw);
		if(canDraw){
			dp.p_cp.correctMessage("Pinceau baissé");
		} else {
			dp.p_cp.correctMessage("Pinceau relevé");
		}
	}
}

class BlocInst extends Instruction {
	private Instruction first;
	private BlocInst rest;
	public BlocInst(Instruction inst, BlocInst bloc) throws Exception {
		first = inst;
		rest = bloc;
	}
	public void exec(ValueEnvironment env, DrawPanel dp) throws Exception {
		if(first!=null){
			first.exec(env, dp);
			if(rest!=null){
				rest.exec(env, dp);
			}
		}
	}
}

class BlocDecl{
	private Declaration first;
	private BlocDecl rest;
	public BlocDecl(Declaration decl, BlocDecl bloc){
		first = decl;
		rest = bloc;
	}
	public void exec(ValueEnvironment env, DrawPanel dp) throws Exception {
		if(first!=null){
			first.exec(env, dp);
			if(rest!=null){
				rest.exec(env, dp);
			}
		}
	}
}

class ValueEnvironment extends HashMap<String, Double> {
	public ValueEnvironment() {
		super();
	}
	public void addVariable(String name) 
	throws Exception {
		this.put(name, 0.0);
	}
	public void setVariable(String name, double value) 
	throws Exception {
		this.put(name, value);
	}
	public double getValue(String name) 
	throws Exception {
		if(this.get(name)==null){
			throw new Exception("Variable non déclarée");
		}
		return this.get(name);
	}
}
