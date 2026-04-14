package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.UpdateHubInfo;
import frc.robot.subsystems.PhotonVision;

public class FlywheelStart extends Command{
    public FlywheelSubsystem s_flywheel;
    public PhotonVision s_photonVision;
    public UpdateHubInfo s_updateHubInfo;
    public double autoFlywheelSpeed = 0.0;
    

    public FlywheelStart(FlywheelSubsystem s_flywheel, PhotonVision s_photonVision, UpdateHubInfo s_updateHubInfo){
        this.s_flywheel = s_flywheel;
        this.s_photonVision = s_photonVision;
        this.s_updateHubInfo = s_updateHubInfo;
        addRequirements(s_flywheel);
    }

    @Override
    public void initialize(){
        //A Quadratic to get the flywheel speed based on our distance from the hub. the y value is the flywheel speed and the x is the distance from the hub.
        if (s_updateHubInfo.getHubDistance() != 0) {
            autoFlywheelSpeed = (31.49597 + (10.19041 * (s_updateHubInfo.getHubDistance() + 0.5969))  
                - (0.4148098 * Math.pow(s_updateHubInfo.getHubDistance()+ 0.5969, 2))) * 0.9;
        }
        s_flywheel.cammeraSpeed = autoFlywheelSpeed;
    }

    @Override
    public void execute(){ 
    }

    @Override
    public void end(boolean isInterupted){
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
