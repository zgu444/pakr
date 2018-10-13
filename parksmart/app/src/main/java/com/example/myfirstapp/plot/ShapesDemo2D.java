//package com.example.myfirstapp.plot;
//
//import java.awt.*;
//import java.awt.event.*;
//import java.awt.geom.*;
//import javax.swing.*;
//import java.util.*;
//
//public class ShapesDemo2D extends JApplet {
//
//    final static int maxCharHeight = 15;
//    final static int minFontSize = 6;
//
//    final static Color bg = Color.white;
//    final static Color fg = Color.black;
//    final static Color red = Color.red;
//    final static Color white = Color.white;
//
//    final static BasicStroke stroke = new BasicStroke(2.0f);
//    final static BasicStroke wideStroke = new BasicStroke(8.0f);
//
//    final static float dash1[] = {10.0f};
//    final static BasicStroke dashed = new BasicStroke(1.0f,
//                                                      BasicStroke.CAP_BUTT,
//                                                      BasicStroke.JOIN_MITER,
//                                                      10.0f, dash1, 0.0f);
//
//    private ArrayList<SensorAdaptor> left_sensors;
//
//    Dimension totalSize;
//    FontMetrics fontMetrics;
//
//
//    public void init() {
//        //Initialize drawing colors
//        setBackground(bg);
//        setForeground(fg);
//        left_sensors = new ArrayList();
//        // left_sensors.add(new DummySensorAdaptor(100, 100, 50));
//    }
//
//    FontMetrics pickFont(Graphics2D g2,
//                         String longString,
//                         int xSpace) {
//        boolean fontFits = false;
//        Font font = g2.getFont();
//        FontMetrics fontMetrics = g2.getFontMetrics();
//        int size = font.getSize();
//        String name = font.getName();
//        int style = font.getStyle();
//
//        while ( !fontFits ) {
//            if ( (fontMetrics.getHeight() <= maxCharHeight)
//                 && (fontMetrics.stringWidth(longString) <= xSpace) ) {
//                fontFits = true;
//            }
//            else {
//                if ( size <= minFontSize ) {
//                    fontFits = true;
//                }
//                else {
//                    g2.setFont(font = new Font(name,
//                                               style,
//                                               --size));
//                    fontMetrics = g2.getFontMetrics();
//                }
//            }
//        }
//
//        return fontMetrics;
//    }
//
//    public void paint(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
//        int gridWidth = d.width / 6;
//        int gridHeight = d.height / 2;
//
//        int x_center = d.width/2;
//        int y_center = d.height/2;
//        fontMetrics = pickFont(g2, "Filled and Stroked GeneralPath",
//                               gridWidth);
//
//        Color fg3D = Color.lightGray;
//
//        g2.setPaint(fg3D);
//        g2.draw3DRect(0, 0, d.width - 1, d.height - 1, true);
//        g2.draw3DRect(3, 3, d.width - 7, d.height - 7, false);
//        g2.setPaint(fg);
//
//        int rectWidth = 79;
//        int stringY = gridHeight - 3 - fontMetrics.getDescent();
//        int rectHeight = 127;
//
//
//        // draw the car
//        Path2D front = new Path2D.Double();
//        front.moveTo(x_center-rectWidth/2, y_center-rectHeight/2);
//        front.curveTo(x_center-rectWidth/2, y_center-rectHeight/2, x_center, y_center-rectHeight/2 - 20, x_center+rectWidth/2, y_center-rectHeight/2);
//        g2.draw(front);
//
//        Path2D back = new Path2D.Double();
//        back.moveTo(x_center-rectWidth/2, y_center+rectHeight/2);
//        back.curveTo(x_center-rectWidth/2, y_center+rectHeight/2, x_center, y_center+rectHeight/2 + 20, x_center+rectWidth/2, y_center+rectHeight/2);
//        g2.draw(back);
//
//        g2.draw(new Line2D.Double(x_center-rectWidth/2, y_center-rectHeight/2, x_center-rectWidth/2, y_center+rectHeight/2));
//        g2.draw(new Line2D.Double(x_center+rectWidth/2, y_center-rectHeight/2, x_center+rectWidth/2, y_center+rectHeight/2));
//
//        // Draw outline on the left side of based on (x,y) of a sensor and its distance feedback
//
//        g2.drawArc(x_center-rectWidth/2-40, y_center-rectHeight/2-40+10, 40*2, 40*2, 173, 15);
//        int x_arc = x_center-rectWidth/2;
//        int y_arc1 = y_center-rectHeight/2+10;
//        int y_arc2 = y_center-rectHeight/2+60;
//        int y_arc3 = y_center-rectHeight/2+120;
//        int dist1 = 40;
//        int dist2 = 30;
//        int dist3 = 50;
//        int x_1 = (int)(x_arc- dist1*(Math.sin(8)));
//        int y_1_low = (int)(y_arc1 - dist1*(Math.cos(8)));
//        int y_1_high = (int)(y_arc1 + dist1*(Math.cos(8)));
//
//        int x_2 = (int)(x_arc - dist2*(Math.sin(8)));
//        int y_2_high = (int)(y_arc2 + dist2*(Math.cos(8)));
//        int y_2_low = (int)(y_arc2 - dist2*(Math.cos(8)));
//
//
//        int x_3 = (int)(x_arc - dist3*(Math.sin(8)));
//        int y_3_high = (int)(y_arc3 + dist3*(Math.cos(8)));
//        int y_3_low = (int)(y_arc3 - dist3*(Math.cos(8)));
//
//        g2.draw(new Line2D.Double(x_1, y_1_low, x_2, y_2_high));
//        g2.draw(new Line2D.Double(x_2, y_2_low, x_3, y_3_high));
//        g2.drawArc(x_center-rectWidth/2-30, y_center-rectHeight/2-30+60, 30*2, 30*2, 173, 15);
//        g2.drawArc(x_center-rectWidth/2-50, y_center-rectHeight/2-50+120, 50*2, 50*2, 173, 15);
//        // for(int i = 0; i < left_sensors.size(); i++){
//        //     g2.drawArc(left_sensors.get(0)+dist, left_sensors);
//        // }
//
//    }
//
//    public static void main(String s[]) {
//        JFrame f = new JFrame("ShapesDemo2D");
//        f.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {System.exit(0);}
//        });
//        JApplet applet = new ShapesDemo2D();
//        f.getContentPane().add("Center", applet);
//        applet.init();
//        f.pack();
//        f.setSize(new Dimension(550,100));
//        f.setVisible(true);
//    }
//
//}