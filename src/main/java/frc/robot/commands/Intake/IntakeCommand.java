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
public class IntakeCommand extends Command {

  public boolean SolenoidStatus;
  public IntakeSubsystem intake;
  public boolean endCommand;
  public double startingIntakeSpeed;
  public boolean willIntakeDown = true;
  /**
       * Creates a new IntakeCommand
       *
       * @param intake The subsystem used by this command.
       */

  public IntakeCommand(IntakeSubsystem intake) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.intake = intake;
    addRequirements(intake);  
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {  
    endCommand = false; 
    if (intake.intakeUp.getAsBoolean()) willIntakeDown = true;
    if (willIntakeDown) {
      intake.intakeMove(false, 0.0, true);
    } else {
      intake.intakeMove(false, 0.0, false);
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
