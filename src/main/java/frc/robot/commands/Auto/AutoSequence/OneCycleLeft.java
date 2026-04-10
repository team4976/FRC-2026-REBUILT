package frc.robot.commands.Auto.AutoSequence;


import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.JitterIntake;
import frc.robot.commands.Auto.AutoIndexAndSpindexCommand;
import frc.robot.commands.Auto.FlywheelStart;
import frc.robot.commands.Turret.TurretScan;
import frc.robot.commands.Turret.TurretScanYaw;
import frc.robot.subsystems.Autos;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.PhotonVision;
import frc.robot.subsystems.TurretSubsystem;

public class OneCycleLeft extends SequentialCommandGroup {

    public OneCycleLeft(
        RobotContainer robotContainer
    ){
        PhotonVision visionSubsystem = robotContainer.turretCam;
        FlywheelSubsystem flywheelSubsystem = robotContainer.flywheelSubsystem;
        IntakeSubsystem intakeSubsystem = robotContainer.intakeSubsystem;
        IndexAndSpindexSubsystem indxerSubsystem = robotContainer.indexAndSpindexSubsystem;
        TurretSubsystem turretSubsystem = robotContainer.turretSubsystem;
        
        Command OneCycleLeft = AutoBuilder.buildAuto("1 Cycle - Left");
        
        addCommands(
            new PrintCommand("Neutral Left Start Far Started"),
            //Commands.deadline(new WaitCommand(0.2), new IntakeCommand(intakeSubsystem, false)),
            //new WaitCommand(0.5),
            OneCycleLeft,
            //Commands.deadline(new WaitCommand(1),new TurretScanYaw(visionSubsystem, turretSubsystem)),
            //new FlywheelStart(flywheelSubsystem, visionSubsystem),
            //new AutoIndexAndSpindexCommand(indxerSubsystem, 0.8, flywheelSubsystem, intakeSubsystem),
            new WaitCommand(6)
            //Commands.deadline(Commands.waitSeconds(6), new JitterIntake(intakeSubsystem)),
            //Commands.deadline(new WaitCommand(0.2), new IntakeCommand(intakeSubsystem, false))
        );
    }
}


