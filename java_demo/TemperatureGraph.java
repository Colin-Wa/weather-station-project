package com.example;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import java.util.Calendar;

public class TemperatureGraph extends JPanel {
    // Initialize the width and height of the graph as well as gap size
    private final int WIDTH = 300;
    private final int HEIGHT = 125;
    private final int BORDER_GAP = 30;
    private final int GRAPH_WIDTH = WIDTH;
    private final int GRAPH_HEIGHT = HEIGHT;

    // Initialize the bounds of the graph
    private final int MAX_TEMP = 100;
    private final int MIN_TEMP = 0;
    private final int MAX_DATA_POINTS = 8;

    private int[] temperatureData;
    private int dataPointCount;

    // Class constructor
    public TemperatureGraph() {
        // Initialize the temperatureData array and dataPointCount
        temperatureData = new int[MAX_DATA_POINTS];
        dataPointCount = 0;
    }

    // Description: Adds the temperature data passed into the function to the graph
    // Precondition: the temperatures array is defined with historical temperatures and temperatureData is initialized
    // Postcondition: The temperatureData is updated, dataPointCount is updated and the graph is repainted to update the values.
    public void addTemperatureData(int[] temperatures) {
        
        temperatureData = temperatures;
        dataPointCount = temperatureData.length;
        repaint();
    }

    // Description: Paints the graph
    // Precondition: temperatureData and dataPointCount is initialized, as well as all graph bounding variables and graph size variables.
    // Postcondition: A graph is drawn on the TemperatureGraph graph
    @Override
    public void paintComponent(Graphics g) {

        // Get the unadjusted current hour of the day
        Calendar cur = Calendar.getInstance();
        int hour = cur.get(Calendar.HOUR_OF_DAY);

        // Paint onto the JPanel
        super.paintComponent(g);

        // Draw graph border
        g.setColor(Color.BLACK);
        g.drawRect(BORDER_GAP, BORDER_GAP, GRAPH_WIDTH, GRAPH_HEIGHT);

        // Draw y-axis labels
        g.drawString(MAX_TEMP + "", BORDER_GAP - 20, BORDER_GAP + 10);
        g.drawString(MIN_TEMP + "", BORDER_GAP - 20, BORDER_GAP + GRAPH_HEIGHT);
        
        // draw x-axis labels
        int xInterval = GRAPH_WIDTH / MAX_DATA_POINTS;
        // For every data point
        for (int i = 0; i < MAX_DATA_POINTS; i++) {
            // Adjust the current hour to the local time zone
            int cur_hour = (hour) - (MAX_DATA_POINTS - (i + 1));
            // Initialize the suffix to am
            String suffix = "am";

            // If the cur_hour is more than 12
            if(cur_hour > 12)
            {
                // Change the suffic to pm
                suffix = "pm";
                // Set the 24 hour clock to a 12 hour clock
                cur_hour = cur_hour - 12;
            }

            // If the cur_hour is less than or equal to 0
            if(cur_hour <= 0)
            {
                // Subtract the hour from 12
                cur_hour = 12 - cur_hour;
            }

            // Draw the axis label for each hour
            g.drawString(cur_hour + suffix + "", BORDER_GAP + i * xInterval, BORDER_GAP + GRAPH_HEIGHT + 20);
        }

        // Draw temperature data points
        
        // Set the drawing color to red
        g.setColor(Color.RED);
        
        // Place a point for each of the dataPoints
        int yInterval = GRAPH_HEIGHT / (MAX_TEMP - MIN_TEMP);
        for (int i = 0; i < dataPointCount; i++) {
            int x = BORDER_GAP + i * xInterval;
            int y = BORDER_GAP + GRAPH_HEIGHT - (temperatureData[i] - MIN_TEMP) * yInterval;
            g.fillOval(x - 3, y - 3, 6, 6);
        }

        // Draw lines to connect temperature data points
        if (dataPointCount > 1) {
            for (int i = 0; i < dataPointCount - 1; i++) {
                int x1 = BORDER_GAP + i * xInterval;
                int y1 = BORDER_GAP + GRAPH_HEIGHT - (temperatureData[i] - MIN_TEMP) * yInterval;
                int x2 = BORDER_GAP + (i + 1) * xInterval;
                int y2 = BORDER_GAP + GRAPH_HEIGHT - (temperatureData[i + 1] - MIN_TEMP) * yInterval;
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }
}