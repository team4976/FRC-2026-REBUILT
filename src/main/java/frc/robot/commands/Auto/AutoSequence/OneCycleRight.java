package frc.robot.commands.Auto.AutoSequence;


import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.TurretScan;
import frc.robot.commands.Auto.AutoIndexAndSpindexCommand;
import frc.robot.commands.Auto.FlywheelStart;
//import frc.robot.commands.Auto.IntakeExtend;

import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.TurretSubsystem;

public class OneCycleRight extends SequentialCommandGroup {

    public OneCycleRight(
        PhotonVision visionSubsystem,
        FlywheelSubsystem flywheelSubsystem,
        Intake intakeSubsystem,
        IndexAndSpindexSubsystem indxerSubsystem,
        TurretSubsystem turretMovementSubsystem
    ){
        
        Command OneCycleRight = AutoBuilder.buildAuto("1 Cycle - Right");
        
        addCommands(
            new PrintCommand("Neutral Right Start Far Started"),
            Commands.deadline(new WaitCommand(0.2), new IntakeCommand(intakeSubsystem, false)),
            new WaitCommand(0.5),
            OneCycleRight,
            Commands.deadline(new WaitCommand(1),new TurretScan(visionSubsystem, turretMovementSubsystem, true)),
            new FlywheelStart(flywheelSubsystem, visionSubsystem),
            new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(6)
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
