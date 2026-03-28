// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

//This subsystem spins the intake motor

package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;


public class Intake extends SubsystemBase {
  public SparkMax intakeMotor; 
  public SparkMax intakeArmLeft;
  public SparkMax intakeArmRight;

  private SparkMaxConfig sparkConfig = new SparkMaxConfig();

  public double currentIntakeSpeed;
      
  public Intake() {
    intakeMotor = new SparkMax(Intake_ID, MotorType.kBrushed);
    intakeArmLeft = new SparkMax(Intake_Arm_Left_ID, MotorType.kBrushless);
    intakeArmRight = new SparkMax(Intake_Arm_Right_ID, MotorType.kBrushless);
    SmartDashboard.putBoolean("Manual Intake", false);
  }   

  public void teleopInit(){ 
  }
  


  //---------------
  //Intake Methods:
  //---------------

  //Stops motor for intake bar, alternitively you can pass 0 to runIntakeMotor
  public void stopIntakeMotor() {
    intakeMotor.set(0); 
    currentIntakeSpeed = 0;
  }
  
  //Runs the motor for the intake bar
  public void runIntakeMotor(double speed) {
    intakeMotor.set(speed);
    currentIntakeSpeed = speed;
  }

  //Stops the intake arms motors
  public void stopIntakeArms(){
    System.out.println("DONE");
    intakeArmLeft.set(0);
    intakeArmRight.set(0);
  }

  //Jitter speed is different than manual speed, isJitter checks which
  //Pass a negative number for offset to lower the default speed, like in auto
  public void intakeDown(boolean isJitter, double offset){
    if (isJitter){
      intakeArmLeft.set(-0.45 - offset);
      intakeArmRight.set(-0.45 - offset);
      return;
    }
    intakeArmLeft.set(-0.25);
    intakeArmRight.set(-0.25);
  }

  //Jitter speed is different than manual speed, isJitter checks which
  //Pass a negative number for offset to lower the default speed, like in auto
  public void intakeUp(boolean isJitter, double offset){
    if (isJitter){
      intakeArmLeft.set(0.45 + offset);
      intakeArmRight.set(0.45 + offset);
      return;
    }
    intakeArmLeft.set(0.25);
    intakeArmRight.set(0.25);
  }



  //-----------------------
  //Intake Command Methods:
  //-----------------------

  //Stops the intakes arms from moving, in the form of a command to provide a runnable
  public Command stopIntakeArmsCommand(){
    return runOnce(()->
      stopIntakeArms()
    );
  }

  //Stops the intake bar motor from moving, in the form of a command to provide a runnable
  public Command stopIntakeMotorCommand(){
    return runOnce(()-> 
      stopIntakeMotor()
    );
  }

  //Puts the intakes arms down has the same parameters as the method, in the form of a command to provide a runnable
  public Command intakeDownCommand(double offset){
    return runOnce(()-> 
      intakeDown(true, offset)
    );
  }

  //Puts the intakes arms up has the same parameters as the method, in the form of a command to provide a runnable
  public Command intakeUpCommand(double offset){
    return runOnce(()-> 
      intakeUp(true, offset)
    );
  }

  

  //---------
  //Periodic
  //---------

  //Manual Control if selected in elastic, when on the normal control wont work (most likely)
  @Override
  public void periodic(){
      if (SmartDashboard.getBoolean("Manual Intake", false)) {
        if (!driverController.povUp().getAsBoolean() || !driverController.povDown().getAsBoolean()){
          intakeArmLeft.set(0);
          intakeArmRight.set(0);
        }
        //intake down is set higher, DONT USE.
        driverController.povUp().whileTrue(intakeDownCommand(0));
        driverController.povDown().whileTrue(intakeUpCommand(0));
      }
  }




    //--------------------------
    //EVERYTHING ENCODER RELATED
    //--------------------------

    //There was a method for toggling the intake that used the intakeExtended status to control its state

    //,,,,,,,,
    //FIELDS:
    //''''''''

    /* 
      public final double maxLeftEncoderPos = 0.0;
      public final double maxRightEncoderPos = 0.0;
      public final double minLeftEncoderPos = 0.0;
      public final double minRightEncoderPos = 0.0;

      public boolean intakeExtended;
    */

    //,,,,,,,,,,,,,,
    //IN CONSTUCTOR:
    //''''''''''''''

      /* 
      //*TESTING* Check what we want the threshold value to be before using
      if (intakeArmLeft.getEncoder().getPosition() >= 0.0 
      || intakeArmLeft.getEncoder().getPosition() >= 0.0){
        return;
      }
      intakeExtended = false;
      */

    //,,,,,,,,
    //METHODS:
    //''''''''
    /* 
      public void intakeDownEncoder(){
        if (intakeExtended){
          return;
        }
        // *TESTING* MAKE SURE THEY BOTH MOVE IN THE SAME DIRECTION Eg. clockwise on left is counterclockwise on right (BAD)
        intakeArmLeft.set(0.5);
        intakeArmRight.set(0.5);
      }

      public void intakeUpEncoder(){
        if (!intakeExtended){
        return;
      }
      intakeArmLeft.set(-0.5);
      intakeArmRight.set(-0.5);
    }
      */

    //,,,,,,,,,,,,
    //In Periodic:
    //''''''''''''

      /* 
      if (intakeArmLeft.getEncoder().getPosition() >= maxLeftEncoderPos 
      || intakeArmLeft.getEncoder().getPosition() >= maxRightEncoderPos){
        intakeArmLeft.setVoltage(0);
        intakeArmRight.setVoltage(0);

        //intake is fully extended
        intakeExtend = true;
        return;
      } else if (intakeArmLeft.getEncoder().getPosition() <= minLeftEncoderPos 
      || intakeArmLeft.getEncoder().getPosition() <= minRightEncoderPos)
        intakeArmLeft.setVoltage(0);
        intakeArmRight.setVoltage(0);

        //intake is fully retracted
        intakeExtend = false;
        return;
      */

      

}


