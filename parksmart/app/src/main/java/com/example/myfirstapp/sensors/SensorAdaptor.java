package com.example.myfirstapp.sensors;

import java.util.ArrayList;
import java.util.List;

public interface SensorAdaptor {
  /**
    Get Distance in Centimeters
  */
  public int getVal(int index);

  public SensorCoordinate getSensorCoordinate(int index);

  public int getSize();

  public void refreshDistance();

}