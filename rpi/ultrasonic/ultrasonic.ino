#include <NewPing.h>
 
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

// NewPing setup of pins and maximum distance
NewPing sonar_0(TRIGGER_PIN_0, ECHO_PIN_0, MAX_DISTANCE); 
NewPing sonar_1(TRIGGER_PIN_1, ECHO_PIN_1, MAX_DISTANCE); 
NewPing sonar_2(TRIGGER_PIN_2, ECHO_PIN_2, MAX_DISTANCE); 
NewPing sonar_3(TRIGGER_PIN_3, ECHO_PIN_3, MAX_DISTANCE); 

void setup() {
   Serial.begin(9600);
}
 
void loop() {
   delay(200);
   unsigned int distance_0 = sonar_0.convert_cm(sonar_0.ping_median());
   unsigned int distance_1 = sonar_1.convert_cm(sonar_1.ping_median());
   unsigned int distance_2 = sonar_2.convert_cm(sonar_2.ping_median());
   unsigned int distance_3 = sonar_3.convert_cm(sonar_3.ping_median());

//   Serial.print("Sensor 0\n");
//   Serial.print(distance_0);
//   Serial.println("cm");
//
//   Serial.print("Sensor 1\n");
//   Serial.print(distance_1);
//   Serial.println("cm");
//
//   Serial.print("Sensor 2\n");
//   Serial.print(distance_2);
//   Serial.println("cm");
//
//   Serial.print("Sensor 3\n");
//   Serial.print(distance_3);
//   Serial.println("cm");

     char *readings = (char*)malloc((SENSOR_NUM+1) * sizeof(int));
     sprintf(readings, "%u, %u, %u, %u", distance_0, distance_1, distance_2, distance_3);
     Serial.println(readings);
}
