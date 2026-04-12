package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.UpdateHubInfo;
import frc.robot.subsystems.PhotonVision;
import static frc.robot.Constants.*;


public class TurretScan extends Command {
    CommandSwerveDrivetrain s_swerve;
    UpdateHubInfo s_updateHubInfo;
    PhotonVision s_turretVision;
    TurretSubsystem s_shooter;

    double autoTurretVoltage = 0.0;
    double turretotargetpostition;
    boolean stopLockedOn = false; // flag to track if the turret is hitting the limit switch in lockedOn mode // our estimated position on the field
    double turretTargetPosition;
    double turretToHubRotations;
    double manualTurretVoltage;
    double turretToRobotAngle; // the turret angle we are currently at relative to robot
    double turrettoFieldAngle; //The turrent angle relative to the field
    double turretTargetAngle; // the angle we want the turret to be at so that we are aiming at the hub
    boolean isAuto = false;
    double turretPosition; // the encoder value of the tuurets motor
    double distance; // distance from the hub to the turret
    double botAngle;

    public TurretScan(UpdateHubInfo s_updateHubInfo, PhotonVision s_turretVision, TurretSubsystem s_shooter, CommandSwerveDrivetrain s_swerve){
        this.s_updateHubInfo = s_updateHubInfo;
        this.s_turretVision = s_turretVision;
        this.s_shooter = s_shooter;
        this.s_swerve = s_swerve;
        addRequirements(s_shooter);
    }

    public TurretScan(UpdateHubInfo s_updateHubInfo, PhotonVision s_turretVision, TurretSubsystem s_shooter, boolean isAuto){
        this.s_updateHubInfo = s_updateHubInfo;
        this.s_turretVision = s_turretVision;
        this.s_shooter = s_shooter;
        this.isAuto  = isAuto;
        addRequirements(s_shooter);
    }

    @Override
    public void initialize() {
        s_shooter.stopbutton = false;
        stopLockedOn = false;
        s_shooter.isAutoAiming = true;
    }

    @Override
    public void execute() {
        System.out.println("TurretScan-Execute");

        // if left or right switch is pressed turn off motor
        if(s_shooter.getLeftSwitch() == false || s_shooter.getRightSwitch() == false){
           s_shooter.stopTurn();
        } else {
        // gets the angle we want to be at to be facing the hub
        turretTargetAngle = s_updateHubInfo.getHubAngle();
        //get the current (field relative) angle bot is facing
        botAngle = s_updateHubInfo.getBotAngle();
        //get (robot relative) turret angle
        turretPosition = s_shooter.getEncoderValue();
        turretToRobotAngle = s_shooter.convertRotationAngle(turretPosition);        
        //Convert from robot relative to field relative angle
        turrettoFieldAngle = botAngle+turretToRobotAngle;

        //Determines voltage to apply to motor based on distance turret angle is away from hub
        turretToHubRotations=s_shooter.convertAngleRotation(turretTargetAngle-turrettoFieldAngle);
        turretotargetpostition = turretPosition + turretToHubRotations;
        if(turretotargetpostition > turretLimitLeft){
            turretotargetpostition = turretLimitLeft - 0.1;
        } else if (turretotargetpostition < turretLimitRight){
            turretotargetpostition = turretLimitRight + 0.1;
        }

        //Add adjustment due to operator override
        if (operatorController.axisGreaterThan(4, 0.3).getAsBoolean()){
            manualTurretVoltage = -(operatorController.getRightX()-0.3)/(0.7/manualNudgeLimit);
        } else if (operatorController.axisLessThan(4, -0.3).getAsBoolean()) {
            manualTurretVoltage = (operatorController.getRightX()-0.3)/(0.7/manualNudgeLimit);
        }

        manualTurretVoltage = s_shooter.convertAngleRotation(manualTurretVoltage);

        s_shooter.turretRotationPID(turretotargetpostition + manualTurretVoltage);
        }
    } 
    

    @Override
    public void end(boolean interrupted) {
        s_shooter.isAutoAiming = false;
        s_shooter.stopTurn();
    }

    @Override
    public boolean isFinished() {
        // if rightTrigger is pressed or stopLocked = true then end command
        if(isAuto) return s_turretVision.AutoShootFlag.getAsBoolean();
        
        if(s_shooter.stopbutton == true){
            return true;
        } else {
            return false;
        }
    }
    
}
