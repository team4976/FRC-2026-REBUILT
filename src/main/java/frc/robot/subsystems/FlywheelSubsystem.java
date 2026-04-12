package frc.robot.subsystems;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class FlywheelSubsystem extends SubsystemBase{
    public TalonFX m_flywheelLeader, m_flywheelFollower;

    public final VelocityVoltage shooterVelocityVoltage = new VelocityVoltage(0).withSlot(0);
    public double cammeraSpeed = 0.0;

    public BooleanSupplier isAutoFlywheel = () -> cammeraSpeed > 0;
    public DoubleSupplier shooterSpeed = () -> m_flywheelLeader.getVelocity().getValueAsDouble();

    public FlywheelSubsystem(){
        //the PID of the flywheel
        Slot0Configs flywheelConfig = new Slot0Configs();
        flywheelConfig.kS = 0.1; // Add 0.1 V output to overcome static friction
        flywheelConfig.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
        flywheelConfig.kP = 0.45; // An error of 1 rps results in 0.11 V output
        flywheelConfig.kI = 0.0; // no output for integrated error
        flywheelConfig.kD = 0.0; // no output for error derivative*/
        
        //creates and configures the motor objects
        m_flywheelLeader = new TalonFX(Flywheel_Lead_ID);
        m_flywheelLeader.getConfigurator().apply(flywheelConfig);

        m_flywheelFollower = new TalonFX(Flywheel_Follower_ID);
        m_flywheelFollower.getConfigurator().apply(flywheelConfig);
        m_flywheelFollower.setControl(new Follower(m_flywheelLeader.getDeviceID(), MotorAlignmentValue.Aligned));
    }

    public void teleopInit(){
        cammeraSpeed = 0;
    }

    /**
     * Spins the flywheels at the desired speed
     * @param targetRPS The Rotations Per Second to spin the flywheels at
     */
    public void spinFlywheel(double targetRPS){
        m_flywheelLeader.setControl(shooterVelocityVoltage.withVelocity(targetRPS));
    }

    @Override 
    public void periodic(){
        double _targetRPS = 0;
        if(isAutoFlywheel.getAsBoolean()) 
            _targetRPS = cammeraSpeed;
        else if (operatorController.axisMagnitudeGreaterThan(1, 0.3).getAsBoolean()){
            _targetRPS = operatorController.getLeftY() * 65;
        }
        spinFlywheel(_targetRPS);
    }
}