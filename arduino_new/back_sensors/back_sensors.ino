// Transmits data from the 5 back sensors 
// to RPi
// Numbering of sensors follow the diagram
#include <NewPing.h>

#define TRIGGER_PIN_4 11
#define ECHO_PIN_4 12
#define TRIGGER_PIN_5 10
#define ECHO_PIN_5 13
#define TRIGGER_PIN_6 9
#define ECHO_PIN_6 8
#define TRIGGER_PIN_7 6 
#define ECHO_PIN_7 7
#define TRIGGER_PIN_8 5 
#define ECHO_PIN_8 4
#define TRIGGER_PIN_9 3
#define ECHO_PIN_9 2

#define SONAR_NUM 6
#define MAX_DISTANCE 200
#define PING_INTERVAL 33 // Milliseconds between sensor pings (29ms is about the min to avoid cross-sensor echo).

char readings[ (SONAR_NUM+1) * 8 ];
unsigned long pingTimer[SONAR_NUM]; // Holds the times when the next ping should happen for each sensor.
unsigned int cm[SONAR_NUM];         // Where the ping distances are stored.
uint8_t currentSensor = 0;          // Keeps track of which sensor is active.

NewPing sonar[SONAR_NUM] = { 
    // NewPing setup of pins and maximum distance
    NewPing(TRIGGER_PIN_4, ECHO_PIN_4, MAX_DISTANCE),
    NewPing(TRIGGER_PIN_5, ECHO_PIN_5, MAX_DISTANCE), 
    NewPing(TRIGGER_PIN_6, ECHO_PIN_6, MAX_DISTANCE), 
    NewPing(TRIGGER_PIN_7, ECHO_PIN_7, MAX_DISTANCE),
    NewPing(TRIGGER_PIN_8, ECHO_PIN_8, MAX_DISTANCE),
    NewPing(TRIGGER_PIN_9, ECHO_PIN_9, MAX_DISTANCE)
};

void setup() {
  Serial.begin(115200);
  pingTimer[0] = millis() + 75;           // First ping starts at 75ms, gives time for the Arduino to chill before starting.
  for (uint8_t i = 1; i < SONAR_NUM; i++) // Set the starting time for each sensor.
    pingTimer[i] = pingTimer[i - 1] + PING_INTERVAL;
}

void loop() {
  for (uint8_t i = 0; i < SONAR_NUM; i++) { // Loop through all the sensors.
    if (millis() >= pingTimer[i]) {         // Is it this sensor's time to ping?
      pingTimer[i] += PING_INTERVAL * SONAR_NUM;  // Set next time this sensor will be pinged.
      if (i == 0 && currentSensor == SONAR_NUM - 1) oneSensorCycle(); // Sensor ping cycle complete, do something with the results.
      sonar[currentSensor].timer_stop();          // Make sure previous timer is canceled before starting a new ping (insurance).
      currentSensor = i;                          // Sensor being accessed.
      cm[currentSensor] = 0;                      // Make distance zero in case there's no ping echo for this sensor.
      sonar[currentSensor].ping_timer(echoCheck); // Do the ping (processing continues, interrupt will call echoCheck to look for echo).
    }
  }
  // Other code that *DOESN'T* analyze ping results can go here.
}

void echoCheck() { // If ping received, set the sensor distance to array.
  if (sonar[currentSensor].check_timer())
    cm[currentSensor] = sonar[currentSensor].ping_result / US_ROUNDTRIP_CM;
}

void oneSensorCycle() { // Sensor ping cycle complete, do something with the results.
  // The following code would be replaced with your code that does something with the ping results.
    if (Serial.available() > 0){

     char inChar =  Serial.read();
  //   Serial.println("????");
  for (int i = 0; i < SONAR_NUM; i++){
    if (cm[i] == 0) cm[i] = MAX_DISTANCE;
  }
      if (inChar == 'p') {
        sprintf(readings, "%u, %u, %u, %u, %u, %u", 
        cm[0], cm[1], cm[2], cm[3], cm[4], cm[5]);
        while(Serial.available()) {Serial.read();}
        Serial.println(readings);
//        Serial.println("Here\n");
      }
    }
}








