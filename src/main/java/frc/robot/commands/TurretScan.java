

package frc.robot.commands;
import java.util.OptionalDouble;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.TurretMovement;
import frc.robot.subsystems.PhotonVision;
import static frc.robot.Constants.*;

public class TurretScan extends Command {
    PhotonVision m_turretVision;
    TurretMovement m_shooter;
    boolean TurningRight = true; // flag in scan to determine if the turret should be turning right or left
    boolean hasTargets = false; // flag to track if the turret see's an april tag
    double distance; // distance from the hub to the turret
    boolean stopLockedOn = false; // flag to track if the turret is hitting the limit switch in lockedOn mode
    Field2d field2d; // our estimated position on the field
    double turretTargetAngle; // the angle we want the turret to be at so that we are aiming at the hub
    double turretAngle; // the turret angle we are currently at
    double turretPosition; // the encoder value of the tuurets motor
    double turretTargetPosition;
    double manualLockedOn;

    public TurretScan(PhotonVision turretVision, TurretMovement shooter){
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
        //System.err.println("execute works");
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

            // gets the turret angle relative to the field
            turretAngle = m_turretVision.getTurretAngle();
            // gets the angle we want to be at to be facing the hub
            turretTargetAngle = m_turretVision.getTurretTargetAngle();

            distance = m_turretVision.getTurretDistance();

            turretPosition = m_shooter.getEncoderValue();

            //turretTargetPosition = m_shooter.convertAngleRotation(turretTargetAngle-turretAngle);

            //m_shooter.turretRotationPID(turretPosition+turretTargetPosition);


            SmartDashboard.putNumber("turretDistance", distance);
            SmartDashboard.putNumber("turretPoseX", field2d.getRobotPose().getX());
            SmartDashboard.putNumber("turretPoseY", field2d.getRobotPose().getY());
            SmartDashboard.putNumber("turretRotation", field2d.getRobotPose().getRotation().getDegrees());
            SmartDashboard.putNumber("targetAngle", turretTargetAngle);
            SmartDashboard.putNumber("turretAngle", turretAngle);
            SmartDashboard.putNumber("turretTargetAngle", turretTargetAngle);
        
            // if left or right switch is pressed while we see a target set stopLockedOn to true
            if(m_shooter.getLeftSwitch() == false || m_shooter.getRightSwitch() == false ||
                m_shooter.getEncoderValue() < Constants.turretLimitRight ||
                 m_shooter.getEncoderValue() > Constants.turretLimitLeft){
                stopLockedOn = true;
                //System.out.println("stopLockedOn " + stopLockedOn);
            }

        Double speedAdjust = 5.0;
        double autoLockedOn = Math.max((turretTargetAngle-turretAngle)/45*speedAdjust, 0.75);
        if (operatorController.axisGreaterThan(5, 0.3).getAsBoolean()){
            manualLockedOn = operatorController.getRightX() * -1;
        } else if (operatorController.axisLessThan(5, -0.3).getAsBoolean()) {
            manualLockedOn = operatorController.getRightX() * -1;
        }
        double totalLockedOn = autoLockedOn + manualLockedOn;
        m_shooter.lockedOn(totalLockedOn);
        System.out.println(Constants.turretManualVoltage);
        }
        m_turretVision.turretAngle = turretAngle;
        m_turretVision.turretTargetAngle = turretTargetAngle;
        
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
