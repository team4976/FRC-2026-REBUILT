package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import static frc.robot.Constants.*;

public class Bindings {
    public RobotContainer robotContainer;

    public Bindings(RobotContainer robotContainer){
        this.robotContainer = robotContainer;
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
        
        Command repeatJidderCommand = 
            Commands.repeatingSequence(
                Commands.deadline(Commands.waitSeconds(0.1), robotContainer.intakeSubsystem.intakeUpCommand()),
                Commands.deadline(Commands.waitSeconds(0.1), robotContainer.intakeSubsystem.intakeDownCommand())           
            );

        driverController.rightBumper().whileTrue(repeatJidderCommand).onFalse(
                Commands.runOnce(
                    ()->robotContainer.intakeCommand.end(true)
                )
            );
        
        //Intake
        driverController.x().onTrue(Commands.deadline(Commands.waitSeconds(0.2), robotContainer.intakeCommand));

        driverController.y().onTrue(intakeSubsystem.stopIntakeMotorCommand());
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
