package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.commands.AlignedShotCommand;
import frc.robot.commands.FlywheelCommand;
import frc.robot.commands.HoodCommand;
import frc.robot.commands.IndexAndSpindexCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.ReverseIntake;
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
    public AlignedShotCommand alignedShotCommand;
    public ReverseIntake reverseIntake;

    public Bindings(IndexAndSpindexCommand indexAndSpindexCommand, IndexAndSpindexCommand reverseIndexer, IntakeCommand intakecommand, 
    FlywheelCommand flywheelCommand, HoodCommand hoodCommand, HoodCommand manualHoodUp, HoodCommand manualHoodDown, TurretScanYaw turretScanYaw, 
    TurretLeft turretLeft, TurretRight turretRight, TurretScan turretScan, AlignedShotCommand alignedShotCommand, ReverseIntake reverseIntake){
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
        this.alignedShotCommand = alignedShotCommand;
        this.reverseIntake = reverseIntake;
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
        operatorController.axisGreaterThan(2, 0.1).toggleOnTrue(turretScanYaw);

        //perfect shot from the aligned spot
        operatorController.x().toggleOnTrue(alignedShotCommand);


        //---------------
        //Manual Overrides
        //---------------

        //Turret
        //Inside of the turret subystems periodic()
        
        //Flywheel
        //Inside of the flywheel subystemcs periodic()

        //Hood
        operatorController.povUp().whileTrue(manualHoodUp);
        operatorController.povDown().whileTrue(manualHoodDown);

        //Indexer
        operatorController.b().whileTrue(reverseIndexer);

        //Reverse Intake
        operatorController.start().whileTrue(reverseIntake);
    }
}
