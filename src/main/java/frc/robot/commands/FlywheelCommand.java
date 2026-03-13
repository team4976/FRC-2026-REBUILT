package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.PhotonVision;
import static frc.robot.Constants.*;

public class FlywheelCommand extends Command{
    public FlywheelSubsystem flywheelSubsystem;
    public PhotonVision photonVision;
    public boolean isOverriden;
    public double autoFlywheelSpeed;
    public double manualFlywheelSpeed;
    public double totalFlywheelSpeed;

    public FlywheelCommand(FlywheelSubsystem flywheelSubsystem, PhotonVision photonVision, boolean isOverriden){
        this.flywheelSubsystem = flywheelSubsystem;
        this.photonVision = photonVision;
        this.isOverriden = isOverriden;
        addRequirements(flywheelSubsystem);
    }

    @Override
    public void initialize(){
        System.out.println("flywheel command initialize");
        flywheelSubsystem.spinFlywheel(0);
        flywheelSubsystem.isAutoFlywheel = true;
    }

    @Override
    public void execute(){ 
        /* 
        if (isOverriden) {
            double leftTriggerAxis = operatorController.getLeftTriggerAxis();
            double flywheelSpeed = leftTriggerAxis * 70;
            flywheelSubsystem.spinFlywheel(flywheelSpeed);
        } else if (!isOverriden) {
            flywheelSubsystem.spinFlywheel(50);
                /*31.49597 + (10.19041 * (photonVision.getDistance() + 0.5969))  
                - (0.4148098 * Math.pow(photonVision.getDistance() + 0.5969, 2)) 
                //SmartDashboard.getNumber("Turret Rotate", 0));
        }  */
        if (photonVision.getDistance() != 0) {
            autoFlywheelSpeed = (31.49597 + (10.19041 * (photonVision.getDistance() + 0.5969))  
                - (0.4148098 * Math.pow(photonVision.getDistance() + 0.5969, 2))) * 0.9;
        } else {
            autoFlywheelSpeed = 50;
        }

        if (operatorController.axisGreaterThan(1, 0.1).getAsBoolean()){
            manualFlywheelSpeed = operatorController.getLeftY() * -8;
        } else if (operatorController.axisLessThan(1, -0.1).getAsBoolean()) {
            manualFlywheelSpeed = operatorController.getLeftY() * -8;
        }
        
        totalFlywheelSpeed = manualFlywheelSpeed + autoFlywheelSpeed;
        flywheelSubsystem.spinFlywheel(totalFlywheelSpeed);
        System.out.println("manual flywheel speed:"  + manualFlywheelSpeed);
        System.out.println("total flywheel speed" + totalFlywheelSpeed);
    }

    @Override
    public void end(boolean isInterupted){
        flywheelSubsystem.isAutoFlywheel = false;
        flywheelSubsystem.spinFlywheel(0);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
