package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.commands.FlywheelCommand;
import frc.robot.commands.HoodCommand;
import frc.robot.commands.IndexAndSpindexCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.TurretLeft;
import frc.robot.commands.TurretRight;
import frc.robot.commands.TurretScan;
import frc.robot.commands.TurretScanYaw;

import static frc.robot.Constants.*;

public class Bindings {
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
    public TurretLeft turretLeft;
    public TurretRight turretRight;

    public Bindings(IndexAndSpindexCommand indexAndSpindexCommand, IndexAndSpindexCommand reverseIndexer, IntakeCommand intakecommand, 
    FlywheelCommand flywheelCommand, HoodCommand hoodCommand, HoodCommand manualHoodUp, HoodCommand manualHoodDown, TurretScanYaw turretScanYaw, 
    TurretLeft turretLeft, TurretRight turretRight, TurretScan turretScan){
        this.indexAndSpindexCommand = indexAndSpindexCommand;
        this.reverseIndexer = reverseIndexer;
        this.intakeCommand = intakecommand;
        this.flywheelCommand = flywheelCommand;
        this.hoodCommand = hoodCommand;
        this.manualHoodUp = manualHoodUp;
        this.manualHoodDown = manualHoodDown;
        this.turretScanYaw = turretScanYaw;
        this.turretLeft = turretLeft;
        this.turretRight = turretRight;
        this.turretScan = turretScan;
        System.out.println("Bindings Initialized");
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
        driverController.axisGreaterThan(3, 0.1).whileTrue(indexAndSpindexCommand);

        //Intake
        driverController.x().onTrue(intakeCommand);
        driverController.axisGreaterThan(3, 0.1).whileTrue(indexAndSpindexCommand);
    }

    public void operatorConfigureBindings(){
        //------------
        //Main Controls
        //------------
        //Spin up flywheels
        //operatorController.a().toggleOnTrue(hoodCommand.withDeadline(flywheelCommand));
        operatorController.a().toggleOnTrue(flywheelCommand);

        //Operator Shoot
        operatorController.axisGreaterThan(3, 0.1).whileTrue(indexAndSpindexCommand);

        //Turret scan
        operatorController.axisGreaterThan(2, 0.1).toggleOnTrue(turretScan);

        //---------------
        //Manual Overrides
        //---------------

        //Turret
        operatorController.povLeft().whileTrue(turretLeft);
        operatorController.povRight().whileTrue(turretRight);
        
        //Flywheel
        //operatorController.leftTrigger(0.1).whileTrue(flywheelOverrideCommand);

        //Hood
        operatorController.povUp().whileTrue(manualHoodUp);
        operatorController.povDown().whileTrue(manualHoodDown);

        //Indexer
        operatorController.b().whileTrue(reverseIndexer);
    }
}
