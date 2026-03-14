// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

//This subsystem spins the intake motor

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.*;


public class Intake extends SubsystemBase {
  public TalonSRX IntakeMotor;

  public static final PneumaticsControlModule pneumaticsControlModule = new PneumaticsControlModule(Solenoid_ID);
  private final Compressor compressor = new Compressor(PCM_ID, PneumaticsModuleType.CTREPCM);
  private Solenoid solenoid;
  public boolean intakeStatus;
      
  public Intake() {
    IntakeMotor = new TalonSRX(Intake_ID);

    intakeStatus = false;
    compressor.enableDigital(); 
    solenoid = pneumaticsControlModule.makeSolenoid(Solenoid_ID);
    solenoid.set(false);
  }   

  public void teleopInit(){
    IntakeMotor.set(ControlMode.PercentOutput, intakeSpeed); 
    solenoid.set(false);
    Commands.waitSeconds(4);
    IntakeMotor.set(ControlMode.PercentOutput, 0); 
    intakeStatus = false;
  }
             
  public void stopIntakeMotor() {
    IntakeMotor.set(ControlMode.PercentOutput, 0); 
  }
    
  public void runIntakeMotor(double speed) {
    IntakeMotor.set(ControlMode.PercentOutput, speed);
    System.out.println("running at speed:" + speed);
  }

  public void forwardSolenoid(){
    System.out.println("solenoid on");   
    solenoid.set(true);
    intakeStatus = true;
  }

  public void reverseSolenoid(){
    System.out.println("solenoid off");
    solenoid.set(false);
    intakeStatus = false;
  }

}


