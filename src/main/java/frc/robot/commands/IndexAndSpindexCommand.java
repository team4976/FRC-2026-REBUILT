package frc.robot.commands;

//import java.lang.invoke.ClassSpecializer.SpeciesData;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.IndexAndSpindexSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.HoodSubsystem;

public class IndexAndSpindexCommand extends Command{
    public IndexAndSpindexSubsystem InSSubsystem;
    public double speed;
    //public FlywheelSubsystem flywheelSubsystem;
    //public HoodSubsystem hoodSubsystem;
    //public boolean IndexOverride = false;
    
    public IndexAndSpindexCommand(IndexAndSpindexSubsystem InSSubsystem, double speed){ //HoodSubsystem hoodSubsystem, FlywheelSubsystem flywheelSubsystem){
        //this.flywheelSubsystem = flywheelSubsystem;
        //this.hoodSubsystem = hoodSubsystem;
        this.InSSubsystem = InSSubsystem;
        this.speed = speed;
        addRequirements(InSSubsystem);
    }

    @Override
    public void initialize(){

    }
    @Override
    public void execute() {
        /*if (reverseSpin == false) {
            if (flywheelSubsystem.getShooterState() == "cantShoot") {
                flywheelSubsystem.setShooterState();
                flywheelSubsystem.spinFlywheel(SmartDashboard.getNumber("Testing/Ben T's Stuff/flywheelSpeed", 40));
                flywheelSubsystem.setShooterState();
                hoodSubsystem.moveHood(SmartDashboard.getNumber("Testing/Ben T's Stuff/hood target position", 4));
            }
            else if (flywheelSubsystem.getShooterState() == "readyToShoot") {
                InSSubsystem.moveFeeder(0.5, 0.5); 
            }
            InSSubsystem.moveFeeder(0.5, 0.5);
        }
        else {
            InSSubsystem.moveFeeder(-0.5, -0.5); //Force move
        }*/
        InSSubsystem.moveFeeder(speed);
    }
    @Override
    public void end(boolean interrupted) {
        InSSubsystem.moveFeeder(0.0);
    }

    @Override
    public boolean isFinished() {
       return false;
    }
}
