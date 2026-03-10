package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.PhotonVision;

public class ManualFlywheelOverideCommand extends Command{
    public FlywheelSubsystem flywheelSubsystem;
    public PhotonVision photonVision;

    public ManualFlywheelOverideCommand(FlywheelSubsystem flywheelSubsystem, PhotonVision photonVision){
        this.flywheelSubsystem = flywheelSubsystem;
        this.photonVision = photonVision;
        addRequirements(flywheelSubsystem);
    }

    @Override
    public void initialize(){
        System.out.println("flywheel command initialize");
        /*if (flywheelSubsystem.getShooterState() == "cantShoot") {
            flywheelSubsystem.spinFlywheel(
                31.49597 + (10.19041 * (photonVision.getDistance() + 0.5969))  
                - (0.4148098 * Math.pow(photonVision.getDistance() + 0.5969, 2)));//SmartDashboard.getNumber("Testing/Ben T's Stuff/flywheelSpeed", 40));
            SmartDashboard.putBoolean("Flywheel spinnin", true);
        }           
        else if (flywheelSubsystem.getShooterState() == "windShooter"
              || flywheelSubsystem.getShooterState() == "readyToShoot") {
            flywheelSubsystem.spinFlywheel(0);
            SmartDashboard.putBoolean("Flywheel spinnin", false);
        }
        flywheelSubsystem.spinFlywheel(
                31.49597 + (10.19041 * (photonVision.getDistance() + 0.5969))  
                - (0.4148098 * Math.pow(photonVision.getDistance() + 0.5969, 2)));//SmartDashboard.getNumber("Testing/Ben T's Stuff/flywheelSpeed", 40));
        //flywheelSubsystem.setShooterState();*/
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
        flywheelSubsystem.spinFlywheel(
            SmartDashboard.getNumber("Testing/Ben T's Stuff/flywheelSpeed", 40));

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
