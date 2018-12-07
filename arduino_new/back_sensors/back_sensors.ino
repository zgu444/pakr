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

#define MEDIUM_CT 5
#define SONAR_NUM 6
#define MAX_DISTANCE 200
#define PING_INTERVAL 33 // Milliseconds between sensor pings (29ms is about the min to avoid cross-sensor echo).

char readings[ (SONAR_NUM+1) * 8 ];
unsigned long pingTimer[SONAR_NUM]; // Holds the times when the next ping should happen for each sensor.
int nxt_med[SONAR_NUM];
unsigned int cm[MEDIUM_CT][SONAR_NUM];         // Where the ping distances are stored.
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
  for (uint8_t i = 0; i < SONAR_NUM; i++){
    // Set the starting time for each sensor.
    if (i == 0) pingTimer[i] = millis() + 75;
    else pingTimer[i] = pingTimer[i - 1] + PING_INTERVAL;
    nxt_med[i] = 0;
  }
}

void loop() {
  for (uint8_t i = 0; i < SONAR_NUM; i++) { // Loop through all the sensors.
    if (millis() >= pingTimer[i]) {         // Is it this sensor's time to ping?
      pingTimer[i] += PING_INTERVAL * SONAR_NUM;  // Set next time this sensor will be pinged.
      if (i == 0 && currentSensor == SONAR_NUM - 1) oneSensorCycle(); // Sensor ping cycle complete, do something with the results.
      sonar[currentSensor].timer_stop();          // Make sure previous timer is canceled before starting a new ping (insurance).
      currentSensor = i;                          // Sensor being accessed.
      cm[nxt_med[currentSensor]][currentSensor] = 0;                      // Make distance zero in case there's no ping echo for this sensor.
      sonar[currentSensor].ping_timer(echoCheck); // Do the ping (processing continues, interrupt will call echoCheck to look for echo).
    }
  }
  // Other code that *DOESN'T* analyze ping results can go here.
}

void echoCheck() { // If ping received, set the sensor distance to array.
  unsigned int res;
  int idx = nxt_med[currentSensor];
  if (sonar[currentSensor].check_timer()){
    res = sonar[currentSensor].ping_result / US_ROUNDTRIP_CM;
    
    cm[idx][currentSensor] = (res == 0) ? MAX_DISTANCE : res;
    nxt_med[currentSensor] = (idx + 1) >= MEDIUM_CT ? 0 : idx + 1;
   
//  Serial.print(idx);
//  Serial.print(" ");
//  Serial.println(currentSensor); 
  }
}
unsigned int med(int i){
  unsigned int res;
  res = (cm[0][i] < cm[1][i]) ? cm[1][i] : cm[0][i];
  res = (res < cm[2][i]) ? cm[2][i] : res;
  unsigned int bk;
  bk = (cm[3][i] < cm[4][i]) ? cm[3][i] : cm[4][i];
  return (res < bk) ? res : bk;
}
void oneSensorCycle() { // Sensor ping cycle complete, do something with the results.
  // The following code would be replaced with your code that does something with the ping results.
    if (Serial.available() > 0){

     char inChar =  Serial.read();
      if (inChar == 'p') {
        sprintf(readings, "%u, %u, %u, %u, %u, %u", 
         med(0), med(1), med(2), med(3), med(4), med(5));
        while(Serial.available()) {Serial.read();}
        Serial.println(readings);
      }
    }
}








