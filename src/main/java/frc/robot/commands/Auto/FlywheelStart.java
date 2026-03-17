package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.PhotonVision;

public class FlywheelStart extends Command{
    public FlywheelSubsystem flywheelSubsystem;
    public PhotonVision photonVision;
    

    public FlywheelStart(FlywheelSubsystem flywheelSubsystem, PhotonVision photonVision){
        this.flywheelSubsystem = flywheelSubsystem;
        this.photonVision = photonVision;
        addRequirements(flywheelSubsystem);
    }

    @Override
    public void initialize(){
        flywheelSubsystem.isAutoFlywheel = true;
        flywheelSubsystem.spinFlywheel(50);
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
