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
    }

    @Override
    public void execute(){
        /* 
        switch (flywheelSubsystem.getShooterState()) {
            case "windShooter":
                flywheelSubsystem.spinFlywheel(SmartDashboard.getNumber("flywheelSpeed", 40));
                break;
            case "cantShoot":
                flywheelSubsystem.stopFlywheel();
                break;
        }
                */
    }

    @Override
    public void end(boolean isInterupted){

    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
