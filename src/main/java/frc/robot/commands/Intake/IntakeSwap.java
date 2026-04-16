package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.IntakeSubsystem.IntakeStates;

public class IntakeSwap extends Command {
    public IntakeSubsystem s_intake;

    public IntakeSwap(IntakeSubsystem intakeSubsystem){
        this.s_intake = intakeSubsystem;
        addRequirements(s_intake);
    }

    @Override
    public void initialize() {
        if(s_intake.getArmIntakeState() == IntakeStates.extending || 
            s_intake.bothArmsAtMax.getAsBoolean()){
                s_intake.retract();
        }
        else if(s_intake.getArmIntakeState() == IntakeStates.retracting || 
            s_intake.bothArmsAtMin.getAsBoolean()){
                s_intake.extend();
        }
        else if(!s_intake.bothArmsAtMax.getAsBoolean()){
            s_intake.extend();
        }
        super.initialize();
    }


    @Override
    public void end(boolean interrupted) {  
        //s_intake.stopIntakeArms();    
    }

    @Override
    public boolean isFinished() {
        return true;/*(s_intake.getArmIntakeState() == IntakeStates.idle || 
            (s_intake.bothArmsAtMax.getAsBoolean() &&  s_intake.getArmIntakeState() == IntakeStates.extending) ||
            (s_intake.bothArmsAtMin.getAsBoolean() &&  s_intake.getArmIntakeState() == IntakeStates.retracting));
        */
    }
}
