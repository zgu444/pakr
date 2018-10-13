package com.example.myfirstapp.sensors;

public class DummySensorAdaptor implements SensorAdaptor {
  private final int x_coor, y_coor;
  private int const_distance;

  public DummySensorAdaptor(int x, int y, int distance){
    x_coor = x;
    y_coor = y;
    const_distance = distance;
  }

  @Override
  public int getDistance(){
    return const_distance;
  }

  @Override
  public int getX(){
    return x_coor;
  }

  @Override
  public int getY(){
    return y_coor;
  }
}