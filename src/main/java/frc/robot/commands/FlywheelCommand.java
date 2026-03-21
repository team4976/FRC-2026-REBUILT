package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.UpdateHubInfo;

import static frc.robot.Constants.*;

public class FlywheelCommand extends Command{
    public FlywheelSubsystem flywheelSubsystem;
    public UpdateHubInfo updateHubInfo;
    public boolean isOverriden;
    public double autoFlywheelSpeed;
    public double manualFlywheelSpeed;
    public double totalFlywheelSpeed;

    public FlywheelCommand(FlywheelSubsystem flywheelSubsystem, UpdateHubInfo updateHubInfo, boolean isOverriden){
        this.flywheelSubsystem = flywheelSubsystem;
        this.updateHubInfo= updateHubInfo;
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
        //Sets the auto flywheel speed
        if (updateHubInfo.getHubDistance() != 0) {
            autoFlywheelSpeed = (31.49597 + (10.19041 * (updateHubInfo.getHubDistance() + 0.5969))  
                - (0.4148098 * Math.pow(updateHubInfo.getHubDistance()+ 0.5969, 2))) * 0.9;
        } else {
            autoFlywheelSpeed = 50;
        }

        //Sets the adder/substractor to the flywheel speed
        if (operatorController.axisGreaterThan(1, 0.1).getAsBoolean()){
            manualFlywheelSpeed = operatorController.getLeftY() * -8;
        } else if (operatorController.axisLessThan(1, -0.1).getAsBoolean()) {
            manualFlywheelSpeed = operatorController.getLeftY() * -8;
        }
        
        //adds the flywheel speeds then spinds the flywheel
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
