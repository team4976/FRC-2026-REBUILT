package frc.robot.commands;

import static frc.robot.Constants.intakeSpeed;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;

public class ReverseIntake extends Command{

    public Intake intake;

    public ReverseIntake(Intake intake){
        this.intake = intake;
        addRequirements(intake);  
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {  
    }

    @Override
    public void execute() {
        intake.runIntakeMotor(-intakeSpeed);
    }
  
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {  
        intake.runIntakeMotor(0.0);
    } 


    @Override
    public boolean isFinished() { 
        return false;
    }
    
}
