package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.HoodSubsystem;

public class HoodCommand extends Command{
    public HoodSubsystem hoodSubsystem;
    public boolean HoodOverride;
    
    public HoodCommand(HoodSubsystem hoodSubsystem, boolean HoodOverride){
        this.hoodSubsystem = hoodSubsystem;
        this.HoodOverride = HoodOverride;
        addRequirements(hoodSubsystem);
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        /*if (HoodOverride == true) {
            hoodSubsystem.forceHoodMove(hoodSpeed);
        }
        else {
            hoodSubsystem.moveHood(SmartDashboard.getNumber("Testing/Ben T's Stuff/hood target position", 4)); 
        }*/
        hoodSubsystem.moveHood(SmartDashboard.getNumber("Testing/Ben T's Stuff/hood target position", 4)); 
    }

    @Override
    public void end(boolean interrupted) {
        hoodSubsystem.moveHood(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
