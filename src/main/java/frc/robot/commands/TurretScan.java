

package frc.robot.commands;
import java.util.OptionalDouble;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.PhotonVision;
import static frc.robot.Constants.*;

public class TurretScan extends Command {
    PhotonVision turretVision;
    TurretSubsystem shooter;
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
    double autoLockedOn = 0.0;

    public TurretScan(PhotonVision turretVision, TurretSubsystem shooter){
        this.turretVision = turretVision;
        addRequirements(turretVision);

        this.shooter = shooter;
        addRequirements(shooter);

        field2d = new Field2d();
    }

    @Override
    public void initialize() {
        TurningRight = true;
        hasTargets = false;
        shooter.stopbutton = false;
        stopLockedOn = false;
        shooter.isAutoAiming = true;
    }

    @Override
    public void execute() {
        //SmartDashboard.putBoolean("TurningRight", TurningRight);

        //get if the robot is seeing the april tag
        hasTargets = turretVision.targetVisible();
        // set the yaw to what the yaw of the april tag is


        if(hasTargets == false || stopLockedOn == true){
            stopLockedOn = false;
            System.err.println("hasTargets = false");
            shooter.stopTurn();
            autoLockedOn = 0;
        }
        else{
            field2d = turretVision.getDistanceAndAngle();
            System.err.println("hasTargets = true");

            // gets the turret angle relative to the field
            turretAngle = turretVision.getTurretAngle();
            // gets the angle we want to be at to be facing the hub
            turretTargetAngle = turretVision.getTurretTargetAngle();

            distance = turretVision.getTurretDistance();

            turretPosition = shooter.getEncoderValue();

            SmartDashboard.putNumber("Test/turretDistance", distance);
            SmartDashboard.putNumber("Test/turretPoseX", field2d.getRobotPose().getX());
            SmartDashboard.putNumber("Test/turretPoseY", field2d.getRobotPose().getY());
            SmartDashboard.putNumber("Test/turretRotation", field2d.getRobotPose().getRotation().getDegrees());
            SmartDashboard.putNumber("Test/targetAngle", turretTargetAngle);
            SmartDashboard.putNumber("Test/turretAngle", turretAngle);
            SmartDashboard.putNumber("Test/turretTargetAngle", turretTargetAngle);

             
        
            // if left or right switch is pressed while we see a target set stopLockedOn to true
            if(shooter.getLeftSwitch() == false || shooter.getRightSwitch() == false ||
                shooter.getEncoderValue() < Constants.turretLimitRight ||
                 shooter.getEncoderValue() > Constants.turretLimitLeft){
                stopLockedOn = true;
                //System.out.println("stopLockedOn " + stopLockedOn);
            }

        Double speedAdjust = 5.0;
        autoLockedOn = (turretTargetAngle-turretAngle)/45*speedAdjust;
        }
        
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
        shooter.lockedOn(totalLockedOn);
        SmartDashboard.putNumber("Testing/Total Turret Voltage", totalLockedOn);
        turretVision.turretAngle = turretAngle;
        turretVision.turretTargetAngle = turretTargetAngle;
        
        
    }

    @Override
    public void end(boolean interrupted) {
        shooter.isAutoAiming = false;
        shooter.stopTurn();
        //System.err.println(Constants.turretManualVoltage);
    }

    @Override
    public boolean isFinished() {
        // if rightTrigger is pressed or stopLocked = true then end command
        
        //return m_turretVision.AutoShootFlag.getAsBoolean();
        
         if(shooter.stopbutton == true){
            return true;
        }
        else{
            return false;
        }
    }
    
}
