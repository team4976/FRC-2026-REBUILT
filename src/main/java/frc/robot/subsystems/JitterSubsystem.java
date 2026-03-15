package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class JitterSubsystem extends SubsystemBase{

    public JitterSubsystem(){}

    public Command jitterRobotForward(){
        return drivetrain.applyRequest(() ->
                robotCentricDrive.withVelocityX(0.5 * MaxSpeed)
            ); 
    }

    public Command jitterRobotBackward(){
        return drivetrain.applyRequest(() ->
                robotCentricDrive.withVelocityX(-0.5 * MaxSpeed)
            ); 
    }
    
}
