
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
import frc.robot.commands.TurretScan;
import frc.robot.commands.Auto.AutoIndexAndSpindexCommand;
import frc.robot.commands.Auto.FlywheelStart;
import frc.robot.commands.Auto.FlywheelStop;
import frc.robot.commands.Auto.IntakeExtend;
import frc.robot.commands.Auto.IntakeRetract;

import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.UpdateHubInfo;

public class NeutralLeftStartFar extends SequentialCommandGroup {

    UpdateHubInfo updateHubInfo;
    PhotonVision visionSubsystem;
    FlywheelSubsystem flywheelSubsystem;
    Intake intakeSubsystem;
    IndexAndSpindexSubsystem indxerSubsystem;
    TurretSubsystem turretMovementSubsystem;

    public NeutralLeftStartFar(
        UpdateHubInfo updateHubInfo,
       PhotonVision visionSubsystem,
        FlywheelSubsystem flywheelSubsystem,
        Intake intakeSubsystem,
        IndexAndSpindexSubsystem indxerSubsystem,
        TurretSubsystem turretMovementSubsystem
    ){
        
        Command neutralLeftFarPath = AutoBuilder.buildAuto("Neutral Left Start Comp");
        
        addCommands(
            new PrintCommand("Neutral Left Start Far Started"),
            new IntakeExtend(intakeSubsystem),
            new WaitCommand(0.5),
            neutralLeftFarPath,
            Commands.deadline(new WaitCommand(1),new TurretScan(updateHubInfo,visionSubsystem, turretMovementSubsystem, true)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(15)
            //new AutoIndexAndSpindexCommand(indxerSubsystem, 0.0, flywheelSubsystem),
            //new IntakeRetract(intakeSubsystem),
            //new FlywheelStop(flywheelSubsystem, visionSubsystem)
        );
    }

    /*
    Auto Fireing Command
        Trigger shootTrigger = new Trigger(visionSubsystem.AutoShootFlag);
        Command indexTrigger =  new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem);
        shootTrigger.onTrue(indexTrigger);
    */
    
}


/*
package frc.robot.commands.Auto.AutoSequence;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.commands.FlywheelStart;
import frc.robot.commands.FlywheelStop;
import frc.robot.commands.TurretScan;
import frc.robot.commands.Auto.AutoIndexAndSpindexCommand;
import frc.robot.commands.Auto.IntakeExtend;
import frc.robot.commands.Auto.IntakeRetract;

import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.TurretSubsystem;

public class NeutralLeftStartFar extends SequentialCommandGroup {

    PhotonVision visionSubsystem;
    FlywheelSubsystem flywheelSubsystem;
    Intake intakeSubsystem;
    IndexAndSpindexSubsystem indxerSubsystem;
    TurretSubsystem turretMovementSubsystem;

    public NeutralLeftStartFar(
        PhotonVision visionSubsystem,
        FlywheelSubsystem flywheelSubsystem,
        Intake intakeSubsystem,
        IndexAndSpindexSubsystem indxerSubsystem,
        TurretSubsystem turretSubsystem
    ){
        
        Command neutralLeftFarPath = AutoBuilder.buildAuto("Copy of Neutral Left Start Comp");
        addCommands(
            new IntakeExtend(intakeSubsystem),
            new WaitCommand(0.5),
            neutralLeftFarPath,
            Commands.deadline(new WaitCommand(1),new TurretScan(visionSubsystem, turretMovementSubsystem, true)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(15)
            //new AutoIndexAndSpindexCommand(indxerSubsystem, 0.0, flywheelSubsystem),
            //new IntakeRetract(intakeSubsystem),
            //new FlywheelStop(flywheelSubsystem, visionSubsystem)
        );
    }
    }

    /*
    Auto Fireing Command
        Trigger shootTrigger = new Trigger(visionSubsystem.AutoShootFlag);
        Command indexTrigger =  new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem);
        shootTrigger.onTrue(indexTrigger);
    */
    


