// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
//test
package frc.robot;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import static edu.wpi.first.units.Units.*;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;

import frc.robot.commands.ToggleIntake;
import frc.robot.commands.Drive;
import frc.robot.generated.OldTunerConstants;
import frc.robot.generated.TurretTunerConstants;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ElasticData;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.VisionData; 
import frc.robot.commands.Climb;
import frc.robot.commands.HoodCommand;
import frc.robot.commands.IndexAndSpindexCommand;
import frc.robot.commands.FlywheelCommand;
import frc.robot.commands.TurretLeft;
import frc.robot.commands.TurretRight;
import frc.robot.commands.TurretScan;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.TurretMovement;
import frc.robot.subsystems.TurretVision;
import frc.robot.Elastic.ElasticContainer;

public class RobotContainer {

    private double MaxSpeed = 1.0 * OldTunerConstants.kSpeedAt12Volts.in(MetersPerSecond) / 3.5; // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.2).withRotationalDeadband(MaxAngularRate * 0.2) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
            private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
    public final CommandSwerveDrivetrain drivetrain = OldTunerConstants.createDrivetrain();

    //Vision Objects, may be good idea to merge into one class and just have dif objects
    private PhotonVision vision = new PhotonVision("testingCamera");
    private final TurretVision m_turretvision = new TurretVision();

    //The variables made for vision testing, may not be needed anymore
    private final double MaxYaw = 32;
    private final double speedDamper = 3.5;
    private final double desiredDistance = 2;
    private final double MaxDistance = 32;
    private double driveWithAprilTag = 0;
    private double driveWithStick = 1;

    //Subsystem Objects/Subsystem Initialization
    private final Intake intake = new Intake();
    private final Pneumatics Pneumatics = new Pneumatics();
    private final ClimberSubsystem climber = new ClimberSubsystem();
    private final TurretMovement m_shooter = new TurretMovement();
    final ToggleIntake activation = new ToggleIntake(Pneumatics, intake);
    public FlywheelSubsystem flywheelSubsystem = new FlywheelSubsystem();
    public HoodSubsystem hoodSubsystem = new HoodSubsystem();
    public IndexAndSpindexSubsystem InSSubsystem = new IndexAndSpindexSubsystem();

    //Controller Objects
    public static final CommandXboxController driverController = new CommandXboxController(0);
    public static final CommandXboxController operatorController = new CommandXboxController(1);

    //logging and elastic/smartdashboard intialization
    private final Telemetry logger = new Telemetry(MaxSpeed);
    private ElasticData elasticData = new ElasticData(logger, vision);

    //for the elastic folder, gonna be merged to elastic data later
    public final ElasticContainer elastic;

    public RobotContainer() {
        elastic = new ElasticContainer(this,logger);
        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(Math.max(-MaxSpeed, Math.min((((((vision.getDistance() - desiredDistance) / MaxDistance) * driveWithAprilTag) + (-driverController.getLeftY() * driveWithStick)) * MaxSpeed), MaxSpeed))) //Drive forward with negative Y (forward)
                    .withVelocityY(((((-vision.getAnyYaw()/MaxYaw) * driveWithAprilTag) + (-driverController.getLeftX() * driveWithStick)) * MaxSpeed) / speedDamper) //Drive left with negative X (left)
                    .withRotationalRate(((((1 - (vision.getZRotation()/Math.PI)) * driveWithAprilTag ) + (-driverController.getRightX() * driveWithStick)) * MaxAngularRate) / speedDamper) // Don't rotate Drive counterclockwise with negative X (left)
                )       
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );
        driverController.a().whileTrue(drivetrain.applyRequest(() -> brake));
        driverController.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-driverController.getLeftY(), -driverController.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        operatorController.b().whileTrue(new Climb(climber));
        operatorController.x().whileTrue(new Drive(intake));
        operatorController.y().onTrue(activation);
        driverController.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));
        driverController.leftTrigger().onTrue(new TurretScan(m_turretvision, m_shooter));
        // calls the method that turns the stopButton for Scan to true
        driverController.rightTrigger().onTrue(m_shooter.runOnce(()->m_shooter.getStopCommand()));
        driverController.povLeft().whileTrue(new TurretLeft(m_turretvision, m_shooter));
        driverController.povRight().whileTrue(new TurretRight(m_turretvision, m_shooter));

        // Spin flywheel and start hood
        operatorController.a().onTrue(new FlywheelCommand(flywheelSubsystem, 20).andThen(new HoodCommand(hoodSubsystem, false, 0)));
    
        //Planned Button Mapping: Driver: Joysticks to drive, Right Bumper is shoot, Left Bumper: brake, B:drive relative. Operator: Climb: Y, Intake: A, Flywheel: X, Manual Overides: Hood up and down d-pad,  

        // Manual hood override
        operatorController.povUp().onTrue(new HoodCommand(hoodSubsystem, true, 0.1));
        operatorController.povUp().onFalse(new HoodCommand(hoodSubsystem, true, 0));
        operatorController.povDown().onTrue(new HoodCommand(hoodSubsystem, true, -0.1));
        operatorController.povDown().onFalse(new HoodCommand(hoodSubsystem, true, 0));

        // Regular Shooting
        driverController.x().onTrue(new IndexAndSpindexCommand(InSSubsystem, false));

        // Force Shoot
        operatorController.b().onTrue(new IndexAndSpindexCommand(InSSubsystem, true));

        // Reset the field-centric heading on left bumper press.
        driverController.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    private final SendableChooser<Command> autoChooser = null;

    public Command getAutonomousCommand() {
        // Simple drive forward auton
        final var idle = new SwerveRequest.Idle();
        return Commands.sequence(
            // Reset our field centric heading to match the robot
            // facing away from our alliance station wall (0 deg).
            drivetrain.runOnce(() -> drivetrain.seedFieldCentric(Rotation2d.kZero)),
            // Then slowly drive forward (away from us) for 5 seconds.
            drivetrain.applyRequest(() ->
                drive.withVelocityX(0.5)
                    .withVelocityY(0)
                    .withRotationalRate(0)
            )
            .withTimeout(5.0),
            // Finally idle for the rest of auton
            drivetrain.applyRequest(() -> idle)
        );
    }


//PRE ORGANIZATION COMMENTS, PROBABLY USELESS

    //driverController.back().and(driverController.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
    //driverController.back().and(driverController.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
    //driverController.start().and(driverController.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
    //driverController.start().and(driverController.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
    //driverController.x().onTrue(pipelineSwitcher());
    //driverController.y().onTrue(toggleJoystix());
    //driverController.leftTrigger(0.5).whileTrue(moveAprilTagLeft());
    //driverController.rightTrigger(0.5).whileTrue(moveAprilTagRight());
    //driverController.a().onTrue(elastic.fieldWidget.getAuto("Test Wait Command"));
    //driverController.b().onTrue(elastic.fieldWidget.getAuto("First Test"));
    //driverController.x().onTrue(elastic.fieldWidget.getAuto("Test Auto"));
    //driverController.y().onTrue(elastic.fieldWidget.getAuto("HPR"));
    //onTrue(getAutonomousCommand());//(new Activation(Pneumatics));
}
