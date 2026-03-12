package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;

public class IndexAndSpindexCommand extends Command{
    public IndexAndSpindexSubsystem InSSubsystem;
    public double speed;
    public FlywheelSubsystem flywheelSubsystem;
    
    public IndexAndSpindexCommand(IndexAndSpindexSubsystem InSSubsystem, double speed, FlywheelSubsystem flywheelSubsystem){
        this.InSSubsystem = InSSubsystem;
        this.flywheelSubsystem = flywheelSubsystem;
        speed = 0.8;
        addRequirements(InSSubsystem);
    }

    @Override
    public void initialize(){

    }
    
    @Override
    public void execute() {
        if (speed < 0) {
            InSSubsystem.moveFeeder(speed);
        } else {
            if (flywheelSubsystem.shooterMotorLeader.getMotorVoltage().getValueAsDouble() > 0) {
              InSSubsystem.moveFeeder(speed);
            }
        }   

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
