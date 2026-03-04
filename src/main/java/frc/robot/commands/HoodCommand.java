package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.HoodSubsystem;

public class HoodCommand extends Command{
    public HoodSubsystem hoodSubsystem;
    public boolean HoodOverride;
    public double hoodSpeed;
    
    public HoodCommand(HoodSubsystem hoodSubsystem, boolean HoodOverride, double hoodSpeed){
        this.hoodSubsystem = hoodSubsystem;
        this.HoodOverride = HoodOverride;
        this.hoodSpeed = hoodSpeed;
        addRequirements(hoodSubsystem);
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        if (HoodOverride) {
            hoodSubsystem.forceHoodMove(hoodSpeed);
        }
        else {
            hoodSubsystem.moveHood(SmartDashboard.getNumber("hood target position", 4)); 
        }
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
