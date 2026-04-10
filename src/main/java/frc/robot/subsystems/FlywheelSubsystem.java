package frc.robot.subsystems;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class FlywheelSubsystem extends SubsystemBase{
    public TalonFX fx_leader, fx_follower;
    final VelocityVoltage shooterVelocityVoltage = new VelocityVoltage(0).withSlot(0);
    public double cammeraSpeed = 0.0;
    public BooleanSupplier isAutoFlywheel = () -> cammeraSpeed > 0;

    public FlywheelSubsystem(){
        //the PID of the flywheel
        Slot0Configs flywheelConfig = new Slot0Configs();
        flywheelConfig.kS = 0.1; // Add 0.1 V output to overcome static friction
        flywheelConfig.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
        flywheelConfig.kP = 0.45; // An error of 1 rps results in 0.11 V output
        flywheelConfig.kI = 0.0; // no output for integrated error
        flywheelConfig.kD = 0.0; // no output for error derivative*/
        
        //creates and configures the motor objects
        fx_leader = new TalonFX(Flywheel_Lead_ID);
        fx_leader.getConfigurator().apply(flywheelConfig);

        fx_follower = new TalonFX(Flywheel_Follower_ID);
        fx_follower.getConfigurator().apply(flywheelConfig);
        fx_follower.setControl(new Follower(fx_leader.getDeviceID(), MotorAlignmentValue.Aligned));
    }

    public void teleopInit(){
        cammeraSpeed = 0;
    }

    /**
     * Spins the flywheels at the desired speed
     * @param targetRPS The Rotations Per Second to spin the flywheels at
     */
    public void spinFlywheel(double targetRPS){
        fx_leader.setControl(shooterVelocityVoltage.withVelocity(targetRPS));
    }

    /**
     * A method to get the current speed of the flywheel motor leader
     * @return The flywheel speed in RPS (Rotations Per Second)
     */
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

    public DoubleSupplier shooterSpeed = () -> fx_leader.getVelocity().getValueAsDouble();
}