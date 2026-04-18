package frc.robot.commands.Turret;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
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
    boolean HitDigitalLimit = false;

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
        /* 
        if(s_shooter.getLeftSwitch() == false || s_shooter.getRightSwitch() == false){
           s_shooter.stopTurn();
        } 
        else {
        */
        // gets the angle we want to be at to be facing the hub
        double turretTargetAngle = s_updateHubInfo.getHubAngle();
        //get the current (field relative) angle bot is facing
        double botAngle = s_updateHubInfo.getBotAngle();
        //double botAngle = Math.toDegrees(MathUtil.angleModulus(Math.toRadians(s_updateHubInfo.getBotAngle() - s_shooter.angle_sign)));
        SmartDashboard.putNumber("SubSystems/Turret/Bot Angle (Post Subtractor)", botAngle);
        //get (robot relative) turret angle
        double turretPosition = s_shooter.getEncoderValue();
        double turretToRobotAngle = s_shooter.convertRotationAngle(turretPosition);  
        SmartDashboard.putNumber("SubSystems/Turret/Turret to Robot Angle", turretToRobotAngle);      
        //Convert from robot relative to field relative angle
        double turretToFieldAngle = botAngle+turretToRobotAngle;
        SmartDashboard.putNumber("SubSystems/Turret/Turret to Field Angle", turretToFieldAngle);      
        //Convert from delta angle to rotations
        turretToHubRotations=s_shooter.convertAngleRotation(turretTargetAngle - turretToFieldAngle);
        SmartDashboard.putNumber("SubSystems/Turret/Turret to Hub Rotations", turretToHubRotations);      
        //Obtain encoder position to move turret to
        turretotargetpostition = turretPosition + turretToHubRotations;
        SmartDashboard.putNumber("SubSystems/Turret/Turret to Target Position", turretotargetpostition);      

        //Add adjustment due to operator override
        double manualTurretRotations = 0;
        double sensitivity = 0.25; // 0.7, origonal 
        if (operatorController.axisGreaterThan(4, 0.3).getAsBoolean()){
            manualTurretRotations = -((operatorController.getRightX()-0.3)/(sensitivity))*(s_shooter.convertAngleRotation(manualNudgeLimit));
        } else if (operatorController.axisLessThan(4, -0.3).getAsBoolean()) {
            manualTurretRotations = -((operatorController.getRightX()+0.3)/(sensitivity))*(s_shooter.convertAngleRotation(manualNudgeLimit));
        }
        
        turretotargetpostition = turretotargetpostition + manualTurretRotations;
        System.out.println("turretotargetposition: " + turretotargetpostition);
        System.out.println("manualLockedOn: " + manualTurretRotations);

        SmartDashboard.putNumber("SubSystems/Turret/turret to target postition (pre pose add)", turretotargetpostition);

        //Check if past software limits and if so reset turret slightly inside these limits
        if(turretotargetpostition > turretLimitLeft){
            //turretotargetpostition = turretLimitLeft - 0.5;
            HitDigitalLimit = true;
            return;
        } 
        else if (turretotargetpostition < turretLimitRight){
            //turretotargetpostition = turretLimitRight + 0.5;
            HitDigitalLimit = true;
            return;
        }
        SmartDashboard.putBoolean("SubSystems/Turret/Hit Software Limit", HitDigitalLimit);      
        
        if (turretotargetpostition < turretLimitRight || turretLimitLeft < turretotargetpostition) return;

        //Setting final turret rotation position
        s_shooter.turretRotationPID(turretotargetpostition);
        SmartDashboard.putNumber("SubSystems/Turret/turret to target postition (final pose add)", turretotargetpostition);
        SmartDashboard.putNumber("SubSystems/Turret/Turret Position", turretPosition);
        
        //}
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
