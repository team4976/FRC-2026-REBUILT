package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeRetract extends Command {

    public IntakeSubsystem s_intake;

    public IntakeRetract(IntakeSubsystem intakeSubsystem){
        this.s_intake = intakeSubsystem;
        addRequirements(s_intake);
    }

    @Override
    public void initialize() {
        s_intake.retract();
        super.initialize();
    }


    @Override
    public void end(boolean interrupted) {  
        s_intake.stopIntakeArms();
    }

    @Override
    public boolean isFinished() {
        return s_intake.bothArmsAtMin.getAsBoolean();
    }
    
}
