package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Intake;


public class Jitter extends SequentialCommandGroup {
    public Jitter(Intake intakeSubsystem){
        addRequirements(intakeSubsystem);
        addCommands(
            Commands.repeatingSequence(
                Commands.deadline(Commands.waitSeconds(2), 
                    Commands.repeatingSequence(
                        Commands.print("RepeatJitter Started"),
                        Commands.deadline(Commands.waitSeconds(0.20), intakeSubsystem.intakeUpCommand(-0.15)),
                        Commands.deadline(Commands.waitSeconds(0.20), intakeSubsystem.intakeDownCommand(0.15))
                        )
                    ),
                Commands.waitSeconds(1)
            )
            //intakeSubsystem.intakeUpCommand(),
            //intakeSubsystem.intakeUpCommand()
            //A
            //B
        );
    }
}
