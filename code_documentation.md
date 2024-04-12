# Code Documentation
[*back to readme*](./README.md)
## **ESP32**
### [main.cpp](./weather_station/src/main.cpp) Functions
___
#### **void setup()**
Description: Setup the ESP32

Parameters: None

Return: Nothing is returned, but the side effects include setting the ESP32 baud rate, and initializing a variable with a string of the previous temperatures.
___
#### **void loop()**
Description: Take the temperature every 10 seconds

Parameters: None

Return: Nothing is returned, but the side effects include sending data to the Java app and save key.
___
#### **void send_to_esp(string str);**
Description: Saves a string to the esp32 atkey

Parameters: Takes a string which is defined with a value to send to the esp.

Return: Nothing is returned, but the side effects include disconnecting from wifi and the provided string is saved to the "keep" atkey of the esp32.
___
#### **void send_to_store(string str);**

Description: Saves a string to the store atkey

Parameters: Takes a string which is defined with a value to send to the store.

Return: Nothing is returned, but the side effects include disconnecting from wifi and the provided string is saved to the "keep" atkey of the store.
___
#### **void send_to_java(string str);**

Description: Saves a string to the Java atkey

Parameters: Takes a string which is defined with a value to send to the Java app.

Return: Nothing is returned, but the side effects include disconnecting from wifi and the provided string is saved to the "test" atkey of the Java app.
___
#### **string read_from_store();**
Description: Retrieves a string from the store atkey

Parameters: None

Return: A string retrieved from the store atkey is returned.
___
#### **list<string> get_transfer_list(std::__cxx11::string cur_temp);**
Description: Updates the transfer list with the current temperature and shifts historical temperatures back one slot each hour

Parameters: Takes a string cur_temp which is the current temperature recorded by the temperature sensor.

Return: Returns a list of strings with an updated current temperature and possibly shifted temperatures (every new hour) is returned.
___
#### **list<string> split(string str, char delimiter);**
Description: Splits a string into an array based on a delimiter

Parameters: Takes a string with a length greater than 0 and contains one or more delimiter. Also takes a character which is used to seperate the elements of the string called the delimiter.

Return: Returns a list of strings where each element is one of the elements of the input string
___

## **Java Application**
### [App.java](./java_demo/demo/src/main/java/com/example/App.java) Functions
___
#### **public App()**
Description: Class constructor

Parameters: None

Return: Nothing is returned, but the side effects include initializing the application frame with a background image, weather data, and a graph.
___
#### **public static void main(String[] args)**
Description: Main function

Parameters: Takes in an array of arguments.

Return: Nothing is returned, but the side effects include looping over the actions of the application infinitely, collecting temperatures and displaying them on the frame.
___
#### **private void update_temp(String temp)**
Description: Update the temperature displayed on the app.

Parameters: Takes in string which holds the current temperature.

Return: Nothing is returned, but the side effects include changing the temperature displayed on the app to the new temperature.
___
### [ImagePanel.java](./java_demo/demo/src/main/java/com/example/ImagePanel.java) Functions
___
#### **public ImagePanel()**

Description: Class constructor

Parameters: None

Return: Nothing is returned, but the side effects include updating the image variable to hold the background image.
___
#### **protected void paintComponent(Graphics g)**
Description: Overloads the paintComponent function to include a background image

Parameters: Takes a graphics object of the frame.

Return: Nothing is returned, but the side effects include displaying a background image on the ImagePanel object.
___
### [TemperatureGraph.java](./java_demo/demo/src/main/java/com/example/TemperatureGraph.java) Functions
___
#### **public TemperatureGraph()**
Description: Class constructor

Parameters: None

Return: Nothing is returned, but the side effects include initializing the temperatureData array and dataPointCount integer
___
#### **public void addTemperatureData(int[] temperatures)**
Description: Adds the temperature data passed into the function to the graph

Parameters: Takes a temperatures array which includes historical temperatures

Return: Nothing is returned, but the side effects include updating the temperatureData array with the new temperatures, and updating the dataPointCount variable with the new number of points.
___
#### **public void paintComponent(Graphics g)**
Description: Paints the graph

Parameters: Takes a graphics object of the frame.

Return: Nothing is returned, but the side effects include drawing data points on the TemperatureGraph graph