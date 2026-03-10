package frc.robot.Elastic;

import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Telemetry;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class ElasticContainer {

    public FieldWidget fieldWidget = new FieldWidget();

    // Initializes the Elastic subsystems.
public ElasticContainer() {
   

    try{
        //PathPlannerPath path = PathPlannerPath.fromPathFile("Elastic Test");
        fieldWidget.addChooser();
        //fieldWidget.addPath("ElasticTest", path);

    }catch(Exception e){
        System.out.println(e.getMessage());
    }
}
}
