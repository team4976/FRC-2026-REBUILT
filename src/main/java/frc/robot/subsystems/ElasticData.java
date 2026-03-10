package frc.robot.subsystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import frc.robot.Telemetry;
import frc.robot.generated.RebuiltTunerConstants;
import static edu.wpi.first.units.Units.*;

import java.util.List;
import java.util.Optional;

import com.pathplanner.lib.path.PathPlannerPath;

import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.Constants;
//was designed to be the only elastic subsystem/container but it isnt currently
//the other two can be merged with this one later, gott set it up for multiple camers with some renaming
//and gotta add all the other stuff.

public class ElasticData extends SubsystemBase{
    private final Telemetry telemetry;
    private final PhotonVision cameraDataMain;
    private final PhotonVision cameraDataTurret;
    private final IndexAndSpindexSubsystem indexAndSpindexSubsystem;
    private final HoodSubsystem hoodSubsystem;
    private final FlywheelSubsystem flywheelSubsystem;
    private final TurretMovement turretMovement;
    private final Intake intakeSubsystem;
    public SendableChooser<PathPlannerPath> sendableChooser = new SendableChooser<>();
    double turretTargetAngle; // the angle we want the turret to be at so that we are aiming at the hub
    //double turretAngle; // the turret angle we are currently at
    //double distance; // distance from the hub to the turret
    double hubWidth = 0.6;
    double radius = 3;
    boolean fuelMakeIt = false;
    double rotation;
    Field2d field2d;
    Optional<Alliance> alliance;


    public ElasticData(Telemetry m_telemetry, PhotonVision camera1, PhotonVision camera2, List<Subsystem> subsystemList){
        //-------------------
        //Object Assignments
        //-------------------

        //Misc Objects
        alliance = DriverStation.getAlliance();
        telemetry = m_telemetry;
        cameraDataMain = camera1;
        cameraDataTurret = camera2;
        field2d = cameraDataMain.getRobotPos();
        if (alliance.isPresent()){
            if (alliance.get() == Alliance.Blue){
                field2d.getObject("Hub").setPose(4.6, 4, new Rotation2d(0.0));
            } else if (alliance.get() == Alliance.Red){
                field2d.getObject("Hub").setPose(11.9, 4, new Rotation2d(0.0));
            } 
        } else {
            field2d.getObject("Hub").setPose(11.9, 4, new Rotation2d(0.0));
        }

        //Subsystem Objects
        indexAndSpindexSubsystem = (IndexAndSpindexSubsystem) subsystemList.get(5);
        hoodSubsystem = (HoodSubsystem) subsystemList.get(4);
        flywheelSubsystem = (FlywheelSubsystem) subsystemList.get(3);
        turretMovement = (TurretMovement) subsystemList.get(2);
        intakeSubsystem = (Intake) subsystemList.get(0);


        //-------------------
        //Non-Periodic Widgets 
        //-------------------

        //swerve widget based on the values gained from telemetry, 100% needs to be tuned
        //and maybe even needs to use different telemtry variables. havent gotten a chance to figure that out yet.
        SmartDashboard.putData("Drive/Swerve Drive", new Sendable() {
            @Override      
            public void initSendable(SendableBuilder builder) {
                builder.setSmartDashboardType("SwerveDrive");
      
                builder.addDoubleProperty("Front Left Angle", () -> telemetry.m_moduleDirections[1].getAngle() /* * 2 * Math.PI */, null);
                builder.addDoubleProperty("Front Left Velocity", () -> telemetry.m_moduleSpeeds[1].getLength() / 3, null);
      
                builder.addDoubleProperty("Front Right Angle", () -> telemetry.m_moduleDirections[2].getAngle() /* * 2 * Math.PI */, null);
                builder.addDoubleProperty("Front Right Velocity", ()  -> telemetry.m_moduleSpeeds[2].getLength() / 3, null);
      
                builder.addDoubleProperty("Back Left Angle", () -> telemetry.m_moduleDirections[3].getAngle() /*  * 2 * Math.PI */, null);
                builder.addDoubleProperty("Back Left Velocity", () -> telemetry.m_moduleSpeeds[3].getLength() / 3, null);
      
                builder.addDoubleProperty("Back Right Angle", () -> telemetry.m_moduleDirections[0].getAngle() /*  * 2 * Math.PI */, null);
                builder.addDoubleProperty("Back Right Velocity", () -> telemetry.m_moduleSpeeds[0].getLength() / 3, null);
      
                builder.addDoubleProperty("Robot Angle", () -> telemetry.m_poseArray[2], null);
            } 
        });

        //Auto Field Chooser 
        SmartDashboard.putData("Testing/Autos/Field Auto", sendableChooser);
        try{
            sendableChooser.setDefaultOption("Elastic Test", PathPlannerPath.fromPathFile("Elastic Test"));
            sendableChooser.addOption("Human Player Left", PathPlannerPath.fromPathFile("Human Player Left"));
            sendableChooser.addOption("Human Player Mid", PathPlannerPath.fromPathFile("Human Player Mid"));
            sendableChooser.addOption("Human Player Right", PathPlannerPath.fromPathFile("Human Player Right"));
            sendableChooser.addOption("Neutral Left", PathPlannerPath.fromPathFile("Neutral Left"));
            sendableChooser.addOption("Neutral Right", PathPlannerPath.fromPathFile("Neutral Right"));
            sendableChooser.addOption("Neutral Mid", PathPlannerPath.fromPathFile("Neutral Mid"));
            sendableChooser.addOption("Depot Right", PathPlannerPath.fromPathFile("Depot Right"));
            sendableChooser.addOption("Depot Left", PathPlannerPath.fromPathFile("Depot Left"));
            sendableChooser.addOption("Depot Mid", PathPlannerPath.fromPathFile("Depot Mid"));
            sendableChooser.addOption("Shoot+Climb", PathPlannerPath.fromPathFile("Shoot+Climb"));
            sendableChooser.addOption("Long Auto Test", PathPlannerPath.fromPathFile("New Auto"));

        } catch (Exception e){
            System.out.print(e.getMessage());
        }

        SmartDashboard.putNumber("Testing/Ben T's Stuff/flywheelSpeed", 0);
        SmartDashboard.putNumber("Testing/Ben T's Stuff/hood target position", 0);

    }

    @Override
    public void periodic(){
        //--------
        //Variables
        //--------

        //variables for the non turret camera values
        double[] targetIDs = cameraDataMain.getIDs().stream()
        .mapToDouble(Double::doubleValue)
        .toArray();

        //Motor id Array
        String[] motorIDs = {"[FRS Swerve] Motor Id:"+ RebuiltTunerConstants.kFrontRightSteerMotorId,"[FRD Swerve] Motor Id:" + RebuiltTunerConstants.kFrontRightDriveMotorId,"[FLS Swerve] Motor Id:"+RebuiltTunerConstants.kFrontLeftSteerMotorId,
        "[FLD Swerve] Motor Id:"+RebuiltTunerConstants.kFrontLeftDriveMotorId,"[RRS Swerve] Motor Id:"+RebuiltTunerConstants.kBackRightSteerMotorId,"[RRD Swerve] Motor Id:"+RebuiltTunerConstants.kBackRightDriveMotorId,"[RLS Swerve] Motor Id:"+RebuiltTunerConstants.kBackLeftSteerMotorId,"[RLD Swerve] Motor Id:"+RebuiltTunerConstants.kBackLeftDriveMotorId,
        "[Turret] Motor Id:"+ 1,"[Hood] Motor Id:"+ 2,"[Flywheel Lead] Motor Id:"+ Constants.Flywheel_Lead_ID,"[Flywheel Follow] Motor Id:"+ Constants.Flywheel_Follower_ID,"[Spindex] Motor Id:"+ Constants.Spindex_ID,
        "[Indexer] Motor Id:"+ Constants.Index_ID,"[Intake] Motor Id:"+ Constants.Intake_ID,"[PCM] Motor Id:","[Pidgeon] Motor Id:"};

        //-------------
        //Vision Widgets
        //-------------

        //Main Cam Based Vision Widgets
        SmartDashboard.putNumber("Vision/Main Cam/Raw pitch", cameraDataMain.getAnyPitch());
        SmartDashboard.putNumber("Vision/Main Cam/Raw yaw", cameraDataMain.getAnyYaw());
        SmartDashboard.putNumberArray("Vision/Main Cam/Target IDs", targetIDs);
        SmartDashboard.putBoolean("Vision/Main Cam/Target Visible", cameraDataMain.targetVisible());
        SmartDashboard.putNumber("Vision/Main Cam/Ambiguity", cameraDataMain.getAmbiguity());
        SmartDashboard.putNumber("Vision/Main Cam/Y Rotation", cameraDataMain.getYRotation());
        SmartDashboard.putNumber("Vision/Main Cam/X Rotation", cameraDataMain.getXRotation());
        SmartDashboard.putNumber("Vision/Main Cam/Z Rotation", cameraDataMain.getZRotation());
        SmartDashboard.putNumber("Vision/Main Cam/Distance", cameraDataMain.getDistance());
        for(var id : targetIDs){
            double yaw = cameraDataMain
            .getTargetYaw((int) id)
            .orElse(Double.NaN);
            if (!Double.isNaN(yaw)){
                SmartDashboard.putNumber("Vision/Main Cam/Target" + id + "yaw", yaw);
            }
        }
        for(var id : targetIDs){
            double pitch = cameraDataMain
            .getTargetYaw((int) id)
            .orElse(Double.NaN);
            if (!Double.isNaN(pitch)){
                SmartDashboard.putNumber("Vision/Main Cam/Target" + id + "pitch", pitch);
            }
        }

        //Turret Based Vision Widgets
        SmartDashboard.putNumber("Vision/Turret Cam/turretDistance", cameraDataTurret.getTurretDistance());
        SmartDashboard.putNumber("Vision/Turret Cam/turretPoseX", cameraDataTurret.getRobotPos().getRobotPose().getX());
        SmartDashboard.putNumber("Vision/Turret Cam/turretPoseY",  cameraDataTurret.getRobotPos().getRobotPose().getY());
        SmartDashboard.putNumber("Vision/Turret Cam/turretRotation", cameraDataTurret.getRobotPos().getRobotPose().getRotation().getDegrees());
        SmartDashboard.putNumber("Vision/Turret Cam/targetAngle", cameraDataTurret.getTurretTargetAngle());
        SmartDashboard.putNumber("Vision/Turret Cam/turretAngle", cameraDataTurret.getTurretAngle());
        SmartDashboard.putNumber("Vision/Turret Cam/Turret Distance Test", cameraDataTurret.vision.turretDistance);
        SmartDashboard.putNumber("Vision/Turret Cam/Turret PoseX Test", cameraDataTurret.getDistanceAndAngle().getRobotPose().getX());
        SmartDashboard.putNumber("Vision/Turret Cam/Turret PoseX Test", cameraDataTurret.getDistanceAndAngle().getRobotPose().getY());
        SmartDashboard.putNumber("turretTargetAngle", cameraDataTurret.getTurretTargetAngle());
        SmartDashboard.putNumber("Vision/Turret Cam/Turret Target Position", turretMovement.convertAngleRotation(cameraDataTurret.getTurretTargetAngle() - cameraDataTurret.getTurretAngle()));

        // Turret Limit Switches
        SmartDashboard.putBoolean("leftLimit", turretMovement.getLeftSwitch());
        SmartDashboard.putBoolean("RightLimit", turretMovement.getRightSwitch());




        //------------
        //Field Widgets
        //------------
        SmartDashboard.putData("Fields/Ideal Field", field2d);
        if (cameraDataMain.targetVisible() == true){
            SmartDashboard.putData("Fields/Robot Position Field", cameraDataMain.getRobotPos());
            SmartDashboard.putData("Fields/Turret Position Field", cameraDataTurret.getDistanceAndAngle());
        }

        //AC- Additional Field2d Stuff
        if (turretMovement.turretSpin != null){
            rotation = (cameraDataTurret.getTurretAngle()/57) + field2d.getRobotPose().getRotation().getDegrees();
        } else {
            rotation = 0.0;
        }
        SmartDashboard.putBoolean("Will the Fuel make it?", fuelMakeIt);
        field2d.getObject("Aim").setPose(field2d.getRobotPose().getX() + (radius * (Math.cos(rotation))), field2d.getRobotPose().getY() + (radius * (Math.sin(rotation))), new Rotation2d(rotation));
        Pose2d aimPose = field2d.getObject("Aim").getPose();
        Pose2d hubPose = field2d.getObject("Hub").getPose();
        if (aimPose.getX() >= (hubPose.getX() - (hubWidth/2)) && aimPose.getX() <= (hubPose.getX() + (hubWidth/2))){
            if (aimPose.getY() >= (hubPose.getY() - hubWidth) && aimPose.getY() <= (hubPose.getY() + hubWidth)) {
                fuelMakeIt = true;
            } else {
                fuelMakeIt = false;
            }
        } else {
            fuelMakeIt = false;
        } 
        


        //Swerve Direction on Field
        if(telemetry != null){
            if(hoodSubsystem != null){
                //field2d.getObject("FR").setPose(field2d.getRobotPose().getX() + 0.42 + (1 * (Math.cos(telemetry.m_moduleDirections[2].getAngle())/57)),field2d.getRobotPose().getY() + 0.343 + (1 * (Math.sin(telemetry.m_moduleDirections[2].getAngle())/57)),new Rotation2d(telemetry.m_moduleDirections[2].getAngle()));
                //field2d.getObject("FL").setPose(field2d.getRobotPose().getX() - 0.42  - (1 * (Math.cos(telemetry.m_moduleDirections[1].getAngle())/57)),field2d.getRobotPose().getY() + 0.343 + (1 * (Math.sin(telemetry.m_moduleDirections[1].getAngle())/57)),new Rotation2d(telemetry.m_moduleDirections[1].getAngle()));
                //field2d.getObject("RR").setPose(field2d.getRobotPose().getX() + 0.42 + (1 * (Math.cos(telemetry.m_moduleDirections[0].getAngle())/57)),field2d.getRobotPose().getY() - 0.343 + (1 * (Math.sin(telemetry.m_moduleDirections[0].getAngle())/57)),new Rotation2d(telemetry.m_moduleDirections[0].getAngle()));
                //field2d.getObject("RL").setPose(field2d.getRobotPose().getX() - 0.42 - (1 * (Math.cos(telemetry.m_moduleDirections[3].getAngle())/57)),field2d.getRobotPose().getY() - 0.343 + (1 * (Math.sin(telemetry.m_moduleDirections[3].getAngle())/57)),new Rotation2d(telemetry.m_moduleDirections[3].getAngle()));
                field2d.getObject("FR").setPose(field2d.getRobotPose().getX() + 0.42, field2d.getRobotPose().getY() + 0.343, new Rotation2d(telemetry.m_moduleDirections[2].getAngle()));
                field2d.getObject("FL").setPose(field2d.getRobotPose().getX() - 0.42, field2d.getRobotPose().getY() + 0.343, new Rotation2d(telemetry.m_moduleDirections[1].getAngle()));
                field2d.getObject("RR").setPose(field2d.getRobotPose().getX() + 0.42, field2d.getRobotPose().getY() - 0.343, new Rotation2d(telemetry.m_moduleDirections[0].getAngle()));
                field2d.getObject("RL").setPose(field2d.getRobotPose().getX() - 0.42, field2d.getRobotPose().getY() - 0.343, new Rotation2d(telemetry.m_moduleDirections[3].getAngle()));
            }
            
        }
        //------------
        //MOTOR WIDGETS
        //------------

        //Voltage Widgets
        SmartDashboard.putNumber("Testing/Motors/Indexer/Index Volatage", indexAndSpindexSubsystem.indexMotor.getAppliedOutput());
        SmartDashboard.putNumber("Testing/Motors/Spindex/Spindex Volatage", indexAndSpindexSubsystem.spindexMotor.getAppliedOutput());
        SmartDashboard.putNumber("Testing/Motors/Hood/Hood Voltage", hoodSubsystem.HoodMotor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("Testing/Motors/Turret/Turret Voltage", turretMovement.turretSpin.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("Testing/Motors/Flywheel Lead/Flywheel Lead Voltage", flywheelSubsystem.ShooterMotorLeader.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("Testing/Motors/Flywheel Follow/Flywheel Follow Voltage", flywheelSubsystem.ShooterMotorFollower.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("Testing/Motors/Intake/Intake Voltage", intakeSubsystem.IntakeMotor.getMotorOutputVoltage());

        //RPM Widgets
        SmartDashboard.putNumber("Testing/Motors/Indexer/Index RPM", indexAndSpindexSubsystem.indexMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("Testing/Motors/Spindex/Spindex RPM", indexAndSpindexSubsystem.spindexMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("Testing/Motors/Hood/Hood RPS", hoodSubsystem.HoodMotor.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("Testing/Motors/Turret/Turret RPS", turretMovement.turretSpin.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("Testing/Motors/Flywheel Lead/Flywheel Lead RPS", flywheelSubsystem.ShooterMotorLeader.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("Testing/Motors/Flywheel Follow/Flywheel Follow RPS", flywheelSubsystem.ShooterMotorFollower.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("Testing/Motors/Intake/Intake Speed (Raw)", intakeSubsystem.IntakeMotor.getSelectedSensorVelocity());

        //Position Widgets
        SmartDashboard.putNumber("Testing/Motors/Hood/Hood Position", hoodSubsystem.HoodMotor.getPosition().getValueAsDouble());
        SmartDashboard.putNumber("Testing/Motors/Turret/Turret Position", turretMovement.turretSpin.getPosition().getValueAsDouble());

        //Limit Switch Pressed Widgets (not technically motor stuff but whatever)
        SmartDashboard.putBoolean("Testing/Limit Switch Left", turretMovement.getLeftSwitch());
        SmartDashboard.putBoolean("Testing/Limit Switch Right", turretMovement.getRightSwitch());

        //Motor id Widgets
        SmartDashboard.putStringArray("Testing/Motors/Motor Id's", motorIDs);
        for (String motorInfo : motorIDs) {
            try {
                int openBracket = motorInfo.indexOf("[");
                int closeBracket = motorInfo.indexOf("]");
                int colonIndex = motorInfo.indexOf(":");

                //gets what is inside the [brackets]
                String folderName = motorInfo.substring(openBracket + 1, closeBracket).trim();
        
                //gets everything after the colon
                String motorId = motorInfo.substring(colonIndex + 1).trim();

                if (!motorId.isEmpty()) {
                    if (folderName.contains("Swerve")){
                        SmartDashboard.putString("Testing/Motors/Swerve/" + folderName + "/Motor Id", motorId);
                    } else {
                        SmartDashboard.putString("Testing/Motors/" + folderName + "/Motor Id", motorId);
                    }
                }
            } catch (Exception e) {
             //this prevents the code from crashing if one string is formatted weirdly
             System.out.println("Error making motor string: " + motorInfo);
            }
        }

        //Ben T's smartdashboard stuff
        SmartDashboard.putString("Testing/Ben T's Stuff/shooter state", flywheelSubsystem.getShooterState());
        SmartDashboard.putNumber("Testing/Ben T's Stuff/shooter speed", flywheelSubsystem.getShooterSpeed());
        SmartDashboard.putString("Testing/Ben T's Stuff/hood State", hoodSubsystem.getHoodState());
        SmartDashboard.putNumber("Testing/Ben T's Stuff/hood position", hoodSubsystem.returnMotor().getPosition().getValueAsDouble());

        //Changes The Path on the Field2d
        sendableChooser.onChange((path)->{
            if(path != null) field2d.getObject("AutoPath").setPoses(path.getPathPoses());;
        });

            
        //updates the Smartdash board Values
        SmartDashboard.updateValues();


        
    }

    

}