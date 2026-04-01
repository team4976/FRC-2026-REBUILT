package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.AlignedShotCommand;
import frc.robot.commands.FlywheelCommand;
import frc.robot.commands.HoodCommand;
import frc.robot.commands.IndexAndSpindexCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.TurretScan;
import frc.robot.commands.TurretScanYaw;
import frc.robot.subsystems.Autos;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.JitterRobot;

import static frc.robot.Constants.*;

public class Bindings {
    public RobotContainer robotContainer;
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
        this.reverseIndexer = robotContainer.reverseIndexer;
        this.intakeCommand = robotContainer.intakeCommand;
        this.flywheelCommand = robotContainer.flywheelCommand;
        this.hoodCommand = robotContainer.hoodCommand;
        this.manualHoodUp = robotContainer.manualHoodUp;
        this.manualHoodDown = robotContainer.manualHoodDown;
        this.turretScanYaw = robotContainer.turretScanYaw;
        this.turretScan = robotContainer.turretScan;
        this.autos = robotContainer.autos;
        this.reverseIntake = robotContainer.reverseIntake;
        this.alignedShotCommand = robotContainer.alignedShotCommand;
        this.jitterSubsystem = robotContainer.jitterSubsystem;
        this.intakeSubsystem = robotContainer.intakeSubsystem;
        this.repeatJidderCommand = robotContainer.repeatJidderCommand;
    }

     public void driverConfigureBindings(){
        //Swerve break and align
        driverController.a().whileTrue(drivetrain.applyRequest(() -> brake));
        driverController.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-driverController.getLeftY(), -driverController.getLeftX()))
        ));
        // Reset the field-centric heading on left bumper press.
        driverController.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        //Regular Shooting
        driverController.axisGreaterThan(3, 0.1).whileTrue(robotContainer.indexAndSpindexCommand);

        //Jitter the Intake
        driverController.rightBumper().whileTrue(repeatJidderCommand).onFalse(
                Commands.runOnce(
                    ()->robotContainer.intakeCommand.end(true)
                )
            );
        
        //Intake
        driverController.x().onTrue(Commands.deadline(Commands.waitSeconds(0.2), robotContainer.intakeCommand));

        //Stops Intake Motor
        driverController.y().onTrue(intakeSubsystem.stopIntakeMotorCommand());

        //Bring Intake Up Slowly
        driverController.start().whileTrue(intakeSubsystem.intakeUpCommand(false, 0)).onFalse(
                    Commands.runOnce(
                    ()->robotContainer.intakeCommand.end(true)
                )
            );
    }

    public void operatorConfigureBindings(){

        //------------
        //Main Controls
        //------------
        //Spin up flywheels
        //operatorController.a().toggleOnTrue(hoodCommand.withDeadline(flywheelCommand));
        operatorController.a().toggleOnTrue(robotContainer.flywheelCommand);
        
        //Operator Shoot
        operatorController.axisGreaterThan(3, 0.1).whileTrue(robotContainer.indexAndSpindexCommand);

        //Turret scan
        operatorController.axisGreaterThan(2, 0.1).toggleOnTrue(robotContainer.turretScanYaw);

        //perfect shot from the aligned spot
        operatorController.x().toggleOnTrue(robotContainer.alignedShotCommand);

        //Jitter Robot
        operatorController.y().whileTrue(
            Commands.repeatingSequence(robotContainer.jitterSubsystem.jitterRobotForward(), 
            (robotContainer.jitterSubsystem.jitterRobotBackward())
            )
        );

        //---------------
        //Manual Overrides
        //---------------

        //Turret
        //Inside of the turret subystems periodic()
        
        //Flywheel
        //Inside of the flywheel subystemcs periodic()

        //Hood
        //operatorController.povUp().whileTrue(manualHoodUp);
        //operatorController.povDown().whileTrue(manualHoodDown);



        //Indexer
        operatorController.b().whileTrue(robotContainer.reverseIndexer);

        //Reverse Intake
        operatorController.povDown().whileTrue(reverseIntake);
    }
}
