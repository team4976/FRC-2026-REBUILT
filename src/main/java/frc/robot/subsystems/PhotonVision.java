package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Telemetry;

public class PhotonVision extends SubsystemBase{
    VisionData vision;
    public Double turretTargetAngle = 0.0;
    public Double turretAngle = 0.0;

    public BooleanSupplier AutoShootFlag = ()->{
        boolean hasVaildTarget = ((turretTargetAngle-turretAngle) < 2.5 && (turretTargetAngle-turretAngle) > -2.5);
        return hasVaildTarget;
    };

    /**
     * A class containing mwthods for the different camera objects we create. 
     * Automatically gets the camera results in a periodic so you just have to call the methods through the object and they will always be up to date.
     * @param cameraName The name of the camera as a string. This is what differentiates different cameras so make sure it is correct.
     * @param logger The telemetry object of the project. Userd for calculations based on robot speed and positioning and the such.
     */
    public PhotonVision(String cameraName, Telemetry logger){
        vision = new VisionData(cameraName, logger);
    }

    @Override
    public void periodic(){
        vision.update();
    }

    /**
     * A method to get what hub we are looking for based on our alliance set in driverstation. Sets the hub IDs to be used in other methods.
     */
    public void getHubPose(){
        vision.getHubPose();
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

    public OptionalDouble getTargetPitch(int id) {
        return vision.getTargetPitch(id);
    }

    public Field2d getRobotPos(){
        return vision.findRobotPos();
    }

    public double getYRotation(){
        return vision.getYRotation();
    }

    public double getXRotation(){
        return vision.getXRotation();
    }

    public double getZRotation(){
        return vision.getZRotation();
    }

    public double getDistance(){
        return vision.getDistance();
    }

    public Field2d getDistanceAndAngle(){
        return vision.getDistanceAndAngle();
    }

    public double getTurretAngle(){
        return vision.getTurretAngle();
    }

    public double getTurretDistance(){
        return vision.getTurretDistance();
    }

    public double getTurretTargetAngle(){
        return vision.getTurretTargetAngle();
    }

    public double getTargetZRotation(int targetID){
        return vision.getTargetZRotation(targetID);
    }

}
