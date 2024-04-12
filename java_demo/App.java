package com.example;

import javax.swing.*;
import java.awt.*;

import org.atsign.client.api.AtClient;
import org.atsign.common.AtSign;
import org.atsign.common.KeyBuilders;
import org.atsign.common.Keys.SharedKey;

import static javax.swing.SwingConstants.CENTER;

public class App extends JPanel {

    // Strings which hold the atsign  values for the esp32, java app, and save
    private static String esp_sign = "@baboon12";
    private static String java_sign = "@sunny39";
    private static String save_sign = "@12underground";

    // Initialize the temperature_label as a private variable to be used throughout the class
    private JLabel temperature_label;

    // Class constructor
    public App() {

        // Set the layout of the panel to a GridLayout with 2 rows and 1 column
        setLayout(new GridLayout(2, 1, 20, 5));

        // Initialize a label with the location of the weather station
        JLabel location_label = new JLabel("Boston");

        // Set the font, color, and alignment of the location_label
        location_label.setFont(new Font("default", 0, 25));
        location_label.setForeground(Color.CYAN);
        location_label.setHorizontalAlignment(CENTER);

        // Initialize the temperature label with an example temperature
        temperature_label = new JLabel("14℃  57.2℉");

        // Set the font, color, and alignment of the temperature_label
        temperature_label.setFont(new Font("default", 0, 15));
        temperature_label.setForeground(Color.CYAN);
        temperature_label.setHorizontalAlignment(CENTER);

        // Add labels to the panel
        add(location_label);
        add(temperature_label);

        // Set background and transparency of the panel
        setBackground(null);
        setOpaque(false);
    }

    // Main function
    public static void main(String[] args) throws Exception {

        // Initialize the AtSigns with the atsign class variables
        AtSign java = new AtSign(java_sign);
        AtSign esp32 = new AtSign(esp_sign);
        AtSign save = new AtSign(save_sign);

        // Initialize the atclient
        AtClient atClient = AtClient.withRemoteSecondary("root.atsign.org:64", java);

        // Initialize the atkey
        SharedKey sharedKey = new KeyBuilders.SharedKeyBuilder(esp32, java).key("test").build();

        // Create the frame to place the elements within
        JFrame frame = new JFrame("Temperature");

        // Set the close operation, size and relative location of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);

        // Initialize panel with an instance of the App
        App panel = new App();

        // Create a new imagepanel
        ImagePanel image_panel =new ImagePanel();

        // Create a temperature graph
        TemperatureGraph temp_graph = new TemperatureGraph();

        // Set the layout of the image_panel as a GridLayout with 4 rows and 3 columns
        image_panel.setLayout(new GridLayout(4,3));

        // Pad the location of the panel with empty JLabels
        image_panel.add(new JLabel());
        image_panel.add(new JLabel());
        image_panel.add(new JLabel());
        image_panel.add(new JLabel());

        // Add the panel to the image_panel
        image_panel.add(panel);

        // Pad the location of the panel with empty JLabels
        image_panel.add(new JLabel());
        image_panel.add(new JLabel());

        // Add the temperature graph to the panel
        image_panel.add(temp_graph);

        // Pad the location of the panel with empty JLabels
        image_panel.add(new JLabel());

        // Set the content pane of the frame to the image_panel and set the frame to visible
        frame.setContentPane(image_panel);
        frame.setVisible(true);

        // Loop while the application is running
        while(true)
        {
            // Set value to the value retrieved from the sharedKey
            String value = atClient.get(sharedKey).get();

            // Split the values retrieved from the sharedKey
            String[] values = value.split("a");

            // Break off the last element of the values
            String last = (String)values[values.length - 1];

            // Initialize temperature_data as all of the elements of values except the last
            int[] temperature_data = new int[values.length];
            for(int i = 0; i < values.length; i++)
            {
                // When adding the temperature value to temperature_data also convert it to fahrenheit
                temperature_data[i] = (Integer.parseInt(values[i]) * (9/5)) + 32;
            }

            // Add the temperature data to the temp_graph
            temp_graph.addTemperatureData(temperature_data);

            // Update the displayed temp
            panel.update_temp(last);

            // Repaint the frame to refresh the temperature value and graph
            frame.repaint();

            // Wait to continue looping for 10 seconds
            Thread.sleep(10000);
        }
    }

    // Description: Update the temperature on the temperature_label
    // Precondition: temp contains the current temperature in celcius, and temperature_label points to the temperature label of the frame
    // Postcondition: The temperature in farenheit and celcius is updated on the frame
    private void update_temp(String temp)
    {
        int deg_f = ((Integer.parseInt(temp) * (9/5)) + 32);
        temperature_label.setText(deg_f + "℉ " + temp + "℃");
    }
}
