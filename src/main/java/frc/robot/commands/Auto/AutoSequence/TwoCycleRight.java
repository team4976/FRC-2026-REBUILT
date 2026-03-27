package frc.robot.commands.Auto.AutoSequence;

import java.util.ArrayList;
import java.util.List;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.commands.Auto.FlywheelStart;
import frc.robot.commands.Auto.FlywheelStop;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.TurretScan;
import frc.robot.commands.TurretScanYaw;
import frc.robot.commands.Auto.AutoIndexAndSpindexCommand;

import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.TurretSubsystem;

public class TwoCycleRight extends SequentialCommandGroup {

    PhotonVision visionSubsystem;
    FlywheelSubsystem flywheelSubsystem;
    Intake intakeSubsystem;
    IndexAndSpindexSubsystem indxerSubsystem;
    TurretSubsystem turretMovementSubsystem;

    public TwoCycleRight(
        PhotonVision visionSubsystem,
        FlywheelSubsystem flywheelSubsystem,
        Intake intakeSubsystem,
        IndexAndSpindexSubsystem indxerSubsystem,
        TurretSubsystem turretMovementSubsystem
    ){
        
        Command OneCycleRight = AutoBuilder.buildAuto("1 Cycle - Right");
        Command OneandHalfCycleRight = AutoBuilder.buildAuto("1.5 Cycle - Right"); 
        Command TwoCycleRight = AutoBuilder.buildAuto("2 Cycle - Right");
        
        addCommands(
            new PrintCommand("Two Cycle Right Started"),
            Commands.deadline(new WaitCommand(0.2), new IntakeCommand(intakeSubsystem, false)),
            new WaitCommand(0.5),
            OneCycleRight,
            Commands.deadline(new WaitCommand(1), new TurretScanYaw(visionSubsystem, turretMovementSubsystem)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(4),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.0, flywheelSubsystem, intakeSubsystem),
            new FlywheelStop(flywheelSubsystem, visionSubsystem),
            OneandHalfCycleRight,
            /*new PrintCommand("TEST A"),
            Commands.deadline(new WaitCommand(1),new TurretScan(visionSubsystem, turretMovementSubsystem, true)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(6),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.0, flywheelSubsystem, intakeSubsystem),
            new FlywheelStop(flywheelSubsystem, visionSubsystem), */
            TwoCycleRight,
            Commands.deadline(new WaitCommand(1),new TurretScan(visionSubsystem, turretMovementSubsystem, true)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(4)
        );
    }
}

