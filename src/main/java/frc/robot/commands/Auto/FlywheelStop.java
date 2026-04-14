package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.PhotonVision;

public class FlywheelStop extends Command{
    public FlywheelSubsystem s_flywheel;
    public PhotonVision s_photonVision;

    public FlywheelStop(FlywheelSubsystem s_flywheel){
        this.s_flywheel = s_flywheel;
        addRequirements(s_flywheel);
    }

    @Override
    public void initialize(){
        s_flywheel.cammeraSpeed = 0;
    }

    @Override
    public void execute(){ 
    }

    @Override
    public void end(boolean isInterupted){
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
