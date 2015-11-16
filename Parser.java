import java.io.*;
import java.util.*;

class Parser {
	/*
	 * Code -> Prog
     * Prog -> Decl | Inst
     * Decl -> VAR var; Decl | e
     * Inst -> var = Exp; | DEBUT BlocInst FIN | AVANCE Exp; | TOURNE Exp; | HAUTPINCEAU; | BASPINCEAU;
     *  | COULEUR Exp; | EPAISSEUR Exp; | SI Exp ALORS Inst InstSuite | TANT QUE Exp FAIRE Inst 
     *  | POUR Exp TOURS Inst | MODE Exp; | SAVE;
     * InstSuite -> SINON Inst | e
     * BlocInst -> Inst BlocInst | e
	 * Exp -> int ExpSuite | var ExpSuite | (Exp) ExpSuite
	 * ExpSuite -> Op ExpSuite | e
     * Op -> + Exp | - Exp | * Exp | / Exp
	 */
    protected LookAhead1 reader;
    protected int pos;
    protected ValueEnvironment env;
    protected DrawPanel dp;

    public Parser(LookAhead1 r, ValueEnvironment e, DrawPanel d) {
	   reader = r;
	   env = e;
	   dp = d;
	   pos = 0;
    }   
    
    public Instruction nontermCode() throws Exception {
        Instruction prog = nontermProg();
        return prog;
    }
    
    public Instruction nontermProg() throws Exception {
    	if(reader.check(Sym.VAR)){
			Declaration decl = nontermDecl();
			return decl;
    	} else if(reader.check(Sym.VARIABLE)||reader.check(Sym.DEBUT)||
    	reader.check(Sym.AVANCE)||reader.check(Sym.TOURNE)||
    	reader.check(Sym.HAUT)||reader.check(Sym.BAS)
    	||reader.check(Sym.COULEUR)||reader.check(Sym.EPAISSEUR)
    	||reader.check(Sym.SI)||reader.check(Sym.TANTQUE)
    	||reader.check(Sym.POUR)||reader.check(Sym.MODE)||reader.check(Sym.SAVE)){
    		Instruction inst = nontermInst();
    		return inst;
    	
    	} else {
			dp.p_cp.wrongMessage("Erreur Instruction prog");
			throw new Exception();
    	}
    }

    public Declaration nontermDecl() throws Exception {
    	if(reader.check(Sym.VAR)){
			term(Sym.VAR);
			String nomVar = reader.getStringValue();
			term(Sym.VARIABLE);
			term(Sym.CONCAT);
			Declaration decl = new Declaration(nomVar);
			decl.exec(env,dp);
			return decl;
		} else {
			dp.p_cp.wrongMessage("Erreur Declaration");
			throw new Exception();
		}
    }


    public Instruction nontermInst() throws Exception {
    	if(reader.check(Sym.VARIABLE)){
			String nomVar = reader.getStringValue();
			term(Sym.VARIABLE);
			term(Sym.EQ);
			Expression exp = nontermExp();
			term(Sym.CONCAT);
			Assignment a = new Assignment(nomVar, exp);
			a.exec(env, dp);
			return a;
			
    	} else if(reader.check(Sym.DEBUT)) {
			term(Sym.DEBUT);
			BlocInst bloc = nontermBlocInst();
			term(Sym.FIN);
			return bloc;
			
    	} else if(reader.check(Sym.AVANCE)) {
			term(Sym.AVANCE);
			Expression exp = nontermExp();
			term(Sym.CONCAT);
			Move m = new Move(exp);
			m.exec(env, dp);
			return m;
    	} else if(reader.check(Sym.TOURNE)) {
			term(Sym.TOURNE);
			Expression exp = nontermExp();
			term(Sym.CONCAT);
			Turn t = new Turn(exp);
			t.exec(env, dp);
			return t;
    	}else if(reader.check(Sym.HAUT)) {
			term(Sym.HAUT);
			term(Sym.CONCAT);
			Position p = new Position(false);
			p.exec(env, dp);
			return p;
    	} else if(reader.check(Sym.BAS)) {
			term(Sym.BAS);
			term(Sym.CONCAT);
			Position p = new Position(true);
			p.exec(env, dp);
			return p;
    	} else if(reader.check(Sym.COULEUR)){
    		term(Sym.COULEUR);
    		Expression exp = nontermExp();
			term(Sym.CONCAT);
			Color c = new Color(exp);
			c.exec(env, dp);
			return c;
    	} else if (reader.check(Sym.EPAISSEUR)){
    		term(Sym.EPAISSEUR);
    		Expression exp = nontermExp();
			term(Sym.CONCAT);
			Size s = new Size(exp);
			s.exec(env, dp);
			return s;
    	} else if(reader.check(Sym.SI)){
    		term(Sym.SI);
    		Expression exp = nontermExp();
    		term(Sym.ALORS);
    		Instruction inst = nontermInst();
    		Instruction s = nontermInstSuite(exp, inst);
    		s.exec(env, dp);
    		return s;
    	} else if(reader.check(Sym.MODE)){
			term(Sym.MODE);
			Expression exp= nontermExp();
			term(Sym.CONCAT);
			Mode m = new Mode(exp);
			m.exec(env, dp);
			return m;
		} else if(reader.check(Sym.SAVE)){
			term(Sym.SAVE);
			term(Sym.CONCAT);
			Save s = new Save();
			s.exec(env, dp);
			return s;
    	} else if(reader.check(Sym.TANTQUE)){
    		term(Sym.TANTQUE);
    		Expression exp = nontermExp();
    		term(Sym.FAIRE);
    		Instruction inst = nontermInst();
    		While w = new While(exp, inst);
    		w.exec(env, dp);
    		return w;
    	} else if(reader.check(Sym.POUR)){
			term(Sym.POUR);
			Expression exp = nontermExp();
			term(Sym.TOURS);
			Instruction inst= nontermInst();
			For f = new For(exp, inst);
			f.exec(env, dp);
			return f;
			
    	} else {
			dp.p_cp.wrongMessage("Erreur Instruction");
			throw new Exception();
    	}
    }

    public Instruction nontermInstSuite(Expression exp, Instruction inst) throws Exception {
    	if(reader.current()==null){
    		If si = new If(exp, inst);
    		si.exec(env, dp);
			return si;
		} else if(reader.check(Sym.SINON)) {
			term(Sym.SINON);
    		Instruction alt = nontermInst();
    		IfElse sinon = new IfElse(exp, inst, alt);
    		sinon.exec(env, dp);
    		return sinon;
		} else {
			dp.p_cp.wrongMessage("Erreur InstructionSuite");
			throw new Exception();
		}
    }


    public BlocInst nontermBlocInst() throws Exception {
    	if(reader.check(Sym.VARIABLE)||reader.check(Sym.DEBUT)||
    	reader.check(Sym.AVANCE)||reader.check(Sym.TOURNE)||
    	reader.check(Sym.HAUT)||reader.check(Sym.BAS)
    	||reader.check(Sym.COULEUR)||reader.check(Sym.EPAISSEUR)
    	||reader.check(Sym.SI)||reader.check(Sym.TANTQUE)
    	||reader.check(Sym.POUR)||reader.check(Sym.MODE)||reader.check(Sym.SAVE)){
			Instruction inst = nontermInst();
			BlocInst bloc = nontermBlocInst();
			return new BlocInst(inst, bloc);
    	} else {
    		if(reader.check(Sym.FIN)){
				return null;
			} else {
				dp.p_cp.wrongMessage("Erreur BlocInst");
				throw new Exception();
			}
    	}
    }


    public Expression nontermExp() throws Exception {
    	if(reader.check(Sym.INT)){
			double value = reader.getIntValue();
			term(Sym.INT);
			Expression suite = nontermExpS(new Int(value));
			return suite;
    	} else if(reader.check(Sym.VARIABLE)) {
			String var = reader.getStringValue();
			term(Sym.VARIABLE);
			Expression suite = nontermExpS(new Var(var));
			return suite;
    	} else if(reader.check(Sym.LPAR)){
			term(Sym.LPAR);
			Expression exp = nontermExp();
			term(Sym.RPAR);
			Expression expSuite = nontermExpS(exp);
			return expSuite;
    	} else {
    		dp.p_cp.wrongMessage("Erreur Expression");
			throw new Exception();
    	}
    }


    public Expression nontermExpS(Expression beginning) throws Exception {
    	if(reader.check(Sym.PLUS)||reader.check(Sym.MINUS)||reader.check(Sym.TIMES)||reader.check(Sym.DIV)){
			Expression exp = nontermOp(beginning);
			Expression exp2 = nontermExpS(exp);
			return exp2;
		} else if(reader.check(Sym.CONCAT)||reader.check(Sym.ALORS)||reader.check(Sym.FAIRE)||reader.check(Sym.TOURS)||reader.check(Sym.RPAR)) {
			return beginning;
		} else {
			
			dp.p_cp.wrongMessage("Erreur ExpresionSuite");
			throw new Exception();
		}
    }

    public Expression nontermOp(Expression beginning) throws Exception {
    	if(reader.check(Sym.PLUS)){
			term(Sym.PLUS);
			Expression exp = nontermExp();
			return new Sum(beginning, exp);
    	} else if(reader.check(Sym.MINUS)){
			term(Sym.MINUS);
			Expression exp = nontermExp();
			return new Difference(beginning, exp);
    	} else if(reader.check(Sym.TIMES)){
			term(Sym.TIMES);
			Expression exp = nontermExp();
			return new Product(beginning, exp);
    	} else if(reader.check(Sym.DIV)) {
			term(Sym.DIV);
			Expression exp = nontermExp();
			return new Division(beginning, exp);
    	} else {
    		dp.p_cp.wrongMessage("Erreur Opérateur");
			throw new Exception();
    	}
    }
    
    public void term(Sym symbol) throws Exception {
		try{
			reader.eat(symbol);
			pos++;
		}catch(ReadException e){
			dp.p_cp.wrongMessage("Erreur "+e.getExpected()+" non reconnu");
			throw new ParserException("attendu : \""+e.getExpected()+"\" trouvé : \""+e.getFound()+"\"");
		}
	}
}
