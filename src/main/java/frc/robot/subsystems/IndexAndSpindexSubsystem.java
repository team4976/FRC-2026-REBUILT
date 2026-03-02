package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class IndexAndSpindexSubsystem extends SubsystemBase{
    public SparkMax indexMotor;
    public SparkMax spindexMotor;
    public FlywheelSubsystem shooterSubsystem = new FlywheelSubsystem();
    public HoodSubsystem hoodSubsystem;

    public IndexAndSpindexSubsystem(TurretVision turretVision){
        hoodSubsystem = new HoodSubsystem(turretVision);
        indexMotor = new SparkMax(Constants.Index_ID, MotorType.kBrushless);
        spindexMotor = new SparkMax(Constants.Spindex_ID, MotorType.kBrushless);
    }

    public void stopFeeder(){
        indexMotor.set(0);
        spindexMotor.set(0);
    }

    public void moveFeeder(){
        System.out.println("AHHHHHHH");
        indexMotor.set(-0.5);
        spindexMotor.set(0.5);
        System.out.println("BANANA");
    }

    @Override
    public void periodic() {
        if (hoodSubsystem.getHoodState() == "readyToShoot" 
        && shooterSubsystem.getShooterState() == "readyToShoot") {
            RobotContainer.driverController.setRumble(GenericHID.RumbleType.kBothRumble, 100);
        }
        else {
            RobotContainer.driverController.setRumble(GenericHID.RumbleType.kBothRumble, 0);
        }
        SmartDashboard.putNumber("tmp", indexMotor.getOutputCurrent());
    }
}
