// Transmits data from the 4 front ultrasonic sensors
// and the 1 gyroscope to RPi
// Numbering of sensors followed the hand-drawn diagram

#include <NewPing.h>
#include<Wire.h>
#include <math.h>

const int MPU=0x68;
int16_t AcX,AcY,AcZ,Tmp,GyX,GyY,GyZ;
double pitch,roll,yaw;

char readings[32];

void setup() {
  Wire.begin();
  Wire.beginTransmission(MPU);
  Wire.write(0x6B);
  Wire.write(0);
  Wire.endTransmission(true);
    
  Serial.begin(115200);
}
 
void loop() {
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

void oneSensorCycle() { // Sensor ping cycle complete, do something with the results.
  // The following code would be replaced with your code that does something with the ping results.
    if (Serial.available() > 0){
    signed int roll_d = (unsigned int) roll;
     char inChar =  Serial.read();
      if (inChar == 'p') {
        sprintf(readings, "%u", roll_d);
        while(Serial.available()) {Serial.read();}
        Serial.println(readings);
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

