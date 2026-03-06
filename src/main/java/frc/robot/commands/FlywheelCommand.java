package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;

public class FlywheelCommand extends Command{
    public FlywheelSubsystem flywheelSubsystem;

    public FlywheelCommand(FlywheelSubsystem flywheelSubsystem){
        this.flywheelSubsystem = flywheelSubsystem;
        addRequirements(flywheelSubsystem);
    }

    @Override
    public void initialize(){
        System.out.println("flywheel command initialize");
        if (flywheelSubsystem.getShooterState() == "cantShoot") {
            flywheelSubsystem.spinFlywheel(SmartDashboard.getNumber("Testing/Ben T's Stuff/flywheelSpeed", 40));
            SmartDashboard.putBoolean("Flywheel spinnin", true);
        }           
        else if (flywheelSubsystem.getShooterState() == "windShooter"
              || flywheelSubsystem.getShooterState() == "readyToShoot") {
            flywheelSubsystem.spinFlywheel(0);
            SmartDashboard.putBoolean("Flywheel spinnin", false);
        }
        flywheelSubsystem.setShooterState();
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
        //flywheelSubsystem.spinFlywheel(0);
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
