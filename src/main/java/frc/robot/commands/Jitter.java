package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Intake;


public class Jitter extends SequentialCommandGroup {
    public Jitter(Intake intake){
        addRequirements(intake);
        addCommands(
            intake.intakeUpCommand(),
            intake.intakeUpCommand()
            //A
            //B
        );
    }
}
