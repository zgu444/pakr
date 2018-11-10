// Transmits data from the 4 front ultrasonic sensors
// and the 1 gyroscope to RPi
// Numbering of sensors followed the hand-drawn diagram

#include <NewPing.h>
#include<Wire.h>
#include <math.h>

const int MPU=0x68;
int16_t AcX,AcY,AcZ,Tmp,GyX,GyY,GyZ;
double pitch,roll,yaw;

#define TRIGGER_PIN_0 11
#define ECHO_PIN_0 12
#define TRIGGER_PIN_1 10
#define ECHO_PIN_1 13
#define TRIGGER_PIN_2 9
#define ECHO_PIN_2 8
#define TRIGGER_PIN_3 6 
#define ECHO_PIN_3 7

#define SONAR_NUM 4
#define MAX_DISTANCE 200
#define PING_INTERVAL 33 // Milliseconds between sensor pings (29ms is about the min to avoid cross-sensor echo).

char readings[ (SONAR_NUM+1) * 8 ];
unsigned long pingTimer[SONAR_NUM]; // Holds the times when the next ping should happen for each sensor.
unsigned int cm[SONAR_NUM];         // Where the ping distances are stored.
uint8_t currentSensor = 0;          // Keeps track of which sensor is active.

NewPing sonar[SONAR_NUM] = { 
// NewPing setup of pins and maximum distance
NewPing (TRIGGER_PIN_0, ECHO_PIN_0, MAX_DISTANCE),
NewPing (TRIGGER_PIN_1, ECHO_PIN_1, MAX_DISTANCE), 
NewPing (TRIGGER_PIN_2, ECHO_PIN_2, MAX_DISTANCE), 
NewPing (TRIGGER_PIN_3, ECHO_PIN_3, MAX_DISTANCE) 
};

void setup() {
  Wire.begin();
  Wire.beginTransmission(MPU);
  Wire.write(0x6B);
  Wire.write(0);
  Wire.endTransmission(true);

  pingTimer[0] = millis() + 75;           // First ping starts at 75ms, gives time for the Arduino to chill before starting.
  for (uint8_t i = 1; i < SONAR_NUM; i++) // Set the starting time for each sensor.
  pingTimer[i] = pingTimer[i - 1] + PING_INTERVAL;
    
  Serial.begin(115200);
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
  Wire.begin();
  Wire.beginTransmission(MPU);
  Wire.write(0x3B);
  int res = Wire.endTransmission(false);
  if  (res == 0) Wire.requestFrom(MPU,14,true);
  
  int AcXoff,AcYoff,AcZoff,GyXoff,GyYoff,GyZoff;
  int temp,toff;
  double t,tx,tf;
  
  //Acceleration data correction
  AcXoff = -950;
  AcYoff = -300;
  AcZoff = 0;
  
  //Gyro correction
  GyXoff = 480;
  GyYoff = 170;
  GyZoff = 210;
  
  //read accel data
  if (Wire.available() ) AcX=(Wire.read()<<8|Wire.read()) + AcXoff;
  if (Wire.available() ) AcY=(Wire.read()<<8|Wire.read()) + AcYoff;
  if (Wire.available() ) AcZ=(Wire.read()<<8|Wire.read()) + AcYoff;
  
  //read gyro data
  if (Wire.available() ) GyX=(Wire.read()<<8|Wire.read()) + GyXoff;
  if (Wire.available() ) GyY=(Wire.read()<<8|Wire.read()) + GyYoff;
  if (Wire.available() ) GyZ=(Wire.read()<<8|Wire.read()) + GyZoff;
  
  //get pitch/roll
  getAngle(AcX,AcY,AcZ);
}

void echoCheck() { // If ping received, set the sensor distance to array.
  if (sonar[currentSensor].check_timer())
    cm[currentSensor] = sonar[currentSensor].ping_result / US_ROUNDTRIP_CM;
}

void oneSensorCycle() { // Sensor ping cycle complete, do something with the results.
  // The following code would be replaced with your code that does something with the ping results.
    if (Serial.available() > 0){
    signed int roll_d = (unsigned int) roll;
  for (int i = 0; i < SONAR_NUM; i++){
    if (cm[i] == 0) cm[i] = MAX_DISTANCE;
  }
     char inChar =  Serial.read();
  //   Serial.println("????");
      if (inChar == 'p') {
        sprintf(readings, "%u, %u, %u, %u, %i", 
        cm[0], cm[1], cm[2], cm[3], roll_d);
        while(Serial.available()) {Serial.read();}
        Serial.println(readings);
//        Serial.println("Here\n");
      }
    }
}

//convert the accel data to pitch/roll
void getAngle(int Vx,int Vy,int Vz) {
  double x = Vx;
  double y = Vy;
  double z = Vz;

  pitch = atan(x/sqrt((y*y) + (z*z)));
  roll = atan(y/sqrt((x*x) + (z*z)));
  yaw = atan(z/sqrt((x*x) + (z*z)));

  //convert radians into degrees
  pitch = pitch * (180.0/PI);
  roll = roll * (180.0/PI) ;
  yaw = yaw * (180/0/PI);
} 

