/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.manuel.cronometros.control;

import javax.swing.JLabel;

/**
 *
 * @author dev_manuel
 */
public class ChronometerController extends Thread {

  private Integer control;

  private int hundredths;
  private int seconds;
  private int minutes;
  private int hours;

  private final JLabel lblCounter;
  private final JLabel lblDelay;
  private final Integer delay;
  private final Integer sleepTime;
  private boolean firstTime;

  private final StringBuilder timeFormmater;

  public ChronometerController(JLabel lblCounter, JLabel lblDelay,
    Integer delay, Integer control) {
    this.timeFormmater = new StringBuilder();
    this.lblCounter = lblCounter;
    this.lblDelay = lblDelay;
    this.hundredths = 0;
    this.seconds = 0;
    this.minutes = 0;
    this.hours = 0;
    this.delay = delay;
    this.control = control;
    this.lblDelay.setText((delay <= 0 ? "0.01" : delay) + " Seconds");
    this.sleepTime = delay <= 0 ? 10 : delay * 1000;
    this.firstTime = true;
  }

  @Override
  public void run() {
    try {
      while (true) {
        switch (control) {
          case 0: {
            Thread.currentThread().interrupt();
            return;
          }
          case 1: {
            updateChronometer();
            break;
          }
          default: {
            System.out.println("En espera");
            break;
          }
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace(System.err);
      Thread.currentThread().interrupt();
    }
  }

  private void updateChronometer() throws InterruptedException {
    if (delay == 0) {
      hundredths += 1;
    } else {
      detectFirstTime();
      hundredths = 0;
    }
    this.adjustHundredths();
    this.adjustSeconds();
    this.adjustMinutes();
    timeFormmater.delete(0, timeFormmater.length());
    timeFormmater.append(adjustTextNumber(hours))
      .append(":")
      .append(adjustTextNumber(minutes))
      .append(":")
      .append(adjustTextNumber(seconds))
      .append(":")
      .append(delay == 0 ? adjustTextNumber(hundredths) : "00");
    lblCounter.setText(timeFormmater.toString());
    Thread.sleep(sleepTime);
  }

  private void adjustHundredths() {
    if (hundredths >= 100) {
      hundredths -= 100;
      seconds += 1;
    }
  }

  private void adjustSeconds() {
    if (seconds >= 60) {
      seconds -= 60;
      minutes += 1;
    }
  }

  private void adjustMinutes() {
    if (minutes >= 60) {
      minutes -= 60;
      hours += 1;
    }
  }

  private String adjustTextNumber(int value) {
    return value > 9 ? value + "" : "0" + value;
  }

  /**
   * @param control the control to set
   */
  public void setControl(Integer control) {
    this.control = control;
  }

  private void detectFirstTime() {
    if(this.firstTime){
      this.firstTime = false;
      return;
    }
    seconds += delay;
  }
  
  public void restartCounter(){
    timeFormmater.delete(0, timeFormmater.length());
    timeFormmater.append("00:00:00:00");
    lblCounter.setText(timeFormmater.toString());
  }

}
