package frc.robot.subsystems;

import java.util.List;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class UpdateOdometry extends SubsystemBase {
    private CommandSwerveDrivetrain swerve;
    private PhotonVision vision;

    double tagAmbiguity;
    double timeStamp;

    private List<Double> targets;
    int numOfTags;
    Optional<EstimatedRobotPose> EstPose;
    Pose2d InitialPose;
    
    public UpdateOdometry(CommandSwerveDrivetrain swerve, PhotonVision vision){
        this.swerve = swerve;
        this. vision = vision;
    }

    @Override
    public void periodic() {
        targets = vision.getIDs();
        numOfTags = vision.getIDs().size();
        updateOdometryWithVision(vision);
    }

    public void updateOdometryWithVision(PhotonVision vision){

        Pose2d robotPos = new Pose2d();
        tagAmbiguity = -1.0;
        timeStamp = 0.0;

        EstPose = vision.getRobotPoseVision();

        if(EstPose.isEmpty()){
            return;
        }

        robotPos = EstPose.get().estimatedPose.toPose2d();
        tagAmbiguity = vision.getAmbiguity();
        timeStamp = EstPose.get().timestampSeconds;
        double distanceToTag = -1.0;

        if(!targets.isEmpty()){ 
            distanceToTag = vision.getDistance();
        }

        if(DriverStation.isDisabled()) {
            InitialPose = robotPos;
            swerve.resetPose(InitialPose); // link to auto
        }

        if(DriverStation.isEnabled()) {
            // Use the multi-target pose for reliable vision poses
            if (numOfTags > 1) {
                swerve.addVisionMeasurement(robotPos, timeStamp);
            } else {
                //qualifying checks for poses derived from a single apriltag
                if ((Math.abs(tagAmbiguity) < Constants.maxAcceptableAmbiguity) && (numOfTags > 0) 
                    && (distanceToTag < Constants.maxAcceptableDistance)) {
                        swerve.addVisionMeasurement(robotPos, timeStamp); 
                }
            }
            
        }
    }
}
