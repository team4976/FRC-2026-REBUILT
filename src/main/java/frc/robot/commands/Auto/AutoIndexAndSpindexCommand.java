package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;

public class AutoIndexAndSpindexCommand extends Command{
    public IndexAndSpindexSubsystem InSSubsystem;
    public double speed;
    public FlywheelSubsystem flywheelSubsystem;
    
    public AutoIndexAndSpindexCommand(IndexAndSpindexSubsystem InSSubsystem, double speed, FlywheelSubsystem flywheelSubsystem){
        this.InSSubsystem = InSSubsystem;
        this.flywheelSubsystem = flywheelSubsystem;
        this.speed = speed;
        addRequirements(InSSubsystem);
    }

    @Override
    public void initialize(){
        InSSubsystem.moveFeeder(speed);
    }
    
    
    @Override
    public void execute() {  

    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
       return true;
    }
}
