package frc.robot.commands.Jitter;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.GlobalCommands;
import frc.robot.commands.Intake.IntakeSwap;
import frc.robot.subsystems.IntakeSubsystem;


public class JitterIntake extends SequentialCommandGroup {

    /**
     * Runs an emulated tapping of the jitter intake button using .deadline commands
     * @param s_intake the subsystem object for the intake
     * @see {@link IntakeSubsystem}
     * @see {@link JitterRobot} for the drivetrain jitter
     */
    //Runs an emulated tapping of the jitter button.
    public JitterIntake(IntakeSubsystem s_intake){
        addRequirements(s_intake);
        addCommands(
            Commands.repeatingSequence(
                Commands.deadline(Commands.waitSeconds(2), 
                    Commands.repeatingSequence(
                        Commands.print("RepeatJitter Started"),
                        Commands.deadline(Commands.waitSeconds(0.20), new IntakeSwap(s_intake))
                        )
                    ),
                Commands.waitSeconds(1)
            )
        );
    }
}
