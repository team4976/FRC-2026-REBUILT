package frc.robot.commands.Turret;
import edu.wpi.first.wpilibj2.command.Command;
//import frc.robot.Constants;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.UpdateHubInfo;
import static frc.robot.Constants.*;


public class TurretScan extends Command {
    UpdateHubInfo s_updateHubInfo;
    PhotonVision s_turretVision;
    TurretSubsystem s_shooter;

    double autoTurretVoltage = 0.0;
    double turretotargetpostition;
    double distance; // distance from the hub to the turret
    boolean stopLockedOn = false; // flag to track if the turret is hitting the limit switch in lockedOn mode // our estimated position on the field
    double turretTargetPosition;
    double turretToHubRotations;

    public TurretScan(UpdateHubInfo updateHubInfo, TurretSubsystem shooter){
        this.s_updateHubInfo = updateHubInfo;
        this.s_shooter = shooter;
        addRequirements(shooter);

        System.out.println("print works turret");

    }

    @Override
    public void initialize() {
        s_shooter.stopbutton = false;
        stopLockedOn = false;
        s_shooter.isAutoAiming = true;
    }

    @Override
    public void execute() {
        //System.out.println("TurretScan-Execute");

        // if left or right switch is pressed turn off motor
        if(s_shooter.getLeftSwitch() == false || s_shooter.getRightSwitch() == false){
           s_shooter.stopTurn();
        } else {
        // gets the angle we want to be at to be facing the hub
        double turretTargetAngle = s_updateHubInfo.getHubAngle();
        //get the current (field relative) angle bot is facing
        double botAngle = s_updateHubInfo.getBotAngle();
        //get (robot relative) turret angle
        double turretPosition = s_shooter.getEncoderValue();
        double turretToRobotAngle = s_shooter.convertRotationAngle(turretPosition);        
        //Convert from robot relative to field relative angle
        double turretToFieldAngle = botAngle+turretToRobotAngle;

        //Determines voltage to apply to motor based on distance turret angle is away from hub
        turretToHubRotations=s_shooter.convertAngleRotation(turretTargetAngle - turretToFieldAngle);
        turretotargetpostition = turretPosition + turretToHubRotations;
        if(turretotargetpostition > turretLimitLeft){
            turretotargetpostition = turretLimitLeft - 0.1;
        } else if (turretotargetpostition < turretLimitRight){
            turretotargetpostition = turretLimitRight + 0.1;
        }

        //Add adjustment due to operator override
        double manualTurretVoltage =0;
        double manualLockedOn = 0;
        if (operatorController.axisGreaterThan(4, 0.3).getAsBoolean()){
            manualTurretVoltage = -(operatorController.getRightX()-0.3)/(0.7/manualNudgeLimit);
        } else if (operatorController.axisLessThan(4, -0.3).getAsBoolean()) {
            manualTurretVoltage = (operatorController.getRightX()-0.3)/(0.7/manualNudgeLimit);
        }

        manualTurretVoltage = s_shooter.convertAngleRotation(manualTurretVoltage);

        System.out.println("turretotargetposition: " + turretotargetpostition);
        System.out.println("manualLockedOn: " + manualLockedOn);
        s_shooter.turretRotationPID(turretotargetpostition + manualLockedOn);
        }
    } 
    

    @Override
    public void end(boolean interrupted) {
        s_shooter.isAutoAiming = false;
        s_shooter.stopTurn();
    }

    @Override
    public boolean isFinished() {
        
        return false;
    }
    
}
