package org.example;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

public class RectanglePanel extends JPanel {

    private List<Rectangle> rectangles;
    private List<Rectangle> intersections;
    private boolean getIntersections = false;

    public RectanglePanel(List<Rectangle> rectangles, List<Rectangle> intersections) {
        this.rectangles = rectangles;
        this.intersections = intersections;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);

        Area combinedArea = new Area();
        for (Rectangle rect : rectangles) {
            combinedArea.add(new Area(rect));
        }

        if (getIntersections) {
            for (int i = 0; i < rectangles.size(); i++) {
                for (int j = i + 1; j < rectangles.size(); j++) {
                    if (rectangles.get(i).intersects(rectangles.get(j))) {
                        Rectangle intersectionRect = rectangles.get(i).intersection(rectangles.get(j));
                        intersections.add(intersectionRect);
                    }
                }
            }

            for (Rectangle intersection : intersections) {
                Area intersectionArea = new Area(intersection);
                combinedArea.subtract(intersectionArea);
            }

            for (Rectangle rect : rectangles) {
                g2d.setColor(Color.BLUE);
                g2d.draw(rect);
            }

            for (Rectangle intersection : intersections) {
                g2d.setColor(getBackground());
                g2d.draw(intersection);
            }
        } else {
            for (Rectangle rect : rectangles) {
                g2d.draw(rect);
            }
        }
    }

    public void setGetIntersections(boolean intersections) {
        this.getIntersections = intersections;
        repaint();
    }

    public static void main(String[] args) {
        List<Rectangle> rectangles = new ArrayList<>();
        List<Rectangle> intersections = new ArrayList<>();
        //rectangles.add(new Rectangle(50, 50, 100, 100));
        //rectangles.add(new Rectangle(80, 80, 120, 120));
        //rectangles.add(new Rectangle(200, 200, 100, 100));
        //rectangles.add(new Rectangle(20, 30, 200, 200));
        //rectangles.add(new Rectangle(90, 180, 35, 100));
        //rectangles.add(new Rectangle(10, 250, 35, 100));

        //rectangles.add(new Rectangle(600, 50, 80, 80));
        //rectangles.add(new Rectangle(700, 80, 90, 90));
        //rectangles.add(new Rectangle(800, 100, 60, 60));
        //rectangles.add(new Rectangle(850, 150, 75, 75));
        //rectangles.add(new Rectangle(750, 200, 122, 100));


        JFrame frame = new JFrame("Rectangles");
        RectanglePanel rectanglePanel = new RectanglePanel(rectangles, intersections);

        JButton showIntersectionsButton = new JButton("Get Intersections");

        JPanel inputPannel = new JPanel();

        JButton rectangleAddButton = new JButton("Rectangle Add");

        JLabel xPointLabel = new JLabel("Point(X): ");
        JLabel yPointLabel = new JLabel("Point(Y): ");
        JLabel widthLabel = new JLabel("Width: ");
        JLabel heightLabel = new JLabel("Height: ");

        JTextField xPointInput = new JTextField(20);
        JTextField yPointInput = new JTextField(20);
        JTextField widthInput = new JTextField(20);
        JTextField heightInput = new JTextField(20);

        rectangleAddButton.addActionListener(e -> {
            try{
                int xRectangle = Integer.parseInt(xPointInput.getText());
                int yRectangle = Integer.parseInt(yPointInput.getText());
                int rectangleWidth = Integer.parseInt(widthInput.getText());
                int rectangleHeight = Integer.parseInt(heightInput.getText());

                if(xRectangle < 0 || yRectangle < 0 || rectangleHeight < 1 || rectangleWidth < 1) {
                    JOptionPane.showMessageDialog(null, "Please enter positive integer numbers.");
                } else if(xRectangle > 900 || yRectangle > 900 || xRectangle + rectangleWidth > 900 || yRectangle + rectangleHeight > 900) {
                    JOptionPane.showMessageDialog(null, "Input out of frame, please do not exceed 900 pixels.");
                }else {
                    rectangles.add(new Rectangle(xRectangle, yRectangle, rectangleWidth, rectangleHeight));
                    rectanglePanel.repaint();
                }
            } catch(NumberFormatException exception) {
                JOptionPane.showMessageDialog(null, "Please provide positive integers for all rectangle information fields.");
            }
        });
        inputPannel.add(xPointLabel);
        inputPannel.add(xPointInput);

        inputPannel.add(yPointLabel);
        inputPannel.add(yPointInput);

        inputPannel.add(widthLabel);
        inputPannel.add(widthInput);

        inputPannel.add(heightLabel);
        inputPannel.add(heightInput);
        inputPannel.add(rectangleAddButton);

        showIntersectionsButton.addActionListener(e -> {
            long startTime = System.nanoTime();
            rectanglePanel.setGetIntersections(true);
            inputPannel.setVisible(false);
            long endTime = System.nanoTime();
            long computationTime = endTime - startTime;
            double computationTimeSeconds = computationTime / 1_000_000.0;
            System.out.println("Shape computed in: " + computationTimeSeconds + " ms");
            frame.repaint();
        });

        JPanel buttons = new JPanel();
        buttons.add(showIntersectionsButton);

        JPanel bottomPannel = new JPanel();
        bottomPannel.setLayout(new BoxLayout(bottomPannel, BoxLayout.Y_AXIS));

        bottomPannel.add(inputPannel);
        bottomPannel.add(buttons);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(rectanglePanel, BorderLayout.CENTER);
        frame.add(bottomPannel, BorderLayout.SOUTH);
        frame.setSize(1250, 1250);
        frame.setVisible(true);


    }
}
