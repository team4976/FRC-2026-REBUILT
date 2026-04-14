package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;

public class AutoIndexAndSpindexCommand extends Command{
    public IndexAndSpindexSubsystem s_indexAndSpinex;
    public double speed;
    public FlywheelSubsystem s_flywheel;
    public IntakeSubsystem s_intake;
    public double currentIntakeSpeed;
    
    public AutoIndexAndSpindexCommand(IndexAndSpindexSubsystem InSSubsystem, double speed, FlywheelSubsystem flywheelSubsystem, IntakeSubsystem intakeSubsystem){
        this.s_indexAndSpinex = InSSubsystem;
        this.s_flywheel = flywheelSubsystem;
        this.speed = speed;
        this.s_intake = intakeSubsystem;
        
        addRequirements(InSSubsystem);
    }

    @Override
    public void initialize(){
        s_indexAndSpinex.moveFeeder(speed);
        //currentIntakeSpeed = s_intake.currentIntakeSpeed.getAsDouble();
        if (s_flywheel.m_flywheelLeader.getMotorVoltage().getValueAsDouble() > 0) {
            //s_intake.runIntakeMotor(-0.95);
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
