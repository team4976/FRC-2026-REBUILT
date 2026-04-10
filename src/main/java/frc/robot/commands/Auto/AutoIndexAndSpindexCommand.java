package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class AutoIndexAndSpindexCommand extends Command{
    public IndexAndSpindexSubsystem InSSubsystem;
    public double speed;
    public FlywheelSubsystem flywheelSubsystem;
    public IntakeSubsystem intakeSubsystem;
    public double currentIntakeSpeed;
    
    public AutoIndexAndSpindexCommand(IndexAndSpindexSubsystem InSSubsystem, double speed, FlywheelSubsystem flywheelSubsystem, IntakeSubsystem intakeSubsystem){
        this.InSSubsystem = InSSubsystem;
        this.flywheelSubsystem = flywheelSubsystem;
        this.speed = speed;
        this.intakeSubsystem = intakeSubsystem;
        
        addRequirements(InSSubsystem);
    }

    @Override
    public void initialize(){
        InSSubsystem.moveFeeder(speed);
        currentIntakeSpeed = intakeSubsystem.currentIntakeSpeed.getAsDouble();
        if (flywheelSubsystem.fx_leader.getMotorVoltage().getValueAsDouble() > 0) {
            intakeSubsystem.runIntakeMotor(-0.95);
        }
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
