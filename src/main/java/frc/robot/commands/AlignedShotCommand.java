package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.PhotonVision;
import static frc.robot.Constants.*;

public class AlignedShotCommand extends Command{
    public FlywheelSubsystem flywheelSubsystem;
    public HoodSubsystem hoodSubsystem;


    public AlignedShotCommand(FlywheelSubsystem flywheelSubsystem, HoodSubsystem hoodSubsystem){
        this.flywheelSubsystem = flywheelSubsystem;
        this.hoodSubsystem = hoodSubsystem;
        addRequirements(flywheelSubsystem);
        addRequirements(hoodSubsystem);
    }

    @Override
    public void initialize(){
        System.out.println("perfect command initialize");
        hoodSubsystem.moveHood(0);
        flywheelSubsystem.spinFlywheel(0);
    }

    @Override
    public void execute(){ 
        flywheelSubsystem.spinFlywheel(65);
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
