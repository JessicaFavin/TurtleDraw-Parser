/* 
 * Creation : 11 avr. 2015
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.text.DecimalFormat;

/**
 * @date 11 avr. 2015
 * @author Anthony CHAFFOT
 * @author  Jessica FAVIN
 */
public class DrawPanel extends JPanel {
    //Taille du canevas = 935*631

    JScrollPane scroll;
    private HeadBar p_hb;
    public ChatPanel p_cp;
    private Color color = Color.RED;
    private int currentX = 0;
    private int currentY = 0;
    private boolean canDraw = false;
    private boolean mode90 = false;
    private boolean erasing = true;
    private boolean save = false;
    private int size = 5;
    private double angle = -Math.PI/2;
    private ArrayList<Line> lines = new ArrayList<Line>();
	private int cmptSave = 0;
    int temp = 1;

    //**************************************************************************
    // CONSTRUCTOR
    //**************************************************************************
    public DrawPanel(HeadBar hb) {
        this.p_hb = hb;
        this.p_cp = null;
        scroll = new JScrollPane(this);
        this.setPreferredSize(new Dimension(800, 600));
        currentY = 631;
    }

    //**************************************************************************
    // METHODS
    //**************************************************************************
    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        //On parcourt notre collection de lignes
        for (Line l : this.lines) {
            g2d.setColor(l.getColor());
            g2d.setStroke(new BasicStroke(l.getSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(l.getPointA().x, l.getPointA().y, l.getPointB().x, l.getPointB().y);
        }
        
        if (!save) {
            g2d.setColor(color);
            g2d.drawOval(currentX - 5, currentY - 5, 10, 10);
            
            g2d.setColor(Color.BLACK);
            g2d.drawLine(currentX, currentY, (int) (10* Math.cos(angle)) + currentX, (int) (10* Math.sin(angle)) + currentY);
        }

    }


    //Efface le contenu
    public void erase() {
        this.erasing = true;
        this.lines = new ArrayList();
        repaint();
    }

    public boolean isIn(double length){
        double tempX = currentX;
        double tempY = currentY;
        tempX += (double) (length * Math.cos(angle));
        tempY += (double) (length * Math.sin(angle));

        if(tempY > 631 || tempY < 0 || tempX >935 || tempX < 0){
            return false;
        }
        return true;
    }

    public void move(double length) throws Exception {
        if(isIn(length)){
            if (canDraw) {
                drawLine(length);
                repaint();
            } else {
                currentX += (double) (length * Math.cos(angle));
                currentY += (double) (length * Math.sin(angle));
                repaint();
                p_cp.correctMessage("Avance de "+ (double)length);
            }
        }
        else{
            p_cp.wrongMessage("En dehors du canevas !");
            throw new Exception();
        }
        
    }

    public void drawLine(double length) {
        Point a = new Point(0, 0);
        Point b = new Point(0, 0);
        a.x = currentX;
        a.y = currentY;
        b.x = (int) (length * Math.cos(angle)) + a.x;
        b.y = (int) (length * Math.sin(angle)) + a.y;

        //System.out.print("Point A (" + a.x + "," + a.y + ")");
        //System.out.println("/ Point B (" + b.x + "," + b.y + ")");

        lines.add(new Line(a, b, size, color));

        currentX = b.x;
        currentY = b.y;
    }
    
    public void turnAngle(double deg) throws Exception {
    	if(mode90&&deg%90!=0){
    		p_cp.wrongMessage("Angle non multiple de 90");
    		throw new Exception();
    	} else {
    		DecimalFormat df = new DecimalFormat("0.00");
    		double rad = Math.toRadians(deg);
			angle -= rad;
			angle = angle % (2*Math.PI);
			if(-1*Math.toDegrees(angle)>=0){
				p_hb.l_angle.setText(df.format(-1*Math.toDegrees(angle)) + " deg");
			} else {
				p_hb.l_angle.setText(df.format(360-Math.toDegrees(angle)) + " deg");
			}
			
			p_cp.correctMessage("Tourné");
		    repaint();
        }
    }

    public void pickColor() {
        int rand = ((int) (Math.random() * (9 + 1 - 1)) + 1);

        switch (rand) {
            case 1:
                color = Color.BLACK;
                break;
            case 2:
                color = Color.CYAN;
                break;
            case 3:
                color = Color.GRAY;
                break;
            case 4:
                color = Color.GREEN;
                break;
            case 5:
                color = Color.MAGENTA;
                break;
            case 6:
                color = Color.ORANGE;
                break;
            case 7:
                color = Color.RED;
                break;
            case 8:
                color = Color.YELLOW;
                break;
            case 9:
                color = Color.BLUE;
                break;
            default:
                color = Color.BLUE;
                break;
        }
        p_hb.p_color.setBackground(color);
    }

    void takePicture(JPanel panel) {
        save = true;
        repaint();
        BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        panel.print(img.getGraphics());
        try {
            ImageIO.write(img, "png", new File("panel" + cmptSave + ".png"));
            p_cp.correctMessage("Image sauvegardée");
            cmptSave++;
            save = false;
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //**************************************************************************
    // SETTERS / GETTERS
    //**************************************************************************
    public void setChat(ChatPanel cp){
    	this.p_cp = cp;
    }
    
    public void setPointerColor(Color c) {
        this.color = c;
        p_hb.p_color.setBackground(c);
    }
    
    public void setMode(boolean b){
    	this.mode90 = b;
    	if(b){
        	p_hb.l_mode.setText("Activé");
        } else {
        	p_hb.l_mode.setText("Désactivé");
        }
    }

    public void setCanDraw(boolean b) {
        this.canDraw = b;
        if(b){
        	p_hb.l_status.setText("Bas");
        } else {
        	p_hb.l_status.setText("Haut");
        }
    }

    public void setSize(int x) {
        this.size = x;
        p_hb.l_taille.setText(String.valueOf(size) + " px");
        repaint();
    }
    
    public void setColor(int value) {
        //int rand = ((int) (Math.random() * (9 + 1 - 1)) + 1);

        switch (value) {
            case 1:
                color = Color.BLACK;
                break;
            case 2:
                color = Color.CYAN;
                break;
            case 3:
                color = Color.GRAY;
                break;
            case 4:
                color = Color.GREEN;
                break;
            case 5:
                color = Color.MAGENTA;
                break;
            case 6:
                color = Color.ORANGE;
                break;
            case 7:
                color = Color.RED;
                break;
            case 8:
                color = Color.YELLOW;
                break;
            case 9:
                color = Color.BLUE;
                break;
            default:
                color = Color.BLUE;
                break;
        }
        p_hb.p_color.setBackground(color);
        repaint();
    }

}
