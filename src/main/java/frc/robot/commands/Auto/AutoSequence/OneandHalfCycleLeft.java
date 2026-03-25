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
import frc.robot.commands.Auto.AutoIndexAndSpindexCommand;

import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.TurretSubsystem;

public class OneandHalfCycleLeft extends SequentialCommandGroup {

    PhotonVision visionSubsystem;
    FlywheelSubsystem flywheelSubsystem;
    Intake intakeSubsystem;
    IndexAndSpindexSubsystem indxerSubsystem;
    TurretSubsystem turretMovementSubsystem;

    public OneandHalfCycleLeft(
        PhotonVision visionSubsystem,
        FlywheelSubsystem flywheelSubsystem,
        Intake intakeSubsystem,
        IndexAndSpindexSubsystem indxerSubsystem,
        TurretSubsystem turretMovementSubsystem
    ){
        
        Command OneCycleLeft = AutoBuilder.buildAuto("1 Cycle - Left");
        Command OneandHalfCycleLeft = AutoBuilder.buildAuto("1.5 Cycle - Left"); 
        
        addCommands(
            new PrintCommand("One and a Half Cycle Left Started"),
            new IntakeCommand(intakeSubsystem, false),
            new WaitCommand(0.5),
            OneCycleLeft,
            Commands.deadline(new WaitCommand(1),new TurretScan(visionSubsystem, turretMovementSubsystem, true)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(6),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.0, flywheelSubsystem, intakeSubsystem),
            new FlywheelStop(flywheelSubsystem, visionSubsystem),
            OneandHalfCycleLeft,
            Commands.deadline(new WaitCommand(1),new TurretScan(visionSubsystem, turretMovementSubsystem, true)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(6)
        );
    }
}