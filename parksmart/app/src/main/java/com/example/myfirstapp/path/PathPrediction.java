import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import java.lang.*;



public class PathPrediction extends JApplet {
    final static Color bg = Color.white;
    final static Color fg = Color.black;
    final static Color path = Color.red;

    final static BasicStroke stroke = new BasicStroke(2.0f);
    final static float dash1[] = {10.0f};
    final static BasicStroke dashed = new BasicStroke(1.0f, 
                                                      BasicStroke.CAP_BUTT, 
                                                      BasicStroke.JOIN_MITER, 
                                                      10.0f, dash1, 0.0f);
    Dimension totalSize;

    private int carWidth, carLength;
    private int wheelAngle;

    public PathPrediction(int width, int length, int angle) {
        //Initialize drawing parameters 
        carWidth = width;
        carLength = length;

        // Angle is measured from straight car position 
        // Positive to the right 
        // Negative to the left
        wheelAngle = angle;
    }

    public void init() {
        //Initialize background colors 
        setBackground(bg);
        setForeground(fg);
    }


    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int gridWidth = d.width / 6;
        int gridHeight = d.height / 2;

        int x_center = d.width/2;
        int y_center = d.height/2;

        g2.setPaint(fg);

        // draw the car
        Path2D front = new Path2D.Double();
        front.moveTo(x_center-carWidth/2, y_center-carLength/2);
        front.curveTo(x_center-carWidth/2, y_center-carLength/2, x_center, y_center-carLength/2 - 20, x_center+carWidth/2, y_center-carLength/2);
        g2.draw(front);

        Path2D back = new Path2D.Double();
        back.moveTo(x_center-carWidth/2, y_center+carLength/2);
        back.curveTo(x_center-carWidth/2, y_center+carLength/2, x_center, y_center+carLength/2 + 20, x_center+carWidth/2, y_center+carLength/2);
        g2.draw(back);

        g2.draw(new Line2D.Double(x_center-carWidth/2, y_center-carLength/2, x_center-carWidth/2, y_center+carLength/2));
        g2.draw(new Line2D.Double(x_center+carWidth/2, y_center-carLength/2, x_center+carWidth/2, y_center+carLength/2));

        // Draw outline on the left side of based on (x,y) of a sensor and its distance feedback 
        g2.setStroke(dashed);
        g2.setPaint(path);
        
        // If wheel turns, draw arcs
        if (wheelAngle != 0) {
            double tan = Math.tan(Math.toRadians(wheelAngle));
            int turn_radius = (int) Math.round(carLength/tan-carWidth/2);
            int turn_center_x = x_center+turn_radius;
            int turn_center_y = y_center+carLength/2;
    
            int upper_left_x_right = turn_center_x - turn_radius + carWidth/2;
            int upper_left_y_right = turn_center_y - turn_radius;
            int arcAngle_right = (int) Math.round(2*carLength/(2*turn_radius*Math.PI)*360);

            int upper_left_x_left = turn_center_x - turn_radius - carWidth/2;
            int upper_left_y_left = turn_center_y - turn_radius - carWidth;
            int arcAngle_left = (int) Math.round(2*carLength/(2*(turn_radius+carWidth)*Math.PI)*360);

            // Draw right curve
            g2.drawArc(upper_left_x_right, upper_left_y_right, 2*turn_radius, 2*turn_radius, 180, arcAngle_right);
    
            // Draw left curve 
            g2.drawArc(upper_left_x_left, upper_left_y_left, 2*(turn_radius+carWidth), 2*(turn_radius+carWidth), 180, arcAngle_left);
        }

        // If wheels didn't turn, draw straight lines
        else {
            g2.draw(new Line2D.Double(x_center-carWidth/2, y_center+carLength/2, x_center-carWidth/2, y_center+carLength*2));
            g2.draw(new Line2D.Double(x_center+carWidth/2, y_center+carLength/2, x_center+carWidth/2, y_center+carLength*2));
        }
        
    }

    public static void main(String s[]) {
        JFrame f = new JFrame("PathPrediction");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        JApplet applet = new PathPrediction(40,60,20);

        f.getContentPane().add("Center", applet);
        applet.init();
        f.pack();
        f.setSize(new Dimension(550,100));
        f.setVisible(true);
    }

}