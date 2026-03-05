package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;

public class FlywheelCommand extends Command{
    public FlywheelSubsystem flywheelSubsystem;
    public double targetRPS;

    public FlywheelCommand(FlywheelSubsystem flywheelSubsystem, double targetRPS){
        this.flywheelSubsystem = flywheelSubsystem;
        this.targetRPS = targetRPS;
        addRequirements(flywheelSubsystem);
    }

    @Override
    public void initialize(){
        flywheelSubsystem.setShooterState();
        System.out.println("flywheel command initialize");
        /*if (flywheelSubsystem.getShooterState() == "windShooter") {
            flywheelSubsystem.spinFlywheel(65);//SmartDashboard.getNumber("flywheelSpeed", 40));
            SmartDashboard.putBoolean("Flywheel spinnin", true);
        }           
        else if (flywheelSubsystem.getShooterState() == "cantShoot") {
            flywheelSubsystem.stopFlywheel();
            SmartDashboard.putBoolean("Flywheel spinnin", false);
        }*/
        flywheelSubsystem.spinFlywheel(65);
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
        flywheelSubsystem.stopFlywheel();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
