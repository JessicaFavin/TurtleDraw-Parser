import java.io.*;
import java.util.*;

class ParserFile {
	/*
	 * Code -> Prog$
     * Prog -> Decl Inst
     * Decl -> VAR var; Decl | e
     * Inst -> var = Exp; | DEBUT BlocInst FIN | AVANCE Exp; | TOURNE Exp; | HAUTPINCEAU; | BASPINCEAU;
     *  | COULEUR Exp; | EPAISSEUR Exp; | SI Exp ALORS Inst InstSuite | TANT QUE Exp FAIRE Inst  | POUR Exp TOURS Inst
     *  | MODE Exp; | SAUVEAGARDE;
     * InstSuite -> SINON Inst | e
     * BlocInst -> Inst BlocInst | e
	 * Exp -> int ExpSuite | var ExpSuite | (Exp) ExpSuite
	 * ExpSuite -> Op ExpSuite | e
     * Op -> + Exp | - Exp | * Exp | / Exp
	 */
    protected LookAhead1 reader;
    protected ValueEnvironment env;
    protected DrawPanel dp;

    public ParserFile(LookAhead1 r, ValueEnvironment e, DrawPanel d) {
	   reader = r;
	   env = e;
	   dp = d;
    }  
    
    public Program nontermCode() throws Exception {
        Program prog = nontermProg();
        reader.eat(Sym.EOF);
        return prog;
    }
    
    
    public Program nontermProg() throws Exception {
    	if(reader.check(Sym.VAR)||reader.check(Sym.VARIABLE)||reader.check(Sym.DEBUT)||reader.check(Sym.AVANCE)||reader.check(Sym.TOURNE)||reader.check(Sym.BAS)||reader.check(Sym.HAUT)||reader.check(Sym.COULEUR)||reader.check(Sym.EPAISSEUR)||reader.check(Sym.MODE)){
			BlocDecl decl = nontermDecl();
			Instruction inst = nontermInst();
			return new Program(decl, inst);
    	} else {
    		dp.p_cp.wrongMessage("Erreur Programme dans le fichier");
			throw new Exception();
    	}
    }


    public BlocDecl nontermDecl() throws Exception {
    	if(reader.check(Sym.VAR)){
			term(Sym.VAR);
			String nomVar = reader.getStringValue();
			term(Sym.VARIABLE);
			term(Sym.CONCAT);
			BlocDecl decl = nontermDecl();
			return new BlocDecl(new Declaration(nomVar), decl);
		} else if(reader.check(Sym.VARIABLE)||reader.check(Sym.DEBUT)||reader.check(Sym.AVANCE)||reader.check(Sym.TOURNE)||reader.check(Sym.BAS)||reader.check(Sym.HAUT)||reader.check(Sym.COULEUR)||reader.check(Sym.EPAISSEUR)||reader.check(Sym.MODE)){
			return null;
		} else {
			dp.p_cp.wrongMessage("Erreur Déclarations dans le fichier");
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
			return new Assignment(nomVar, exp);
    	} else if(reader.check(Sym.DEBUT)) {
			term(Sym.DEBUT);
			BlocInst bloc = nontermBlocInst();
			term(Sym.FIN);
			return bloc;
    	} else if(reader.check(Sym.AVANCE)) {
			term(Sym.AVANCE);
			Expression exp = nontermExp();
			term(Sym.CONCAT);
			return new Move(exp);
    	} else if(reader.check(Sym.TOURNE)) {
			term(Sym.TOURNE);
			Expression exp = nontermExp();
			term(Sym.CONCAT);
			return new Turn(exp);
    	}else if(reader.check(Sym.HAUT)) {
			term(Sym.HAUT);
			term(Sym.CONCAT);
			return new Position(false);
    	} else if(reader.check(Sym.BAS)) {
			term(Sym.BAS);
			term(Sym.CONCAT);
			return new Position(true);
    	} else if(reader.check(Sym.COULEUR)){
    		term(Sym.COULEUR);
    		Expression exp = nontermExp();
			term(Sym.CONCAT);
			return new Color(exp);
    	} else if (reader.check(Sym.EPAISSEUR)){
    		term(Sym.EPAISSEUR);
    		Expression exp = nontermExp();
			term(Sym.CONCAT);
			return new Size(exp);
    	} else if(reader.check(Sym.SI)){
    		term(Sym.SI);
    		Expression exp = nontermExp();
    		term(Sym.ALORS);
    		Instruction inst = nontermInst();
    		return nontermInstSuite(exp, inst);
    	} else if(reader.check(Sym.TANTQUE)){
    		term(Sym.TANTQUE);
    		Expression exp = nontermExp();
    		term(Sym.FAIRE);
    		Instruction inst = nontermInst();
    		return new While(exp, inst);
    	} else if(reader.check(Sym.POUR)){
			term(Sym.POUR);
			Expression exp = nontermExp();
			term(Sym.TOURS);
			Instruction inst= nontermInst();
			return new For(exp, inst);
		} else if(reader.check(Sym.MODE)){
			term(Sym.MODE);
			Expression exp= nontermExp();
			term(Sym.CONCAT);
			return new Mode(exp);
    	} else {
    		dp.p_cp.wrongMessage("Erreur Instructions dans le fichier");
			throw new Exception();
    	}
    }

    public Instruction nontermInstSuite(Expression exp, Instruction inst) throws Exception {
		if(reader.check(Sym.SINON)) {
			term(Sym.SINON);
    		Instruction alt = nontermInst();
    		return new IfElse(exp, inst, alt);
		} else if(reader.check(Sym.VARIABLE)||reader.check(Sym.DEBUT)||
    	reader.check(Sym.AVANCE)||reader.check(Sym.TOURNE)||
    	reader.check(Sym.HAUT)||reader.check(Sym.BAS)
    	||reader.check(Sym.COULEUR)||reader.check(Sym.EPAISSEUR)
    	||reader.check(Sym.SI)||reader.check(Sym.TANTQUE)||reader.check(Sym.POUR)
    	||reader.check(Sym.MODE)||reader.check(Sym.EOF)||reader.check(Sym.FIN)){
			return new If(exp, inst);
    	} else {
			dp.p_cp.wrongMessage("Erreur InstructionSuite dans le fichier");
			throw new Exception();
    	}
    }


    public BlocInst nontermBlocInst() throws Exception {
    	if(reader.check(Sym.VARIABLE)||reader.check(Sym.DEBUT)||
    	reader.check(Sym.AVANCE)||reader.check(Sym.TOURNE)||
    	reader.check(Sym.HAUT)||reader.check(Sym.BAS)
    	||reader.check(Sym.COULEUR)||reader.check(Sym.EPAISSEUR)
    	||reader.check(Sym.SI)||reader.check(Sym.TANTQUE)
    	||reader.check(Sym.POUR)||reader.check(Sym.MODE)){
			Instruction inst = nontermInst();
			BlocInst bloc = nontermBlocInst();
			return new BlocInst(inst, bloc);
    	} else {
    		if(reader.check(Sym.FIN)){
				return null;
			} else {
				dp.p_cp.wrongMessage("Erreur BlocInst dans le fichier");
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
    		dp.p_cp.wrongMessage("Erreur Expression dans le fichier");
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
			dp.p_cp.wrongMessage("Erreur ExpresionSuite dans le fichier");
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
    		dp.p_cp.wrongMessage("Erreur Opérateur dans le fichier");
			throw new Exception();
    	}
    }
    
    public void term(Sym symbol) throws Exception {
		try{
			reader.eat(symbol);
		}catch(ReadException e){
			dp.p_cp.wrongMessage("Erreur "+e.getExpected()+" non reconnu dans le fichier");
			throw new ParserException("attendu \""+e.getExpected()+"\" trouvé \""+e.getFound()+"\"");
		}
	}
}
