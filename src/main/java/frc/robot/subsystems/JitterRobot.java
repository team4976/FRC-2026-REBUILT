package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class JitterRobot extends SubsystemBase{

    public JitterRobot(){}

    public Command jitterRobotForward(){
        return Commands.deadline(Commands.waitSeconds(0.1), drivetrain.applyRequest(() ->
                robotCentricDrive.withVelocityX(0.5 * MaxSpeed))
        );
    }

    public Command jitterRobotBackward(){
        return Commands.deadline(Commands.waitSeconds(0.1), drivetrain.applyRequest(() ->
                robotCentricDrive.withVelocityX(-0.5 * MaxSpeed))
        );
    }
    
}
