package frc.robot.subsystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Telemetry;
import frc.robot.generated.RebuiltTunerConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;

import frc.robot.Constants;
//was designed to be the only elastic subsystem/container but it isnt currently
//the other two can be merged with this one later, gott set it up for multiple camers with some renaming
//and gotta add all the other stuff.
import frc.robot.RobotContainer;

public class ElasticData extends SubsystemBase{
    private RobotContainer robotContainer;
    private final Telemetry telemetry;
    private final PhotonVision cameraDataMain;
    private final PhotonVision cameraDataTurret;
    private final IndexAndSpindexSubsystem indexAndSpindexSubsystem;
    private final HoodSubsystem hoodSubsystem;
    private final FlywheelSubsystem flywheelSubsystem;
    private final TurretSubsystem turretSubsystem;
    private final IntakeSubsystem intakeSubsystem;
    public SendableChooser<String[]> autoChooser = new SendableChooser<>();
    Field2d field2d;
    Optional<Alliance> alliance;
    List<Pose2d> pose2ds = new ArrayList<>();
    public double currentTime;
    public double startTime;
    PowerDistribution PDH;

    public ElasticData(RobotContainer robotContainer){
        //-------------------
        //Object Assignments
        //-------------------

        //Misc Objects
        this.PDH = robotContainer.PDH;
        telemetry = robotContainer.logger;
        cameraDataMain = robotContainer.vision;
        cameraDataTurret = robotContainer.m_turretvision;
        field2d = cameraDataMain.getRobotPos();
        alliance = DriverStation.getAlliance();
        if (alliance.isPresent()){
            if (alliance.get() == Alliance.Blue){
                field2d.getObject("Hub").setPose(4.6, 4, new Rotation2d(0.0));
            } else if (alliance.get() == Alliance.Red){
                field2d.getObject("Hub").setPose(11.9, 4, new Rotation2d(0.0));
            } 
        } else {
            field2d.getObject("Hub").setPose(11.9, 4, new Rotation2d(0.0));
        }

        //Motor id List
        String[] motorIDs = {"[FRS Swerve] Motor Id:"+ RebuiltTunerConstants.kFrontRightSteerMotorId,"[FRD Swerve] Motor Id:" + RebuiltTunerConstants.kFrontRightDriveMotorId,"[FLS Swerve] Motor Id:"+RebuiltTunerConstants.kFrontLeftSteerMotorId,
        "[FLD Swerve] Motor Id:"+RebuiltTunerConstants.kFrontLeftDriveMotorId,"[RRS Swerve] Motor Id:"+RebuiltTunerConstants.kBackRightSteerMotorId,"[RRD Swerve] Motor Id:"+RebuiltTunerConstants.kBackRightDriveMotorId,"[RLS Swerve] Motor Id:"+RebuiltTunerConstants.kBackLeftSteerMotorId,"[RLD Swerve] Motor Id:"+RebuiltTunerConstants.kBackLeftDriveMotorId,
        "[Turret] Motor Id:"+ 1,"[Hood] Motor Id:"+ 2,"[Flywheel Lead] Motor Id:"+ Constants.Flywheel_Lead_ID,"[Flywheel Follow] Motor Id:"+ Constants.Flywheel_Follower_ID,"[Spindex] Motor Id:"+ Constants.Spindex_ID,
        "[Indexer] Motor Id:"+ Constants.Index_ID,"[Intake] Motor Id:"+ Constants.Intake_ID,"[PCM] Motor Id:","[Pidgeon] Motor Id:"};

        //Subsystem Objects
        this.robotContainer = robotContainer;
        indexAndSpindexSubsystem = robotContainer.indexAndSpindexSubsystem;
        hoodSubsystem = robotContainer.hoodSubsystem;
        flywheelSubsystem = robotContainer.flywheelSubsystem;
        turretSubsystem = robotContainer.turretSubsystem;
        intakeSubsystem = robotContainer.intakeSubsystem;


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
        SmartDashboard.putData("Autos/Auto Select", autoChooser);
        try{
            autoChooser.setDefaultOption("1 Cycle - Right", new String[]{"1 Cycle - Right"});
            autoChooser.addOption("1 Cycle - Left", new String[]{"1 Cycle - Left"});
            autoChooser.addOption("1.5 Cycle - Right", new String[]{"1 Cycle - Right", "1.5 Cycle - Right"});
            autoChooser.addOption("1.5 Cycle - Left", new String[]{"1 Cycle - Left", "1.5 Cycle - Left"});
            autoChooser.addOption("2 Cycle - Right", new String[]{"1 Cycle - Right", "1.5 Cycle - Right", "2 Cycle - Right"});
            autoChooser.addOption("2 Cycle - Left", new String[]{"1 Cycle - Left", "1.5 Cycle - Left", "2 Cycle - Left"});
            autoChooser.addOption("Hub to Shoot to Depot", new String[]{"Hub to Shoot", "Depot to Shoot"});
            autoChooser.addOption("Hub to Shoot", new String[]{"Hub to Shoot"});
            autoChooser.addOption("Shoot to Nuetral", new String[]{"Shoot to Neutral"});
            autoChooser.addOption("No Auto", new String[]{"No Auto"});

        } catch (Exception e){
            System.out.print(e.getMessage());
        }

        //Changes The Path on the Field2d
        autoChooser.onChange((autoPath)->{
            pose2ds.clear();
            try{
                for (String auto : autoPath) {
                    
                    List<PathPlannerPath> paths = PathPlannerAuto.getPathGroupFromAutoFile(auto);

                    for (PathPlannerPath path : paths) {
                        pose2ds.addAll(path.getPathPoses());
                    }
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }            
            if(pose2ds != null) field2d.getObject("AutoPath").setPoses(pose2ds);
        });

        SmartDashboard.putNumber("Testing/Ben T's Stuff/flywheelSpeed", 0);
        SmartDashboard.putNumber("Testing/Ben T's Stuff/hood target position", 0);

    }

    public void autonomousInit(){
        startTime = System.currentTimeMillis();
    }

    @Override
    public void periodic(){
        //--------
        //Variables
        //--------
        
        //Non Turret Camera Variables
        double[] targetIDs = cameraDataMain.getIDs().stream()
        .mapToDouble(Double::doubleValue)
        .toArray();

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
        SmartDashboard.putNumber("Vision/Turret Cam/turretTargetAngle", cameraDataTurret.getTurretTargetAngle());
        SmartDashboard.putNumber("Vision/Turret Cam/Turret Target Position", turretSubsystem.convertAngleRotation(cameraDataTurret.getTurretTargetAngle() - cameraDataTurret.getTurretAngle()));

        // Turret Limit Switches
        SmartDashboard.putBoolean("Testing/Left Limit Switch Status", turretSubsystem.getLeftSwitch());
        SmartDashboard.putBoolean("Testing/Right Limit Switch Status", turretSubsystem.getRightSwitch());

        SmartDashboard.putNumber("Testing/Calculated Flywheel Speed", 31.49597 + (10.19041 * (cameraDataMain.getDistance() + 0.5969))  
                - (0.4148098 * Math.pow(cameraDataMain.getDistance() + 0.5969, 2)));



        //------------- 
        //FIELD WIDGETS
        //-------------
        SmartDashboard.putData("Fields/Ideal Field", field2d);
        SmartDashboard.putData("Fields/Turret Position Field", cameraDataTurret.getDistanceAndAngle());
        


        //-------------
        //MOTOR WIDGETS
        //-------------

        //Voltage Widgets
        SmartDashboard.putNumber("QC/Motors/Indexer/Index Volatage", indexAndSpindexSubsystem.indexMotor.getAppliedOutput());
        SmartDashboard.putNumber("QC/Motors/Spindex/Spindex Volatage", indexAndSpindexSubsystem.spindexMotor.getAppliedOutput());
        SmartDashboard.putNumber("QC/Motors/Hood/Hood Voltage", hoodSubsystem.HoodMotor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Turret/Turret Voltage", turretSubsystem.turretMotor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Flywheel Lead/Flywheel Lead Voltage", flywheelSubsystem.shooterMotorLeader.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Flywheel Follow/Flywheel Follow Voltage", flywheelSubsystem.shooterMotorFollower.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Intake/Intake Voltage", intakeSubsystem.intakeMotor.getAppliedOutput());
        SmartDashboard.putNumber("QC/Motors/Intake/Intake Arm Left Voltage", intakeSubsystem.intakeArmLeft.getAppliedOutput());
        SmartDashboard.putNumber("QC/Motors/Intake/Intake Arm Right Voltage", intakeSubsystem.intakeArmRight.getAppliedOutput());

        //RPM Widgets
        SmartDashboard.putNumber("QC/Motors/Indexer/Index RPM", indexAndSpindexSubsystem.indexMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("QC/Motors/Spindex/Spindex RPM", indexAndSpindexSubsystem.spindexMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("QC/Motors/Hood/Hood RPS", hoodSubsystem.HoodMotor.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Turret/Turret RPS", turretSubsystem.turretMotor.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Flywheel Lead/Flywheel Lead RPS", flywheelSubsystem.shooterMotorLeader.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Flywheel Follow/Flywheel Follow RPS", flywheelSubsystem.shooterMotorFollower.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Intake/Intake Speed (Raw)", intakeSubsystem.intakeMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("QC/Motors/Intake/Intake Arm Left Speed (Raw)", intakeSubsystem.intakeArmLeft.getEncoder().getVelocity());
        SmartDashboard.putNumber("QC/Motors/Intake/Intake Arm Right Speed (Raw)", intakeSubsystem.intakeArmRight.getEncoder().getVelocity());

        //Position Widgets
        SmartDashboard.putNumber("QC/Motors/Hood/Hood Position", hoodSubsystem.HoodMotor.getPosition().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Turret/Turret Position", turretSubsystem.turretMotor.getPosition().getValueAsDouble());
        SmartDashboard.putNumber("Intake Left Arm Encoder", intakeSubsystem.intakeArmLeft.getEncoder().getPosition());
        SmartDashboard.putNumber("Intake Right Arm Encoder", intakeSubsystem.intakeArmRight.getEncoder().getPosition());

        //--------
        //BOOLEANS
        //--------

        //Limit Switch Pressed Widgets (not technically motor stuff but whatever)
        SmartDashboard.putBoolean("QC/Limit Switch Left", turretSubsystem.getLeftSwitch());
        SmartDashboard.putBoolean("QC/Limit Switch Right", turretSubsystem.getRightSwitch());

        //Boolean Widgets
        SmartDashboard.putBoolean("QC/Is Auto Aiming", turretSubsystem.isAutoAiming);
        SmartDashboard.putBoolean("QC/Is Auto Flywheel", flywheelSubsystem.isAutoFlywheel);

        


        //---------
        //MISC
        //---------

        //PDP/PDH

        //The PDP updates voltage in 0.05 Volt increments
        SmartDashboard.putNumber("Logging/PDH/PDH Voltage", PDH.getVoltage());

        //Current
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Total Current (All Channels)", PDH.getTotalCurrent());
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 0 Current", PDH.getCurrent(0));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 1 Current", PDH.getCurrent(1));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 2 Current", PDH.getCurrent(2));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 3 Current", PDH.getCurrent(3));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 4 Current", PDH.getCurrent(4));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 5 Current", PDH.getCurrent(5));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 6 Current", PDH.getCurrent(6));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 7 Current", PDH.getCurrent(7));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 8 Current", PDH.getCurrent(8));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 9 Current", PDH.getCurrent(9));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 10 Current", PDH.getCurrent(10));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 11 Current", PDH.getCurrent(11));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 12 Current", PDH.getCurrent(12));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 13 Current", PDH.getCurrent(13));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 14 Current", PDH.getCurrent(14));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 15 Current", PDH.getCurrent(15));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 16 Current", PDH.getCurrent(16));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 17 Current", PDH.getCurrent(17));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 18 Current", PDH.getCurrent(18));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 19 Current", PDH.getCurrent(19));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 20 Current", PDH.getCurrent(20));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 21 Current", PDH.getCurrent(21));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 22 Current", PDH.getCurrent(22));
        SmartDashboard.putNumber("Logging/PDH/Current/PDH Channel 23 Current", PDH.getCurrent(23));


        //Time
        currentTime = System.currentTimeMillis();
        double timeDifference = currentTime - startTime;
        double timeInSeconds = timeDifference / 1000;
        SmartDashboard.putNumber("Timer", timeInSeconds);


        //Ben T's smartdashboard stuff
        SmartDashboard.putNumber("Testing/Ben T's Stuff/shooter speed", flywheelSubsystem.getShooterSpeed());
        SmartDashboard.putString("Testing/Ben T's Stuff/hood State", hoodSubsystem.getHoodState());
        SmartDashboard.putNumber("Testing/Ben T's Stuff/hood position", hoodSubsystem.returnMotor().getPosition().getValueAsDouble());
            
        //updates the Smartdash board Values
        SmartDashboard.updateValues();


        
    }

    

}