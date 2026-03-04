package frc.robot.subsystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Telemetry;
import frc.robot.generated.RebuiltTunerConstants;
import static edu.wpi.first.units.Units.*;
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
    private final TurretMovement turretMovement;
    double Hubwidth = 0.6;
    double Radius = 3;
    boolean fuelMakeIt = false;
    Field2d Field2d = new Field2d();
    double Rotation;
    String[] motorIDs = {"[FRS Swerve] Motor Id:" + RebuiltTunerConstants.kFrontRightSteerMotorId, "[FRD Swerve] Motor Id:" + RebuiltTunerConstants.kFrontRightDriveMotorId, "[FLS Swerve] Motor Id:" + RebuiltTunerConstants.kFrontLeftSteerMotorId,
    "[FLD Swerve] Motor Id:" + RebuiltTunerConstants.kFrontLeftDriveMotorId, "[RRS Swerve] Motor Id:" + RebuiltTunerConstants.kBackRightSteerMotorId, "[RRD Swerve] Motor Id:" + RebuiltTunerConstants.kBackRightDriveMotorId, 
    "[RLS Swerve] Motor Id:" + RebuiltTunerConstants.kBackLeftSteerMotorId, "[RLD Swerve] Motor Id:" + RebuiltTunerConstants.kBackLeftDriveMotorId,
    "[Turret] Motor Id:" + 1, "[Hood] Motor Id:"+ 2, "[Flywheel Lead] Motor Id:" + Constants.Flywheel_Lead_ID, "[Flywheel Follow] Motor Id:" + Constants.Flywheel_Follower_ID, "[Spindex] Motor Id:" + Constants.Spindex_ID,
    "[Indexer] Motor Id:" + Constants.Index_ID, "[Intake] Motor Id:" + Constants.Intake_ID, "[PCM] Motor Id: unknown", "[Pidgeon] Motor Id: unknown"};

    public ElasticData(Telemetry m_telemetry, PhotonVision camera1, PhotonVision camera2, IndexAndSpindexSubsystem indexAndSpindexSubsystem, TurretMovement turretMovement){
        //objects for the two classes
        telemetry = m_telemetry;
        cameraDataMain = camera1;
        cameraDataTurret = camera2;
        this.indexAndSpindexSubsystem = indexAndSpindexSubsystem;
        this.turretMovement = turretMovement;

        //swerve widget based on the values gained from telemetry, 100% needs to be tuned
        //and maybe even needs to use different telemtry variables. havent gotten a chance to figure that out yet.
        SmartDashboard.putData("Drive/Swerve Drive", new Sendable() {
            @Override      
            public void initSendable(SendableBuilder builder) {
                builder.setSmartDashboardType("SwerveDrive");
      
                builder.addDoubleProperty("Front Left Angle", () -> telemetry.m_moduleDirections[1].getAngle() /* * 2 * Math.PI */, null);
                builder.addDoubleProperty("Front Left Velocity", () -> telemetry.m_moduleSpeeds[1].getLength(), null);
      
                builder.addDoubleProperty("Front Right Angle", () -> telemetry.m_moduleDirections[2].getAngle() /* * 2 * Math.PI */, null);
                builder.addDoubleProperty("Front Right Velocity", ()  -> telemetry.m_moduleSpeeds[2].getLength(), null);
      
                builder.addDoubleProperty("Back Left Angle", () -> telemetry.m_moduleDirections[3].getAngle() /*  * 2 * Math.PI */, null);
                builder.addDoubleProperty("Back Left Velocity", () -> telemetry.m_moduleSpeeds[3].getLength(), null);
      
                builder.addDoubleProperty("Back Right Angle", () -> telemetry.m_moduleDirections[0].getAngle() /*  * 2 * Math.PI */, null);
                builder.addDoubleProperty("Back Right Velocity", () -> telemetry.m_moduleSpeeds[0].getLength(), null);
      
                builder.addDoubleProperty("Robot Angle", () -> telemetry.m_poseArray[2], null);
            } 
        });

        //Ben T's smartdashboard stuff
        SmartDashboard.putNumber("Testing/Ben T's Stuff/flywheelSpeed", 0);
        SmartDashboard.putNumber("Testing/Ben T's Stuff/hood target position", 0);
    }

    @Override
    public void periodic(){
        //variables for the non turret camera values
        double[] targetIDs = cameraDataMain.getIDs().stream()
        .mapToDouble(Double::doubleValue)
        .toArray();

        //smartdashboard values putting for non turret camera
        SmartDashboard.putNumber("Vision/Main Cam/Raw pitch", cameraDataMain.getAnyPitch());
        SmartDashboard.putNumber("Vision/Main Cam/Raw yaw", cameraDataMain.getAnyYaw());
        SmartDashboard.putNumberArray("Vision/Main Cam/Target IDs", targetIDs);
        SmartDashboard.putBoolean("Vision/Main Cam/Target Visible", cameraDataMain.targetVisible());
        SmartDashboard.putNumber("Vision/Main Cam/Ambiguity", cameraDataMain.getAmbiguity());
        SmartDashboard.putNumber("Vision/Main Cam/Y Rotation", cameraDataMain.getYRotation());
        SmartDashboard.putNumber("Vision/Main Cam/X Rotation", cameraDataMain.getXRotation());
        SmartDashboard.putNumber("Vision/Main Cam/Z Rotation", cameraDataMain.getZRotation());
        SmartDashboard.putNumber("Vision/Main Cam/Distance", cameraDataMain.getDistance());
        SmartDashboard.putData("Fields/Plain Field", Field2d);
        if(cameraDataMain.targetVisible() == true){
            SmartDashboard.putData("Fields/Robot Position Field", cameraDataMain.getRobotPos());
            SmartDashboard.putData("Fields/Turret Position Field", cameraDataTurret.getDistanceAndAngle());
        }
    
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
        
        //AC- Additional Field2d Stuff
        Field2d.getObject("Hub").setPose(4.6,4,new Rotation2d(0.0));
        if(turretMovement.returnMotor() != null){
            Rotation =  turretMovement.returnMotor().getPosition().getValueAsDouble() + Field2d.getRobotPose().getRotation().getDegrees();
        }
        else{
            Rotation = 0.0;
        }

        SmartDashboard.putBoolean("Will the Fuel make it?", fuelMakeIt);
        Field2d.getObject("Aim").setPose(Field2d.getRobotPose().getX() +  (Radius * (Math.cos(Rotation))),Field2d.getRobotPose().getY() + (Radius * (Math.sin(Rotation))),new Rotation2d(Rotation));
        Pose2d AimPose = Field2d.getObject("Aim").getPose();
        Pose2d HubPose = Field2d.getObject("Hub").getPose();
        if(AimPose.getX() >= (HubPose.getX()-(Hubwidth/2)) && AimPose.getX() <= (HubPose.getX()+(Hubwidth/2))){
            if (AimPose.getY() >= (HubPose.getY()-Hubwidth) && AimPose.getY() <= (HubPose.getY()+Hubwidth)) {
                fuelMakeIt = true;
            }
            else{
                fuelMakeIt = false;
            }
        }
        else{
            fuelMakeIt = false;
        } 
                    //Ac - Motor Id's
            //Ac - Motor Id's
        //new HoodSubsystem(cameraDataMain).returnMotor().getDeviceID()


        //Motor Widgets
        SmartDashboard.putNumber("Testing/Motors/Indexer/Index Volatage", indexAndSpindexSubsystem.indexMotor.getAppliedOutput());
        SmartDashboard.putNumber("Testing/Motors/Spindex/Spindex Volatage", indexAndSpindexSubsystem.spindexMotor.getAppliedOutput());
        SmartDashboard.putStringArray("Testing/Motors/Motor Id Constants", motorIDs);
        //new TurretMovement().returnMotor().getDeviceID()
        for (String motorInfo : motorIDs) {
            try {
                int openBracket = motorInfo.indexOf("[");
                int closeBracket = motorInfo.indexOf("]");
                int colonIndex = motorInfo.indexOf(":");

                //gets what is inside the [brackets]
                String folderName = motorInfo.substring(openBracket + 1, closeBracket).trim();
        
                // gets everything after the colon
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

        //updates the Smartdash board Values
        SmartDashboard.updateValues();


        
    }

    

}