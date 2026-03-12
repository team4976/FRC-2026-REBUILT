// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
//test
package frc.robot;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;

import static edu.wpi.first.units.Units.*;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;

import frc.robot.commands.intakeCommand;
import frc.robot.commands.Auto.IntakeExtend;
import frc.robot.generated.RebuiltTunerConstants;
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
import frc.robot.commands.FlywheelStart;
import frc.robot.commands.FlywheelStop;
import frc.robot.commands.TurretLeft;
import frc.robot.commands.TurretRight;
import frc.robot.commands.TurretScan;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.TurretMovement;
import static frc.robot.Constants.*;

import java.util.List;

import frc.robot.subsystems.ElasticData;

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
    public IndexAndSpindexCommand indexAndSpindexCommand = new IndexAndSpindexCommand(indexAndSpindexSubsystem, 0.5);//hoodSubsystem, flywheelSubsystem);
    public IndexAndSpindexCommand reverseIndexer = new IndexAndSpindexCommand(indexAndSpindexSubsystem, -0.5);//hoodSubsystem, flywheelSubsystem);
    public intakeCommand IntakeCommand = new intakeCommand(intakeSubsystem);
    public FlywheelCommand flywheelCommand = new FlywheelCommand(flywheelSubsystem, m_turretvision);
    public HoodCommand hoodCommand = new HoodCommand(hoodSubsystem, m_turretvision, false, 0);
    public HoodCommand manualHoodUp = new HoodCommand(hoodSubsystem, m_turretvision, true, 0.5);
    public HoodCommand manualHoodDown = new HoodCommand(hoodSubsystem, m_turretvision, true, -0.5);
    public FlywheelStart flyWheelStart = new FlywheelStart(flywheelSubsystem, vision);
    public FlywheelStop flywheelStop = new FlywheelStop(flywheelSubsystem, vision);

    //Controller Objects
    public static final CommandXboxController driverController = new CommandXboxController(0);
    public static final CommandXboxController operatorController = new CommandXboxController(1);

    //elastic/smartdashboard intialization 
    private ElasticData elasticData = new ElasticData(logger, vision, m_turretvision, allSubsystemsList);

    //for the elastic folder, gonna be merged to elastic data later

    public RobotContainer() {
        drivetrain.configureAutoBuilder();
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
        // Reset the field-centric heading on left bumper press.
        driverController.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        // Regular Shooting
        //Should be right bumper
        driverController.axisGreaterThan(3, 0.3).whileTrue(indexAndSpindexCommand);
        driverController.x().onTrue(IntakeCommand);
        //driverController.y().onTrue(pneumaticIntake);

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        //operatorController.start().whileTrue(new Climb(climber));
        //driverController.leftTrigger().onTrue(new TurretScan(m_turretvision, turretMovement));
        // calls the method that turns the stopButton for Scan to true
        //driverController.rightTrigger().onTrue(turretMovement.runOnce(()->turretMovement.getStopCommand()));
        operatorController.povLeft().whileTrue(new TurretLeft(m_turretvision, turretMovement));
        operatorController.povRight().whileTrue(new TurretRight(m_turretvision, turretMovement));

        // Spin flywheel and start hood should be a (operator controller)
        //driverController.rightBumper().toggleOnTrue(flywheelCommand);
        driverController.rightBumper().toggleOnTrue(hoodCommand.withDeadline(flywheelCommand));
        
        // Manual hood override
        operatorController.povUp().whileTrue(manualHoodUp);
        operatorController.povDown().whileTrue(manualHoodDown);
        //operatorController.a().toggleOnTrue(hoodCommand.withDeadline(flywheelCommand));
        //operatorController.leftBumper().whileTrue(indexAndSpindexCommand);
        operatorController.b().whileTrue(reverseIndexer);
        drivetrain.registerTelemetry(logger::telemeterize);
    }

    private final SendableChooser<Command> autoChooser = null;

    public Command getAutonomousCommand() { 
        //elasticData..addChooser();
        IntakeExtend test = new IntakeExtend(intakeSubsystem);
        Command pathCommand = AutoBuilder.buildAuto("Final Auto 1");
        return test.andThen(new WaitCommand(.5)).andThen(pathCommand).andThen(TurretScan).andThen(flyWheelStart).andThen(new WaitCommand(.5)).andThen(indexAndSpindexCommand).andThen(new WaitCommand(.5)).andThen(flywheelStop);//(Command) elastic.fieldWidget.commandChooser.getSelected();
        //(Command) elastic.fieldWidget.commandChooser.getSelected();
        //System.out.println("*********: "+test.getName());
        //return test.andThen(IntakeCommand);
        //return test.andThen(flyWheelStart).andThen(new WaitCommand(.5)).andThen(indexAndSpindexCommand).andThen(new WaitCommand(.5)).andThen(flywheelStop);//(Command) elastic.fieldWidget.commandChooser.getSelected();
        
        //.alongWith(new TurretScan(m_turretvision, turretMovement))
        // Simple drive forward auton
        /*
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
//PRE ORGANIZATION COMMENTS, PROBABLY USELESS (IS USELESS)

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

