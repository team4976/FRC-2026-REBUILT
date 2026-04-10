package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.UpdateHubInfo;

public class FlywheelStart extends Command{
    public FlywheelSubsystem flywheelSubsystem;
    public PhotonVision photonVision;
    public UpdateHubInfo updateHubInfo;
    public double autoFlywheelSpeed = 0.0;
    

    public FlywheelStart(FlywheelSubsystem flywheelSubsystem, PhotonVision photonVision, UpdateHubInfo updateHubInfo){
        this.flywheelSubsystem = flywheelSubsystem;
        this.photonVision = photonVision;
        this.updateHubInfo = updateHubInfo;
        addRequirements(flywheelSubsystem);
    }

    @Override
    public void initialize(){
        flywheelSubsystem.isAutoFlywheel = true;
        if (updateHubInfo.getHubDistance() != 0) {
            autoFlywheelSpeed = (31.49597 + (10.19041 * (updateHubInfo.getHubDistance() + 0.5969))  
                - (0.4148098 * Math.pow(updateHubInfo.getHubDistance()+ 0.5969, 2))) * 0.9;
        }
        flywheelSubsystem.spinFlywheel(autoFlywheelSpeed);
    }

    @Override
    public void execute(){ 
        /*if (flywheelSubsystem.getShooterState() == "windShooter") {
            flywheelSubsystem.spinFlywheel(65);//SmartDashboard.getNumber("flywheelSpeed", 40));
            SmartDashboard.putBoolean("Flywheel spinnin", true);
        }           
        else if (flywheelSubsystem.getShooterState() == "cantShoot") {
            flywheelSubsystem.stopFlywheel();
            SmartDashboard.putBoolean("Flywheel spinnin", false);
        }*/
    }

    @Override
    public void end(boolean isInterupted){
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
