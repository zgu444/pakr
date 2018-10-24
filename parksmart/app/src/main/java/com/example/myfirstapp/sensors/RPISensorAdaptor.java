//package com.example.myfirstapp.sensors;
//
//import com.example.myfirstapp.comm.SocketClient;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class RPISensorAdaptor implements SensorAdaptor{
//    public static final int PORT_NUMBER = 18500;
//
//    public final List<SensorCoordinate> sensors;
//    private final List<Integer> value_reading;
//    private final SocketClient socket_client;
//
//    public RPISensorAdaptor(){
//        sensors = new ArrayList<>();
//        value_reading = new ArrayList<>();
//        socket_client = new SocketClient(PORT_NUMBER);
//    }
//
//    /**
//     Get Distance in Centimeters
//     */
//    public int getVal(int index){
//        return value_reading.get(index);
//    }
//
//
//    public void refreshDistance(){
//        String reader = socket_client.writeToAndReadFromSocket("0");
//        parseReadings(reader);
//    }
//
//    private void parseReadings(String reader){
//        /*
//        To be implemented
//         */
//    }
//}