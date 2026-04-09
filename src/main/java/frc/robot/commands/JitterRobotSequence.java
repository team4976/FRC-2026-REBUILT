package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class JitterRobotSequence extends SequentialCommandGroup {
    public JitterRobotSequence(){
        addCommands(
            Commands.deadline(Commands.waitSeconds(0.1), new JitterRobot(true)),
            Commands.deadline(Commands.waitSeconds(0.1), new JitterRobot(false))
        );
    }
}
