package frc.robot.commands.ExampleIntake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Example_IntakeSubsystem;
import frc.robot.subsystems.Example_IntakeSubsystem.IntakeStates;

public class IntakeSwap extends Command {
    public Example_IntakeSubsystem s_intake;

    public IntakeSwap(Example_IntakeSubsystem intakeSubsystem){
        this.s_intake = intakeSubsystem;
        addRequirements(s_intake);
    }

    @Override
    public void initialize() {
        if(s_intake.getArmIntakeState() == IntakeStates.extending || 
            s_intake.bothArmsAtMax.getAsBoolean()){
                s_intake.retract();
        }
        else if(s_intake.getArmIntakeState() == IntakeStates.retracting|| 
            s_intake.bothArmsAtMin.getAsBoolean()){
                s_intake.extend();
        }
        super.initialize();
    }


    @Override
    public void end(boolean interrupted) {  
        s_intake.stopIntakeArms();    
    }

    @Override
    public boolean isFinished() {
        return s_intake.getArmIntakeState() == IntakeStates.idle || 
            (s_intake.bothArmsAtMax.getAsBoolean() &&  s_intake.getArmIntakeState() == IntakeStates.extending) ||
            (s_intake.bothArmsAtMin.getAsBoolean() &&  s_intake.getArmIntakeState() == IntakeStates.retracting);
    }
}
