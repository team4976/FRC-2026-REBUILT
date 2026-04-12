package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class IndexAndSpindexCommand extends Command{
    public IndexAndSpindexSubsystem s_indexAndSpindex;
    public FlywheelSubsystem s_flywheel;
    public IntakeSubsystem s_intake;

    public double startingIntakeSpeed;
    public double speed;
    
    public IndexAndSpindexCommand(IndexAndSpindexSubsystem s_indexAndSpindex, double speed, FlywheelSubsystem s_flywheel, IntakeSubsystem s_intake){
        this.s_indexAndSpindex = s_indexAndSpindex;
        this.s_flywheel = s_flywheel;
        this.s_intake = s_intake;
        this.speed = speed;
        addRequirements(s_indexAndSpindex);
    }

    @Override
    public void initialize(){
        startingIntakeSpeed = s_intake.currentIntakeSpeed.getAsDouble();
    }
    
    @Override
    public void execute() {
        s_indexAndSpindex.moveFeeder(speed);
        if (speed > 0){
            s_intake.runIntakeMotor(-0.90);
        }
    }

    @Override
    public void end(boolean interrupted) {
        s_indexAndSpindex.moveFeeder(0.0);
        s_intake.runIntakeMotor(startingIntakeSpeed);
    }

    @Override
    public boolean isFinished() {
       return false;
    }
}
