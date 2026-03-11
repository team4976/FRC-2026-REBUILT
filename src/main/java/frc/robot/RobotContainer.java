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
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;

//import frc.robot.commands.IntakeCommand;
import frc.robot.generated.RebuiltTunerConstants;
import frc.robot.generated.TurretTunerConstants;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ElasticData;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.SmartDashboardHub;
import frc.robot.commands.Climb;
import frc.robot.commands.HoodCommand;
import frc.robot.commands.IndexAndSpindexCommand;
import frc.robot.commands.FlywheelCommand;
import frc.robot.commands.TurretLeft;
import frc.robot.commands.TurretRight;
import frc.robot.commands.TurretScan;
import frc.robot.commands.TurretScanYaw;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.TurretMovement;
import static frc.robot.Constants.*;

import java.util.List;

import frc.robot.subsystems.ElasticData;
import frc.robot.commands.intakeCommand;

public class RobotContainer {
//Shooting is op, Intake is drive 
    //Logging
    private final Telemetry logger = new Telemetry(Constants.MaxSpeed);

    //Vision Objects, may be good idea to merge into one class and just have dif objects
    private final PhotonVision vision = new PhotonVision("testingCamera", logger);
    private final PhotonVision m_turretvision = new PhotonVision("testingCamera", logger);

    //Subsystem Objects/Subsystem Initialization
    private final Intake intakeSubsystem = new Intake();
    private final ClimberSubsystem climber = new ClimberSubsystem();
    private final TurretMovement turretMovement = new TurretMovement();
    public final FlywheelSubsystem flywheelSubsystem = new FlywheelSubsystem();
    public final HoodSubsystem hoodSubsystem = new HoodSubsystem(m_turretvision);
    public final TurretScan turretScan = new TurretScan(m_turretvision, turretMovement);
    public final TurretScanYaw turretScanYaw = new TurretScanYaw(m_turretvision, turretMovement);
    public final TurretLeft turretLeft = new TurretLeft(m_turretvision, turretMovement);
    public final TurretRight turretRight = new TurretRight(m_turretvision, turretMovement);
    public final IndexAndSpindexSubsystem indexAndSpindexSubsystem = new IndexAndSpindexSubsystem(m_turretvision, hoodSubsystem, flywheelSubsystem);
    //public SmartDashboardHub smartDashboardHub = new SmartDashboardHub();
    public final List<Subsystem> allSubsystemsList = List.of(
        intakeSubsystem,
        climber,
        turretMovement,
        flywheelSubsystem,
        hoodSubsystem,
        indexAndSpindexSubsystem
    );

    //Command Objects
    public IndexAndSpindexCommand indexAndSpindexCommand = new IndexAndSpindexCommand(indexAndSpindexSubsystem, false, hoodSubsystem, flywheelSubsystem);
    public intakeCommand intakeCommand = new intakeCommand(intakeSubsystem);
    public FlywheelCommand flywheelCommand = new FlywheelCommand(flywheelSubsystem);

    //Controller Objects
    public static final CommandXboxController driverController = new CommandXboxController(0);
    public static final CommandXboxController operatorController = new CommandXboxController(1);

    //elastic/smartdashboard intialization 
    private ElasticData elasticData = new ElasticData(logger, vision, m_turretvision, allSubsystemsList);

    //for the elastic folder, gonna be merged to elastic data later

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-driverController.getLeftY() * Constants.MaxSpeed)
                //((driverController.povUp().getAsBoolean())?-1:(driverController.povDown().getAsBoolean())?1:.0) * MaxSpeed
                .withVelocityY(-driverController.getLeftX() * Constants.MaxSpeed)
                .withRotationalRate(-driverController.getRightX() * Constants.MaxAngularRate)
            )    
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );
        driverController.a().whileTrue(drivetrain.applyRequest(() -> brake));
        driverController.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-driverController.getLeftY(), -driverController.getLeftX()))
        ));

        driverController.leftTrigger().toggleOnTrue(turretScanYaw);
        // calls the method that turns the stopButton for Scan to true
        //driverController.rightTrigger().onTrue(turretMovement.runOnce(()->turretMovement.getStopCommand()));
        //operatorController.leftBumper().onTrue(turretMovement.runOnce(()->turretMovement.forceTurretFlip()));
        //driverController.povLeft().whileTrue(new TurretLeft(m_turretvision, turretMovement));
        //driverController.povRight().whileTrue(new TurretRight(m_turretvision, turretMovement));
        // Reset the field-centric heading on left bumper press.
        driverController.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        // Regular Shooting
        //Should be right bumper
        driverController.axisGreaterThan(3, 0.3).whileTrue(indexAndSpindexCommand);
        driverController.x().onTrue(intakeCommand);
        //driverController.leftBumper().whileTrue(flywheelCommand);
        //driverController.y().onTrue(pneumaticIntake);

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        //operatorController.start().whileTrue(new Climb(climber));
        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        //operatorController.b().whileTrue(new Climb(climber));
        //driverController.leftTrigger().onTrue(new TurretScan(m_turretvision, turretMovement));
        // calls the method that turns the stopButton for Scan to true
        //driverController.rightTrigger().onTrue(turretMovement.runOnce(()->turretMovement.getStopCommand()));
        driverController.povLeft().whileTrue(turretLeft);
        driverController.povRight().whileTrue(turretRight);

        // Spin flywheel and start hood should be a (operator controller)
        driverController.rightBumper().whileTrue(flywheelCommand);
    
        // Manual hood override
        operatorController.povUp().onTrue(new HoodCommand(hoodSubsystem, true));
        operatorController.povUp().onFalse(new HoodCommand(hoodSubsystem, true));
        operatorController.povDown().onTrue(new HoodCommand(hoodSubsystem, true));
        operatorController.povDown().onFalse(new HoodCommand(hoodSubsystem, true));
        // Force Shoot
        //operatorController.b().whileTrue(new IndexAndSpindexCommand(InSSubsystem, true, hoodSubsystem));
        drivetrain.registerTelemetry(logger::telemeterize);
    }

    private final SendableChooser<Command> autoChooser = null;

    public Command getAutonomousCommand() {
        // Simple drive forward auton
        return Commands.print("d");
        //final var idle = new SwerveRequest.Idle();
        /*return Commands.sequence(
            // Reset our field centric heading to match the robot
            // facing away from our alliance station wall (0 deg).
            drivetrain.runOnce(() -> drivetrain.seedFieldCentric(Rotation2d.kZero)),
            // Then slowly drive forward (away from us) for 5 seconds.
            drivetrain.applyRequest(() ->
                drive.withVelocityX(0.5)
                    .withVelocityY(0)
                    .withRotationalRate(0)
            )
            .withTimeout(5.0);
            // Finally idle for the rest of auton
            drivetrain.applyRequest(() -> idle)
           
        );
        */
    }

}
