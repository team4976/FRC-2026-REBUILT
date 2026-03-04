package frc.robot.commands;

import javax.naming.LimitExceededException;

import com.revrobotics.spark.config.LimitSwitchConfig;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Pneumatics;
/** An example command that uses an example subsystem. 
 * @param <Drive>*/
@SuppressWarnings("unused")
public class intakePneumatic extends Command {
boolean SolenoidStatus;
Pneumatics pneumatics;
Intake intake;
  /**
       * Creates a new ExampleCommand.
       *
       * @param subsystem The subsystem used by this command.
       */

public intakePneumatic(Pneumatics pneumatics, Intake intake) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake);
    addRequirements(pneumatics);
 this.pneumatics = pneumatics;
 this.SolenoidStatus = false;
 this.intake = intake;
}
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {  
  /* 
if (SolenoidStatus == false) {
    pneumatics.forwardSolenoid();
    SolenoidStatus = true;
    intake.runIntake(Constants.intakeSpeed);

  }
else if (SolenoidStatus == true) {
    pneumatics.reverseSolenoid();
    SolenoidStatus = false;
    intake.stop();
  }
    */
  pneumatics.forwardSolenoid();
}

@Override
  public void execute() {
    //pneumatics.testMove();

  }
  
  // Called once the command ends or is interrupted.

  @Override
  public void end(boolean interrupted) {  
    pneumatics.reverseSolenoid();

}


  @Override
  public boolean isFinished() { 
    return false;
  }
}
