package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Telemetry;
import frc.robot.generated.TunerConstants;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonUtils;
import org.photonvision.struct.PhotonTrackedTargetSerde;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import java.util.List;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;

public class TurretVision extends SubsystemBase {
    private final PhotonPoseEstimator photonEstimator;
    private PhotonCamera turretCamera;
    PhotonPipelineResult result;
    PhotonUtils turretUtils;
    Pose2d estimateTurret2d;
    boolean hasTargets;
    Optional<Alliance> ally;

    double DistanceX;
    double DistanceY;
    double RedAllianceSqaured;

    double turretAngle;
    double turretDistance;
    double turretTargetAngle;
    double HubX; // final hub X accounting for movement
    double HubY; // final hub Y accounting for movement

    double DriveVelocityX;
    double DriveVelocityY;
    double ballAirTime;
    double hubMovedX; // How much did the hub move from the real hub to the virtual one
    double hubMovedY;
    double hubOrigX; // the original hub position
    double hubOrigY;

    Field2d field2d = new Field2d();

    Telemetry logger;

    public TurretVision(Telemetry logger){
        this.logger = logger;
        // sets up turretCamera
        turretCamera = new PhotonCamera("Turret_Camera");
        photonEstimator = new PhotonPoseEstimator(Constants.kTagLayout, Constants.kRobotToCam);

        ally = DriverStation.getAlliance();

        if (ally.isPresent()) {
            if (ally.get() == Alliance.Blue) {
                hubOrigX = Constants.BlueHubX; 
                hubOrigY = Constants.BlueHubY;
                    }
            if (ally.get() == Alliance.Red) {
                hubOrigX = Constants.RedHubX; 
                hubOrigY = Constants.RedHubY;
            }}
    }

    // gets the if we have AprilTags in the vision or not
    public boolean getTags(){
        //System.err.println("getTags Works");
        result = turretCamera.getLatestResult();
        hasTargets = result.hasTargets();
        return hasTargets;
    }

    // gets the yaw of the april tag
    
    public double getYaw(){
        result = turretCamera.getLatestResult();
        hasTargets = result.hasTargets();
        if(hasTargets){
            PhotonTrackedTarget target = result.getBestTarget();
            return target.getYaw();
        }
        else{
            return 0;
        }
    }

    // gets the robot pose based on two april tags or tries with one
    public Field2d getDistanceAndAngle(){
         HubX = hubOrigX;
         HubY = hubOrigY;

        if (result != null && result.hasTargets()){
            var cameraResult = result.getMultiTagResult();
            if (cameraResult != null && cameraResult.isEmpty() == false) {
                var fieldToCamera = cameraResult.get().estimatedPose.best;
                field2d.setRobotPose(new Pose2d(fieldToCamera.getX(), fieldToCamera.getY(), fieldToCamera.getRotation().toRotation2d()));

                // Initial Distance calculation
                DistanceX = HubX - field2d.getRobotPose().getX();
                DistanceY = HubY - field2d.getRobotPose().getY();
                turretDistance = Math.sqrt(DistanceX*DistanceX + DistanceY*DistanceY);

                for (int i = 0; i < 5; i++){
                    ballAirTime = turretDistance*0.5; // TESTING REMOVE LATER
                    // update BallAirTime
                    //turretDistance = driveVelocity*BallAirTime;
                    hubMovedX = DriveVelocityX*-1*ballAirTime;
                    hubMovedY = DriveVelocityY*-1*ballAirTime;

                    // moving the virtual hub
                    HubX = HubX + hubMovedX;
                    HubY = HubY + hubMovedY;
                    // making a new distance based on the virtual hub
                    DistanceX = HubX - field2d.getRobotPose().getX();
                    DistanceY = HubY - field2d.getRobotPose().getY();
                    turretDistance = Math.sqrt(DistanceX*DistanceX + DistanceY*DistanceY);

                }


                SmartDashboard.putNumber("Virtual Hub x", HubX);
                SmartDashboard.putNumber("Virtual Hub x", HubX);

                // calculates the angle we want to get to
                turretTargetAngle = Math.atan(DistanceX / DistanceY);

                // calculates the angle of the bot from the middle
                turretAngle = (fieldToCamera.getRotation().toRotation2d().getDegrees());
            } 
            else {
                field2d.setRobotPose(new Pose2d()); //Maybe not such a good idea
            }
        } else {
            field2d.setRobotPose(new Pose2d());
        }
        return field2d;
    }

    // when called gives you the turret distance calculated previously
    public double getTurretDistance(){
        return turretDistance;
    }

    // when called gives you the turret angle calculated previously
    public double getTurretAngle(){
        return turretAngle;
    }

    // when called gives you the targetTurretAngle calculated previously
    public double getTurretTargetAngle(){
        return turretTargetAngle;
    }

    @Override
    public void periodic() { 
        if( logger.driveState != null){
        DriveVelocityX = logger.driveState.Speeds.vxMetersPerSecond;
        DriveVelocityY = logger.driveState.Speeds.vyMetersPerSecond;
        SmartDashboard.putNumber("VXSpeeds", logger.driveState.Speeds.vxMetersPerSecond);
        SmartDashboard.putNumber("VYSpeeds", logger.driveState.Speeds.vyMetersPerSecond);
        }
    }
}