/* 
 * Creation : 14 avr. 2015
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;



/**
 * @date    14 avr. 2015
 * @author  Anthony CHAFFOT
 * @author  Jessica FAVIN
 */
public class HeadBar extends JPanel{
	private ChatPanel p_cp;
    public JPanel p_center = new JPanel();
    public JPanel p_left = new JPanel();
    public JPanel p_angle = new JPanel();
    public JPanel p_size = new JPanel();
    public JPanel p_invisible = new JPanel();
    public JPanel p_status = new JPanel();
    public JPanel p_mode = new JPanel();
    public JLabel angle = new JLabel("Angle : ");
    public JLabel taille = new JLabel(" Epaisseur :" );
    public JLabel l_taille = new JLabel("5 px");
    public JLabel l_angle = new JLabel("90.00");
    public JLabel mode = new JLabel("Mode 90° : ");
    public JLabel l_mode = new JLabel("Désactivé");
    public JPanel p_color = new JPanel();
    public JLabel status = new JLabel("Pinceau :");
    public JLabel l_status = new JLabel("Haut");
    public JButton b_UseFile = new JButton("Charger");
    public JButton b_reset = new JButton("Effacer");
    public JLabel l_logo = new JLabel(new ImageIcon("logoTurtleDraw.png"));
    public JLabel l_sign = new JLabel(new ImageIcon("signature.png"));
    
    //**************************************************************************
    // CONSTRUCTOR
    //**************************************************************************
    public HeadBar(){
        this.setLayout(new BorderLayout());
        p_color.setPreferredSize(new Dimension(40,26));
        p_invisible.setPreferredSize(new Dimension(1,32));
        p_color.setBackground(Color.red);
        p_angle.add(angle);
        p_angle.add(l_angle);
        
        angle.setForeground(Color.LIGHT_GRAY);
        l_angle.setForeground(Color.WHITE);
        
        p_size.add(taille);
        p_size.add(l_taille);
        
        taille.setForeground(Color.LIGHT_GRAY);
        l_taille.setForeground(Color.WHITE);
        
        p_status.add(status);
        p_status.add(l_status);
        
        status.setForeground(Color.LIGHT_GRAY);
        l_status.setForeground(Color.WHITE);
        
        p_mode.add(mode);
        p_mode.add(l_mode);
        
        mode.setForeground(Color.LIGHT_GRAY);
        l_mode.setForeground(Color.WHITE);
        
        p_angle.setBackground(Color.DARK_GRAY);
        p_size.setBackground(Color.DARK_GRAY);
        p_status.setBackground(Color.DARK_GRAY);
        p_mode.setBackground(Color.DARK_GRAY);
        
        b_UseFile.setPreferredSize(new Dimension(100, 26));
        b_reset.setPreferredSize(new Dimension(85, 26));
        
        p_center.add(p_invisible);
        p_center.add(p_color);
        p_center.add(p_size);
        p_center.add(p_angle);
        p_center.add(p_status);
        p_center.add(p_mode);
        p_center.add(b_UseFile);
        p_center.add(b_reset);
        setBtnActions();
        
        p_left.add(l_logo);
        this.add(p_left, BorderLayout.WEST);
        this.add(p_center, BorderLayout.CENTER);
        this.add(l_sign, BorderLayout.EAST);
        
        this.p_cp = null;
    }

    //**************************************************************************
    // METHODS
    //**************************************************************************
    
    public void setBtnActions() {
        b_UseFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String txt = new String();
                JFileChooser chooser = new JFileChooser();
                chooser.setPreferredSize(new java.awt.Dimension(700, 600));
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                	File selection = chooser.getSelectedFile();
        			txt = selection.getPath();
                    p_cp.execParserFile(txt);
        			
                }
            }
        });
        
        b_reset.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		p_cp.dp.erase();
        		p_cp.correctMessage("Effacé");
        	}
        });
    }

    //**************************************************************************
    // SETTERS / GETTERS
    //**************************************************************************
    
    public void setChat(ChatPanel cp){
    	this.p_cp = cp;
    }

}
