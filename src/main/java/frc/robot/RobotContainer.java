// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
//test
package frc.robot;


import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;

import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ElasticData;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.PhotonVision;
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
    public final HoodSubsystem hoodSubsystem = new HoodSubsystem();
    public final TurretScan turretScan = new TurretScan(m_turretvision, turretMovement);
    public final TurretScanYaw turretScanYaw = new TurretScanYaw(m_turretvision, turretMovement);
    public final TurretLeft turretLeft = new TurretLeft(m_turretvision, turretMovement);
    public final TurretRight turretRight = new TurretRight(m_turretvision, turretMovement);
    public final IndexAndSpindexSubsystem indexAndSpindexSubsystem = new IndexAndSpindexSubsystem(m_turretvision, hoodSubsystem, flywheelSubsystem);
    public final List<Subsystem> allSubsystemsList = List.of(
        intakeSubsystem,
        climber,
        turretMovement,
        flywheelSubsystem,
        hoodSubsystem,
        indexAndSpindexSubsystem
    );

    //Command Objects
    public IndexAndSpindexCommand indexAndSpindexCommand = new IndexAndSpindexCommand(indexAndSpindexSubsystem, 0.5);//hoodSubsystem, flywheelSubsystem);
    public IndexAndSpindexCommand reverseIndexer = new IndexAndSpindexCommand(indexAndSpindexSubsystem, -0.5);//hoodSubsystem, flywheelSubsystem);
    public IntakeCommand intakeCommand = new IntakeCommand(intakeSubsystem);
    public FlywheelCommand flywheelCommand = new FlywheelCommand(flywheelSubsystem, m_turretvision, false);
    public FlywheelCommand flywheelOverrideCommand = new FlywheelCommand(flywheelSubsystem, m_turretvision, true);
    public HoodCommand hoodCommand = new HoodCommand(hoodSubsystem, m_turretvision, false, 0);
    public HoodCommand manualHoodUp = new HoodCommand(hoodSubsystem, m_turretvision, true, 0.5);
    public HoodCommand manualHoodDown = new HoodCommand(hoodSubsystem, m_turretvision, true, -0.5);
    //public Command hoodAndFlywheel = new ParallelDeadlineGroup(flywheelCommand, hoodCommand);

    //elastic/smartdashboard intialization 
    private ElasticData elasticData = new ElasticData(logger, vision, m_turretvision, allSubsystemsList);

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

        driverConfigureBindings();
        operatorConfigureBindings();
        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public void driverConfigureBindings(){
        //Swerve
        driverController.a().whileTrue(drivetrain.applyRequest(() -> brake));
        driverController.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-driverController.getLeftY(), -driverController.getLeftX()))
        ));
        // Reset the field-centric heading on left bumper press.
        driverController.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        //Turret
        driverController.leftTrigger().toggleOnTrue(turretScanYaw);

        //Regular Shooting
        driverController.rightBumper().whileTrue(indexAndSpindexCommand);

        //Intake
        driverController.x().onTrue(intakeCommand);
    }

    public void operatorConfigureBindings(){
        //------------
        //Main Controls
        //------------
        operatorController.a().toggleOnTrue(hoodCommand.withDeadline(flywheelCommand));

        //---------------
        //Manual Overrides
        //---------------

        //Turret
        operatorController.povLeft().whileTrue(turretLeft);
        operatorController.povRight().whileTrue(turretRight);
        
        //Flywheel
        operatorController.leftTrigger(0.1).whileTrue(flywheelOverrideCommand);

        //Hood
        operatorController.povUp().whileTrue(manualHoodUp);
        operatorController.povDown().whileTrue(manualHoodDown);

        //Indexer
        operatorController.b().whileTrue(reverseIndexer);
    }

    private final SendableChooser<Command> autoChooser = null;

    public Command getAutonomousCommand() {
        return Commands.print("d");
    }

}
