package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.BooleanSupplier;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Telemetry;

public class PhotonVision extends SubsystemBase{
    VisionData vision;
    CommandSwerveDrivetrain swerve;
    Transform3d transform3d;
    public Double turretTargetAngle = 0.0;
    public Double turretAngle = 0.0;

    public BooleanSupplier AutoShootFlag = ()->{
            boolean hasVaildTarget = ((turretTargetAngle-turretAngle) < 2.5 && (turretTargetAngle-turretAngle) > -2.5);
          //  System.out.println("Has Vaild Target: " + hasVaildTarget);
            return hasVaildTarget;
         };

    /**
     * A class containing mwthods for the different camera objects we create. 
     * Automatically gets the camera results in a periodic so you just have to call the methods through the object and they will always be up to date.
     * @param cameraName The name of the camera as a string. This is what differentiates different cameras so make sure it is correct.
     * @param logger The telemetry object of the project. Userd for calculations based on robot speed and positioning and the such.
     */
    public PhotonVision(String cameraName, Telemetry logger, CommandSwerveDrivetrain swerve, Transform3d transform3d){
        vision = new VisionData(cameraName, logger, swerve, transform3d);
    }

    @Override
    public void periodic(){
        vision.update();
    }

    /**
     * Gets the ambiguity of the target highest in the pipeline.
     * @return The targets ambiguity as a double.
     */
    public double getAmbiguity(){
        return vision.getAmbiguity();
    }

    /**
     * Checks if we can see any april tags. Is true if we see tags and is false if not.
     * @return Whether we see a tag or not as a boolean.
     */
    public boolean targetVisible() {
        return vision.targetVisible();
    }

    /**
     * Gets the yaw of the target highest in the pipeline. Is able to get any targets yaw.
     * <p> Yaw is the distance from the center of the camera the target is on the X axis.
     * @return The yaw of the target as a double.
     */
    public double getAnyYaw() {
        return vision.getAnyYaw();
    }

    /**
     * Gets the Pitch of the target highest in the pipeline. Is able to get any targets yaw.
     * <p> Pitch is the distance from the center of the camera the target is on the Y axis.
     * @return The yaw of the target as a double.
     */
    public double getAnyPitch() {
        return vision.getAnyPitch();
    }

    /**
     * Not a great setup on my part but this is a method for getting a list of all april tag IDs currently seen by the camera.
     * @return The IDs seen by the camera in a list.
     */
    public List<Double> getIDs() {
        return vision.getIDs();
    }

    /**
     * NEEDS TO BE UPDATED. 
     * <p> A method for getting the yaw of a desired april tag based on ID. Not well made.
     * <p> Is passed an array of tag IDs but will only return the yaw of the first tag in the array it sees.
     * This is only useful if we can properly triangulate the position of the hub using any of the tags we pass.
     * @param IDs The array of IDs we want to potentially getthe yaw of
     * @return The yaw of the first target from the list that we see.
     */
    public OptionalDouble getTargetYaw(int[] IDs) {
        return vision.getTargetYaw(IDs);
    }

    /**
     * NEEDS TO BE UPDATED. 
     * <p> Get the pitch of a singular desired april tag ID.
     * @param id The id to get the yaw of.
     * @return The pitch of the desired tag if seen at the time of the method call.
     */
    public OptionalDouble getTargetPitch(int id) {
        return vision.getTargetPitch(id);
    }

    /**
     * Gets the pose of the robot based on any seen april tags. Robot pose is placed on a Field2d and can be gotten through the .getRobotPose() method.
     * @return The field2d with the robot pose. Is 0.0 if not tags are seen at time of call.
     */
    public Field2d getRobotPos(){
        return vision.findRobotPos();
    }

    public List<PhotonTrackedTarget> getTargets(){
        return vision.getTargets();
    }

    public Optional<EstimatedRobotPose> getRobotPoseVision(){
        return vision.getRobotPoseVision();
    }

    /**
     * Gets the Y rotation of the target highest in the pipeline. 
     * <p> Y rotation is a 3d rotation of the april tag on the Y rotation axis. 
     * Y rotation is if the apriltag is rotated on a horizontal centered line along the imaginary X axis of the tag. 
     * Imagine it spinning like one of those rotating whiteboards or mirrors that allow you to rotate to see both sides.
     * Eg. the top half of the apriltag is closer to the camera than the bottom half. 
     * @return The Y rotation of the target in radians as a double.
     */
    public double getYRotation(){
        return vision.getYRotation();
    }

    /**
     * Gets the X rotation of the target highest in the pipeline. 
     * <p> X rotation is a 2d rotation of the april tag on the X rotation axis. 
     * X rotation is if the apriltag is rotated on a 3d centered line along an imaginary Z axis going through the tag. 
     * Imagine it like the face of the tag rotating in 2d.
     * Eg. the top of the apriltag rotates 90 degrees clockwise resulting in 
     * the top now being the right side, the right side becoming the bottom, the bottom becoming the left side, and the left side becoming the top. 
     * @return The X rotation of the target in radians as a double.
     */
    public double getXRotation(){
        return vision.getXRotation();
    }

    /**
     * Gets the Z rotation of the target highest in the pipeline. 
     * <p> Z rotation is a 3d rotation of the april tag on the Z rotation axis. 
     * Z rotation is if the apriltag is rotated on a vertical centered line along an imaginary Y axis. 
     * Imagine it like a rotating hidden door or just a door in general. 
     * Eg. The right side of the tag is closer to the camera than the left side of the tag
     * @return The Z rotation of the target in radians as a double.
     */
    public double getZRotation(){
        return vision.getZRotation();
    }

    /**
     * 
     * @return
     */
    public double getDistance(){
        return vision.getDistance();
    }

    public double getBotAngle(){
        return vision.getBotAngle();
    }

    /**
     * 
     * @return
     */
    public double getTurretDistance(){
        return vision.getTurretDistance();
    }

    /**
     * 
     * @return
     */
    public double getTurretTargetAngle(){
        return vision.getTurretTargetAngle();
    }

    /**
     * 
     * @param targetID
     * @return
     */
    public double getTargetZRotation(int targetID){
        return vision.getTargetZRotation(targetID);
    }

}
