

package frc.robot.commands.Turret;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
//import frc.robot.Constants;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.UpdateHubInfo;
import static frc.robot.Constants.*;


public class TurretScan extends Command {
    UpdateHubInfo updateHubInfo;
    TurretSubsystem shooter;
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

    public TurretScan(UpdateHubInfo updateHubInfo, TurretSubsystem shooter){
        this.updateHubInfo = updateHubInfo;
        this.shooter = shooter;
        addRequirements(shooter);

        System.out.println("print works turret");

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

        //Determines voltage to apply to motor based on distance turret angle is away from hub
        turretToHubRotations=shooter.convertAngleRotation(turretTargetAngle-turrettoFieldAngle);
        turretotargetpostition = turretPosition + turretToHubRotations;
        if(turretotargetpostition > turretLimitLeft){
            turretotargetpostition = turretLimitLeft - 0.1;
        }
        else if(turretotargetpostition < turretLimitRight){
            turretotargetpostition = turretLimitRight + 0.1;
        }

        //Add adjustment due to operator override
        if (operatorController.axisGreaterThan(4, 0.3).getAsBoolean()){
            manualLockedOn = -(operatorController.getRightX()-0.3)/(0.7/manualNudgeLimit);
        } else if (operatorController.axisLessThan(4, -0.3).getAsBoolean()) {
            manualLockedOn = (operatorController.getRightX()-0.3)/(0.7/manualNudgeLimit);
        }

        manualLockedOn = shooter.convertAngleRotation(manualLockedOn);

        System.out.println("turretotargetposition: " + turretotargetpostition);
        System.out.println("manualLockedOn: " + manualLockedOn);
        shooter.turretRotationPID(turretotargetpostition + manualLockedOn);
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
        
        return false;
    }
    
}
