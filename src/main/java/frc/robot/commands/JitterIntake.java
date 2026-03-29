package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.JitterSubsystem;


public class JitterIntake extends SequentialCommandGroup {

    /**
     * Runs an emulated tapping of the jitter intake button using .deadline commands
     * @param intakeSubsystem the subsystem object for the intake
     * @see {@link IntakeSubsystem}
     * @see {@link JitterSubsystem} for the drivetrain jitter
     */
    //Runs an emulated tapping of the jitter button.
    public JitterIntake(IntakeSubsystem intakeSubsystem){
        addRequirements(intakeSubsystem);
        addCommands(
            Commands.repeatingSequence(
                Commands.deadline(Commands.waitSeconds(2), 
                    Commands.repeatingSequence(
                        Commands.print("RepeatJitter Started"),
                        Commands.deadline(Commands.waitSeconds(0.20), intakeSubsystem.intakeUpCommand(-0.15)),
                        Commands.deadline(Commands.waitSeconds(0.20), intakeSubsystem.intakeDownCommand(-0.15))
                        )
                    ),
                Commands.waitSeconds(1)
            )
        );
    }
}
