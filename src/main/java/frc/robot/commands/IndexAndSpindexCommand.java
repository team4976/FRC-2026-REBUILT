package frc.robot.commands;

import static frc.robot.Constants.intakeSpeed;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.Intake;

public class IndexAndSpindexCommand extends Command{
    public IndexAndSpindexSubsystem InSSubsystem;
    public double speed;
    public FlywheelSubsystem flywheelSubsystem;
    public double currentIntakeSpeed;
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
        currentIntakeSpeed = intakeSubsystem.currentIntakeSpeed;
        
    }
    
    @Override
    public void execute() {
        if (speed < 0) {
            InSSubsystem.moveFeeder(speed);
        } else {
            if (flywheelSubsystem.shooterMotorLeader.getMotorVoltage().getValueAsDouble() > 0) {
              InSSubsystem.moveFeeder(speed);
              intakeSubsystem.runIntakeMotor(.95);
            }
        }   

    }

    @Override
    public void end(boolean interrupted) {
        InSSubsystem.moveFeeder(0.0);
        intakeSubsystem.runIntakeMotor(currentIntakeSpeed);
    }

    @Override
    public boolean isFinished() {
       return false;
    }
}
