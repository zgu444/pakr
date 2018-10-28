package com.example.myfirstapp.sensors;

import android.hardware.Sensor;

import java.util.ArrayList;
import java.util.List;

public interface SensorAdaptor {

  public SensorCoordinate[] getSensors();

  public int getSize();

  public void refreshDistance();

}