package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class IndexAndSpindexSubsystem extends SubsystemBase{
    public SparkMax m_index, m_spindex;
    private SparkMaxConfig sparkConfig = new SparkMaxConfig();

    public IndexAndSpindexSubsystem(){

        m_index = new SparkMax(Index_ID, MotorType.kBrushless);
        m_spindex = new SparkMax(Spindex_ID, MotorType.kBrushless);

        sparkConfig.inverted(true);
        m_index.configure(sparkConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    }

    //Sets the motor to 0 for the beginning of teleop
    public void teleopInit(){
        stopFeeder();
    }

    /**
     * A method to stop the indexer and spindexer together. Alternitively you could just call moveFeeder with 0.0 as the argument.
     */
    public void stopFeeder(){
        m_index.set(0);
        m_spindex.set(0);
    }

    /**
     * Spins both the indexer and the spindexer at the desired speed. 
     * @param speed The Speed to set. Value should be between -1.0 and 1.0.
     */
    public void moveFeeder(double speed){
        m_index.set(speed);
        m_spindex.set(speed);
    }

    @Override
    public void periodic() {
    }
}
