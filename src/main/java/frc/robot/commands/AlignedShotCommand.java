package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.HoodSubsystem;

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
        hoodSubsystem.moveHood(0);
        flywheelSubsystem.spinFlywheel(0);
        flywheelSubsystem.isAutoFlywheel = true;
    }

    @Override
    public void execute(){ 
        flywheelSubsystem.spinFlywheel(65);
    }

    @Override
    public void end(boolean isInterupted){
        flywheelSubsystem.spinFlywheel(0);
        flywheelSubsystem.isAutoFlywheel = false;

    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
