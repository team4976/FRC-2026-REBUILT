package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class IndexAndSpindexSubsystem extends SubsystemBase{
    public SparkMax indexMotor;
    public SparkMax spindexMotor;
    public FlywheelSubsystem flywheelSubsystem;
    public HoodSubsystem hoodSubsystem;
    private SparkMaxConfig sparkConfig = new SparkMaxConfig();

    public IndexAndSpindexSubsystem(PhotonVision turretVision, HoodSubsystem hoodSubsystem, FlywheelSubsystem flywheelSubsystem){
        this.flywheelSubsystem = flywheelSubsystem;
        this.hoodSubsystem = hoodSubsystem;
        indexMotor = new SparkMax(Constants.Index_ID, MotorType.kBrushless);
        sparkConfig.inverted(true);
        indexMotor.configure(sparkConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
        spindexMotor = new SparkMax(Constants.Spindex_ID, MotorType.kBrushless);
    }

    public void stopFeeder(){
        indexMotor.set(0);
        spindexMotor.set(0);
    }

    public void moveFeeder(double speed){
        indexMotor.set(speed);
        spindexMotor.set(speed);
        //System.out.println("Spinning indexer at:" +  + "spinning spindexer at:" + spindexSpeed);
    }

    @Override
    public void periodic() {
        if (hoodSubsystem.getHoodState() == "readyToShoot" 
        && flywheelSubsystem.getShooterState() == "readyToShoot") {
            RobotContainer.driverController.setRumble(GenericHID.RumbleType.kBothRumble, 100);
        }
        else {
            RobotContainer.driverController.setRumble(GenericHID.RumbleType.kBothRumble, 0);
        }
    }
}
