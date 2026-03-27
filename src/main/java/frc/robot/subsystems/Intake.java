// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

//This subsystem spins the intake motor

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import static frc.robot.Constants.*;

import java.util.Date;


public class Intake extends SubsystemBase {
  public SparkMax intakeMotor; 
  //Will change intakeMotor to Sparkmax
  public SparkMax intakeArmLeft;
  public SparkMax intakeArmRight;

  private SparkMaxConfig sparkConfig = new SparkMaxConfig();

  public final double maxLeftEncoderPos = 0.0;
  public final double maxRightEncoderPos = 0.0;
  public final double minLeftEncoderPos = 0.0;
  public final double minRightEncoderPos = 0.0;

  public boolean intakeExtended;
  public double currentIntakeSpeed;
      
  public Intake() {
    intakeMotor = new SparkMax(Intake_ID, MotorType.kBrushed);
    intakeArmLeft = new SparkMax(Intake_Arm_Left_ID, MotorType.kBrushless);
    intakeArmRight = new SparkMax(Intake_Arm_Right_ID, MotorType.kBrushless);
    SmartDashboard.putBoolean("Manual Intake", false);

    intakeExtended = false;
  }   

  public void teleopInit(){ 
    /* 
    //*TESTING* Check what we want the threshold value to be before using
    if (intakeArmLeft.getEncoder().getPosition() >= 0.0 
    || intakeArmLeft.getEncoder().getPosition() >= 0.0){
      return;
    }
    */
  }


  public void toggleIntake(){
    //CommandScheduler.getInstance().schedule(Commands);
    //Commands.deadline(Commands.waitSeconds(0.1), testIntakeCommand);  
    
    //intakeDown();
      //runIntakeMotor(intakeSpeed);
      //Commands.waitSeconds(0.1).andThen(testIntakeCommand);
      //intakeExtended = true;
      /* 
      intakeUp();
      Commands.deadline(Commands.waitSeconds(0.2), stopIntakeCommand());
      Commands.waitSeconds(1);
       */

  }

  Command testCommand (){
    stopIntake();
    stopIntakeMotor();
    return Commands.print("DONE");
  }
 
    public Command jitterIntakeUp(){
        return Commands.deadline(Commands.waitSeconds(0.15), intakeUpCommand());
    }

    public Command jitterIntakeDown(){
        return Commands.deadline(Commands.waitSeconds(0.1), intakeDownCommand());
    }
             
  public void stopIntakeMotor() {
    intakeMotor.set(0); 
    currentIntakeSpeed = 0;
  }
    
  public void runIntakeMotor(double speed) {
    intakeMotor.set(speed);
    currentIntakeSpeed = speed;
  }
  //Stops the intake arms motors
  public void stopIntake(){
    System.out.println("DONE");
    intakeArmLeft.set(0);
    intakeArmRight.set(0);
  }

  public void intakeDown(){
    intakeArmLeft.set(-0.5);
    intakeArmRight.set(-0.5);
  }

  public void intakeUp(){
    intakeArmLeft.set(0.5);
    intakeArmRight.set(0.5);
  }

  public void intakeUpMoreSpeed(){
    intakeArmLeft.set(0.60);
    intakeArmRight.set(0.60);
  }

  public Command stopIntakeCommand(){
    return runOnce(()->{
        System.out.println("TEST");
           stopIntake();
    }
 
    );
  }

  public Command intakeDownCommand(){
    return runOnce(()-> 
      intakeDown()
    );
  }

  public Command intakeUpCommand(){
    return runOnce(()-> 
      intakeUpMoreSpeed()
    );
  }

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

  @Override
  public void periodic(){

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

      if (SmartDashboard.getBoolean("Manual Intake", false)) {
        if (!driverController.povUp().getAsBoolean() || !driverController.povDown().getAsBoolean()){
          intakeArmLeft.set(0);
          intakeArmRight.set(0);
        }
        driverController.povUp().whileTrue(intakeDownCommand());
        driverController.povDown().whileTrue(intakeUpCommand());
      }


    }

}


