package frc.robot.commands.ExampleIntake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Example_IntakeSubsystem;

public class IntakeRetract extends Command {

    public Example_IntakeSubsystem s_intake;

    public IntakeRetract(Example_IntakeSubsystem intakeSubsystem){
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
