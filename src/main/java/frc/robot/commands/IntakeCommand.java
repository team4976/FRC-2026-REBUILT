package frc.robot.commands;

import javax.naming.LimitExceededException;

import com.revrobotics.spark.config.LimitSwitchConfig;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;
/** An example command that uses an example subsystem. 
 * @param <Drive> */
@SuppressWarnings("unused")
public class IntakeCommand extends Command {
boolean SolenoidStatus;
Intake intake;
boolean stop;
  /**
       * Creates a new ExampleCommand.
       *
       * @param subsystem The subsystem used by this command.
       */

public IntakeCommand(Intake intake) {
    // Use addRequirements() here to declare subsystem dependencies.
  this.intake = intake;
  addRequirements(intake);  
}
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {  
  
    if (!intake.intakeStatus) {
      intake.forwardSolenoid();
      intake.runIntakeMotor(Constants.intakeSpeed);
      stop = true;
    } else if (intake.intakeStatus) {
      intake.reverseSolenoid();
      Commands.waitSeconds(2);
      intake.stopIntakeMotor();
      stop = true;
    }
  
}

@Override
  public void execute() {
  }
  
  // Called once the command ends or is interrupted.

  @Override
  public void end(boolean interrupted) {  

  }


  @Override
  public boolean isFinished() { 
    return stop;
  }
}
