package frc.robot.commands;

import static frc.robot.Constants.intakeSpeed;

import javax.naming.LimitExceededException;

import com.revrobotics.spark.config.LimitSwitchConfig;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;
/** The Command for the Intake
 * @param <Drive> */
@SuppressWarnings("unused")
public class IntakeCommand extends Command {

  public boolean SolenoidStatus;
  public Intake intake;
  public boolean endCommand;
  public boolean isIntakeReversed;
  public double startingIntakeSpeed;
  /**
       * Creates a new IntakeCommand
       *
       * @param intake The subsystem used by this command.
       */

  public IntakeCommand(Intake intake, boolean isIntakeReversed) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.intake = intake;
    this.isIntakeReversed = isIntakeReversed;
    addRequirements(intake);  
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {  
    
    endCommand = false; 
    startingIntakeSpeed = intake.currentIntakeSpeed;

    if (isIntakeReversed) {
      intake.runIntakeMotor(-intakeSpeed);
    } else if (!isIntakeReversed) {
      intake.runIntakeMotor(intakeSpeed);
      intake.intakeDown();
    } 

  }

  @Override
  public void execute() {
  }
  
  // Called once the command ends or is interrupted.

  @Override
  public void end(boolean interrupted) {  
    if (isIntakeReversed) {
      intake.runIntakeMotor(startingIntakeSpeed);
      return;
    }
    intake.stopIntake();
  }


  @Override
  public boolean isFinished() { 
    return endCommand;
  }
}
