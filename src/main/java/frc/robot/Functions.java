package frc.robot;

import frc.robot.subsystems.PhotonVision;

public class Functions {

    public static double GetCalculatedFlywheelSpeed (PhotonVision camera){
        return 31.49597 + (10.19041 * (camera.getDistance() + 0.5969)) - (0.4148098 * Math.pow(camera.getDistance() + 0.5969, 2));
    };
    public static double GetCalculatedFlywheelSpeed (PhotonVision camera, double offset){
        return 31.49597 + (10.19041 * (camera.getDistance() + 0.5969)) - (0.4148098 * Math.pow(camera.getDistance() + 0.5969, 2)) * offset;
    };
    
}
