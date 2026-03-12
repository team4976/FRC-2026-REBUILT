

package frc.robot.commands;
import static frc.robot.Constants.yaw;

import java.util.OptionalDouble;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.TurretMovement;
import frc.robot.subsystems.PhotonVision;

public class TurretScanYaw extends Command {
    PhotonVision m_turretVision;
    TurretMovement m_shooter;
    boolean TurningRight = true; // flag in scan to determine if the turret should be turning right or left
    boolean hasTargets = false; // flag to track if the turret see's an april tag
    double distance; // distance from the hub to the turret
    boolean stopLockedOn = false; // flag to track if the turret is hitting the limit switch in lockedOn mode
    Field2d field2d; // our estimated position on the field
    double turretyaw;
    double turretPosition;

    public TurretScanYaw(PhotonVision turretVision, TurretMovement shooter){
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
        Constants.stopbutton = false;
        stopLockedOn = false;

    }

    @Override
    public void execute() {
        SmartDashboard.putBoolean("TurningRight", TurningRight);
        //get if the robot is seeing the april tag
        hasTargets = m_turretVision.targetVisible();
        // set the yaw to what the yaw of the april tag is


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

        turretyaw= yaw.getAsDouble();
        Double speedAdjust = 5.0;
        m_shooter.lockedOn(Math.max(((turretyaw)/45*-speedAdjust),0.75));
        System.out.println(Constants.turretManualVoltage);


        }
    }

    @Override
    public void end(boolean interrupted) {
        m_shooter.stopTurn();
        //System.err.println(Constants.turretManualVoltage);
    }

    @Override
    public boolean isFinished() {
        // if rightTrigger is pressed or stopLocked = true then end command
    if(Constants.stopbutton == true){
            return true;
    }
    else{
        return false;
    }
    }
    
}
