package frc.robot;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.MetersPerSecond;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.apriltag.AprilTagFields;

import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.generated.RebuiltTunerConstants;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  //Controller Objects
  public static final CommandXboxController driverController = new CommandXboxController(0);
  public static final CommandXboxController operatorController = new CommandXboxController(1);

  //Swerve Constants
  //swerve drive variables and objects
  public static final double MaxSpeed = RebuiltTunerConstants.kSpeedAt12Volts.in(MetersPerSecond);// * 0.75; // kSpeedAt12Volts desired top speed
  public static final double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); //rotations per second max angular velocity
  /* Setting up bindings for necessary control of the swerve drive platform */
  public static final SwerveRequest.RobotCentric robotCentricDrive = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
  public static final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
          .withDeadband(0.2).withRotationalDeadband(0.2) // Add a 10% deadband
          .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
  public static final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
  public static final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
  public static final CommandSwerveDrivetrain drivetrain = RebuiltTunerConstants.createDrivetrain(); //TurretTunerConstants.createDrivetrain();
  public static final SwerveRequest idle = new SwerveRequest.Idle();

  //ID constants
  public static final int Index_ID = 31;
  public static final int Spindex_ID = 40;
  public static final int Intake_ID = 41;
  public static final int Turret_ID = 43;
  public static final int Flywheel_Lead_ID = 46;
  public static final int Flywheel_Follower_ID = 47;
  public static final int Hood_ID = 48;
  public static final int Intake_Arm_Right_ID = 50;
  public static final int Intake_Arm_Left_ID = 51;



  //Speed Constants
  public static final double intakeSpeed = 0.95;
  public static final double turretScanVoltage = 2;
  public static final double turretManualVoltage = 1.5; //2

  //Unsed and potentially no longer accurate but I left for refernce just in case.
  public static final double turretCameraHeight = 0.4826;
  public static final double TagHeight = 1.084;

  //Auto aim stuff, Some of this should NOT be constants but whatever
  // converts from motor rotations to flywheel rotations
  public static final double flywheelGearRatio = 1/1.667;
  public static final double flywheelRadius = Units.inchesToMeters(2);  

  public static final int kDriverControllerPort = 0;
  public static final int LEFT_LEADER_ID = 1;
  public static double yaw;
  public static final double targetYaw = 0.0;
  public static double currentYaw;
  public static double currentSkew;
  public static final double RedHubX = 11.915394;
  public static final double RedHubY = 4.042283;
  public static final double BlueHubX = 4.625594;
  public static final double BlueHubY = 4.042283;

  //Important turret limits
  public static final double turretLimitLeft = 21.7626953125; // software limits on turret motion
  public static final double turretLimitRight = -11.59228515625;
  public static final double manualNudgeLimit = 5; // maximum degrees manual turretMovement moves

  public static final double maxAcceptableAmbiguity = 0.1;
  public static final double maxAcceptableDistance = 4;

  public static double TurretCamYaw;

  public static final AprilTagFieldLayout kTagLayout = AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);
  public static final Transform3d leftBackCamTransform3d = new Transform3d(new Translation3d(-0.314325, 0.314325, 0.1524), new Rotation3d(0, Math.toRadians(-30), Math.toRadians(120)));
  public static final Transform3d rightBackCamTransform3d = new Transform3d(new Translation3d(-0.314325, -0.314325, 0.1524), new Rotation3d(0, Math.toRadians(-30), Math.toRadians(-150)));
  public static final Transform3d turretCamTransform = new Transform3d(new Translation3d(-0.20,-0.312 , 0.45), new Rotation3d(0, 0/*Math.toRadians(-30)*/, 0/*TurretCamYaw*/));
}
