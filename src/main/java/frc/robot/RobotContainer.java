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

    //Vision Objects
    public final PhotonVision s_leftBackCam = new PhotonVision("leftBackCam", logger, drivetrain, leftBackCamTransform3d);
    public final PhotonVision s_rightBackCam = new PhotonVision("rightBackCam", logger, drivetrain, rightBackCamTransform3d);
    public final PhotonVision s_turretCam = new PhotonVision("testingCamera", logger, drivetrain, turretCamTransform);

    //update odometry objects for each camera
    private final UpdateOdometry s_updateOdometryRight = new UpdateOdometry(drivetrain, s_rightBackCam);
    private final UpdateOdometry s_updateOdometryLeft = new UpdateOdometry(drivetrain, s_leftBackCam);
    private final UpdateOdometry s_updateOdometryTurret = new UpdateOdometry(drivetrain, s_turretCam);

    //Hub Object, use to get info on hub distance and angle
    public final UpdateHubInfo s_updateHubInfo = new UpdateHubInfo(drivetrain);

    //Subsystem Objects/Subsystem Initialization
    public final IntakeSubsystem s_intake = new IntakeSubsystem();
    public final TurretSubsystem s_turret = new TurretSubsystem();
    public final FlywheelSubsystem s_flywheel = new FlywheelSubsystem();
    public final IndexAndSpindexSubsystem s_indexAndSpindex = new IndexAndSpindexSubsystem();
    public Autos s_autos;
    public final Example_IntakeSubsystem s_intakeExample = new Example_IntakeSubsystem();

    public Bindings bindings;
    public Command selectedAuto;
    public StringLogEntry logEntry = new StringLogEntry(DataLogManager.getLog(), "positionLog");
    public PowerDistribution PDH = new PowerDistribution(1, ModuleType.kRev);
    private ElasticData elasticData = new ElasticData(this);

    Command repeatJidderCommand = Commands.repeatingSequence(
            Commands.print("RepeatJitter Started"),
            Commands.deadline(Commands.waitSeconds(0.20), s_intake.intakeCommand(true, 0.0,false)),
            Commands.deadline(Commands.waitSeconds(0.20), s_intake.intakeCommand(true, 0.0, false))           
        );


    public RobotContainer() {
        //Warnings Suppression
        PhotonCamera.setVersionCheckEnabled(false);
        DriverStation.silenceJoystickConnectionWarning(true);
        PDH.setSwitchableChannel(true);

        drivetrain.configureAutoBuilder();
        s_autos =  new Autos(this);

        configureBindings();
    }

    public void teleopInit(){
        s_flywheel.teleopInit();
        s_indexAndSpindex.teleopInit();
        s_intake.teleopInit();
        s_turret.teleopInit();
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
                selectedAuto = s_autos.OneCycleRight;
                break;
            case "1 Cycle - Left":
                selectedAuto = s_autos.OneCycleLeft;
                break;
            case "1.5 Cycle - Right":
                selectedAuto = s_autos.OneandHalfCycleRight;
                break;
            case "1.5 Cycle - Left":
                selectedAuto = s_autos.OneandHalfCycleLeft;
                break;
            case "2 Cycle - Right":
                selectedAuto = s_autos.TwoCycleRight;
                break;
            case "2 Cycle - Left":
                selectedAuto = s_autos.TwoCycleLeft;
                break;
            case "Depot to Shoot":
                selectedAuto = s_autos.HubtoShoottoDepot;
                break;
            case "Hub to Shoot":
                selectedAuto = s_autos.HubtoShoot;
                break;
            case "Shoot to Neutral":
                selectedAuto = s_autos.ShoottoNeutral;
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
