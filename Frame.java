/* 
 * Creation : 11 avr. 2015
 */

import java.awt.BorderLayout;
import javax.swing.JFrame;



/**
 * @date    11 avr. 2015
 * @author  Anthony CHAFFOT
 * @author  Jessica FAVIN
 */
public class Frame extends JFrame{
    ChatPanel p_chat;
    DrawPanel p_draw;
    HeadBar   p_hb;
    
    //**************************************************************************
    // CONSTRUCTOR
    //**************************************************************************
    public Frame(){
        this.pack();
        this.setSize(1200, 700);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("TurtleDraw");
        this.getContentPane().setLayout(new BorderLayout());
        this.setVisible(true);
        this.initComponents();
        this.addEachComponents();
        this.validate();
    }

    //**************************************************************************
    // METHODS
    //**************************************************************************
    private void initComponents(){
        p_hb   = new HeadBar();
        p_draw = new DrawPanel(p_hb);
        p_chat = new ChatPanel(p_draw);
        p_draw.setChat(p_chat);
        p_hb.setChat(p_chat);
    }
    
    private void addEachComponents(){
        this.getContentPane().add(p_draw.scroll, BorderLayout.CENTER);
        this.getContentPane().add(p_chat, BorderLayout.EAST);
        this.getContentPane().add(p_hb, BorderLayout.NORTH);
    }
    //**************************************************************************
    // SETTERS / GETTERS
    //**************************************************************************

}
