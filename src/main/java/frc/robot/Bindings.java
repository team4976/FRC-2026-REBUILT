package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.JitterRobotSequence;

import static frc.robot.Constants.*;

public class Bindings {
    /*public RobotContainer robotContainer;
    public IndexAndSpindexCommand indexAndSpindexCommand;
    public IndexAndSpindexCommand reverseIndexer;
    public IntakeCommand intakeCommand;
    public FlywheelCommand flywheelCommand;
    public FlywheelCommand flywheelOverrideCommand;
    public HoodCommand hoodCommand;
    public HoodCommand manualHoodUp;
    public HoodCommand manualHoodDown;
    public TurretScan turretScan;
    public TurretScanYaw turretScanYaw;
    public AlignedShotCommand alignedShotCommand;
    public Autos autos;
    public IntakeCommand reverseIntake;
    public JitterRobot jitterSubsystem;
    public IntakeSubsystem intakeSubsystem;
    public Command repeatJidderCommand;

    public Bindings(RobotContainer robotContainer){
        this.robotContainer = robotContainer;
        this.indexAndSpindexCommand = robotContainer.indexAndSpindexCommand;
        this.reverseIndexer = robotContainer.reverseIndexerCommand;
        this.intakeCommand = robotContainer.intakeCommand;
        this.flywheelCommand = robotContainer.flywheelCommand;
        this.hoodCommand = robotContainer.hoodCommand;
        this.manualHoodUp = robotContainer.manualHoodUpCommand;
        this.manualHoodDown = robotContainer.manualHoodDownCommand;
        this.turretScanYaw = robotContainer.turretScanYawCommand;
        this.turretScan = robotContainer.turretScanCommand;
        this.autos = robotContainer.autos;
        this.reverseIntake = robotContainer.reverseIntakeCommand;
        this.alignedShotCommand = robotContainer.alignedShotCommand;
        this.jitterSubsystem = robotContainer.jitterSubsystem;
        this.intakeSubsystem = robotContainer.intakeSubsystem;
        this.repeatJidderCommand = robotContainer.repeatJidderCommand;
    }*/

    public static void driverConfigureBindings(RobotContainer robotContainer){
        //Swerve break and align
        driverController.a().whileTrue(drivetrain.applyRequest(() -> brake));
        driverController.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-driverController.getLeftY(), -driverController.getLeftX()))
        ));
        // Reset the field-centric heading on left bumper press.
        driverController.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        //Regular Shooting
        driverController.axisGreaterThan(3, 0.1).whileTrue(GlobalCommands.instance.indexAndSpindexCommand);

        //Jitter the Intake
        driverController.rightBumper().whileTrue(robotContainer.repeatJidderCommand).onFalse(
                Commands.runOnce(
                    ()->GlobalCommands.instance.intakeCommand.end(true)
                )
            );
        
        //Intake
        driverController.x().onTrue(Commands.deadline(Commands.waitSeconds(0.2), GlobalCommands.instance.intakeCommand));

        //Stops Intake Motor
        driverController.y().onTrue(robotContainer.intakeSubsystem.stopIntakeMotorCommand());

        //Bring Intake Up Slowly
        driverController.start().whileTrue(robotContainer.intakeSubsystem.intakeCommand(false, 0, false)).onFalse(
                    Commands.runOnce(
                    ()->GlobalCommands.instance.intakeCommand.end(true)
                )
            );
        
        //Auto Aim
        driverController.y().toggleOnTrue(GlobalCommands.instance.turretScanCommand);
    }

    public static void operatorConfigureBindings(RobotContainer robotContainer){

        //------------
        //Main Controls
        //------------
        //Spin up flywheels
        //operatorController.a().toggleOnTrue(hoodCommand.withDeadline(flywheelCommand));
        //operatorController.a().toggleOnTrue(GlobalCommands.instance.flywheelCommand);
        
        //Operator Shoot
        operatorController.axisGreaterThan(3, 0.1).whileTrue(GlobalCommands.instance.indexAndSpindexCommand);

        //Turret scan
        operatorController.axisGreaterThan(2, 0.1).toggleOnTrue(GlobalCommands.instance.turretScanYawCommand);

        //perfect shot from the aligned spot
        operatorController.x().toggleOnTrue(GlobalCommands.instance.alignedShotCommand);

        //Jitter Robot
        operatorController.y().whileTrue(
            Commands.repeatingSequence(new JitterRobotSequence())
        );

        //---------------
        //Manual Overrides
        //---------------

        //Turret
        //Inside of the turret subsystems periodic()
        
        //Flywheel
        //Inside of the flywheel subsystems periodic()

        //Hood
        //operatorController.povUp().whileTrue(manualHoodUp);
        //operatorController.povDown().whileTrue(manualHoodDown);



        //Indexer
        //TODO: fix
        operatorController.b().whileTrue(GlobalCommands.instance.reverseIndexerCommand);

        //Reverse Intake
        operatorController.povDown().whileTrue(GlobalCommands.instance.reverseIntakeCommand);
    }
}
