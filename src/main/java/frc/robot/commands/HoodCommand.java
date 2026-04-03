package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.UpdateHubInfo;

import static frc.robot.Constants.*;

public class HoodCommand extends Command{
    public HoodSubsystem hoodSubsystem;
    public UpdateHubInfo updateHubInfo;
    public boolean HoodOverride;
    public double hoodSpeed;
    
    public HoodCommand(HoodSubsystem hoodSubsystem, UpdateHubInfo updateHubInfo, boolean HoodOverride, double hoodSpeed){
        this.hoodSubsystem = hoodSubsystem;
        this.updateHubInfo = updateHubInfo;
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
            hoodSubsystem.moveHood(SmartDashboard.getNumber("Testing/Ben T's Stuff/hood target position", 4));    
            SmartDashboard.putString("hood testing", "hood move up");            
        }
    }

    @Override
    public void end(boolean interrupted) {
        if (HoodOverride) {
            hoodSubsystem.forceHoodMove(0);
        }
        else {
            if (operatorController.povUp().getAsBoolean() == false
             && operatorController.povDown().getAsBoolean() == false) {
                hoodSubsystem.moveHood(0);
                SmartDashboard.putString("hood testing", "hood went to 0");
            }
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
