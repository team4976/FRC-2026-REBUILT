package frc.robot.subsystems;

import static frc.robot.Constants.BlueHubX;
import static frc.robot.Constants.BlueHubY;
import static frc.robot.Constants.RedHubX;
import static frc.robot.Constants.RedHubY;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class UpdateHubInfo extends SubsystemBase{
    CommandSwerveDrivetrain swerve;
        // Hub Information
    private Optional<Alliance> alliance;  //Team alliance (Red or Blue)
    private double distanceX; // the distance between us and the virtual hub on the X plane
    private double distanceY; // the distance between us and the virtual hub on the Y plane
    public double hubOrigX;
    public double hubOrigY;
    public double hubDistance;  // Total Distance to hub
    public double hubAngle;
    public double botAngle;
    public double hubId;

  public UpdateHubInfo(CommandSwerveDrivetrain swerve){
    this.swerve = swerve;

   //Logic to determine HubX and HubY coordinates
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

    public double getHubDistance(){
        double hubX = hubOrigX;
        double hubY = hubOrigY;

        // using odometry to get the distance from us and the hub
        distanceX = hubX - swerve.getState().Pose.getX();
        distanceY = hubY - swerve.getState().Pose.getY();

        hubDistance = Math.sqrt(distanceX*distanceX + distanceY*distanceY);
        return  hubDistance;

     }

     public double getHubAngle(){
        double hubX = hubOrigX;
        double hubY = hubOrigY;

        distanceX = hubX - swerve.getState().Pose.getX();
        distanceY = hubY - swerve.getState().Pose.getY();

                    // calculates the angle we want to get to
        hubAngle = Math.toDegrees(Math.atan2( distanceY,distanceX));
        return  hubAngle;

     }

    public double getBotAngle(){
        botAngle=swerve.getState().Pose.getRotation().getDegrees();
        return  botAngle;
     }
    
    
}
