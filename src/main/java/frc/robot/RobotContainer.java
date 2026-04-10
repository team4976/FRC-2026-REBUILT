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
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;


import frc.robot.subsystems.*;
import static frc.robot.Constants.*;
import org.photonvision.PhotonCamera;

public class RobotContainer {
    //Logging
    public final Telemetry logger = new Telemetry(Constants.MaxSpeed);

    //Vision Objects, may be good idea to merge into one class and just have dif objects
    public final PhotonVision leftBackCam = new PhotonVision("leftBackCam", logger, drivetrain, leftBackCamTransform3d);
    public final PhotonVision rightBackCam = new PhotonVision("rightBackCam", logger, drivetrain, rightBackCamTransform3d);
    public final PhotonVision turretCam = new PhotonVision("testingCamera", logger, drivetrain, turretCamTransform);

    private final UpdateOdometry updateOdometryRight = new UpdateOdometry(drivetrain, rightBackCam);
    private final UpdateOdometry updateOdometryLeft = new UpdateOdometry(drivetrain, leftBackCam);
    private final UpdateOdometry updateOdometryTurret = new UpdateOdometry(drivetrain, turretCam);

    //Hub Object, use to get info on hub distance and angle
    public final UpdateHubInfo updateHubInfo = new UpdateHubInfo(drivetrain);

    //Subsystem Objects/Subsystem Initialization
    public final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
    public final TurretSubsystem turretSubsystem = new TurretSubsystem();
    public final HoodSubsystem hoodSubsystem = new HoodSubsystem();
    public final FlywheelSubsystem flywheelSubsystem = new FlywheelSubsystem();
    public final IndexAndSpindexSubsystem indexAndSpindexSubsystem = new IndexAndSpindexSubsystem();
    

    public Bindings bindings;
    public Autos autos;
    public Command selectedAuto;

    //elastic/smartdashboard intialization 

    public StringLogEntry logEntry = new StringLogEntry(DataLogManager.getLog(), "positionLog");
    public PowerDistribution PDH = new PowerDistribution(1, ModuleType.kRev);
    private ElasticData elasticData = new ElasticData(this);


    Command repeatJidderCommand = Commands.repeatingSequence(
            Commands.print("RepeatJitter Started"),
            Commands.deadline(Commands.waitSeconds(0.20), intakeSubsystem.intakeCommand(true, 0.0,false)),
            Commands.deadline(Commands.waitSeconds(0.20), intakeSubsystem.intakeCommand(true, 0.0, false))           
        );


    public RobotContainer() {
        //Warnings Suppression
        PhotonCamera.setVersionCheckEnabled(false);
        DriverStation.silenceJoystickConnectionWarning(true);
        PDH.setSwitchableChannel(true);

        drivetrain.configureAutoBuilder();
        autos =  new Autos(this);

        configureBindings();
    }

    public void teleopInit(){
        flywheelSubsystem.teleopInit();
        hoodSubsystem.teleopInit();
        indexAndSpindexSubsystem.teleopInit();
        intakeSubsystem.teleopInit();
        turretSubsystem.teleopInit();
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


        GlobalCommands.instance.init(this);
        Bindings.driverConfigureBindings(this);
        Bindings.operatorConfigureBindings(this);
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
            case "Depot to Shoot":
                selectedAuto = autos.HubtoShoottoDepot;
                break;
            case "Hub to Shoot":
                selectedAuto = autos.HubtoShoot;
                break;
            case "Shoot to Neutral":
                selectedAuto = autos.ShoottoNeutral;
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

    //public JitterRobot jitterSubsystem = new JitterRobot();

    //Command Objects
    /*
    public IndexAndSpindexCommand indexAndSpindexCommand = new IndexAndSpindexCommand(indexAndSpindexSubsystem, 1.0, flywheelSubsystem, intakeSubsystem);//hoodSubsystem, flywheelSubsystem);
    public IndexAndSpindexCommand reverseIndexerCommand = new IndexAndSpindexCommand(indexAndSpindexSubsystem, -0.8, flywheelSubsystem, intakeSubsystem);//hoodSubsystem, flywheelSubsystem);
    public IntakeCommand intakeCommand = new IntakeCommand(intakeSubsystem, false);
    public IntakeCommand reverseIntakeCommand = new IntakeCommand(intakeSubsystem, true);
    public FlywheelCommand flywheelCommand = new FlywheelCommand(flywheelSubsystem, updateHubInfo);
    public HoodCommand hoodCommand = new HoodCommand(hoodSubsystem, updateHubInfo, false, 0);
    public HoodCommand manualHoodUpCommand = new HoodCommand(hoodSubsystem, updateHubInfo, true, 0.5);
    public HoodCommand manualHoodDownCommand = new HoodCommand(hoodSubsystem, updateHubInfo, true, -0.5);
    public TurretScan turretScanCommand = new TurretScan(updateHubInfo, rightBackCam, turretSubsystem, drivetrain);
    public TurretScanYaw turretScanYawCommand = new TurretScanYaw(rightBackCam, turretSubsystem);
    public AlignedShotCommand alignedShotCommand = new AlignedShotCommand(flywheelSubsystem, hoodSubsystem);
    
*/