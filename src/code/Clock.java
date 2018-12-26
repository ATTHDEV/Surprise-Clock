/*
    โปรเเกรมนี้เขียนขึ้นเพื่อมอบเป็นของขวัญวันเกิดให้กับเพื่อน
    มิได้เขียนขึ้นเพื่อเเสวงหาประโยชน์อื่นใด 
    อรรถวุฒิ พ่วงศิริ
*/
package code;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Clock extends JPanel implements Runnable{
    private int[] scale;
    private int rCeter,r,degree,lastX,lastY,WIDTH,HEIGHT,scaleImage,timeScale;
    private Image image[];
    private boolean effect,runThread,Capasity,isDrag;
    Dimension screenSize;
    public Clock(JFrame root) {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = screenSize.width;
        HEIGHT = screenSize.height;
        this.setPreferredSize(screenSize);
        this.setDoubleBuffered(true);
        this.setOpaque(false);
        this.scale = new int[12];
        this.r = 125;
        this.rCeter = 30;
        this.degree = 0;
        this.Capasity = false;
        this.effect = false;
        this.image = new Image[2];
        this.scaleImage = 0;
        this.timeScale = 0;
        this.isDrag = true;
        for (int i = 0; i < this.scale.length; i++) {
            this.scale[i] = i*30;
        }
        runThread = true;
        this.initComponents();
        this.initialMouse(root);
        Thread loop = new Thread(this);
        loop.start();
    }
    private void initComponents() {
        JLabel jLabel1 = new javax.swing.JLabel();
        JLabel jLabel2 = new javax.swing.JLabel();
        JLabel jLabel3 = new javax.swing.JLabel();
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/close.png")));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.exit(0);
            }
        });
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); 
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/minimize.png"))); 
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JFrame root = (JFrame)getTopLevelAncestor();
                root.setExtendedState(JFrame.ICONIFIED);
            }
        });
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); 
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/close.png"))); 
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Capasity = !Capasity;
                scaleImage=0;
                effect = !effect;
                
                Thread t = new Thread(() -> {
                    try {
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream ais = AudioSystem.getAudioInputStream(Clock.class.getResource("/res/bday.wav"));
                        clip.open(ais);
                        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        gainControl.setValue(-28.0f);
                        clip.start();
                        while(runThread){
                            Thread.sleep(30);
                            timeScale++;
                            if(timeScale>=600){
                                isDrag = true;
                                runThread = false;
                            }
                        }
                        clip.close();
                        isDrag = true;
                    }  catch (Exception e) {
                            e.printStackTrace();
                    }
                });
                if(effect){
                    timeScale = 0;
                    isDrag = false;
                    runThread = true;
                    t.start();
                }
                else {
                    runThread=false;
                    isDrag = true;
                }
            }
        });
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); 
        setLayout(null);
        jLabel1.setBounds(WIDTH/2+115,HEIGHT/2-250,30,30);
        jLabel2.setBounds(WIDTH/2+145,HEIGHT/2-230,30,30);
        jLabel3.setBounds(WIDTH/2-15,HEIGHT/2-15,30,30);
        add(jLabel1);
        add(jLabel2);
        add(jLabel3);
        image[0] = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/res/hbd5.gif"));
        image[1] = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/res/balloon.png"));
        
    }
    private void initialMouse(JFrame root){
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                lastX = e.getXOnScreen();
                lastY = e.getYOnScreen();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                if(isDrag){
                    setCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR)); 
                    int x = e.getXOnScreen();
                    int y = e.getYOnScreen();
                    root.setLocation(getLocationOnScreen().x + x - lastX,getLocationOnScreen().y + y - lastY);
                    lastX = x;
                    lastY = y;
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR)); 
            } 
        };
        this.addMouseListener(ma);
        this.addMouseMotionListener(ma);
    } 
    @Override
    public void paint(Graphics g) {
        super.paint(g); 
        // วาดตัวนาฬิกา
        Graphics2D dc = (Graphics2D)g;
        dc.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        int x = (int)(r*2 * Math.cos(Math.toRadians(this.degree)));
        int y = (int)(r*2 * Math.sin(Math.toRadians(this.degree)));
        this.degree++;
        RadialGradientPaint p = new RadialGradientPaint
        (
            new Point2D.Double(getWidth() / 2.0,getHeight() / 2.0),
            getWidth() / 2.0f,
            new float[] { 0.0f, 0.5f },
            new Color[] { 
                new Color(6, 76, 160,this.Capasity&&!isDrag?127:255),
                new Color(0.0f, 0.0f, 0.0f,this.Capasity&&!isDrag?0.8f:1.0f) 
            }
        );
        dc.setColor(Color.white);
        dc.setStroke(new BasicStroke(8));
        dc.drawOval(WIDTH/2-r*2,HEIGHT/2-r*2,r*4,r*4);
        dc.setPaint(p);
        dc.fillOval(WIDTH/2-r*2,HEIGHT/2-r*2,r*4,r*4);
        dc.setColor(Color.white);
        dc.setStroke(new BasicStroke(4));
        for (int i = 0; i < this.scale.length; i++) {
            int x2 = (int)(r*2 * Math.cos(Math.toRadians(this.scale[i])));
            int y2 = (int)(r*2 * Math.sin(Math.toRadians(this.scale[i])));
            int x3 = (int)((r*2-60) * Math.cos(Math.toRadians(this.scale[i])));
            int y3 = (int)((r*2-60) * Math.sin(Math.toRadians(this.scale[i])));
            dc.drawLine(WIDTH/2+x3, HEIGHT/2+y3,WIDTH/2+x2,HEIGHT/2+y2);
        }
        dc.fillOval(WIDTH/2-this.rCeter/2,HEIGHT/2-this.rCeter/2,this.rCeter,this.rCeter);
        GregorianCalendar gcalendar = new GregorianCalendar();
        dc.setStroke(new BasicStroke(12));
        // ดึงเวลาปัจจุบันจากระบบ
        float S = gcalendar.get(Calendar.SECOND);
        float M = gcalendar.get(Calendar.MINUTE);
        float H = gcalendar.get(Calendar.HOUR);
        // นำเวลาที่ได้มาคำนวนว่าจะวาดเข็มนาฬิกายังไง
        x = (int)((r*2-80) * Math.cos(Math.toRadians((H+M/60)*30-90)));
        y = (int)((r*2-80) * Math.sin(Math.toRadians((H+M/60)*30-90)));
        dc.drawLine(WIDTH/2, HEIGHT/2,WIDTH/2+x,HEIGHT/2+y);
        x = (int)((r*2-2) * Math.cos(Math.toRadians(M*6-90)));
        y = (int)((r*2-2) * Math.sin(Math.toRadians(M*6-90)));
        dc.drawLine(WIDTH/2, HEIGHT/2,WIDTH/2+x,HEIGHT/2+y);
        x = (int)((r*2-2) * Math.cos(Math.toRadians(S*6-90)));
        y = (int)((r*2-2) * Math.sin(Math.toRadians(S*6-90)));
        // วาดตัวเข็มนาฬิกา
        dc.setStroke(new BasicStroke(5));
        dc.setColor(Color.red);
        dc.drawLine(WIDTH/2, HEIGHT/2,WIDTH/2+x,HEIGHT/2+y);
        dc.fillOval(WIDTH/2-this.rCeter/4,HEIGHT/2-this.rCeter/4,this.rCeter/2,this.rCeter/2);
        if(this.effect){
            if(this.scaleImage<600)
            {
                this.Capasity = true;
                dc.drawImage(image[0],50,-500+this.scaleImage,374+50,this.scaleImage, 0, 0,374,500, this);
                dc.drawImage(image[1],WIDTH-400,HEIGHT-this.scaleImage,354+WIDTH-400,HEIGHT+579-this.scaleImage, 0, 0,354,579, this);
                this.scaleImage+=1;
            }
            else 
            {
                this.Capasity = false;
                dc.drawImage(image[0],50,-500+this.scaleImage,374+50,this.scaleImage, 0, 0,374,500, this);
                dc.drawImage(image[1],WIDTH-400,HEIGHT-this.scaleImage,354+WIDTH-400,HEIGHT+579-this.scaleImage, 0, 0,354,579, this);
            }
        }
    }
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(10);
                repaint();
            } catch (InterruptedException ex) {
                Logger.getLogger(Clock.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) { 
        JFrame root = new JFrame();
        root.add(new Clock(root));
        root.setUndecorated(true);
        root.pack();
        root.setLocationRelativeTo(null);
        root.setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root.toFront();
        root.setVisible(true);
    }
}