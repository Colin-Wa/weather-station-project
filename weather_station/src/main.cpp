#include <Arduino.h>
#include <WiFiClientSecure.h>
#include <SPIFFS.h>
#include "at_client.h"
#include <utility>
#include <time.h>
#include <list>
#include <sstream>

#include "main.h"
#include "constants.h"

#define THERM_PIN 32

using namespace std;

int last_hour = -1;

string last_get;

AtClient *at_client;
AtKey *at_key;

// Strings which signify the names of the AtSigns to be used

// AtSign used to send from the ESP32
const string esp_sign = "@baboon12";
// AtSign used to send from the Java app
const string java_sign = "@sunny39";
// A third AtSign to save to
const string save_sign = "@12underground";

// Ensure all three AtSign names above are saved to the same
// strings as they are in the Java app


void setup()
{

    // Set the baud rate of the ESP32
    Serial.begin(115200); 

    // Read the historical temperature values from the save_sign
    last_get = read_from_store();
}

void loop()
{ 
    int temp_val;
    int temp_dat;
    string value;
    list<string> list_of_previous;

    // Read the temperature data from the temperature sensor
    temp_val = analogRead(THERM_PIN);
    temp_dat = (140 * temp_val) / 1024;

    // Get the updated transfer list after adding the new temperature
    list_of_previous = get_transfer_list(to_string(temp_dat));

    // Place delimiters into the string of temperatures
    value = "";
    for (auto it = list_of_previous.begin(); it != list_of_previous.end(); it++)
    {
      value += *it + "a";
    }

    // Send the historical temperatures string to the Java app
    send_to_java(value);
    // Send the historical temperatures string to the save sign
    send_to_store(value);

    // Set last get (prevents fetching from the save sign every loop)
    last_get = value;

    // Wait 10 seconds
    delay(10000);
}

void send_to_esp(string str)
{
    // Ensure wifi is enabled
    WiFi.disconnect(false);

    // Initialize AtSigns
    const auto *save = new AtSign(save_sign);
    const auto *esp32 = new AtSign(esp_sign); 

    // Initialize atkeys
    const auto u_keys = keys_reader::read_keys(*save);

    // Initialize atclient
    auto u_at_client = new AtClient(*save, u_keys); 

    // Set the key value
    auto u_at_key = new AtKey("keep", save, esp32);

    // Authenticate the atclient
    u_at_client->pkam_authenticate(SSID, PASSWORD);

    // Write the value to the key value
    u_at_client->put_ak(*u_at_key, str);

    // Delete the objects created to save memory
    delete(save);
    delete(esp32);
    delete(u_at_key);
    delete(u_at_client);

    // Disable the wifi to free wifi card
    WiFi.disconnect(true);
}

void send_to_store(string str)
{
    // Ensure wifi is enabled
    WiFi.disconnect(false);

    // Initialize AtSigns
    const auto *esp32 = new AtSign(esp_sign); 
    const auto *save = new AtSign(save_sign);

    // Initialize atkeys
    const auto e_keys = keys_reader::read_keys(*esp32);

    // Initialize atclient
    auto e_at_client = new AtClient(*esp32, e_keys);

    // Set the key value
    auto e_at_key = new AtKey("keep", esp32, save);

    // Authenticate the atclient
    e_at_client->pkam_authenticate(SSID, PASSWORD);

    // Write the value to the key value
    e_at_client->put_ak(*e_at_key, str);

    // Delete the objects created to save memory
    delete(save);
    delete(esp32);
    delete(e_at_key);
    delete(e_at_client);

    // Disable the wifi to free wifi card
    WiFi.disconnect(true);
}

void send_to_java(string str)
{
    // Ensure wifi is enabled
    WiFi.disconnect(false);

    // Initialize AtSigns
    const auto *esp32 = new AtSign(esp_sign); 
    const auto *java = new AtSign(java_sign);

    // Initialize atkeys
    const auto e_keys = keys_reader::read_keys(*esp32);

    // Initialize atclient
    auto e_at_client = new AtClient(*esp32, e_keys); 

    // Set the key value
    auto e_at_key = new AtKey("test", esp32, java);

    // Authenticate the atclient
    e_at_client->pkam_authenticate(SSID, PASSWORD);

    // Write the value to the key value
    e_at_client->put_ak(*e_at_key, str);

    // Delete the objects created to save memory
    delete(java);
    delete(esp32);
    delete(e_at_key);
    delete(e_at_client);

    // Disable the wifi to free wifi card
    WiFi.disconnect(true);
}

string read_from_store()
{
    // Ensure wifi is enabled
    WiFi.disconnect(false);

    // Initialize AtSigns
    const auto *save = new AtSign(save_sign);
    const auto *esp32 = new AtSign(esp_sign); 

    // Initialize atkeys
    const auto u_keys = keys_reader::read_keys(*save);

    // Initialize atclient
    auto u_at_client = new AtClient(*save, u_keys);

    // Set the key value
    auto u_at_key = new AtKey("keep", esp32, save);

    // Authenticate the atclient
    u_at_client->pkam_authenticate(SSID, PASSWORD);

    // Get a string from the key value
    string output = u_at_client->get_ak(*u_at_key);

    // Delete the objects created to save memory
    delete(save);
    delete(esp32);
    delete(u_at_key);
    delete(u_at_client);

    // Disable the wifi to free wifi card
    WiFi.disconnect(true);

    // Return the output
    return output;
}

list<string> get_transfer_list(std::__cxx11::string cur_temp)
{
    list<string> values;
    time_t current_time;

    // Get the current hour
    current_time = time(0);
    tm *ltm = localtime(&current_time);
    int cur_hour = ltm->tm_hour;

    // If the current hour is different from the last hour
    // (an hour has passed)
    if(cur_hour != last_hour)
    {
        // Read the historical temperature string
        // and update the last_get variable
        string get_back = read_from_store();
        last_get = get_back;

        // Create an array out of the string in last_get
        values = split(last_get, 'a');

        // Remove the first temperature (before 8 hours ago)
        values.pop_front();

        // Add the current temperature to the end of the array
        values.push_back(cur_temp);

        // Set the last_hour to the current hour
        last_hour = cur_hour;
    }
    else
    {
        // Create an array out of the string in last_get
        values = split(last_get, 'a');

        // Update the last element of the array with the
        // current temperature
        values.pop_back();
        values.push_back(cur_temp);
    }

    // Ensure that the array is not longer than 9 elements
    while(values.size() > 9)
    {
        // Pop the first element
        values.pop_front();
    }

    // Return the values array
    return values;
}

list<string> split(string str, char delimiter)
{
    list<string> output;
    string temp;
    
    // Convert the string to a stringstream
    stringstream s_string(str);

    // While there are elements left in the stringstream
    while(getline(s_string, temp, delimiter))
    {
        // Push each element to the end of the array
        output.push_back(temp);
    }
    
    // Return the newly split array
    return output;
}