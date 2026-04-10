

package frc.robot.commands.Turret;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.UpdateHubInfo;


public class TurretScan extends Command {
    UpdateHubInfo updateHubInfo;
    PhotonVision turretVision;
    TurretSubsystem shooter;
    CommandSwerveDrivetrain swerve;
    double distance; // distance from the hub to the turret
    boolean stopLockedOn = false; // flag to track if the turret is hitting the limit switch in lockedOn mode // our estimated position on the field
    double turretTargetAngle; // the angle we want the turret to be at so that we are aiming at the hub
    double turrettoRobotAngle; // the turret angle we are currently at relative to robot
    double turrettoFieldAngle; //The turrent angle relative to the field
    double botAngle;
    double turretPosition; // the encoder value of the tuurets motor
    double turretTargetPosition;
    double manualLockedOn;
    double autoLockedOn = 0.0;
    double turretToHubRotations;
    double turretotargetpostition;
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
        shooter.stopbutton = false;
        stopLockedOn = false;
        shooter.isAutoAiming = true;
    }

    @Override
    public void execute() {
        //SmartDashboard.putBoolean("TurningRight", TurningRight);
        // set the yaw to what the yaw of the april tag is
        System.out.println("TurretScan-Execute");


        // if left or right switch is pressed turn off motor
        if(shooter.getLeftSwitch() == false || shooter.getRightSwitch() == false){
           shooter.stopTurn();
        }
        else{
        // gets the angle we want to be at to be facing the hub
        turretTargetAngle = updateHubInfo.getHubAngle();
        //get the current (field relative) angle bot is facing
        botAngle = updateHubInfo.getBotAngle();
        //get (robot relative) turret angle
        turretPosition = shooter.getEncoderValue();
        turrettoRobotAngle = shooter.convertRotationAngle(turretPosition);        
        //Convert from robot relative to field relative angle
        turrettoFieldAngle = botAngle+turrettoRobotAngle;
        
        Double speedAdjust = 5.0;  //Coefficient to increase turrent speed

        //Determines voltage to apply to motor based on distance turret angle is away from hub
        turretToHubRotations=shooter.convertAngleRotation(turretTargetAngle-turrettoFieldAngle);///45*speedAdjust;
        turretotargetpostition = turretPosition + turretToHubRotations;
        if(turretotargetpostition > Constants.turretLimitLeft){
            turretotargetpostition = Constants.turretLimitLeft - 0.1;
        }
        else if(turretotargetpostition < Constants.turretLimitRight){
            turretotargetpostition = Constants.turretLimitRight + 0.1;
        }
        shooter.turretRotationPID(turretotargetpostition);
        System.out.println(turretPosition+turretToHubRotations);

        //Add adjustment due to operator override
       /*  if (operatorController.axisGreaterThan(4, 0.3).getAsBoolean()){
            manualLockedOn = operatorController.getRightX() * -1;
        } else if (operatorController.axisLessThan(4, -0.3).getAsBoolean()) {
            if (autoLockedOn <= -0.6) {
                manualLockedOn = operatorController.getRightX() * -2;
            } else if (autoLockedOn > -0.5) {
                manualLockedOn = operatorController.getRightX() * -1;
            }
        }
        double totalLockedOn = autoLockedOn + manualLockedOn;
        
        if(totalLockedOn > Constants.turretScanVoltage) totalLockedOn = Constants.turretScanVoltage;

        else if(totalLockedOn < -Constants.turretScanVoltage) totalLockedOn = -Constants.turretScanVoltage;
        shooter.lockedOn(totalLockedOn);
        SmartDashboard.putNumber("Testing/Total Turret Voltage", totalLockedOn);  
        } */
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
