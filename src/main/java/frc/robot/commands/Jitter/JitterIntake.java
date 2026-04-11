package frc.robot.commands.Jitter;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.IntakeSubsystem;


public class JitterIntake extends SequentialCommandGroup {

    /**
     * Runs an emulated tapping of the jitter intake button using .deadline commands
     * @param intakeSubsystem the subsystem object for the intake
     * @see {@link IntakeSubsystem}
     * @see {@link JitterRobot} for the drivetrain jitter
     */
    //Runs an emulated tapping of the jitter button.
    public JitterIntake(IntakeSubsystem intakeSubsystem){
        addRequirements(intakeSubsystem);
        addCommands(
            Commands.repeatingSequence(
                Commands.deadline(Commands.waitSeconds(2), 
                    Commands.repeatingSequence(
                        Commands.print("RepeatJitter Started"),
                        Commands.deadline(Commands.waitSeconds(0.20), intakeSubsystem.intakeCommand(true, -0.15,false)),
                        Commands.deadline(Commands.waitSeconds(0.20), intakeSubsystem.intakeCommand(true, -0.15,true))
                        )
                    ),
                Commands.waitSeconds(1)
            )
        );
    }
}
