package frc.robot.subsystems;

import static frc.robot.Constants.BlueHubX;
import static frc.robot.Constants.BlueHubY;
import static frc.robot.Constants.RedHubX;
import static frc.robot.Constants.RedHubY;
import static frc.robot.Constants.flywheelGearRatio;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class UpdateHubInfo extends SubsystemBase{
    CommandSwerveDrivetrain swerve;
    FlywheelSubsystem flywheelSubsystem;
    
        // Hub Information
    private Optional<Alliance> alliance;  //Team alliance (Red or Blue)
    private double distanceX; // the distance between us and the virtual hub on the X plane
    private double distanceY; // the distance between us and the virtual hub on the Y plane
    public double hubOrigX;
    public double hubOrigY;
    public double hubMovedX;
    public double hubMovedY;
    public double hubDistance;  // Total Distance to hub
    public double hubAngle;
    public double botAngle;
    public double hubId;
    public double ballAirTime;
    public double ballVelocity;
    public double ballVelocityX;
    public double FlywheelSpeed;

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

        /*for (int i = 0; i < 5; i++){
            // calculation to know what the flywheel will be set to given the distance
           FlywheelSpeed = (31.49597 + (10.19041 * (hubDistance + 0.5969))  
                - (0.4148098 * Math.pow(hubDistance+ 0.5969, 2))) * 0.9;
            //equation to get the velocity of the ball from the velocity of the flywheel
            ballVelocity = FlywheelSpeed*Constants.flywheelGearRatio*360*0.0508*0.5;
            //the ball gets around 50% of the velocity of the flywheel
            //getting the velocity of the ball in the x direction
            ballVelocityX = ballVelocity*Math.cos(Math.toRadians(60));

            ballAirTime = hubDistance/ballVelocityX;
            // update BallAirTime
            //turretDistance = driveVelocity*BallAirTime;
            hubMovedX = swerve.getState().Speeds.vxMetersPerSecond*-1*ballAirTime;
            hubMovedY = swerve.getState().Speeds.vyMetersPerSecond*-1*ballAirTime;

            // Moving the virtual hub
            hubX = hubX + hubMovedX;
            hubY = hubY + hubMovedY;
            // making a new distance based on the virtual hub
            distanceX = hubX - swerve.getState().Pose.getX();
            distanceY = hubY - swerve.getState().Pose.getY();
            hubDistance = Math.sqrt(distanceX*distanceX + distanceY*distanceY);
        }*/

        return  hubDistance;

     }

     public double getHubAngle(){
        getHubDistance();
        // calculates the angle we want to get to
        hubAngle = Math.toDegrees(Math.atan2( distanceY,distanceX));
        return  hubAngle;

     }


    public double getBotAngle(){
        botAngle=swerve.getState().Pose.getRotation().getDegrees();
        return  botAngle;
     }
    
    
}
