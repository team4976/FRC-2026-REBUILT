package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.Jitter.JitterIntake;
import frc.robot.commands.Jitter.JitterRobotSequence;

import static frc.robot.Constants.*;

public class Bindings {
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
        driverController.x().onTrue(GlobalCommands.instance.intakeCommand);
        
        //driverController.start(new JitterIntake());

        //Stops Intake Motor
        driverController.y().onTrue(GlobalCommands.instance.intakeBarCommand);

        //Bring Intake Up Slowly
        driverController.start().whileTrue(robotContainer.intakeSubsystem.intakeCommand(false, 0, false)).onFalse(
                    Commands.runOnce(
                    ()->GlobalCommands.instance.intakeCommand.end(true)
                )
            );
        
        
    }

    public static void operatorConfigureBindings(RobotContainer robotContainer){

        //------------
        //Main Controls
        //------------
        //Spin up flywheels
        operatorController.a().toggleOnTrue(GlobalCommands.instance.flywheelCommand);
        
        //Operator Shoot
        operatorController.axisGreaterThan(3, 0.1).whileTrue(GlobalCommands.instance.indexAndSpindexCommand);

        //Turret scan
        operatorController.axisGreaterThan(2, 0.1).toggleOnTrue(GlobalCommands.instance.turretScanCommand);
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



        //Indexer
        operatorController.b().whileTrue(GlobalCommands.instance.reverseIndexerCommand);

        //Reverse Intake
        operatorController.povDown().whileTrue(GlobalCommands.instance.reverseIntakeCommand);
    }
}
