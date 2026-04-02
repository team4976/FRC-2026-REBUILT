package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.*;

public class FlywheelSubsystem extends SubsystemBase{
    public TalonFX shooterMotorLeader;
    public TalonFX shooterMotorFollower;
    public double targetRPS;
    final VelocityVoltage shooterVelocityVoltage = new VelocityVoltage(0).withSlot(0);
    public boolean isAutoFlywheel;
    public double lStickAxis;

   
    public FlywheelSubsystem(){
        //the PID of the flywheel
        var flywheelConfig = new Slot0Configs();
        flywheelConfig.kS = 0.1; // Add 0.1 V output to overcome static friction
        flywheelConfig.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
        flywheelConfig.kP = 0.45; // An error of 1 rps results in 0.11 V output
        flywheelConfig.kI = 0.0; // no output for integrated error
        flywheelConfig.kD = 0.0; // no output for error derivative*/
        
        //creates and configures the motor objects
        shooterMotorLeader = new TalonFX(Flywheel_Lead_ID);
        shooterMotorFollower = new TalonFX(Flywheel_Follower_ID);
        shooterMotorLeader.getConfigurator().apply(flywheelConfig);
        shooterMotorFollower.getConfigurator().apply(flywheelConfig);
        shooterMotorFollower.setControl(new Follower(shooterMotorLeader.getDeviceID(), MotorAlignmentValue.Aligned));
    }

    public void teleopInit(){
        shooterMotorLeader.set(0.0);
        isAutoFlywheel = false;
    }

    /**
     * Spins the flywheels at the desired speed
     * @param targetRPS The Rotations Per Second to spin the flywheels at
     */
    public void spinFlywheel(double targetRPS){
        shooterMotorLeader.setControl(shooterVelocityVoltage.withVelocity(targetRPS));
        this.targetRPS = targetRPS;
    }

    /**
     * A method to get the current speed of the flywheel motor leader
     * @return The flywheel speed in RPS (Rotations Per Second)
     */
    public double getShooterSpeed(){
        return shooterMotorLeader.getVelocity().getValueAsDouble();
    }

    @Override
    public void periodic(){
        //Flywheel Override
        if (isAutoFlywheel) {
            return;
        } else if (!isAutoFlywheel){
            if (operatorController.axisGreaterThan(1, 0.3).getAsBoolean()){
                lStickAxis = operatorController.getLeftY();
                spinFlywheel(lStickAxis * 65);
            } else if (operatorController.axisLessThan(1, -0.3).getAsBoolean()){
                lStickAxis = operatorController.getLeftY();
                spinFlywheel(lStickAxis * 65);
            } else {
                spinFlywheel(0);
            }
        }
    }
}
