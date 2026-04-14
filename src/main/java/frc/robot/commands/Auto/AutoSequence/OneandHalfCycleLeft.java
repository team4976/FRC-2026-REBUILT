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
import frc.robot.commands.Intake.Intake;
import frc.robot.commands.Intake.IntakeSwap;
//import frc.robot.commands.Jitter.JitterIntake;
import frc.robot.commands.Turret.TurretScan;
import frc.robot.commands.Turret.TurretScanYaw;
import frc.robot.RobotContainer;
import frc.robot.commands.Auto.AutoIndexAndSpindexCommand;
import frc.robot.subsystems.Autos;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;

import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.UpdateHubInfo;

public class OneandHalfCycleLeft extends SequentialCommandGroup {

    public OneandHalfCycleLeft(
        RobotContainer robotContainer
    ){
        PhotonVision visionSubsystem = robotContainer.s_turretCam;
        FlywheelSubsystem flywheelSubsystem = robotContainer.s_flywheel;
        IntakeSubsystem intakeSubsystem = robotContainer.s_intake;
        IndexAndSpindexSubsystem indxerSubsystem = robotContainer.s_indexAndSpindex;
        TurretSubsystem turretSubsystem = robotContainer.s_turret;
        UpdateHubInfo updateHubInfo = robotContainer.s_updateHubInfo;
        
        Command OneCycleLeft = AutoBuilder.buildAuto("1 Cycle - Left");
        Command OneandHalfCycleLeft = AutoBuilder.buildAuto("1.5 Cycle - Left"); 
        
        
        addCommands(
            new PrintCommand("One and a Half Cycle Left Started"),
            Commands.deadline(new WaitCommand(0.2), new IntakeSwap(intakeSubsystem)), new Intake(intakeSubsystem),
            new WaitCommand(0.5),
            OneCycleLeft.alongWith(new TurretScan(null, turretSubsystem)),
            //Commands.deadline(new WaitCommand(1), new TurretScanYaw(visionSubsystem, turretSubsystem)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem, updateHubInfo),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(6),
            //Commands.deadline(Commands.waitSeconds(6), new JitterIntake(intakeSubsystem)),
            //Commands.deadline(new WaitCommand(0.2), new IntakeCommand(intakeSubsystem, false)),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.0, flywheelSubsystem, intakeSubsystem),
            new FlywheelStop(flywheelSubsystem),
            OneandHalfCycleLeft.alongWith(new TurretScan(null, turretSubsystem))
        );
    }
}