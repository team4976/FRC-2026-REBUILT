package frc.robot;

import frc.robot.commands.Jitter.JitterRobotSequence;
import frc.robot.commands.Intake.IntakeArmsCommand;
import frc.robot.commands.Intake.IntakeBarCommand;
import frc.robot.commands.IndexAndSpindexCommand;
import frc.robot.commands.Turret.TurretScanYaw;
import frc.robot.commands.AlignedShotCommand;
import frc.robot.commands.Turret.TurretScan;
import frc.robot.commands.FlywheelCommand;
import frc.robot.subsystems.UpdateHubInfo;

public class GlobalCommands {
    public static GlobalCommands instance = new GlobalCommands();

    boolean isSetup = false;

    //Command Objects
    public IndexAndSpindexCommand c_indexAndSpindex; 
    public IndexAndSpindexCommand c_reverseIndexer; 
    public IntakeBarCommand c_reverseIntakeBar; 
    public IntakeBarCommand c_intakeBar;
    public IntakeArmsCommand c_intakeArms; 
    public FlywheelCommand c_flywheel; 
    public TurretScanYaw c_turretScanYaw; 
    public TurretScan c_turretScan; 
    public AlignedShotCommand c_alignedShot;
    public JitterRobotSequence jitterRobotSequence = new JitterRobotSequence();

    public void init(RobotContainer robotContainer){
        if(isSetup) return;
        c_turretScan = new TurretScan(robotContainer.s_updateHubInfo, robotContainer.s_turretCam, robotContainer.s_turret , false);
        c_indexAndSpindex = new IndexAndSpindexCommand(robotContainer.s_indexAndSpindex, 1.0, robotContainer.s_flywheel, robotContainer.s_intake);
        c_reverseIndexer = new IndexAndSpindexCommand(robotContainer.s_indexAndSpindex, -0.8, robotContainer.s_flywheel, robotContainer.s_intake);
        c_intakeArms = new IntakeArmsCommand(robotContainer.s_intake);
        c_reverseIntakeBar = new IntakeBarCommand(robotContainer.s_intake, true);
        c_intakeBar = new IntakeBarCommand(robotContainer.s_intake, false);
        c_flywheel = new FlywheelCommand(robotContainer.s_flywheel, robotContainer.s_updateHubInfo);
        c_turretScan = new TurretScan(robotContainer.s_updateHubInfo, robotContainer.s_rightBackCam, robotContainer.s_turret, Constants.drivetrain);
        c_turretScanYaw = new TurretScanYaw(robotContainer.s_rightBackCam, robotContainer.s_turret);
        c_alignedShot = new AlignedShotCommand(robotContainer.s_flywheel);

        isSetup = true;
    }
}
