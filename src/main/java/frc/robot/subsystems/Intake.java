// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

//This subsystem spins the intake motor

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import static frc.robot.Constants.*;


public class Intake extends SubsystemBase {
  public TalonSRX intakeMotor; 
  //Will change intakeMotor to Sparkmax
  public TalonFX intakeArmLeft;
  public TalonFX intakeArmRight;

  public final double maxLeftEncoderPos = 0.0;
  public final double maxRightEncoderPos = 0.0;
  public final double minLeftEncoderPos = 0.0;
  public final double minRightEncoderPos = 0.0;

  public static final PneumaticsControlModule pneumaticsControlModule = new PneumaticsControlModule(PCM_ID);
  private final Compressor compressor = new Compressor(Compressor_ID, PneumaticsModuleType.CTREPCM);
  private Solenoid solenoid;
  public boolean intakeStatus;
  public double currentIntakeSpeed;
      
  public Intake() {
    intakeMotor = new TalonSRX(Intake_ID);
    intakeArmLeft = new TalonFX(Intake_Arm_Left_ID);
    intakeArmRight = new TalonFX(Intake_Arm_Right_ID);


    intakeStatus = false;
    compressor.enableDigital(); 
    solenoid = pneumaticsControlModule.makeSolenoid(Solenoid_ID);
    solenoid.set(false);
  }   

  public void teleopInit(){
    intakeStatus = false;
    intakeMotor.set(ControlMode.PercentOutput, currentIntakeSpeed); 
    intakeUp();
    Commands.waitSeconds(4);
    intakeMotor.set(ControlMode.PercentOutput, 0); 
    currentIntakeSpeed = 0;
  }

  //was used for pnuematics.
  public void toggleIntake(){
    if (!intakeStatus) {
      intakeDown();
      runIntakeMotor(Constants.intakeSpeed);
    } else if (intakeStatus) {
      intakeUp();
      Commands.waitSeconds(1);
      stopIntakeMotor();
    }
  }
             
  public void stopIntakeMotor() {
    intakeMotor.set(ControlMode.PercentOutput, 0); 
    currentIntakeSpeed = 0;
  }
    
  public void runIntakeMotor(double speed) {
    intakeMotor.set(ControlMode.PercentOutput, speed);
    currentIntakeSpeed = speed;
  }

  public void intakeDown(){
    if (intakeStatus){
      return;
    }

    // *TESTING* MAKE SURE THEY BOTH MOVE IN THE SAME DIRECTION Eg. clockwise on left is counterclockwise on right (BAD)
    intakeArmLeft.setVoltage(1);
    intakeArmRight.setVoltage(1);

  }

  public void intakeUp(){
    if (!intakeStatus){
      return;
    }
    intakeArmLeft.setVoltage(-1);
    intakeArmRight.setVoltage(-1);
  }

  @Override
  public void periodic(){
    if (intakeArmLeft.getPosition().getValueAsDouble() >= maxLeftEncoderPos 
    || intakeArmLeft.getPosition().getValueAsDouble() >= maxRightEncoderPos){
      intakeArmLeft.setVoltage(0);
      intakeArmRight.setVoltage(0);
      
      //intake is fully extended
      intakeStatus = true;
      return;

    } else if (intakeArmLeft.getPosition().getValueAsDouble() >= minLeftEncoderPos 
    || intakeArmLeft.getPosition().getValueAsDouble() >= minRightEncoderPos)
      intakeArmLeft.setVoltage(0);
      intakeArmRight.setVoltage(0);

      //intake is fully retracted
      intakeStatus = false;
      return;
    }

}


