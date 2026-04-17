package frc.robot.commands.Turret;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.TurretSubsystem;



public class TurretReset extends Command {
    TurretSubsystem s_turret;

    public TurretReset(TurretSubsystem s_turret){
        this.s_turret = s_turret;
        addRequirements(s_turret);

    }

    @Override
    public void initialize() {
        s_turret.resetEncoder();
        System.out.println("Reset Turret Pose/Encoder");
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
