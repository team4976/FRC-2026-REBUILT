// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

//This subsystem spins the intake motor

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Intake extends SubsystemBase {
  public TalonSRX IntakeMotor;

  private static DigitalInput m_toplimitSwitch = new DigitalInput(0);
      
  public Intake() {
    IntakeMotor = new TalonSRX(Constants.Intake_ID); //Defines motor 1
  }   
             
          public void stop() {
    //        IntakeMotor.set(ControlMode.PercentOutput0); 
            IntakeMotor.set(ControlMode.PercentOutput, 0); 
          }
        
          public void runIntake(double speed) {
  //          IntakeMotor.set(ControlMode.PercentOutput,speed);
            IntakeMotor.set(ControlMode.PercentOutput, speed);
          }
        
        public boolean checkswitchstatus() {
            // Limit switch is pressed
            return (m_toplimitSwitch.get());
        }

  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  public void setFeederRoller(int i) {
    throw new UnsupportedOperationException("Unimplemented method 'setFeederRoller'");
  }
  //Catches errors
    }


