package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.Intake;

public class IndexAndSpindexCommand extends Command{
    public IndexAndSpindexSubsystem InSSubsystem;
    public double speed;
    public FlywheelSubsystem flywheelSubsystem;
    public double startingIntakeSpeed;
    public Intake intakeSubsystem;
    
    public IndexAndSpindexCommand(IndexAndSpindexSubsystem InSSubsystem, double speed, FlywheelSubsystem flywheelSubsystem, Intake intakeSubsystem){
        this.InSSubsystem = InSSubsystem;
        this.flywheelSubsystem = flywheelSubsystem;
        this.intakeSubsystem = intakeSubsystem;
        this.speed = speed;
        addRequirements(InSSubsystem);
    }

    @Override
    public void initialize(){
        startingIntakeSpeed = intakeSubsystem.currentIntakeSpeed;
    }
    
    @Override
    public void execute() {
        /* 
        if (speed < 0) {
            InSSubsystem.moveFeeder(speed);
        } else {
            if (flywheelSubsystem.shooterMotorLeader.getMotorVoltage().getValueAsDouble() > 0) {
              InSSubsystem.moveFeeder(speed);
              intakeSubsystem.runIntakeMotor(-0.90);
            }
        }   
            */
        InSSubsystem.moveFeeder(speed);
        if (speed > 0){
            intakeSubsystem.runIntakeMotor(-0.90);
        }
    }

    @Override
    public void end(boolean interrupted) {
        InSSubsystem.moveFeeder(0.0);
        intakeSubsystem.runIntakeMotor(startingIntakeSpeed);
    }

    @Override
    public boolean isFinished() {
       return false;
    }
}
