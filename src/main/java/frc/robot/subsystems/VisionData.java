package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Telemetry;

import static frc.robot.Constants.*;


public class VisionData{
    //making the variable for the camera used--
    //when using this class and making the variable for the--
    //latest result that gets updated using the update method--
    //inside of a periodic keeping the latest result uniform among--
    //all methods in the file.

    //field (as in the java field not the smartdashboard field) decalrations 
    private PhotonCamera camera;
    private Telemetry logger;
    private PhotonPipelineResult latestResult;
    private Optional<Alliance> alliance;
    public double hubOrigX;
    public double hubOrigY;
    private double DriveVelocityX; // the velocity we are travelling in Y direction
    private double DriveVelocityY; // the velocity we are travelling in the X direction
    private double distanceX; // the distance between us and the virtual hub on the X plane
    private double distanceY; // the distance between us and the virtual hub on the Y plane
    public double turretDistance;
    public double turretAngle;
    public double turretTargetAngle;
    public double hubId;
    Field2d field2d = new Field2d();
    List<Double> idYaws;


    //the constructor, having the camera as a parameter--
    //means the methods in this class can be used dynamically--
    //with any camera and dont need to be changed if the camera--
    //name gets changed throughout the season.
    public VisionData(String cameraName, Telemetry logger){
        camera = new PhotonCamera(cameraName);
        this.logger = logger;
        
        SmartDashboard.putNumber("Testing/Hub Location X", hubOrigX );
        SmartDashboard.putNumber("Testing/Hub Location Y", hubOrigY );
    }

    public void getHubPose(){

        alliance = DriverStation.getAlliance();

        if (alliance.isPresent()) {
            if (alliance.get() == Alliance.Blue) {
                hubOrigX = BlueHubX; 
                hubOrigY = BlueHubY;
                
                hubId = 26;
            }
            if (alliance.get() == Alliance.Red) {
                hubOrigX = RedHubX; 
                hubOrigY = RedHubY;
                hubId = 10;
            }
        }
    }

    //called at the top of the periodic in PhotonVision, keeps the camera frame used uniform.
    //also updates the Drive Velocities and the turrret distance and angle by calling the method but doesnt use the returned value.
    public void update() {
        var results = camera.getAllUnreadResults();
        if (!results.isEmpty()) {
            latestResult = results.get(results.size() - 1);
        }
        if(logger.driveState != null){
        DriveVelocityX = logger.driveState.Speeds.vxMetersPerSecond;
        DriveVelocityY = logger.driveState.Speeds.vyMetersPerSecond;
        }
        getDistanceAndAngle();
    }
    
    //gets the abiguity
    public double getAmbiguity(){
        if (latestResult != null && latestResult.hasTargets()){
            PhotonTrackedTarget target = latestResult.getBestTarget();
            return target.getPoseAmbiguity();
        } else {
            return -1.0;
        }

    }


    //returns an array list of all april tag IDs seen by the camera.
    //turns out there is a better native way to do this with the getBestCameraToTarget and  methods
    public List<Double> getIDs(){
        List<Double> targetIDs = new ArrayList<>();
        if(latestResult != null && latestResult.hasTargets()){
            for (var target : latestResult.getTargets()){
                targetIDs.add(targetIDs.size(), (double) target.getFiducialId());
            }
        }
        return targetIDs;
    }

    //returns the yaw of a desired target if that target is seen by the camera.
    public OptionalDouble getTargetYaw(int[] tagIDs){
       double yaw = Double.NaN;
        if(latestResult != null && latestResult.hasTargets()){
            for (var target : latestResult.getTargets()){
                for (var tagID : tagIDs)
                    if (target.getFiducialId() == tagID){
                        yaw = target.getYaw();
                        break;
                        //idYaws.add(target.getYaw());
                    }
            }
            if(yaw == Double.NaN) return OptionalDouble.empty();
            return OptionalDouble.of(yaw);
        }
        return OptionalDouble.empty();
    }

    //returns the pitch of a desired target if that target is seen by the camera.
    public OptionalDouble getTargetPitch(int tagID){
        if(latestResult != null && latestResult.hasTargets()){
            for (var target : latestResult.getTargets()){
                if (target.getFiducialId() == tagID){
                    return OptionalDouble.of(target.getPitch());
                }
            }
        }
        return OptionalDouble.empty();
    }

    //returns the yaw of the target most desired in the pipline, the closest or first one seen I think rn
    public double getAnyYaw(){
        if (latestResult != null && latestResult.hasTargets()){
            var target = latestResult.getBestTarget();
            return target.getYaw();
        } else {
            return 0.0;
        }

    }

    //same as any yaw, gets the most desired pitch of the target, both of them work on id's or balls
    public double getAnyPitch(){
        if (latestResult != null && latestResult.hasTargets()){
            var target = latestResult.getBestTarget();
            return target.getPitch();
        } else {
            return 0.0;
        }
    }

    //returns a boolean for if the camera sees a target.
    public boolean targetVisible(){
        return latestResult != null && latestResult.hasTargets();
    }

    //gets and sets robot pose and returns a field with the modified robot pose, may be better to just modify the pose of a preexisting field but this works too
    public Field2d findRobotPos(){
        if (latestResult != null && latestResult.hasTargets()){
            var cameraResult = latestResult.getMultiTagResult();
            if (cameraResult != null && cameraResult.isEmpty() == false) {
                    var fieldToCamera = cameraResult.get().estimatedPose.best;
                    field2d.setRobotPose(new Pose2d(fieldToCamera.getX(), fieldToCamera.getY(), fieldToCamera.getRotation().toRotation2d()));
            } else {  
                field2d.setRobotPose(new Pose2d()); //Maybe not such a good idea
            }
        } else {
            field2d.setRobotPose(new Pose2d());
        }
        return field2d;
    }

    //changes the pipline type, for us thats from apriltag to ball
    public void pipelineSwitcher(int pipelineID){
        camera.setPipelineIndex(pipelineID);
    }
    
    //gets the current pipeline and returns it
    public double getPipelineMethod(){
        return camera.getPipelineIndex();
    }

    //gets the Y rotation of the target seen, refer to photon docs to see which axis this moves on
    public double getYRotation(){
        if (latestResult != null && latestResult.hasTargets()){
            var target = latestResult.getBestTarget();
            double rotationY = target.getBestCameraToTarget().getRotation().getY();
            return rotationY;
        } else {
            return 0.0;
        }
    }

    //gets the X rotation of the target seen, refer to photon docs to see which axis this moves on
    public double getXRotation(){
        if (latestResult != null && latestResult.hasTargets()){
            var target = latestResult.getBestTarget();
            double rotationX = target.getBestCameraToTarget().getRotation().getX();
            return rotationX;
        } else {
            return 0.0;
        }
    }

    //gets the Z rotation of the target seen, refer to photon docs to see which axis this moves on
    //returns in radians, centered will flip between -179.99 and 179.99 and they will get closer to 0 as you rotate until it goes back around or cuts out.
    public double getZRotation(){
        if (latestResult != null && latestResult.hasTargets()){
            var target = latestResult.getBestTarget();
            double rotationZ = target.getBestCameraToTarget().getRotation().getZ();
            return rotationZ;
        } else {
            return 0.0;
        }
    }

    public double getTargetZRotation(int tagID){
        if (latestResult != null && latestResult.hasTargets()){
            for (var target : latestResult.getTargets()){
                if (target.getFiducialId() == tagID){
                    double rotationZ = target.getBestCameraToTarget().getRotation().getZ();
                    return rotationZ;
                }
            }
        }
        return 0.0;
    }

    //gets the plain distance of the camera from the apriltag in meters
    public double getDistance(){
        if (latestResult != null && latestResult.hasTargets()){
            var target = latestResult.getBestTarget();
            double distance = target.getBestCameraToTarget().getTranslation().getX();
            return distance;
        }
        return 0.0;
    }

    
    // gets the robot pose based on two april tags or tries with one
    public Field2d getDistanceAndAngle(){
        double hubX = hubOrigX;
        double hubY = hubOrigY;

        if (latestResult != null && latestResult.hasTargets()){
            var cameraResult = latestResult.getMultiTagResult();
            if (cameraResult != null && cameraResult.isEmpty() == false) {
                var fieldToCamera = cameraResult.get().estimatedPose.best;
                field2d.setRobotPose(new Pose2d(fieldToCamera.getX(), fieldToCamera.getY(), fieldToCamera.getRotation().toRotation2d()));

            // Initial Distance calculation
            distanceX = hubX - field2d.getRobotPose().getX();
            distanceY = hubY - field2d.getRobotPose().getY();

            turretDistance = Math.sqrt(distanceX*distanceX + distanceY*distanceY);
            SmartDashboard.putString("Testing/DistanceX",""+String.format("%.2f",hubX)+" - "+String.format("%.2f",field2d.getRobotPose().getX()) +"= "+String.format("%.2f",distanceX));
            SmartDashboard.putString("Testing/DistanceY",""+String.format("%.2f",hubY)+" - "+String.format("%.2f",field2d.getRobotPose().getY()) +"= "+String.format("%.2f",distanceY));
            SmartDashboard.putString("Testing/turretDistance Split", "sqrt("+String.format("%.2f",distanceX)+"*"+String.format("%.2f",distanceX) +"+"+ String.format("%.2f",distanceY)+"*"+(String.format("%.2f",distanceY)+")=" +turretDistance));

            // calculates the angle we want to get to
            turretTargetAngle = Math.toDegrees(Math.atan2( distanceY,distanceX));
            // calculates the angle of the bot from the middle
            turretAngle = (fieldToCamera.getRotation().toRotation2d().getDegrees());
            } else {
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






}

