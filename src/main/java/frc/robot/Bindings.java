package frc.robot;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.commands.Intake.Intake;
import frc.robot.commands.Intake.IntakeEject;
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
        driverController.axisGreaterThan(3, 0.1).whileTrue(GlobalCommands.instance.c_indexAndSpindex);


        //Intake Mappings

        //Jitter the Intake (DEPRACATED)
        /*
        driverController.rightBumper().whileTrue(robotContainer.repeatJidderCommand).onFalse(
                Commands.runOnce(
                    ()->GlobalCommands.instance.c_intakeArms.end(true)
                )
            );
             */

        //Intake in/out Toggle
        //driverController.x().onTrue(GlobalCommands.instance.c_intakeArms);
        
        driverController.x().onTrue(GlobalCommands.instance.c_intakeSwap);

        driverController.y().onTrue(new Intake(robotContainer.s_intake));
        //TESTING REMOVE BEFORE COMP, jitter intake command used in auto, only mapped for testing rn.
        driverController.start().whileTrue(new JitterIntake(robotContainer.s_intake));

        //Intake Bar Motor Toggle
        //driverController.y().onTrue(GlobalCommands.instance.c_intakeBar);
    }

    public static void operatorConfigureBindings(RobotContainer robotContainer){

        //------------
        //Main Controls
        //------------
        //Spin up flywheels
        operatorController.a().toggleOnTrue(GlobalCommands.instance.c_flywheel);
        
        //Operator Shoot
        operatorController.axisGreaterThan(3, 0.1).whileTrue(GlobalCommands.instance.c_indexAndSpindex);

        //Turret scan
        operatorController.axisGreaterThan(2, 0.1).toggleOnTrue(GlobalCommands.instance.c_turretScan);

        //perfect shot from the aligned spot
        operatorController.x().toggleOnTrue(GlobalCommands.instance.c_alignedShot);

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
        operatorController.b().whileTrue(GlobalCommands.instance.c_reverseIndexer);

        //Reverse Intake
        operatorController.povDown().whileTrue(new IntakeEject(robotContainer.s_intake));
    }
}
