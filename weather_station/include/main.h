#ifndef MAIN_H
#define MAIN_H

#include <Arduino.h>
#include <WiFiClientSecure.h>
#include <SPIFFS.h>
#include "at_client.h"
#include <utility>
#include <time.h>
#include <list>
#include <sstream>

using namespace std;

// Description: Saves a string to the esp32 atkey
// Pre: esp_sign and save_sign are initialized with the value of atsigns which have their .atkey files in the data folder. Wifi information is also defined in the constants.h file. str is defined with a value to send to the esp.
// Post: Wifi is disconected and the provided string is saved to the "keep" atkey of the esp32.
void send_to_esp(string str);

// Description: Saves a string to the store atkey
// Pre: esp_sign and save_sign are initialized with the value of atsigns which have their .atkey files in the data folder. Wifi information is also defined in the constants.h file. str is defined with a value to send to the store.
// Post: Wifi is disconected and the provided string is saved to the "keep" atkey of the store.
void send_to_store(string str);

// Description: Saves a string to the Java atkey
// Pre: esp_sign and java_sign are initialized with the value of atsigns which have their .atkey files in the data folder. Wifi information is also defined in the constants.h file. str is defined with a value to send to the Java app.
// Post: Wifi is disconected and the provided string is saved to the "test" atkey of the Java app.
void send_to_java(string str);

// Description: Retrieves a string from the store atkey
// Pre: esp_sign and save_sign are initialized with the value of atsigns which have their .atkey files in the data folder. Wifi information is also defined in the constants.h file.
// Post: Wifi is disconected and the the string retrieved from the store atkey is returned.
string read_from_store();

// Description: Updates the transfer list with the current temperature and shifts historical temperatures back one slot each hour
// Pre: The cur_temp is the current temperature recorded by the temperature sensor.
// Post: A list of strings with an updated current temperature and possibly shifted temperatures (every new hour) is returned.
list<string> get_transfer_list(std::__cxx11::string cur_temp);

// Description: Splits a string into an array based on a delimiter
// Pre: The string has a length greater than 0 and contains one or more delimiter, the delimiter is defined
// Post: A list of strings is returned where each element is one of the elements of the string
list<string> split(string str, char delimiter);

#endif