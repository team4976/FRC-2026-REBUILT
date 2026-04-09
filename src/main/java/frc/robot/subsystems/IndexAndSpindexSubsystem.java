package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IndexAndSpindexSubsystem extends SubsystemBase{

    public SparkMax indexMotor, spindexMotor;
    private SparkMaxConfig sparkConfig = new SparkMaxConfig();

    public IndexAndSpindexSubsystem(){

        indexMotor = new SparkMax(Constants.Index_ID, MotorType.kBrushless);
        spindexMotor = new SparkMax(Constants.Spindex_ID, MotorType.kBrushless);

        sparkConfig.inverted(true);
        indexMotor.configure(sparkConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    }

    //Sets the motor to 0 for the beginning of teleop
    //could have used stopFeeder but consistency w/other subsystems
    public void teleopInit(){
        stopFeeder();
    }

    /**
     * A method to stop the indexer and spindexer together. Alternitively you could just call moveFeeder with 0.0 as the argument.
     */
    public void stopFeeder(){
        moveFeeder(0);
    }

    /**
     * Spins both the indexer and the spindexer at the desired speed. 
     * @param speed The Speed to set. Value should be between -1.0 and 1.0.
     */
    public void moveFeeder(double speed){
        indexMotor.set(speed);
        spindexMotor.set(speed);
    }

    @Override
    public void periodic() {
    }
}

    //public FlywheelSubsystem flywheelSubsystem;
    //public HoodSubsystem hoodSubsystem;        
    //HoodSubsystem hoodSubsystem, FlywheelSubsystem flywheelSubsystem
    //this.flywheelSubsystem = flywheelSubsystem;
    //this.hoodSubsystem = hoodSubsystem;
        


        //indexMotor.set(0);
        //spindexMotor.set(0);
