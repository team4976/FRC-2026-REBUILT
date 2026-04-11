package frc.robot.subsystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Telemetry;
import frc.robot.generated.RebuiltTunerConstants;

import static frc.robot.Constants.drivetrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;

import frc.robot.Constants;
import frc.robot.Functions;
//was designed to be the only elastic subsystem/container but it isnt currently
//the other two can be merged with this one later, gott set it up for multiple camers with some renaming
//and gotta add all the other stuff.
import frc.robot.RobotContainer;

public class ElasticData extends SubsystemBase{
    private final Telemetry telemetry;
    /**
    * Back Left Camera Objects
    */
    private final PhotonVision cameraBackLeft;
    private final PhotonVision cameraBackRight;
    private final PhotonVision cameraTurret;

    private final IndexAndSpindexSubsystem indexAndSpindexSubsystem;
    private final FlywheelSubsystem flywheelSubsystem;
    private final TurretSubsystem turretSubsystem;
    private final IntakeSubsystem intakeSubsystem;
    private final CommandSwerveDrivetrain swerve;
    public SendableChooser<String[]> autoChooser = new SendableChooser<>();
    double turretTargetAngle; // the angle we want the turret to be at so that we are aiming at the hub
    //double turretAngle; // the turret angle we are currently at
    //double distance; // distance from the hub to the turret
    double hubWidth = 0.6;
    double radius = 3;
    boolean fuelMakeIt = false;
    double rotation;
    Pose2d robotPose;
    Field2d field2d = new Field2d();
    Optional<Alliance> alliance;
    List<Pose2d> pose2ds = new ArrayList<>();
    public double currentTime;
    public double startTime;
    PowerDistribution PDH;

    String[] selectedAutoPath = new String[0];

    public ElasticData(RobotContainer robotContainer){
        //-------------------
        //Object Assignments
        //-------------------

        //Misc Objects
        this.PDH = robotContainer.PDH;
        telemetry = robotContainer.logger;
        cameraBackLeft = robotContainer.leftBackCam;
        cameraBackRight = robotContainer.rightBackCam;
        cameraTurret = robotContainer.turretCam;
        this.swerve = drivetrain;
        field2d = cameraBackLeft.getRobotPosField2d();
        alliance = DriverStation.getAlliance();
        double hubX = 4.6;
        if (alliance.isPresent()){
            if (alliance.get() == Alliance.Red){
                hubX = 11.9;
            }
        }
        field2d.getObject("Hub").setPose(hubX, 4,Rotation2d.kZero);
        

        //Motor id List
        String[] motorIDs = {
            "[FRS Swerve] Motor Id:"+ RebuiltTunerConstants.kFrontRightSteerMotorId,
            "[FRD Swerve] Motor Id:" + RebuiltTunerConstants.kFrontRightDriveMotorId,
            "[FLS Swerve] Motor Id:"+RebuiltTunerConstants.kFrontLeftSteerMotorId,
            "[FLD Swerve] Motor Id:"+RebuiltTunerConstants.kFrontLeftDriveMotorId,
            "[RRS Swerve] Motor Id:"+RebuiltTunerConstants.kBackRightSteerMotorId,
            "[RRD Swerve] Motor Id:"+RebuiltTunerConstants.kBackRightDriveMotorId,
            "[RLS Swerve] Motor Id:"+RebuiltTunerConstants.kBackLeftSteerMotorId,
            "[RLD Swerve] Motor Id:"+RebuiltTunerConstants.kBackLeftDriveMotorId,
            "[Turret] Motor Id:"+ 1,
            "[Hood] Motor Id:"+ 2,
            "[Flywheel Lead] Motor Id:"+ Constants.Flywheel_Lead_ID,
            "[Flywheel Follow] Motor Id:"+ Constants.Flywheel_Follower_ID,
            "[Spindex] Motor Id:"+ Constants.Spindex_ID,
            "[Indexer] Motor Id:"+ Constants.Index_ID,
            "[Intake] Motor Id:"+ Constants.Intake_ID,
            "[PCM] Motor Id:",
            "[Pidgeon] Motor Id:"
        };

        //Subsystem Objects
        //this.robotContainer = robotContainer;
        indexAndSpindexSubsystem = robotContainer.indexAndSpindexSubsystem;
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
            selectedAutoPath = autoPath;
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
            if(pose2ds != null) 
                field2d.getObject("AutoPath").setPoses(pose2ds);
        });
        addDataToSmartDashBoard();
    }

    public void autonomousInit(){
        startTime = System.currentTimeMillis();
        selectedAutoPath = autoChooser.getSelected();
    }

    public void addDataToSmartDashBoard(){

        // Turret Limit Switches
        SmartDashboard.putBoolean("Testing/Left Limit Switch Status", turretSubsystem.getLeftSwitch());
        SmartDashboard.putBoolean("Testing/Right Limit Switch Status", turretSubsystem.getRightSwitch());

        SmartDashboard.putNumber("Testing/Calculated Flywheel Speed Based on Turret", Functions.GetCalculatedFlywheelSpeed(cameraTurret));

        SmartDashboard.putNumber("Testing/Calculated Flywheel Speed Based on UpdateHubInfo", Functions.GetCalculatedFlywheelSpeed(cameraTurret, 0.9));

        //------------- 
        //FIELD WIDGETS
        //-------------
        SmartDashboard.putData("Fields/Ideal Field", field2d);
        SmartDashboard.putData("Fields/Robot Position Field", cameraBackLeft.vision.field2dSwerve);     

        //-------------
        //MOTOR WIDGETS
        //-------------
        //Voltage Widgets
        SmartDashboard.putNumber("QC/Motors/Indexer/Index Volatage", indexAndSpindexSubsystem.indexMotor.getAppliedOutput());
        SmartDashboard.putNumber("QC/Motors/Spindex/Spindex Volatage", indexAndSpindexSubsystem.spindexMotor.getAppliedOutput());
        SmartDashboard.putNumber("QC/Motors/Turret/Turret Voltage", turretSubsystem.turretMotor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Flywheel Lead/Flywheel Lead Voltage", flywheelSubsystem.fx_leader.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Flywheel Follow/Flywheel Follow Voltage", flywheelSubsystem.fx_follower.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Intake/Intake Voltage", intakeSubsystem.m_intake.getAppliedOutput());
        SmartDashboard.putNumber("QC/Motors/Intake/Intake Arm Left Voltage", intakeSubsystem.m_leftArm.getAppliedOutput());
        SmartDashboard.putNumber("QC/Motors/Intake/Intake Arm Right Voltage", intakeSubsystem.m_rightArm.getAppliedOutput());

        //RPM Widgets
        SmartDashboard.putNumber("QC/Motors/Indexer/Index RPM", indexAndSpindexSubsystem.indexMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("QC/Motors/Spindex/Spindex RPM", indexAndSpindexSubsystem.spindexMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("QC/Motors/Turret/Turret RPS", turretSubsystem.turretMotor.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Flywheel Lead/Flywheel Lead RPS", flywheelSubsystem.fx_leader.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Flywheel Follow/Flywheel Follow RPS", flywheelSubsystem.fx_follower.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("QC/Motors/Intake/Intake Speed (Raw)", intakeSubsystem.m_intake.getEncoder().getVelocity());
        SmartDashboard.putNumber("QC/Motors/Intake/Intake Arm Left Speed (Raw)", intakeSubsystem.m_leftArm.getEncoder().getVelocity());
        SmartDashboard.putNumber("QC/Motors/Intake/Intake Arm Right Speed (Raw)", intakeSubsystem.m_rightArm.getEncoder().getVelocity());

        //Position Widgets
        SmartDashboard.putNumber("QC/Motors/Turret/Turret Position", turretSubsystem.turretMotor.getPosition().getValueAsDouble());
        SmartDashboard.putNumber("Intake Left Arm Encoder", intakeSubsystem.m_leftArm.getEncoder().getPosition());
        SmartDashboard.putNumber("Intake Right Arm Encoder", intakeSubsystem.m_rightArm.getEncoder().getPosition());
        SmartDashboard.putNumber("Intake Left Arm Encoder 2", intakeSubsystem.leftArmPose.getAsDouble());
        SmartDashboard.putNumber("Intake Right Arm Encoder 2", intakeSubsystem.rightArmPose.getAsDouble());

        //--------
        //BOOLEANS
        //--------

        //Limit Switch Pressed Widgets (not technically motor stuff but whatever)
        SmartDashboard.putBoolean("QC/Limit Switch Left", turretSubsystem.getLeftSwitch());
        SmartDashboard.putBoolean("QC/Limit Switch Right", turretSubsystem.getRightSwitch());

        //Boolean Widgets
        SmartDashboard.putBoolean("QC/Is Auto Aiming", turretSubsystem.isAutoAiming);
        //SmartDashboard.putBoolean("QC/Is Auto Flywheel", flywheelSubsystem.isAutoFlywheel);

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
        SmartDashboard.putNumber("Testing/Ben T's Stuff/shooter speed", flywheelSubsystem.shooterSpeed.getAsDouble());
            
        //updates the Smartdash board Values
    }


    @Override
    public void periodic(){
        try{
            addDataToSmartDashBoard();
        //--------
        //Variables
        //--------
        
        //Non Turret Camera Variables
        HashMap<String,double[]> targets = new HashMap<String,double[]>();
        targets.put("cameraBackLeft", cameraBackLeft.getIDs().stream()
            .mapToDouble(Double::doubleValue).toArray());
        targets.put("cameraBackRight", cameraBackRight.getIDs().stream()
            .mapToDouble(Double::doubleValue).toArray());


        //-------------
        //Vision Widgets
        //-------------

        //Back Left Cam Based Vision Widgets
        SmartDashboard.putNumber("Vision/Back Left Cam/Raw pitch", cameraBackLeft.getAnyPitch());
        SmartDashboard.putNumber("Vision/Back Left Cam/Raw yaw", cameraBackLeft.getAnyYaw());
        SmartDashboard.putNumberArray("Vision/Back Left Cam/Target IDs", targets.get("cameraBackLeft"));
        SmartDashboard.putBoolean("Vision/Back Left Cam/Target Visible", cameraBackLeft.targetVisible());
        SmartDashboard.putNumber("Vision/Back Left Cam/Ambiguity", cameraBackLeft.getAmbiguity());
        SmartDashboard.putNumber("Vision/Back Left Cam/Y Rotation", cameraBackLeft.getYRotation());
        SmartDashboard.putNumber("Vision/Back Left Cam/X Rotation", cameraBackLeft.getXRotation());
        SmartDashboard.putNumber("Vision/Back Left Cam/Z Rotation", cameraBackLeft.getZRotation());
        SmartDashboard.putNumber("Vision/Back Left Cam/Distance", cameraBackLeft.getDistance());

        //Back Right Cam Based Vision Widgets
        SmartDashboard.putNumber("Vision/Back Left Cam/Raw pitch", cameraBackRight.getAnyPitch());
        SmartDashboard.putNumber("Vision/Back Left Cam/Raw yaw", cameraBackRight.getAnyYaw());
        SmartDashboard.putNumberArray("Vision/Back Left Cam/Target IDs", targets.get("cameraBackLeft"));
        SmartDashboard.putBoolean("Vision/Back Left Cam/Target Visible", cameraBackRight.targetVisible());
        SmartDashboard.putNumber("Vision/Back Left Cam/Ambiguity", cameraBackRight.getAmbiguity());
        SmartDashboard.putNumber("Vision/Back Left Cam/Y Rotation", cameraBackRight.getYRotation());
        SmartDashboard.putNumber("Vision/Back Left Cam/X Rotation", cameraBackRight.getXRotation());
        SmartDashboard.putNumber("Vision/Back Left Cam/Z Rotation", cameraBackRight.getZRotation());
        SmartDashboard.putNumber("Vision/Back Left Cam/Distance", cameraBackRight.getDistance());

        //Turret Based Vision Widgets
        SmartDashboard.putNumber("Vision/Turret Cam/turretDistance", cameraTurret.getTurretDistance());
        SmartDashboard.putNumber("Vision/Turret Cam/turretPoseX", cameraTurret.getRobotPos().getX());
        SmartDashboard.putNumber("Vision/Turret Cam/turretPoseY",  cameraTurret.getRobotPos().getY());
        SmartDashboard.putNumber("Vision/Turret Cam/turretRotation", cameraTurret.getRobotPos().getRotation().getDegrees());
        SmartDashboard.putNumber("Vision/Turret Cam/targetAngle", cameraTurret.getTurretTargetAngle());
        SmartDashboard.putNumber("Vision/Turret Cam/turretAngle", cameraTurret.getBotAngle());
        SmartDashboard.putNumber("Vision/Turret Cam/Turret Distance Test", cameraTurret.vision.turretDistance);
        SmartDashboard.putNumber("Vision/Turret Cam/Turret PoseX Test", swerve.getState().Pose.getX());
        SmartDashboard.putNumber("Vision/Turret Cam/Turret PoseY Test", swerve.getState().Pose.getY());
        SmartDashboard.putNumber("Vision/Turret Cam/turretTargetAngle", cameraTurret.getTurretTargetAngle());
        SmartDashboard.putNumber("Vision/Turret Cam/Turret Target Position", turretSubsystem.convertAngleRotation(cameraTurret.getTurretTargetAngle() - cameraTurret.getBotAngle()));

        SmartDashboard.updateValues();

        }catch(Exception e){
            
        }
    }

}