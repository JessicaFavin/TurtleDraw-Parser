/* 
 * Creation : 11 févr. 2015
 * Project Computer Science L2 Semester 4 - TurtleDraw
 */

import java.io.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;




/**
 * <h1></h1>
 * 
 * 
 * @date    Avril 11. 2015
 * @author  Anthony CHAFFOT
 * @author  Jessica FAVIN
 */
public class ChatPanel extends JPanel {
    private     JPanel          	p_sentence;
    private     JPanel          	p_north;
    private     JTextField      	tf_sentence;
    private     JTextPane       	tp_chat;
    private     JScrollPane     	sp_scroll;
    private     StyledDocument  	sd_chat;
    private     JPanel          	p_correct;
    private     JLabel          	l_correct;
    private		ValueEnvironment 	env ;
    private 	String				beginning;
    private		int					inBloc 		= 0;
    
    public     DrawPanel       	dp;
    Icon icon = new ImageIcon("src/turtledraw/checked.png");
    Icon icon2 = new ImageIcon("src/turtledraw/cross.png");
    

    //**************************************************************************
    // CONSTRUCTOR
    //**************************************************************************
    /**
     * Constructor of the ChatPanel
     */
    public ChatPanel(DrawPanel dp) {
        this.dp = dp;
        this.initComponents();
        this.setSizes();
        this.addEachComponents();
        this.setActions();
    }
    
    //**************************************************************************
    // METHODS
    //**************************************************************************
    /**
     * Set the size of the chat
     */
    private void setSizes(){
        tf_sentence.setPreferredSize(new Dimension(250, 25));
    }
    
    /**
     * Init all components
     */
    private void initComponents() {
        p_sentence          = new JPanel();
        p_north             = new JPanel();
        p_correct           = new JPanel();
        tf_sentence         = new JTextField();
        tp_chat             = new JTextPane();
        l_correct           = new JLabel("Commencez");
        sd_chat             = tp_chat.getStyledDocument();
        p_north             .setLayout(new BorderLayout());
        this                .setLayout(new BorderLayout());
        env 				= new ValueEnvironment();
        beginning 			= "";
        setupChat();
    }

    /**
     * Set the chat with the the scroll pane
     */
    private void setupChat() {
        sp_scroll = new JScrollPane(tp_chat);
        tp_chat.setEditable(false);
        sp_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        p_correct.setBackground(Color.LIGHT_GRAY);
        l_correct.setForeground(Color.BLACK);
        
    }

    /**
     * add each components to the panel
     */
    private void addEachComponents() {
        p_correct.add(l_correct);
        p_north.add(p_correct, BorderLayout.NORTH);
        p_north.add(sp_scroll, BorderLayout.CENTER);
        p_sentence.add(tf_sentence);
        this.add(p_north, BorderLayout.CENTER);
        this.add(p_sentence, BorderLayout.SOUTH);
    }

    public void printChatMessage(String string) {
        tp_chat.setText(string);
        tp_chat.setCaretPosition(tp_chat.getText().length());
    }

    public void correctMessage(String str){
        l_correct.setText(str);
        p_correct.setBackground(new Color(0, 160, 0));
        l_correct.setForeground(Color.WHITE);
    }
    
    public void wrongMessage(String str){
        l_correct.setText(str);
        p_correct.setBackground(Color.RED);
        l_correct.setForeground(Color.WHITE);
    }
    
    public void execParser(String str){
    	Reader reader = new StringReader(str);
		Lexer lexer = new Lexer(reader);
		try{
			LookAhead1 look = new LookAhead1(lexer);
		
			try{
				Parser parser = new Parser(look, env, dp);
				Instruction prog = parser.nontermCode();
				dp.repaint();
			} catch (Exception ex){
				wrongMessage("Erreur instruction");
			}
		} catch(Exception exp) {
			wrongMessage("Instruction non valide");
		}
    }
    
    public void execParserFile(String str){
    	
        File input = new File(str);
        try{
			Reader reader = new FileReader(input);
			
		     
			ValueEnvironment en = new ValueEnvironment();
			Lexer lexer = new Lexer(reader);
			LookAhead1 look = new LookAhead1(lexer);
			ParserFile parser = new ParserFile(look, en, dp);
			Program prog = parser.nontermCode();
			prog.run(en, dp);
			correctMessage("Fichier éxécuté");
		} catch(FileNotFoundException ex){
			wrongMessage("Fichier introuvable");
		} catch(Exception e){
			wrongMessage("Erreur dans le fichier");
		}
    }


    public void setActions() {
        tf_sentence.addKeyListener(
            new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_ENTER) {
                        String sentence = tf_sentence.getText();
                        tp_chat.setText(tp_chat.getText()+ " "+sentence+"\n");
                        tp_chat.setCaretPosition(tp_chat.getText().length());
                        tf_sentence.setText("");
                        
                        if(inBloc>0){
                        	if(sentence.startsWith("Fin")){
                        		beginning = beginning.concat(" ").concat(sentence);
                        		inBloc--;
                        		if(inBloc==0){
                        			System.out.println(beginning);
                        			execParser(beginning);
		                    		beginning="";
                        		}
                        		
		                    }else if (sentence.startsWith("Debut")){
		                    		beginning = beginning.concat(" ").concat(sentence);
		                    		inBloc++;
		                    		correctMessage("Début bloc d'instruction "+inBloc);
		                    } else {
		                    	beginning = beginning.concat(" ").concat(sentence);
			                	correctMessage("Instruction sauvegardée");
	                    	}
                        }else if (sentence.startsWith("Debut")){
                        	beginning = beginning.concat(" ").concat(sentence);
                        	inBloc++;
                        	correctMessage("Début bloc d'instruction "+inBloc);
                        } else {
                        	beginning = beginning.concat(" ").concat(sentence);
		                    execParser(beginning);
		                    beginning="";
						}
					}
                }

                public void keyReleased(KeyEvent e) {
                }

                public void keyTyped(KeyEvent e) {
                }
            }
        );
    }
    
}
