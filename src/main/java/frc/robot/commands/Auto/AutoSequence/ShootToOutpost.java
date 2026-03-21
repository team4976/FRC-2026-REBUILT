package frc.robot.commands.Auto.AutoSequence;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.UpdateHubInfo;
import frc.robot.commands.Auto.IntakeExtend;
import frc.robot.commands.TurretScan;
import frc.robot.commands.Auto.AutoIndexAndSpindexCommand;
import frc.robot.commands.Auto.FlywheelStart;
import frc.robot.commands.Auto.FlywheelStop;
import frc.robot.commands.Auto.IntakeRetract;

public class ShootToOutpost extends SequentialCommandGroup {
    
    PhotonVision visionSubsystem;
    FlywheelSubsystem flywheelSubsystem;
    Intake intakeSubsystem;
    IndexAndSpindexSubsystem indxerSubsystem;
    TurretSubsystem turretMovementSubsystem;


    public ShootToOutpost(
        UpdateHubInfo updateHubInfo,
        PhotonVision visionSubsystem,
        FlywheelSubsystem flywheelSubsystem,
        Intake intakeSubsystem,
        IndexAndSpindexSubsystem indxerSubsystem,
        TurretSubsystem turretMovementSubsystem
    ){
        Command ShootToOutpost1pathCommand = AutoBuilder.buildAuto("Shoot to Outpost 1");
        Command ShootToOutpost2pathCommand = AutoBuilder.buildAuto("Shoot to Outpost 2");
        Command ShootToOutpost3pathCommand = AutoBuilder.buildAuto("Shoot to Outpost 3");
        addCommands(
            new PrintCommand("Shoot To Outpost Auto Started"),
            new IntakeExtend(intakeSubsystem),
            new WaitCommand(.5),
            ShootToOutpost1pathCommand,
            Commands.deadline(new WaitCommand(1),new TurretScan(updateHubInfo,visionSubsystem, turretMovementSubsystem, true)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem),
            new WaitCommand(2),
            new FlywheelStop(flywheelSubsystem, visionSubsystem),
            ShootToOutpost2pathCommand,
            new WaitCommand(2),
            ShootToOutpost3pathCommand,
            Commands.deadline(new WaitCommand(1),new TurretScan(updateHubInfo,visionSubsystem, turretMovementSubsystem, true)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(2),
            new IntakeRetract(intakeSubsystem),
            new FlywheelStop(flywheelSubsystem, visionSubsystem)
        );
    }
}
