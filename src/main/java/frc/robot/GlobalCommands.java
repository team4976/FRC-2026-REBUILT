package frc.robot;

import frc.robot.commands.AlignedShotCommand;
import frc.robot.commands.FlywheelCommand;
import frc.robot.commands.IndexAndSpindexCommand;
import frc.robot.commands.Intake.IntakeBarCommand;
import frc.robot.commands.Intake.IntakeCommand;
import frc.robot.commands.Jitter.JitterRobotSequence;
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
    public IntakeBarCommand reverseIntakeCommand; 
    public IntakeBarCommand intakeBarCommand;
    public FlywheelCommand flywheelCommand; 
    public TurretScan turretScanCommand; 
    public TurretScanYaw turretScanYawCommand; 
    public AlignedShotCommand alignedShotCommand;
    public JitterRobotSequence jitterRobotSequence= new JitterRobotSequence();

    public void init(RobotContainer robotContainer){ System.out.println("print works GlobalCommand");
        if(isSetup) return;
        //turretScanCommand = new TurretScan(updateHubInfo, robotContainer.turretCam, robotContainer.turretSubsystem , false);
        indexAndSpindexCommand = new IndexAndSpindexCommand(robotContainer.indexAndSpindexSubsystem, 1.0, robotContainer.flywheelSubsystem, robotContainer.intakeSubsystem);
        reverseIndexerCommand = new IndexAndSpindexCommand(robotContainer.indexAndSpindexSubsystem, -0.8, robotContainer.flywheelSubsystem, robotContainer.intakeSubsystem);
        intakeCommand = new IntakeCommand(robotContainer.intakeSubsystem);
        reverseIntakeCommand = new IntakeBarCommand(robotContainer.intakeSubsystem, true);
        intakeBarCommand = new IntakeBarCommand(robotContainer.intakeSubsystem, false);
        flywheelCommand = new FlywheelCommand(robotContainer.flywheelSubsystem, robotContainer.updateHubInfo);
        turretScanCommand = new TurretScan(robotContainer.updateHubInfo, robotContainer.turretSubsystem);
        turretScanYawCommand = new TurretScanYaw(robotContainer.rightBackCam, robotContainer.turretSubsystem);
        alignedShotCommand= new AlignedShotCommand(robotContainer.flywheelSubsystem);

        isSetup = true;
    }
    
}
