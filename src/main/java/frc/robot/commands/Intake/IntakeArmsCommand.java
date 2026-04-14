package frc.robot.commands.Intake;

import javax.naming.LimitExceededException;

import com.revrobotics.spark.config.LimitSwitchConfig;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.Constants;
/** The Command for the Intake Arms*/
@SuppressWarnings("unused")
public class IntakeArmsCommand extends Command {
  public IntakeSubsystem s_intake;

  public boolean endCommand;
  /**
   * The command used for the control of the intakes arms, each call changes the direction they are moving.
   * @param s_intake The subsystem (s_intake) used by this command.
   */
  public IntakeArmsCommand(IntakeSubsystem s_intake) {
    this.s_intake = s_intake;
    addRequirements(s_intake);  
  }

  @Override
  public void initialize() {  
    endCommand = false; 
    s_intake.intakeMove(false, 0.0, s_intake.getAndSwapDirection());
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
