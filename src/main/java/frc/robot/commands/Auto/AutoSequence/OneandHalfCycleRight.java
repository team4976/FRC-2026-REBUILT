package frc.robot.commands.Auto.AutoSequence;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.commands.Auto.FlywheelStart;
import frc.robot.commands.Auto.FlywheelStop;
import frc.robot.commands.Intake.IntakeCommand;
import frc.robot.commands.Jitter.JitterIntake;
import frc.robot.commands.Turret.TurretScan;
import frc.robot.commands.Turret.TurretScanYaw;
import frc.robot.RobotContainer;
import frc.robot.commands.Auto.AutoIndexAndSpindexCommand;
import frc.robot.subsystems.Autos;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.UpdateHubInfo;

public class OneandHalfCycleRight extends SequentialCommandGroup {

    public OneandHalfCycleRight(
        RobotContainer robotContainer
    ){
        PhotonVision visionSubsystem = robotContainer.turretCam;
        FlywheelSubsystem flywheelSubsystem = robotContainer.flywheelSubsystem;
        IntakeSubsystem intakeSubsystem = robotContainer.intakeSubsystem;
        IndexAndSpindexSubsystem indxerSubsystem = robotContainer.indexAndSpindexSubsystem;
        TurretSubsystem turretSubsystem = robotContainer.turretSubsystem;
        UpdateHubInfo updateHubInfo = robotContainer.updateHubInfo;

        Command OneCycleRight = AutoBuilder.buildAuto("1 Cycle - Right");
        Command OneandHalfCycleRight = AutoBuilder.buildAuto("1.5 Cycle - Right"); 
        
        addCommands(
            new PrintCommand("One and a Half Cycle Right Started"),
            Commands.deadline(new WaitCommand(0.2), new IntakeCommand(intakeSubsystem)),
            new WaitCommand(0.5),
            OneCycleRight,
            //Commands.deadline(new WaitCommand(1), new TurretScanYaw(visionSubsystem, turretSubsystem)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem, updateHubInfo),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(6),
            //Commands.deadline(Commands.waitSeconds(6), new JitterIntake(intakeSubsystem)),
            //Commands.deadline(new WaitCommand(0.2), new IntakeCommand(intakeSubsystem, false)),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.0, flywheelSubsystem, intakeSubsystem),
            new FlywheelStop(flywheelSubsystem, visionSubsystem),
            OneandHalfCycleRight
        );
    }
}