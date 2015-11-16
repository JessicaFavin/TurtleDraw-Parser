To lunch the program you can use the Makefile with the next command lines :
 - make all
 - make run

Some files which can be loaded named bad1 bad2 or good (1 2 3 4 5 6) are available to test faster.

To see more clearly what you can do : 

"("			{return new Token(Sym.LPAR);}
 ")"			{return new Token(Sym.RPAR);}
 "Var"			{return new Token(Sym.VAR);}
 "Debut"		{return new Token(Sym.DEBUT);}
 "Fin"			{return new Token(Sym.FIN);}
 "Hautpinceau"	{return new Token(Sym.HAUT);}
 "Baspinceau"	{return new Token(Sym.BAS);}
 "Tourne"		{return new Token(Sym.TOURNE);}
 "Avance"		{return new Token(Sym.AVANCE);}
 "Couleur"		{return new Token(Sym.COULEUR);}
 "Epaisseur"	{return new Token(Sym.EPAISSEUR);}
 "Si"			{return new Token(Sym.SI);}
 "Alors"		{return new Token(Sym.ALORS);}
 "Sinon"		{return new Token(Sym.SINON);}
 "Tant que"		{return new Token(Sym.TANTQUE);}
 "Faire"		{return new Token(Sym.FAIRE);}
 "Pour"			{return new Token(Sym.POUR);}
 "Tour"			{return new Token(Sym.TOURS);}
 "Tours"		{return new Token(Sym.TOURS);}
 "Mode"			{return new Token(Sym.MODE);}
 "Sauvegarde"	{return new Token(Sym.SAVE);}
 "="			{return new Token(Sym.EQ);}
 ";"			{return new Token(Sym.CONCAT);}
 "+"			{return new Token(Sym.PLUS);}
 "-"			{return new Token(Sym.MINUS);}
 "*"			{return new Token(Sym.TIMES);}
 "/"			{return new Token(Sym.DIV);}
 {variable}		{return new VarToken(Sym.VARIABLE, yytext());}
 {int}			{return new IntToken(Sym.INT, Double.parseDouble(yytext()));}

 And to go further you are please to look Gammaire.txt where the grammars are explained.