package frc.robot.subsystems;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Telemetry;
import frc.robot.generated.OldTunerConstants;
import static edu.wpi.first.units.Units.*;

//was designed to be the only elastic subsystem/container but it isnt currently
//the other two can be merged with this one later, gott set it up for multiple camers with some renaming
//and gotta add all the other stuff.

public class ElasticData extends SubsystemBase{
    private final Telemetry telemetry;
    private final PhotonVision cameraData;
    Field2d Field2d = new Field2d();
    public ElasticData(Telemetry m_telemetry, PhotonVision camera){
        //objects for the two classes
        telemetry = m_telemetry;
        cameraData = camera;

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
        SmartDashboard.putNumber("Testing/Ben T's Stuff/kV", 0.1);
        SmartDashboard.putNumber("Testing/Ben T's Stuff/kP", 0.4);
        SmartDashboard.putNumber("Testing/Ben T's Stuff/kI", 0.0);
        SmartDashboard.putNumber("Testing/Ben T's Stuff/kD", 0.0);
    }


    @Override
    public void periodic(){
        //variables for the non turret camera values
        boolean targetVisible = cameraData.targetVisible();
        double[] targetIDs = cameraData.getIDs().stream()
        .mapToDouble(Double::doubleValue)
        .toArray();
        double yRotation = cameraData.getYRotation();
        double xRotation = cameraData.getXRotation();
        double zRotation = cameraData.getZRotation();
        double ambiguity = cameraData.getAmbiguity();
        double distance = cameraData.getDistance();

        //smartdashboard values putting for non turret camera
        SmartDashboard.putNumber("Vision/Raw pitch", cameraData.getAnyPitch());
        SmartDashboard.putNumber("Vision/Raw yaw", cameraData.getAnyYaw());
        SmartDashboard.putNumberArray("Vision/Target IDs", targetIDs);
        SmartDashboard.putBoolean("Vision/Target Visible", targetVisible);
        SmartDashboard.putNumber("Vision/Ambiguity", ambiguity);
        SmartDashboard.putNumber("Vision/Y Rotation", yRotation);
        SmartDashboard.putNumber("Vision/X Rotation", xRotation);
        SmartDashboard.putNumber("Vision/Z Rotation", zRotation);
        SmartDashboard.putNumber("Vision/Distance", distance);
        SmartDashboard.putData("Fields/Plain Field", Field2d);
        if(cameraData.targetVisible() == true){
            SmartDashboard.putData("Fields/Robot Position Field", cameraData.getRobotPos());
            SmartDashboard.putData("Field/Turret Position Field", cameraData.getDistanceAndAngle());
        }
        for(var id : targetIDs){
            double yaw = cameraData
            .getTargetYaw((int) id)
            .orElse(Double.NaN);
            if (!Double.isNaN(yaw)){
                SmartDashboard.putNumber("Target" + id + "yaw", yaw);
            }
        }
        for(var id : targetIDs){
            double pitch = cameraData
            .getTargetYaw((int) id)
            .orElse(Double.NaN);
            if (!Double.isNaN(pitch)){
                SmartDashboard.putNumber("Target" + id + "pitch", pitch);
            }
        }
        
        
        //updates the 
        SmartDashboard.updateValues();


        
    }

    



}
