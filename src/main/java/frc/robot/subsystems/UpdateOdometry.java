package frc.robot.subsystems;

import java.util.List;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
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

    private List<PhotonTrackedTarget> targets;
    int numOfTags;
    Optional<EstimatedRobotPose> EstPose;
    Pose2d InitialPose;
    private Matrix<N3, N1> good = VecBuilder.fill(0.1, 0.1, 6.0);
    
    public UpdateOdometry(CommandSwerveDrivetrain swerve, PhotonVision vision){
        this.swerve = swerve;
        this. vision = vision;
    }

    @Override
    public void periodic() {
        targets = vision.getTargets();
        numOfTags = vision.getIDs().size();
        //System.out.println(vision.vision.cameraName);
        updateOdometryWithVision(vision);
        //System.err.println("periodic working in updateOdometry");
    }

    public void updateOdometryWithVision(PhotonVision vision){

       // double startTime = System.currentTimeMillis();
        
        Pose2d robotPos = new Pose2d();
        tagAmbiguity = -1.0;
        timeStamp = 0.0;
        EstPose = vision.getRobotPoseVision();

        // if EstPose returns empty then exit the method
        if(EstPose.isEmpty()){
            return;
        }

        // set robotPose(Pose2d) to the EstPose
        robotPos = EstPose.get().estimatedPose.toPose2d();
        tagAmbiguity = vision.getAmbiguity();
        //get when the picture was taken
        timeStamp = EstPose.get().timestampSeconds;
        double distanceToTag = -1.0;

        // if targets is not empty get the distance from vision
        if(!targets.isEmpty()){ 
            distanceToTag = vision.getDistance();
        }
        ///System.err.println("made it past !targets.isEmpty");

        // if bot is disabled reset the pose to a position you get off of april tags
        if(DriverStation.isDisabled()) {
            InitialPose = robotPos;
            swerve.resetPose(InitialPose); // link to auto
        }

        if(DriverStation.isEnabled()) {
            // Use the multi-target pose for reliable vision poses
            if (numOfTags > 1) {
                swerve.addVisionMeasurement(robotPos, timeStamp, good);
                //System.err.println(" 2 tags seen");
            } else {
                //qualifying checks for poses gotten from a single apriltag
                if ((Math.abs(tagAmbiguity) < Constants.maxAcceptableAmbiguity) && (numOfTags > 0) 
                    && (distanceToTag < Constants.maxAcceptableDistance)) {
                        swerve.addVisionMeasurement(robotPos, timeStamp , good); 
                        //System.err.println("1 tag seen");
                }
            }
            
        }
        //double endTime = System.currentTimeMillis();

        //System.out.println(startTime);
       // System.out.println(endTime);

    }
}
