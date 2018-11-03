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

#define SENSOR_NUM 4
#define MAX_DISTANCE 200

char readings[ (SENSOR_NUM+1) * sizeof(int) ];

// NewPing setup of pins and maximum distance
NewPing sonar_0(TRIGGER_PIN_0, ECHO_PIN_0, MAX_DISTANCE); 
NewPing sonar_1(TRIGGER_PIN_1, ECHO_PIN_1, MAX_DISTANCE); 
NewPing sonar_2(TRIGGER_PIN_2, ECHO_PIN_2, MAX_DISTANCE); 
NewPing sonar_3(TRIGGER_PIN_3, ECHO_PIN_3, MAX_DISTANCE); 

void setup() {
  Wire.begin();
  Wire.beginTransmission(MPU);
  Wire.write(0x6B);
  Wire.write(0);
  Wire.endTransmission(true);
  Serial.begin(115200);
}
 
void loop() {
   unsigned int distance_0 = sonar_0.convert_cm(sonar_0.ping_median());
   unsigned int distance_1 = sonar_1.convert_cm(sonar_1.ping_median());
   unsigned int distance_2 = sonar_2.convert_cm(sonar_2.ping_median());
   unsigned int distance_3 = sonar_3.convert_cm(sonar_3.ping_median());

  Wire.beginTransmission(MPU);
  Wire.write(0x3B);
  Wire.endTransmission(false);
  Wire.requestFrom(MPU,14,true);
  
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
  AcX=(Wire.read()<<8|Wire.read()) + AcXoff;
  AcY=(Wire.read()<<8|Wire.read()) + AcYoff;
  AcZ=(Wire.read()<<8|Wire.read()) + AcYoff;
  
  //read gyro data
  GyX=(Wire.read()<<8|Wire.read()) + GyXoff;
  GyY=(Wire.read()<<8|Wire.read()) + GyYoff;
  GyZ=(Wire.read()<<8|Wire.read()) + GyZoff;
  
  //get pitch/roll
  getAngle(AcX,AcY,AcZ);

//  Serial.println(roll);
  signed int roll_d = (unsigned int) roll;
  
  // needs revision
  sprintf(readings, "%u, %u, %u, %u, %i", distance_0, distance_1, distance_2, distance_3, roll_d);
  
  if (stringComplete) {
    Serial.println(readings);
    stringComplete = false;
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

/*
  SerialEvent occurs whenever a new data comes in the hardware serial RX. This
  routine is run between each time loop() runs, so using delay inside loop can
  delay response. Multiple bytes of data may be available.
*/
void serialEvent() {
  while (Serial.available()) {
    // get the new byte:
    char inChar = (char)Serial.read();
    // if the incoming character is a newline, set a flag so the main loop can
    // do something about it:
    if (inChar == 'p') {
      stringComplete = true;
    }
  }
}
