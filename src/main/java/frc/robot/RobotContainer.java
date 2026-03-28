// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
//test
package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.Autos;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ElasticData;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.JitterSubsystem;
import frc.robot.subsystems.PhotonVision;
import frc.robot.commands.HoodCommand;
import frc.robot.commands.IndexAndSpindexCommand;
import frc.robot.commands.AlignedShotCommand;
import frc.robot.commands.FlywheelCommand;
import frc.robot.commands.TurretScan;
import frc.robot.commands.TurretScanYaw;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import static frc.robot.Constants.*;

import java.util.List;

import org.photonvision.PhotonCamera;



public class RobotContainer {
    //Logging
    private final Telemetry logger = new Telemetry(Constants.MaxSpeed);

    //Vision Objects, may be good idea to merge into one class and just have dif objects
    private final PhotonVision vision = new PhotonVision("testingCamera", logger);
    private final PhotonVision m_turretvision = new PhotonVision("testingCamera", logger);

    //Subsystem Objects/Subsystem Initialization
    public final Intake intakeSubsystem = new Intake();
    public final ClimberSubsystem climber = new ClimberSubsystem();
    public final TurretSubsystem turretMovement = new TurretSubsystem();
    public final FlywheelSubsystem flywheelSubsystem = new FlywheelSubsystem();
    public final HoodSubsystem hoodSubsystem = new HoodSubsystem();
    public final IndexAndSpindexSubsystem indexAndSpindexSubsystem = new IndexAndSpindexSubsystem(hoodSubsystem, flywheelSubsystem);
    public final List<Subsystem> allSubsystemsList = List.of(
        intakeSubsystem,
        climber,
        turretMovement,
        flywheelSubsystem,
        hoodSubsystem,
        indexAndSpindexSubsystem
    );
    
    public Autos autos;
    public JitterSubsystem jitterSubsystem = new JitterSubsystem();

    //Command Objects
    public IndexAndSpindexCommand indexAndSpindexCommand = new IndexAndSpindexCommand(indexAndSpindexSubsystem, 1.0, flywheelSubsystem, intakeSubsystem);//hoodSubsystem, flywheelSubsystem);
    public IndexAndSpindexCommand reverseIndexer = new IndexAndSpindexCommand(indexAndSpindexSubsystem, -0.8, flywheelSubsystem, intakeSubsystem);//hoodSubsystem, flywheelSubsystem);
    public IntakeCommand intakeCommand = new IntakeCommand(intakeSubsystem, false);
    public IntakeCommand reverseIntake = new IntakeCommand(intakeSubsystem, true);
    public FlywheelCommand flywheelCommand = new FlywheelCommand(flywheelSubsystem, m_turretvision, false);
    public FlywheelCommand flywheelOverrideCommand = new FlywheelCommand(flywheelSubsystem, m_turretvision, true);
    public HoodCommand hoodCommand = new HoodCommand(hoodSubsystem, m_turretvision, false, 0);
    public HoodCommand manualHoodUp = new HoodCommand(hoodSubsystem, m_turretvision, true, 0.5);
    public HoodCommand manualHoodDown = new HoodCommand(hoodSubsystem, m_turretvision, true, -0.5);
    public final TurretScan turretScan = new TurretScan(m_turretvision, turretMovement);
    public final TurretScanYaw turretScanYaw = new TurretScanYaw(m_turretvision, turretMovement);
    //public Command hoodAndFlywheel = new ParallelDeadlineGroup(flywheelCommand, hoodCommand);
    public AlignedShotCommand alignedShotCommand = new AlignedShotCommand(flywheelSubsystem, hoodSubsystem);
    //public ReverseIntake reverseIntake = new ReverseIntake(intakeSubsystem);

    //elastic/smartdashboard intialization 
    private ElasticData elasticData = new ElasticData(logger, vision, m_turretvision, allSubsystemsList);

    public Bindings bindings;

    public StringLogEntry logEntry = new StringLogEntry(DataLogManager.getLog(), "positionLog");

    PowerDistribution pdp = new PowerDistribution(1, ModuleType.kRev);

    public Command selectedAuto;

    public RobotContainer() {
        drivetrain.configureAutoBuilder();

        autos =  new Autos(intakeSubsystem,vision,flywheelSubsystem,indexAndSpindexSubsystem,turretMovement);

        bindings = new Bindings(indexAndSpindexCommand, reverseIndexer, intakeCommand, flywheelCommand, hoodCommand, manualHoodUp, manualHoodDown, turretScanYaw, 
        turretScan, alignedShotCommand, autos, reverseIntake, jitterSubsystem, intakeSubsystem);

        configureBindings();

        pdp.setSwitchableChannel(true);

        //Warnings Suppression
        PhotonCamera.setVersionCheckEnabled(false);
        DriverStation.silenceJoystickConnectionWarning(true);


    }

    public void teleopInit(){
        flywheelSubsystem.teleopInit();
        hoodSubsystem.teleopInit();
        indexAndSpindexSubsystem.teleopInit();
        intakeSubsystem.teleopInit();
        turretMovement.teleopInit();
    }

    public void getOdometryPose(){
        Pose2d currentRobotPose = drivetrain.getState().Pose;
        String currentRobotPoseString = currentRobotPose.toString();
        logEntry.append(currentRobotPoseString);
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

        bindings.driverConfigureBindings();
        bindings.operatorConfigureBindings();
        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public void autoInit(){
        teleopInit();

        elasticData.autonomousInit();
        String value = elasticData.autoChooser.getSelected()[elasticData.autoChooser.getSelected().length -1];
        switch(value){
            case "1 Cycle - Right":
                selectedAuto = autos.OneCycleRight;
                break;
            case "1 Cycle - Left":
                selectedAuto = autos.OneCycleLeft;
                break;
            case "1.5 Cycle - Right":
                selectedAuto = autos.OneandHalfCycleRight;
                break;
            case "1.5 Cycle - Left":
                selectedAuto = autos.OneandHalfCycleLeft;
                break;
            case "2 Cycle - Right":
                selectedAuto = autos.TwoCycleRight;
                break;
            case "2 Cycle - Left":
                selectedAuto = autos.TwoCycleLeft;
                break;
            case "Hub to Shoot":
                selectedAuto = autos.HubtoShoottoDepot;
                break;
            case "No Auto":
                selectedAuto = Commands.waitSeconds(1);
                break;
            default: 
                selectedAuto = Commands.waitSeconds(1);
                break;
            
        }
    }

    public Command getAutonomousCommand() { 
    
        return selectedAuto;
    }
}
