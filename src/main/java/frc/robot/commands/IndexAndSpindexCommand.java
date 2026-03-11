package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IndexAndSpindexSubsystem;

public class IndexAndSpindexCommand extends Command{
    public IndexAndSpindexSubsystem InSSubsystem;
    public double speed;
    
    public IndexAndSpindexCommand(IndexAndSpindexSubsystem InSSubsystem, double speed){
        this.InSSubsystem = InSSubsystem;
        this.speed = speed;
        addRequirements(InSSubsystem);
    }

    @Override
    public void initialize(){

    }
    
    @Override
    public void execute() {
        InSSubsystem.moveFeeder(speed);
    }

    @Override
    public void end(boolean interrupted) {
        InSSubsystem.moveFeeder(0.0);
    }

    @Override
    public boolean isFinished() {
       return false;
    }
}
