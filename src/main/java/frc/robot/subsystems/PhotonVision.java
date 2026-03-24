package frc.robot.subsystems;

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
            System.out.println("Has Vaild Target: " + hasVaildTarget);
            return hasVaildTarget;
         };

    public PhotonVision(String cameraName, Telemetry logger, CommandSwerveDrivetrain swerve, Transform3d transform3d){
        vision = new VisionData(cameraName, logger, swerve, transform3d);
    }

    @Override
    public void periodic(){
        vision.update();
    }

    public double getAmbiguity(){
        return vision.getAmbiguity();
    }

    public boolean hasTarget(int id) {
        return vision.hasTarget(id);
    }

    public boolean targetVisible() {
        return vision.targetVisible();
    }

    public double getAnyYaw() {
        return vision.getAnyYaw();
    }

    public double getAnyPitch() {
        return vision.getAnyPitch();
    }

    public List<Double> getIDs() {
        return vision.getIDs();
    }

    public OptionalDouble getTargetYaw(int id) {
        return vision.getTargetYaw(id);
    }

    public OptionalDouble getTargetPitch(int id) {
        return vision.getTargetPitch(id);
    }

    public Field2d getRobotPos(){
        return vision.findRobotPos();
    }

    public List<PhotonTrackedTarget> getTargets(){
        return vision.getTargets();
    }

    public Optional<EstimatedRobotPose> getRobotPoseVision(){
        return vision.getRobotPoseVision();
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

    public double getBotAngle(){
        return vision.getBotAngle();
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
