// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import frc.robot.generated.RebuiltTunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  //serve constants
  //swerve drive variables and objects
  public static double MaxSpeed = RebuiltTunerConstants.kSpeedAt12Volts.in(MetersPerSecond) * 0.5; // kSpeedAt12Volts desired top speed
  public static double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); //rotations per second max angular velocity
  /* Setting up bindings for necessary control of the swerve drive platform */
  public static final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
          .withDeadband(0.2).withRotationalDeadband(0.2) // Add a 10% deadband
          .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
  public static final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
  public static final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
  public static final CommandSwerveDrivetrain drivetrain = RebuiltTunerConstants.createDrivetrain();
  public static final SwerveRequest idle = new SwerveRequest.Idle();

  //id constants
  public static final int Hood_ID = 48;
  public static final int Flywheel_Lead_ID = 46;
  public static final int Flywheel_Follower_ID = 47;
  public static final int Index_ID = 31;
  public static final int Spindex_ID = 40;
  public static final int Turret_ID = 43;
  public static final int Intake_ID = 41;
  public static final int Intake_Channel = 2;


  public static final int kDriverControllerPort = 0;
  public static final int LEFT_LEADER_ID = 1;
  public static double turretScanVoltage = 1.2;
  public static double turretManualVoltage = .5; //2
  public static boolean stopbutton = false;
  public static double yaw;
  public static double targetYaw = 0.0;
  public static double currentYaw;
  public static double currentSkew;
  public static boolean targetVisible = false;
  public static int framesMissed = 0; //How many frames has the turret not seen the april tag
  public static final double turretCameraHeight = .4826;
  public static final double TagHeight = 1.084;
  public static final double CameraPitch = 0;
  public static final double TagPitch = 0;
// middle of the hub red: 13.0155555556, 4.41236111111
// middle of the hub blue: 5.05861111111, 4.41236111111
  public static double RedHubX = 11.915394;
  public static double RedHubY = 4.042283;
  public static double BlueHubX = 4.625594;
  public static double BlueHubY = 4.042283;
  public static int hubId = 0;

  public static final double turretLimitLeft = 21.7626953125; // software limits on turret motion
  public static final double turretLimitRight = -11.59228515625;
  public static final double turretDeadzoneSize = 41.667 - (Constants.turretLimitLeft - Constants.turretLimitRight);// the turret encoder position the turret cannot go
  // 8.6056


  public static final AprilTagFieldLayout kTagLayout = AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);
  public static final Transform3d kRobotToCam = new Transform3d(new Translation3d(0.0, 0.0, 0.635), new Rotation3d(0, 0, 0));
  public static double intakeSpeed = 0.60;
}
