

package frc.robot.commands;
import java.util.OptionalDouble;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.PhotonVision;
import static frc.robot.Constants.*;

public class TurretScanYaw extends Command {
    PhotonVision m_turretVision;
    TurretSubsystem m_shooter;
    boolean TurningRight = true; // flag in scan to determine if the turret should be turning right or left
    boolean hasTargets = false; // flag to track if the turret see's an april tag
    double distance; // distance from the hub to the turret
    boolean stopLockedOn = false; // flag to track if the turret is hitting the limit switch in lockedOn mode
    Field2d field2d; // our estimated position on the field
    double turretYaw;
    double turretPosition;
    public double autoLockedOn;
    public double manualLockedOn;
    public double tagRotation;

    public TurretScanYaw(PhotonVision turretVision, TurretSubsystem shooter){
        m_turretVision = turretVision;
        addRequirements(turretVision);

        m_shooter = shooter;
        addRequirements(shooter);

        field2d = new Field2d();
    }

    @Override
    public void initialize() {
        TurningRight = true;
        hasTargets = false;
        m_shooter.stopbutton = false;
        stopLockedOn = false;

    }

    @Override
    public void execute() {
        SmartDashboard.putBoolean("TurningRight", TurningRight);
        //get if the robot is seeing the april tag
        hasTargets = m_turretVision.targetVisible();
        // set the yaw to what the yaw of the april tag is

        operatorController.setRumble(RumbleType.kBothRumble, 0.2);

        if(hasTargets == false || stopLockedOn == true){
            stopLockedOn = false;
            System.err.println("hasTargets = false");
            m_shooter.stopTurn();
        }
        else{
            field2d = m_turretVision.getDistanceAndAngle();
            System.err.println("hasTargets = false");

            distance = m_turretVision.getTurretDistance();

            turretPosition = m_shooter.getEncoderValue();
        
            // if left or right switch is pressed while we see a target set stopLockedOn to true
            if(m_shooter.getLeftSwitch() == false || m_shooter.getRightSwitch() == false ||
                m_shooter.getEncoderValue() < Constants.turretLimitRight ||
                 m_shooter.getEncoderValue() > Constants.turretLimitLeft){
                stopLockedOn = true;
                //System.out.println("stopLockedOn " + stopLockedOn);
            }

        //Setting the voltage of the motor to the yaw of the target multiplied by 5
        OptionalDouble yaw = m_turretVision.getTargetYaw(Constants.hubId);
        if (yaw.isEmpty()) return;

        tagRotation = m_turretVision.getTargetZRotation(Constants.hubId);

        turretYaw = yaw.getAsDouble();
        Double speedAdjust = 5.0;
        autoLockedOn = (turretYaw)/45*-speedAdjust;

        if (operatorController.axisGreaterThan(4, 0.3).getAsBoolean()){
            manualLockedOn = operatorController.getRightX() * -1;
        } else if (operatorController.axisLessThan(4, -0.3).getAsBoolean()) {
            if (autoLockedOn <= -0.6) {
                manualLockedOn = operatorController.getRightX() * -2;
            } else if (autoLockedOn > -0.5) {
                manualLockedOn = operatorController.getRightX() * -1;
            }
        }

        double totalLockedOn = autoLockedOn + manualLockedOn;

        if(totalLockedOn > 0.7) totalLockedOn = 0.7;

        else if(totalLockedOn < -0.7) totalLockedOn = -0.7;

        m_shooter.lockedOn(totalLockedOn);

        }
    }

    @Override
    public void end(boolean interrupted) {
        m_shooter.stopTurn();
        operatorController.setRumble(RumbleType.kBothRumble, 0.0);
        //System.err.println(Constants.turretManualVoltage);
    }

    @Override
    public boolean isFinished() {
        // if rightTrigger is pressed or stopLocked = true then end command
    if(m_shooter.stopbutton == true){
            return true;
    }
    else{
        return false;
    }
    }
    
}
