// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

//This subsystem spins the intake motor

package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;


public class IntakeSubsystem extends SubsystemBase {
  public SparkMax m_intake; 
  public SparkMax m_leftArm;
  public SparkMax m_rightArm;

  public final double maxLeftEncoderPos = -5.0;
  public final double maxRightEncoderPos = -0.6;
  public final double minLeftEncoderPos = -0.01;
  public final double minRightEncoderPos = -0.01;

  public DoubleSupplier currentIntakeSpeed = () -> m_intake.getAppliedOutput();
  public DoubleSupplier leftArmPose = () -> m_leftArm.getEncoder().getPosition();
  public DoubleSupplier rightArmPose = () -> m_rightArm.getEncoder().getPosition();;
  public BooleanSupplier intakeLimitSwitch = () -> {
    boolean isLeftLimit = leftArmPose.getAsDouble() <= minLeftEncoderPos || leftArmPose.getAsDouble() >= maxLeftEncoderPos;
    boolean isRightLimit = rightArmPose.getAsDouble() <= minRightEncoderPos || rightArmPose.getAsDouble() >= maxRightEncoderPos;

    return isLeftLimit || isRightLimit;
  };
  public BooleanSupplier intakeDown = () -> 
    leftArmPose.getAsDouble() >= maxLeftEncoderPos || rightArmPose.getAsDouble() >= maxRightEncoderPos;
  public BooleanSupplier intakeUp = () -> 
    leftArmPose.getAsDouble() <= minLeftEncoderPos || rightArmPose.getAsDouble() <= minRightEncoderPos;


      
  public IntakeSubsystem() {
    m_intake = new SparkMax(Intake_ID, MotorType.kBrushed);
    m_leftArm = new SparkMax(Intake_Arm_Left_ID, MotorType.kBrushless);
    m_rightArm = new SparkMax(Intake_Arm_Right_ID, MotorType.kBrushless);
    SmartDashboard.putBoolean("Manual Intake", false);
  }   

  public void teleopInit(){ 
  }
  

  //Stops motor for intake bar, alternitively you can pass 0 to runIntakeMotor
  public void stopIntakeMotor() {
    m_intake.set(0); 
  }
  //Runs the motor for the intake bar
  public void runIntakeMotor(double speed) {
    m_intake.set(speed);
  }
  //Stops the intake arms motors
  public void stopIntakeArms(){
    m_leftArm.set(0);
    m_rightArm.set(0);
  }

  /**
   * 
   * @param isJitter whether the method is being called for the jitter or manual
   * @param offset the speed offset from the default of 0.45 during jitter and 0.25 in normal. 
   * Enter a negative value to lower the speed.
  */
  //Jitter speed is different than manual speed, isJitter checks which
  //Pass a negative number for offset to lower the default speed, like in auto
  public void intakeMove(boolean isJitter, double offset, boolean isDown){
    double speed = (isJitter)?0.45 + offset:0.25;
    if(!isDown) speed = speed * -1;
    m_leftArm.set(speed);
    m_rightArm.set(speed);
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

    /**
   * Constructs a command that moves the intakes arms down
   *
   * @param isJitter whether the method is being called for the jitter or manual
   * @param offset the speed offset from the default of 0.45 during jitter and 0.25 in normal. 
   * Enter a negative value to lower the speed.
   * @return the command
   */
  //Puts the intakes arms down has the same parameters as the method, in the form of a command to provide a runnable
  public Command intakeCommand(boolean isJitter, double offset, boolean isDown){
    return runOnce(()-> 
      intakeMove(isJitter, offset, isDown)
    );
  }
  
  //---------
  //Periodic
  //---------

  //Manual Control if selected in elastic, when on the normal control wont work (most likely)
  @Override
  public void periodic(){
    if (intakeLimitSwitch.getAsBoolean()) stopIntakeArms();

      if (SmartDashboard.getBoolean("Manual Intake", false)) {
        if (!driverController.povUp().getAsBoolean() || !driverController.povDown().getAsBoolean()){
          stopIntakeArms();
        }
        //intake down is set higher, DONT USE.
        driverController.povUp().whileTrue(intakeCommand(false, 0, false));
        driverController.povDown().whileTrue(intakeCommand(false, 0, true));
      }
  }

  /*
    public void intakeDown(boolean isJitter, double offset){
    if (isJitter){
      m_leftArm.set(-0.45 - offset);
      m_rightArm.set(-0.45 - offset);
      return;
    }
    m_leftArm.set(-0.25);
    m_rightArm.set(-0.25);
  }
   public void intakeUp(boolean isJitter, double offset){
    if (isJitter){
      m_leftArm.set(0.45 + offset);
      m_rightArm.set(0.45 + offset);
      return;
    }
    m_leftArm.set(0.25);
    m_rightArm.set(0.25);
  }

   /**
   * Constructs a command that moves the intakes arms down
   *
   * @param isJitter whether the method is being called for the jitter or manual
   * @param offset the speed offset from the default of 0.45 during jitter and 0.25 in normal. 
   * Enter a negative value to lower the speed.
   * @return the command
   
  //Puts the intakes arms down has the same parameters as the method, in the form of a command to provide a runnable
  public Command intakeDownCommand(boolean isJitter, double offset){
    return runOnce(()-> 
      intakeMove(isJitter, offset, true)
    );
  }

  /**
   * Constructs a command that moves the intakes arms up
   *
   * @param isJitter whether the method is being called for the jitter or manual
   * @param offset the speed offset from the default of 0.45 during jitter and 0.25 in normal. 
   * Enter a negative value to lower the speed.
   * @return the command
   
  //Puts the intakes arms up has the same parameters as the method, in the form of a command to provide a runnable
  public Command intakeUpCommand(boolean isJitter, double offset){
    return runOnce(()-> 
      intakeMove(isJitter, offset, false)
    );
  }


   */




    //-------------------------------------------------------
    //EVERYTHING ENCODER RELATED (Was Planned But Never Used)
    //-------------------------------------------------------

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
      || intakeArmLeft.getEncoder().getPosition() <= 0.0){
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


