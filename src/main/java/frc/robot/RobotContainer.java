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

import frc.robot.commands.intakePneumatic;
import frc.robot.commands.IntakeMotor;
import frc.robot.generated.RebuiltTunerConstants;
import frc.robot.generated.TurretTunerConstants;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ElasticData;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.Pneumatics;
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
import frc.robot.Elastic.ElasticContainer;
import static frc.robot.Constants.*;
import frc.robot.subsystems.ElasticData;

public class RobotContainer {
//Shooting is op, Intake is drive 
    //Logging
    private final Telemetry logger = new Telemetry(Constants.MaxSpeed);

    //Vision Objects, may be good idea to merge into one class and just have dif objects
    private final PhotonVision vision = new PhotonVision("testingCamera", logger);
    private final PhotonVision m_turretvision = new PhotonVision("testingCamera", logger);

    //Subsystem Objects/Subsystem Initialization
    private final Pneumatics Pneumatics = new Pneumatics();
    private final Intake motorIntake = new Intake();
    private final intakePneumatic pneumaticIntake = new intakePneumatic(Pneumatics, motorIntake);
    private final ClimberSubsystem climber = new ClimberSubsystem();
    private final TurretMovement turretMovement = new TurretMovement();
    public FlywheelSubsystem flywheelSubsystem = new FlywheelSubsystem();
    public HoodSubsystem hoodSubsystem = new HoodSubsystem(m_turretvision);
    public IndexAndSpindexSubsystem InSSubsystem = new IndexAndSpindexSubsystem(m_turretvision, hoodSubsystem);
    public IntakeMotor intakeMotorCommand = new IntakeMotor(motorIntake);
    public IndexAndSpindexCommand indexAndSpindexCommand = new IndexAndSpindexCommand(InSSubsystem, false, hoodSubsystem);
    //Controller Objects
    public static final CommandXboxController driverController = new CommandXboxController(0);
    public static final CommandXboxController operatorController = new CommandXboxController(1);

    //elastic/smartdashboard intialization 
    //private ElasticData elasticData = new ElasticData(logger, vision, m_turretvision);
    private ElasticData elasticData = new ElasticData(logger, vision, m_turretvision, InSSubsystem);

    //for the elastic folder, gonna be merged to elastic data later
    public final ElasticContainer elastic;

    public RobotContainer() {
        elastic = new ElasticContainer();
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

        //driverController.leftTrigger().onTrue(new TurretScan(m_turretvision, turretMovement));
        // calls the method that turns the stopButton for Scan to true
        //driverController.rightTrigger().onTrue(turretMovement.runOnce(()->turretMovement.getStopCommand()));
        //driverController.povLeft().whileTrue(new TurretLeft(m_turretvision, turretMovement));
        //driverController.povRight().whileTrue(new TurretRight(m_turretvision, turretMovement));
        // Regular Shooting
        //driverController.rightBumper().whileTrue(new IndexAndSpindexCommand(InSSubsystem, false, hoodSubsystem));
        driverController.x().whileTrue(intakeMotorCommand);
        driverController.y().whileTrue(indexAndSpindexCommand);
        //driverController.x().onTrue(pneumaticIntake);

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        //operatorController.start().whileTrue(new Climb(climber));
        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        //operatorController.b().whileTrue(new Climb(climber));
        //operatorController.x().whileTrue(new IntakeMotor(motorIntake));
        //operatorController.x().onTrue(pneumaticIntake);
        //driverController.leftTrigger().onTrue(new TurretScan(m_turretvision, turretMovement));
        // calls the method that turns the stopButton for Scan to true
        //driverController.rightTrigger().onTrue(turretMovement.runOnce(()->turretMovement.getStopCommand()));
        //driverController.povLeft().whileTrue(new TurretLeft(m_turretvision, turretMovement));
        //driverController.povRight().whileTrue(new TurretRight(m_turretvision, turretMovement));

        // Spin flywheel and start hood
        //operatorController.a().onTrue(new FlywheelCommand(flywheelSubsystem, 20));
    
        // Manual hood override
        //operatorController.povUp().onTrue(new HoodCommand(hoodSubsystem, true, 0.1));
        //operatorController.povUp().onFalse(new HoodCommand(hoodSubsystem, true, 0));
        //operatorController.povDown().onTrue(new HoodCommand(hoodSubsystem, true, -0.1));
        //operatorController.povDown().onFalse(new HoodCommand(hoodSubsystem, true, 0));

        // Regular Shooting
        //driverController.rightBumper().whileTrue(new IndexAndSpindexCommand(InSSubsystem, false, hoodSubsystem));

        // Force Shoot
        //operatorController.b().whileTrue(new IndexAndSpindexCommand(InSSubsystem, true, hoodSubsystem));

        // Reset the field-centric heading on left bumper press.
        driverController.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

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
