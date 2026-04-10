package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;

public class AlignedShotCommand extends Command{
    public FlywheelSubsystem flywheelSubsystem;


    public AlignedShotCommand(FlywheelSubsystem flywheelSubsystem){
        this.flywheelSubsystem = flywheelSubsystem;
        addRequirements(flywheelSubsystem);
    }

    @Override
    public void initialize(){
        flywheelSubsystem.cammeraSpeed = 0;
    }

    @Override
    public void execute(){ 
        flywheelSubsystem.cammeraSpeed = 65;
    }

    @Override
    public void end(boolean isInterupted){
        flywheelSubsystem.cammeraSpeed = 0;
        //flywheelSubsystem.isAutoFlywheel = false;

    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
