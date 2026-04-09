package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.RobotContainer;
import frc.robot.commands.Auto.AutoSequence.*;

public class Autos {
    
    public RobotContainer robotContainer;
    public Command OneCycleRight;
    public Command OneCycleLeft;
    public Command OneandHalfCycleRight;
    public Command OneandHalfCycleLeft;
    public Command TwoCycleRight;
    public Command TwoCycleLeft;
    public Command HubtoShoottoDepot;
    public Command HubtoShoot;
    public Command ShoottoNeutral;
    
    public Autos(RobotContainer robotContainer){
        this.robotContainer = robotContainer;
        //loadCommands();
    }
    
    public void loadCommands(){
        OneCycleRight = new OneCycleRight(robotContainer); 
        OneCycleLeft = new OneCycleLeft(robotContainer);
        OneandHalfCycleRight = new OneandHalfCycleRight(robotContainer); 
        OneandHalfCycleLeft = new OneandHalfCycleLeft(robotContainer); 
        TwoCycleRight = new TwoCycleRight(robotContainer); 
        TwoCycleLeft = new TwoCycleLeft(robotContainer); 
        HubtoShoottoDepot = new HubtoDepottoShoot(robotContainer); 
        HubtoShoot = new HubtoShoot(robotContainer);
        ShoottoNeutral = new ShoottoNeutral(robotContainer);
    }
}