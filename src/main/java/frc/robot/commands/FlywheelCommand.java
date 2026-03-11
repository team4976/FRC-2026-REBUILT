package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.PhotonVision;
import static frc.robot.Constants.*;

public class FlywheelCommand extends Command{
    public FlywheelSubsystem flywheelSubsystem;
    public PhotonVision photonVision;
    public boolean isOverriden;

    public FlywheelCommand(FlywheelSubsystem flywheelSubsystem, PhotonVision photonVision, boolean isOverriden){
        this.flywheelSubsystem = flywheelSubsystem;
        this.photonVision = photonVision;
        this.isOverriden = isOverriden;
        addRequirements(flywheelSubsystem);
    }

    @Override
    public void initialize(){
        System.out.println("flywheel command initialize");
    }

    @Override
    public void execute(){ 
        if (isOverriden) {
            double leftTriggerAxis = operatorController.getLeftTriggerAxis();
            double flywheelSpeed = leftTriggerAxis * 70;
            flywheelSubsystem.spinFlywheel(flywheelSpeed);
        } else if (!isOverriden) {
            flywheelSubsystem.spinFlywheel(50);
                /*31.49597 + (10.19041 * (photonVision.getDistance() + 0.5969))  
                - (0.4148098 * Math.pow(photonVision.getDistance() + 0.5969, 2))*/ 
                //SmartDashboard.getNumber("Turret Rotate", 0));
        }
    }

    @Override
    public void end(boolean isInterupted){
        flywheelSubsystem.spinFlywheel(0);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
