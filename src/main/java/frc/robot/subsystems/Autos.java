package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.commands.Auto.AutoSequence.HubtoDepottoShoot;
import frc.robot.commands.Auto.AutoSequence.HubtoShoot;
import frc.robot.commands.Auto.AutoSequence.OneCycleLeft;
import frc.robot.commands.Auto.AutoSequence.OneCycleRight;
import frc.robot.commands.Auto.AutoSequence.OneandHalfCycleRight;
import frc.robot.commands.Auto.AutoSequence.OneandHalfCycleLeft;
import frc.robot.commands.Auto.AutoSequence.TwoCycleRight;
import frc.robot.commands.Auto.AutoSequence.TwoCycleLeft;

public class Autos {
    
    RobotContainer robotContainer;
    public IntakeSubsystem intakeSubsystem;
    public PhotonVision vision;
    public FlywheelSubsystem flywheelSubsystem;
    public IndexAndSpindexSubsystem indexAndSpindexSubsystem;
    public TurretSubsystem turretSubsystem;

    public Autos(RobotContainer robotContainer){
        this.robotContainer = robotContainer;
        this.intakeSubsystem = robotContainer.intakeSubsystem;
        this.vision = robotContainer.vision;
        this.flywheelSubsystem = robotContainer.flywheelSubsystem;
        this.indexAndSpindexSubsystem = robotContainer.indexAndSpindexSubsystem;
        this.turretSubsystem = robotContainer.turretSubsystem;
        loadCommands();
    }

    public Command OneCycleRight;
    public Command OneCycleLeft;
    public Command OneandHalfCycleRight;
    public Command OneandHalfCycleLeft;
    public Command TwoCycleRight;
    public Command TwoCycleLeft;
    public Command HubtoShoottoDepot;
    public Command HubtoShoot;
    public Command jitterCommand1;
    public Command jitterCommand2;

    public void loadCommands(){
        OneCycleRight = new OneCycleRight(this); 
        OneCycleLeft = new OneCycleLeft(this);
        OneandHalfCycleRight = new OneandHalfCycleRight(this); 
        OneandHalfCycleLeft = new OneandHalfCycleLeft(this); 
        TwoCycleRight = new TwoCycleRight(this); 
        TwoCycleLeft = new TwoCycleLeft(this); 
        HubtoShoottoDepot = new HubtoDepottoShoot(this); 
        HubtoShoot = new HubtoShoot(this); 
    }
}