package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.PhotonVision;

public class TurretRight extends Command {
    PhotonVision m_turretVision;
    TurretSubsystem m_shooter;
    boolean stopSwitch;

    public TurretRight(PhotonVision turretVision, TurretSubsystem shooter){
        m_turretVision = turretVision;
        addRequirements(turretVision);

         m_shooter = shooter;
        addRequirements(shooter);

        stopSwitch = false;
    }

    @Override
    public void initialize(){
        stopSwitch = false;
        m_shooter.turnRight(Constants.turretManualVoltage);
    }

    @Override
    public void execute(){
        // if the switch on the right side of the bot is being hit than set stopSwitch to true
        if(m_shooter.getRightSwitch() == false || m_shooter.getEncoderValue() < Constants.turretLimitRight){
            stopSwitch = true;
        }

    }

    @Override
    public void end(boolean interrupted){
        m_shooter.stopTurn();
        stopSwitch = false;
    }

    @Override
    public boolean isFinished(){
        // if stopSwitch is true stop command
        return stopSwitch;
    }

}
