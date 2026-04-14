package frc.robot.commands.Auto.AutoSequence;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.commands.Auto.FlywheelStart;
import frc.robot.commands.Auto.FlywheelStop;
import frc.robot.commands.ExampleIntake.IntakeExtend;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Example_IntakeSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.UpdateHubInfo;

public class TwoCycleLeft extends SequentialCommandGroup {

    public TwoCycleLeft(
        RobotContainer robotContainer
    ){
        PhotonVision visionSubsystem = robotContainer.s_turretCam;
        FlywheelSubsystem flywheelSubsystem = robotContainer.s_flywheel;
        Example_IntakeSubsystem intakeSubsystem = robotContainer.s_intake;
        IndexAndSpindexSubsystem indxerSubsystem = robotContainer.s_indexAndSpindex;
        TurretSubsystem turretSubsystem = robotContainer.s_turret;
        UpdateHubInfo updateHubInfo = robotContainer.s_updateHubInfo;

        Command OneCycleLeft = AutoBuilder.buildAuto("1 Cycle - Left");
        Command OneandHalfCycleLeft = AutoBuilder.buildAuto("1.5 Cycle - Left"); 
        Command TwoCycleLeft = AutoBuilder.buildAuto("2 Cycle - Left");
        
        addCommands(
            new PrintCommand("Two Cycle Left Started"),
            Commands.deadline(new WaitCommand(0.2), new IntakeExtend(intakeSubsystem)),
            new WaitCommand(0.5),
            OneCycleLeft,
            //Commands.deadline(new WaitCommand(1), new TurretScanYaw(visionSubsystem, turretMovementSubsystem)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem, updateHubInfo),
            //new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(6),
            //Commands.deadline(Commands.waitSeconds(4), new JitterIntake(intakeSubsystem)),
            //Commands.deadline(new WaitCommand(0.2), new IntakeCommand(intakeSubsystem, false)),
            //new AutoIndexAndSpindexCommand(indxerSubsystem, 0.0, flywheelSubsystem, intakeSubsystem),
            new FlywheelStop(flywheelSubsystem),
            OneandHalfCycleLeft,
            TwoCycleLeft,
            //Commands.deadline(new WaitCommand(1),new TurretScan(visionSubsystem, turretMovementSubsystem, true)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem, updateHubInfo),
            //new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(6)
        );
    }
}

