// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot.commands;

import javax.naming.LimitExceededException;

import com.revrobotics.spark.config.LimitSwitchConfig;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
/** A command for the motor portion of the intake
 * @param <Drive>*/
@SuppressWarnings("unused")
public class IntakeMotor extends Command {
boolean isRunning = false;
Intake intake;
/**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public IntakeMotor(Intake intake) {
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(intake);
      this.intake = intake;
    }
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      if (isRunning == false){
        isRunning = true;
      } else if (isRunning == true) {
        isRunning = false;
      }

      System.out.println("Intake Motor Command Initialized");  
    }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (isRunning == true) {
      intake.setFeederRoller(0.0);
    } else if (isRunning == false) {
      intake.setFeederRoller(0.0);
    }
  }
 
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
      intake.setFeederRoller(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
