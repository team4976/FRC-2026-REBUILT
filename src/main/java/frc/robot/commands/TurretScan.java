

package frc.robot.commands;
import java.util.OptionalDouble;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.UpdateHubInfo;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.PhotonVision;
import static frc.robot.Constants.*;

public class TurretScan extends Command {
    UpdateHubInfo updateHubInfo;
    PhotonVision turretVision;
    TurretSubsystem shooter;
    CommandSwerveDrivetrain swerve;
    boolean TurningRight = true; // flag in scan to determine if the turret should be turning right or left
    boolean hasTargets = false; // flag to track if the turret see's an april tag
    double distance; // distance from the hub to the turret
    boolean stopLockedOn = false; // flag to track if the turret is hitting the limit switch in lockedOn mode // our estimated position on the field
    double turretTargetAngle; // the angle we want the turret to be at so that we are aiming at the hub
    double turretAngle; // the turret angle we are currently at
    double botAngle;
    double turretPosition; // the encoder value of the tuurets motor
    double turretTargetPosition;
    double manualLockedOn;
    double autoLockedOn = 0.0;
    boolean isAuto = false;

    public TurretScan(UpdateHubInfo updateHubInfo, PhotonVision turretVision, TurretSubsystem shooter, CommandSwerveDrivetrain swerve){
        this.updateHubInfo = updateHubInfo;
        this.turretVision = turretVision;
        this.shooter = shooter;
        addRequirements(shooter);

        this.swerve = swerve;

    }

    public TurretScan(UpdateHubInfo updateHubInfo, PhotonVision turretVision,TurretSubsystem shooter, boolean isAuto){
        this.updateHubInfo = updateHubInfo;
        this.turretVision = turretVision;
        this.shooter = shooter;
        addRequirements(shooter);

        //field2d = new Field2d();
        this.isAuto  = isAuto;
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
        // set the yaw to what the yaw of the april tag is


        // if left or right switch is pressed turn off motor
        if(shooter.getLeftSwitch() == false || shooter.getRightSwitch() == false ||
           shooter.getEncoderValue() < Constants.turretLimitRight ||
           shooter.getEncoderValue() > Constants.turretLimitLeft){
           shooter.stopTurn();
        }
        else{
            // gets the angle we want to be at to be facing the hub
        turretTargetAngle = updateHubInfo.getHubAngle();

        turretPosition = shooter.getEncoderValue();
        turretAngle = shooter.convertRotationAngle(turretPosition);
        botAngle = updateHubInfo.getBotAngle();
        

        Double speedAdjust = 5.0;
        autoLockedOn = (turretTargetAngle-botAngle - turretAngle)/45*speedAdjust;

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
        if(isAuto) return turretVision.AutoShootFlag.getAsBoolean();
        
        //return m_turretVision.AutoShootFlag.getAsBoolean();
        
         if(shooter.stopbutton == true){
            return true;
        }
        else{
            return false;
        }
    }
    
}
