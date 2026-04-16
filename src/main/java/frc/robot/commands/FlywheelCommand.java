package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.UpdateHubInfo;
import static frc.robot.Constants.*;

public class FlywheelCommand extends Command{
    public UpdateHubInfo s_updateHubInfo;
    public FlywheelSubsystem s_flywheel;

    public double manualFlywheelSpeed;
    public double totalFlywheelSpeed;
    public double autoFlywheelSpeed;

    public FlywheelCommand(FlywheelSubsystem flywheelSubsystem, UpdateHubInfo updateHubInfo){
        this.s_flywheel = flywheelSubsystem;
        this.s_updateHubInfo = updateHubInfo;
        addRequirements(flywheelSubsystem);
        this.s_flywheel = flywheelSubsystem;
        this.s_updateHubInfo = updateHubInfo;
    }

    @Override
    public void initialize(){
        s_flywheel.cammeraSpeed = 0;
    }

    @Override
    public void execute(){ 
        //Sets the auto flywheel speed
        //this equation is for the quadratic made by the relation of the distance to flywheel speed. 
        //the y is the flywheel speed and the x is the distance from the target 
        //(if we dont use a turret camera then the x needs to be changed to the calculated distance of the robot from the hub)
        if (s_updateHubInfo.getHubDistance() != 0) {
            /* 
            autoFlywheelSpeed = (31.49597 + (10.19041 * (s_updateHubInfo.getHubDistance() + 0.5969))  
                - (0.4148098 * Math.pow(s_updateHubInfo.getHubDistance()+ 0.5969, 2))) * 0.9;
                */
            autoFlywheelSpeed = s_flywheel.speedTable.get(s_updateHubInfo.getHubDistance()) * 0.9;
        }

        //Sets the adder/substractor to the flywheel speed
        if (operatorController.axisMagnitudeGreaterThan(1, 0.1).getAsBoolean()){
            manualFlywheelSpeed = operatorController.getLeftY() * -8;
        }
        
        //adds the flywheel speeds then sends it to the subsystem
        totalFlywheelSpeed = manualFlywheelSpeed + autoFlywheelSpeed;
        s_flywheel.cammeraSpeed = totalFlywheelSpeed;
    }

    @Override
    public void end(boolean isInterupted){
        s_flywheel.cammeraSpeed = 0;
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
