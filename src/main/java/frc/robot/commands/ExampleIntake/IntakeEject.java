package frc.robot.commands.ExampleIntake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Example_IntakeSubsystem;
import frc.robot.subsystems.Example_IntakeSubsystem.IntakeStates;

public class IntakeEject extends Command {
    public Example_IntakeSubsystem s_intake;

    public IntakeEject(Example_IntakeSubsystem intakeSubsystem){
        this.s_intake = intakeSubsystem;
        addRequirements(s_intake);
    }

    @Override
    public void initialize() {
        s_intake.eject();
        super.initialize();
    }

    @Override
    public void end(boolean interrupted) {  
        if(s_intake.getIntakeMotorState() == IntakeStates.idle)
            s_intake.stopIntakeMotor();
    }

    @Override
    public boolean isFinished() {
        return s_intake.getIntakeMotorState() != IntakeStates.ejecting;
    }
}
