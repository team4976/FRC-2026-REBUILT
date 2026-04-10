package frc.robot;

import frc.robot.commands.AlignedShotCommand;
import frc.robot.commands.FlywheelCommand;
import frc.robot.commands.IndexAndSpindexCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.JitterRobotSequence;
import frc.robot.commands.Turret.TurretScan;
import frc.robot.commands.Turret.TurretScanYaw;
import frc.robot.subsystems.UpdateHubInfo;

public class GlobalCommands {
        //Hub Object, use to get info on hub distance and angle
    
    public static GlobalCommands instance = new GlobalCommands();
    boolean isSetup = false;
    //Command Objects
    public UpdateHubInfo updateHubInfo;
    public IndexAndSpindexCommand indexAndSpindexCommand; 
    public IndexAndSpindexCommand reverseIndexerCommand; 
    public IntakeCommand intakeCommand; 
    public IntakeCommand reverseIntakeCommand; 
    public FlywheelCommand flywheelCommand; 
    public TurretScan turretScanCommand; 
    public TurretScanYaw turretScanYawCommand; 
    public AlignedShotCommand alignedShotCommand;
    public JitterRobotSequence jitterRobotSequence= new JitterRobotSequence();

    public void init(RobotContainer robotContainer){
        if(isSetup) return;
        updateHubInfo = new UpdateHubInfo(Constants.drivetrain);
        turretScanCommand = new TurretScan(updateHubInfo, robotContainer.turretCam, robotContainer.turretSubsystem , false);
        indexAndSpindexCommand = new IndexAndSpindexCommand(robotContainer.indexAndSpindexSubsystem, 1.0, robotContainer.flywheelSubsystem, robotContainer.intakeSubsystem);
        reverseIndexerCommand = new IndexAndSpindexCommand(robotContainer.indexAndSpindexSubsystem, -0.8, robotContainer.flywheelSubsystem, robotContainer.intakeSubsystem);
        intakeCommand = new IntakeCommand(robotContainer.intakeSubsystem, false);
        reverseIntakeCommand = new IntakeCommand(robotContainer.intakeSubsystem, true);
        flywheelCommand = new FlywheelCommand(robotContainer.flywheelSubsystem, updateHubInfo);
        turretScanCommand = new TurretScan(updateHubInfo, robotContainer.rightBackCam, robotContainer.turretSubsystem, Constants.drivetrain);
        turretScanYawCommand = new TurretScanYaw(robotContainer.rightBackCam, robotContainer.turretSubsystem);
        alignedShotCommand= new AlignedShotCommand(robotContainer.flywheelSubsystem);

        isSetup = true;
    }
    
}
