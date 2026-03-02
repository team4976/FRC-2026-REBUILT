package frc.robot.Elastic;

import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.RobotContainer;
import frc.robot.Telemetry;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class ElasticContainer {

    public FieldWidget fieldWidget = new FieldWidget();
    public final CommandSwerveDrivetrain drivetrain;
    Telemetry telemetry;
    public Double robotAngle;

    // Initializes the Elastic subsystems.
public ElasticContainer(RobotContainer robotContainer, Telemetry _telemetry) {
    telemetry = _telemetry;
    drivetrain = robotContainer.drivetrain;
    robotAngle = drivetrain
        .getRotation3d()
        .toRotation2d()
        .getRadians();
   

    try{
        //PathPlannerPath path = PathPlannerPath.fromPathFile("Elastic Test");
        fieldWidget.addChooser();
        //fieldWidget.addPath("ElasticTest", path);

    }catch(Exception e){
        System.out.println(e.getMessage());
    }
}
}
