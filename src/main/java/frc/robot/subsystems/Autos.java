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
    TurretScan turretScan;

    public Autos(Intake intakeSubsystem, PhotonVision vision, FlywheelSubsystem flywheelSubsystem, IndexAndSpindexSubsystem indexAndSpindexSubsystem, TurretScan turretScan){
        this.intakeSubsystem = intakeSubsystem;
        this.vision = vision;
        this.flywheelSubsystem = flywheelSubsystem;
        this.indexAndSpindexSubsystem = indexAndSpindexSubsystem;
        this.turretScan = turretScan;
        loadCommands();
    }

    IntakeExtend intakeExtend;
     IntakeRetract intakeRetract;
     FlywheelStart flywheelStart;
    FlywheelStop flywheelStop;
    AutoIndexAndSpindexCommand index;


     Command pathCommand;
      Trigger trigger;
    void loadCommands (){
        intakeExtend = new IntakeExtend(intakeSubsystem);
        intakeRetract = new IntakeRetract(intakeSubsystem);
        flywheelStart = new FlywheelStart(flywheelSubsystem,vision);
        flywheelStop = new FlywheelStop(flywheelSubsystem,vision);
        index = new AutoIndexAndSpindexCommand(indexAndSpindexSubsystem, MaxSpeed, flywheelSubsystem);
        
        pathCommand = AutoBuilder.buildAuto("Final Auto 1");
        trigger = new Trigger(vision.AutoShootFlag);

        buildAutos();
    }
    
    
        
    public Command auto1;
    void buildAutos(){
        
        //trigger.onTrue(indexAndSpindexCommand);
        auto1 = intakeExtend.andThen(new WaitCommand(.5)).andThen(pathCommand).andThen(turretScan).andThen(flywheelStart).andThen(index).andThen(new WaitCommand(4)).andThen(intakeRetract).andThen(flywheelStop);//(Command) elastic.fieldWidget.commandChooser.getSelected();
         
    }
    
}
