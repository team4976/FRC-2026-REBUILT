package frc.robot.subsystems;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.commands.Auto.AutoSequence.HubtoDepottoShoot;
import frc.robot.commands.Auto.AutoSequence.HubtoShoot;
import frc.robot.commands.Auto.AutoSequence.ShoottoNeutral;
import frc.robot.commands.Auto.AutoSequence.OneCycleLeft;
import frc.robot.commands.Auto.AutoSequence.OneCycleRight;
import frc.robot.commands.Auto.AutoSequence.OneandHalfCycleRight;
import frc.robot.commands.Auto.AutoSequence.OneandHalfCycleLeft;
import frc.robot.commands.Auto.AutoSequence.TwoCycleRight;
import frc.robot.commands.Auto.AutoSequence.TwoCycleLeft;

public class Autos {
    
    public RobotContainer robotContainer;

    public Autos(RobotContainer robotContainer){
        this.robotContainer = robotContainer;
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
    public Command ShoottoNeutral;
    //public Command shootToOutpost;
    //public Command jitterCommand1;
    //public Command jitterCommand2;

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