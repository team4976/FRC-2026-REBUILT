package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.PhotonVision;

public class FlywheelStop extends Command{
    public FlywheelSubsystem flywheelSubsystem;
    public PhotonVision photonVision;

    public FlywheelStop(FlywheelSubsystem flywheelSubsystem, PhotonVision photonVision){
        this.flywheelSubsystem = flywheelSubsystem;
        this.photonVision = photonVision;
        addRequirements(flywheelSubsystem);
    }

    @Override
    public void initialize(){
        flywheelSubsystem.cammeraSpeed = 0;
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
