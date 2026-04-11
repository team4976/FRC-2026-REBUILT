package frc.robot.commands.Intake;

import static frc.robot.Constants.intakeSpeed;

import javax.naming.LimitExceededException;

import com.revrobotics.spark.config.LimitSwitchConfig;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.IntakeSubsystem;
/** The Command for the Intake
 * @param <Drive> */
@SuppressWarnings("unused")
public class IntakeBarCommand extends Command {

  public IntakeSubsystem intake;
  public boolean endCommand;
  public boolean isIntakeReversed;

  /**
       * Creates a new IntakeCommand
       *
       * @param intake The subsystem used by this command.
       */

  public IntakeBarCommand(IntakeSubsystem intake, boolean isIntakeReversed) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.intake = intake;
    this.isIntakeReversed = isIntakeReversed;
    addRequirements(intake);  
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    endCommand = false;
    if (isIntakeReversed){
      double setIntakeSpeed = (intake.currentIntakeSpeed.getAsDouble() >= 0.0)?-1:0;
      intake.runIntakeMotor(setIntakeSpeed);
    } else {
      double setIntakeSpeed = (intake.currentIntakeSpeed.getAsDouble() <= 0.0)?1:0;
      intake.runIntakeMotor(setIntakeSpeed);
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
