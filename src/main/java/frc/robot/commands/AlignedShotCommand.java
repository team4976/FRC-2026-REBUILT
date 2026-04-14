package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.FlywheelSubsystem;

public class AlignedShotCommand extends Command{
    public FlywheelSubsystem s_flywheel;

    public AlignedShotCommand(FlywheelSubsystem s_flywheel){
        this.s_flywheel = s_flywheel;
        addRequirements(s_flywheel);
    }

    @Override
    public void initialize(){
        s_flywheel.cammeraSpeed = 0;
    }

    @Override
    public void execute(){ 
        s_flywheel.cammeraSpeed = 65;
    }

    @Override
    public void end(boolean isInterupted){
        s_flywheel.cammeraSpeed = 0;
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
