package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class JitterSubsystem extends SubsystemBase{

    public JitterSubsystem(){}

    public Command jitterRobotForward(){
        System.out.println("Starting Forwards Swerve");
        return Commands.deadline(Commands.waitSeconds(0.2), drivetrain.applyRequest(() ->
                robotCentricDrive.withVelocityX(0.5 * MaxSpeed))
        );
    }

    public Command jitterRobotBackward(){
        System.out.println("Starting Backwards Swerve");
        return Commands.deadline(Commands.waitSeconds(0.2), drivetrain.applyRequest(() ->
                robotCentricDrive.withVelocityX(-0.5 * MaxSpeed))
        );
    }
    
}
