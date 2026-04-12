package frc.robot.commands.Intake;

import javax.naming.LimitExceededException;

import com.revrobotics.spark.config.LimitSwitchConfig;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.Constants;


/** The Command for the Intake Bar */
@SuppressWarnings("unused")
public class IntakeBarCommand extends Command {

  public IntakeSubsystem s_intake;

  public boolean isIntakeReversed;
  public boolean endCommand;

  /**
   * The Intake Bar Command, used to toggle the state of the intake bar (Spinning/not spinning)
   * @param s_intake The s_intake object that our code base uses.
   * @param isIntakeReversed whether the intake bar should be reversed. one object for yes and one for no.
  */
  public IntakeBarCommand(IntakeSubsystem s_intake, boolean isIntakeReversed) {
    this.isIntakeReversed = isIntakeReversed;
    this.s_intake = s_intake;
    addRequirements(s_intake);  
  }

  @Override
  public void initialize() {
    endCommand = false;
    if (isIntakeReversed){
      double setIntakeSpeed = (s_intake.currentIntakeSpeed.getAsDouble() >= 0.0)?-1:0;
      s_intake.runIntakeMotor(setIntakeSpeed);
    } else {
      double setIntakeSpeed = (s_intake.currentIntakeSpeed.getAsDouble() <= 0.0)?1:0;
      s_intake.runIntakeMotor(setIntakeSpeed);
    }
    endCommand = true;
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {  
  }


  @Override
  public boolean isFinished() { 
    return endCommand;
  }
}
