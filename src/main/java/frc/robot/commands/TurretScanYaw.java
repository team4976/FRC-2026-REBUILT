

package frc.robot.commands;
import java.util.OptionalDouble;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.PhotonVision;
import static frc.robot.Constants.*;

public class TurretScanYaw extends Command {
    PhotonVision turretVision;
    TurretSubsystem turretSubsystem;
    boolean hasTargets = false; // flag to track if the turret see's an april tag
    boolean stopLockedOn = false; // flag to track if the turret is hitting the limit switch in lockedOn mode
    double turretYaw;
    /**
     * The voltage to move the turret at, automatically set when seeing an apriltag
     */
    public double autoTurretVoltage;
    /**
     * The voltage manually added to the turret during auto aim to correct for errors in the auto. <b> NOT </b> a full manual override.
     * Look at {@link TurretSubsystem} in the periodic() for the full manual override.
     */
    public double manualTurretVoltage;
    /**
     * The tag IDs we want to be able to find the yaw for during the auto aim 
     * <p> Tags: {@code 10, 26}
     */
    int[] tagIDs = new int[]{10, 26};

    /**
     * Not used but will be used for triangulating the hub center if we stick with yaw in the future
     */
    public double tagRotation;


    public TurretScanYaw(PhotonVision turretVision, TurretSubsystem turretSubsystem){
        this.turretVision = turretVision;
        addRequirements(turretVision);

        this.turretSubsystem = turretSubsystem;
        addRequirements(turretSubsystem);
    }

    @Override
    public void initialize() {
        hasTargets = false;
        turretSubsystem.stopbutton = false;
        stopLockedOn = false;
    }

    @Override
    public void execute() {

        //get if the robot is seeing the april tag
        hasTargets = turretVision.targetVisible();
        // set the yaw to what the yaw of the april tag is

        operatorController.setRumble(RumbleType.kBothRumble, 0.2);

        if (hasTargets == false || stopLockedOn == true){
            stopLockedOn = false;
            System.err.println("hasTargets = false");
            turretSubsystem.stopTurn();
        } else {
            System.err.println("hasTargets = true");
        
            //if left or right switch is pressed while we see a target set stopLockedOn to true
            //This may be unecessary because it is also present in the periodic of the subsystem but I'll leave it for now
            if(turretSubsystem.getLeftSwitch() == false || turretSubsystem.getRightSwitch() == false ||
                turretSubsystem.getEncoderValue() < Constants.turretLimitRight ||
                turretSubsystem.getEncoderValue() > Constants.turretLimitLeft){
                stopLockedOn = true;
            }

            //Setting the voltage of the motor to the yaw of the target multiplied by 5
            OptionalDouble yaw = turretVision.getTargetYaw(tagIDs);
            if (yaw.isEmpty()) return;
            if (yaw.getAsDouble() == 0.0) return;
            System.out.println("Yaw is not empty");

            turretYaw = yaw.getAsDouble();

            Double speedAdjust = 5.0;

            //Why are we setting the voltage like this? what do these numbers mean? they seem kind of arbitrary.
            autoTurretVoltage = (turretYaw)/45*-speedAdjust;

            if (operatorController.axisGreaterThan(4, 0.3).getAsBoolean()){
                manualTurretVoltage = operatorController.getRightX() * -1;
            } else if (operatorController.axisLessThan(4, -0.3).getAsBoolean()) {
                if (autoTurretVoltage <= -0.6) {
                    manualTurretVoltage = operatorController.getRightX() * -2;
                } else if (autoTurretVoltage > -0.5) {
                    manualTurretVoltage = operatorController.getRightX() * -1;
                }
            }

            double totalLockedOn = autoTurretVoltage + manualTurretVoltage;

            if(totalLockedOn > 0.7) totalLockedOn = 0.7;

            else if(totalLockedOn < -0.7) totalLockedOn = -0.7;

            turretSubsystem.lockedOn(totalLockedOn);
        }
    }

    @Override
    public void end(boolean interrupted) {
        turretSubsystem.stopTurn();
        operatorController.setRumble(RumbleType.kBothRumble, 0.0);
    }

    @Override
    public boolean isFinished() {
        if(turretSubsystem.stopbutton == true){
            return true;
        }
        return false;
    }
}
