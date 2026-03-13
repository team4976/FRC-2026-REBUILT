package frc.robot.subsystems;

import static frc.robot.Constants.MaxSpeed;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.FlywheelStart;
import frc.robot.commands.FlywheelStop;
import frc.robot.commands.TurretScan;
import frc.robot.commands.Auto.AutoIndexAndSpindexCommand;
import frc.robot.commands.Auto.IntakeExtend;
import frc.robot.commands.Auto.IntakeRetract;

public class Autos {

    Intake intakeSubsystem;
    PhotonVision vision;
    FlywheelSubsystem flywheelSubsystem;
    IndexAndSpindexSubsystem indexAndSpindexSubsystem;
    TurretMovement turretMovement;

    public Autos(Intake intakeSubsystem, PhotonVision vision, FlywheelSubsystem flywheelSubsystem,IndexAndSpindexSubsystem indexAndSpindexSubsystem,TurretMovement turretMovement){
        this.intakeSubsystem = intakeSubsystem;
        this.vision = vision;
        this.flywheelSubsystem = flywheelSubsystem;
        this.indexAndSpindexSubsystem = indexAndSpindexSubsystem;
        this.turretMovement = turretMovement;
        loadCommands();
    }

    IntakeExtend intakeExtend;
    IntakeRetract intakeRetract;
    FlywheelStart flywheelStart;
    FlywheelStop flywheelStop;
    AutoIndexAndSpindexCommand index;


     Command NeutralRightFarpathCommand;
     //Command NeutralLeftFarpathCommand;
     //Command NeutralRightClosepathCommand;
     //Command NeutralLeftClosepathCommand;
     //Command ShoottoDepot1pathCommand;
     //Command ShoottoDepot2pathCommand;
     //Command ShoottoDepot3pathCommand;
     Command ShoottoOutpost1pathCommand;
     Command ShoottoOutpost2pathCommand;
     Command ShoottoOutpost3pathCommand;

      Trigger trigger;

    void loadCommands (){
        intakeExtend = new IntakeExtend(intakeSubsystem);
        intakeRetract = new IntakeRetract(intakeSubsystem);
        flywheelStart = new FlywheelStart(flywheelSubsystem,vision);
        flywheelStop = new FlywheelStop(flywheelSubsystem,vision);
        index = new AutoIndexAndSpindexCommand(indexAndSpindexSubsystem, MaxSpeed, flywheelSubsystem);
        
        NeutralRightFarpathCommand = AutoBuilder.buildAuto("Neutral Right Start Far");
        //NeutralLeftFarpathCommand = AutoBuilder.buildAuto("Neutral Left Start Far");
        //NeutralRightClosepathCommand = AutoBuilder.buildAuto("Neutral Right Start Close");
        //NeutralLeftClosepathCommand = AutoBuilder.buildAuto("Neutral Left Start Close");
        //ShoottoDepot1pathCommand = AutoBuilder.buildAuto("Shoot to Depot 1");
        //ShoottoDepot2pathCommand = AutoBuilder.buildAuto("Shoot to Depot 2");
        //ShoottoDepot3pathCommand = AutoBuilder.buildAuto("Shoot to Depot 3");
        ShoottoOutpost1pathCommand = AutoBuilder.buildAuto("Shoot to Outpost 1");
        ShoottoOutpost2pathCommand = AutoBuilder.buildAuto("Shoot to Outpost 2");
        ShoottoOutpost3pathCommand = AutoBuilder.buildAuto("Shoot to Outpost 3");
        
        trigger = new Trigger(vision.AutoShootFlag);

        buildAutos();
    }
        
    public Command NeutralRightStartFar;
    public Command NeutralRightStartClose;
    public Command NeutralLeftStartFar;
    public Command NeutralLeftStartClose;
    public Command ShoottoDepot;
    public Command ShoottoOutpost;
    void buildAutos(){
        Trigger trigger = new Trigger(vision.AutoShootFlag);
        trigger = trigger.onTrue(index);
        TurretScan turretScan = new TurretScan(vision, turretMovement);
        //trigger.onTrue(indexAndSpindexCommand);
        NeutralRightStartFar = intakeExtend
            .andThen(new WaitCommand(.5))
            .andThen(NeutralRightFarpathCommand)
            .andThen(turretScan)
            .andThen(flywheelStart)
            .andThen(index)
            .andThen(new WaitCommand(2))
            .andThen(intakeRetract)
            .andThen(flywheelStop);//(Command) elastic.fieldWidget.commandChooser.getSelected();
        
       
       //NeutralLeftStartFar =  intakeExtend.andThen(new WaitCommand(.5)).andThen(NeutralLeftFarpathCommand).andThen(turretScan).andThen(flywheelStart).andThen(index).andThen(new WaitCommand(2)).andThen(intakeRetract).andThen(flywheelStop);//(Command) elastic.fieldWidget.commandChooser.getSelected();
        //NeutralRightStartClose =  intakeExtend.andThen(new WaitCommand(.5)).andThen(NeutralRightClosepathCommand).andThen(turretScan).andThen(flywheelStart).andThen(index).andThen(new WaitCommand(2)).andThen(intakeRetract).andThen(flywheelStop);//(Command) elastic.fieldWidget.commandChooser.getSelected();
        //NeutralLeftStartClose = intakeExtend.andThen(new WaitCommand(.5)).andThen(NeutralRightClosepathCommand).andThen(turretScan).andThen(flywheelStart).andThen(index).andThen(new WaitCommand(4)).andThen(intakeRetract).andThen(flywheelStop);//(Command) elastic.fieldWidget.commandChooser.getSelected();
        //ShoottoDepot = intakeExtend.andThen(new WaitCommand(.5).andThen(ShoottoDepot1pathCommand).andThen(turretScan).andThen(flywheelStart).andThen(index).andThen(new WaitCommand(2)).andThen(flywheelStop).andThen(ShoottoDepot2pathCommand).andThen(ShoottoDepot3pathCommand).andThen().andThen(turretScan).andThen(flywheelStart).andThen(index).andThen(new WaitCommand(2)).andThen(intakeRetract).andThen(flywheelStop));
        
        TurretScan turretScan2 = new TurretScan(vision, turretMovement);
        TurretScan turretScan3 = new TurretScan(vision, turretMovement);
        FlywheelStart flywheelStart1 = new FlywheelStart(flywheelSubsystem, vision);
        FlywheelStart flywheelStart2 = new FlywheelStart(flywheelSubsystem, vision);
        FlywheelStop flywheelStop2 = new FlywheelStop(flywheelSubsystem,vision);
        ShoottoOutpost = intakeExtend
            .andThen(new WaitCommand(.5))
            .andThen(ShoottoOutpost1pathCommand)
            .andThen(turretScan2)
            .andThen(flywheelStart1)
            .andThen(new WaitCommand(2))
            .andThen(flywheelStop)
            .andThen(ShoottoOutpost2pathCommand)
            .andThen(new WaitCommand(2))
            .andThen(ShoottoOutpost3pathCommand)
            .andThen(turretScan3)
            .andThen(flywheelStart2)
            .andThen(index)
            .andThen(new WaitCommand(2))
            .andThen(intakeRetract)
            .andThen(flywheelStop2);
    }
    
}
