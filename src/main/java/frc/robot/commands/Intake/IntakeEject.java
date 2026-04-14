package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.IntakeSubsystem.IntakeStates;

public class IntakeEject extends Command {
    public IntakeSubsystem s_intake;

    public IntakeEject(IntakeSubsystem intakeSubsystem){
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
