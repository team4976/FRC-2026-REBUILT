package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.HoodSubsystem;

public class IndexAndSpindexCommand extends Command{
    public IndexAndSpindexSubsystem InSSubsystem;
    public FlywheelSubsystem flywheelSubsystem;
    public HoodSubsystem hoodSubsystem;
    public boolean IndexOverride = false;
    public boolean ForceSpin = false;
    
    public IndexAndSpindexCommand(IndexAndSpindexSubsystem InSSubsystem, boolean ForceSpin, HoodSubsystem hoodSubsystem, FlywheelSubsystem flywheelSubsystem){
        this.flywheelSubsystem = flywheelSubsystem;
        this.hoodSubsystem = hoodSubsystem;
        this.InSSubsystem = InSSubsystem;
        this.ForceSpin = ForceSpin;
        addRequirements(InSSubsystem);
    }

    @Override
    public void initialize(){
        //InSSubsystem.moveFeeder(0.5, 0.5);
    }
    @Override
    public void execute() {
        /*if (ForceSpin == false) {
            if (flywheelSubsystem.getShooterState() == "cantShoot") {
                flywheelSubsystem.setShooterState();
                flywheelSubsystem.spinFlywheel(SmartDashboard.getNumber("Testing/Ben T's Stuff/flywheelSpeed", 40));
                hoodSubsystem.moveHood(SmartDashboard.getNumber("Testing/Ben T's Stuff/hood target position", 4));
            }
            else if (flywheelSubsystem.getShooterState() == "readyToShoot") {
                InSSubsystem.moveFeeder(0.5, 0.5); 
            }
        }
        else {
            System.out.println("Move feeder");
            InSSubsystem.moveFeeder(0.5, 0.5); //Force move
        }*/
        
        InSSubsystem.moveFeeder(0.5, 0.5); //Force move
    }
    @Override
    public void end(boolean interrupted) {
        InSSubsystem.moveFeeder(0.0, 0.0);
    }

    @Override
    public boolean isFinished() {
        /*if (RobotContainer.driverController.axisLessThan(3, 0.3).getAsBoolean() == true && ForceSpin == false
        || RobotContainer.operatorController.b().getAsBoolean() == false && ForceSpin == true) {
            return true;
        }
        else {
            return false;
        }   */
       return false;
    }
}
