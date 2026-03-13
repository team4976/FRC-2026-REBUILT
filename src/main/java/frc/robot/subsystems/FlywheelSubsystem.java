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
    public String shooterState = "cantShoot";
    public double targetRPS;
    final VelocityVoltage shooterVelocityVoltage = new VelocityVoltage(0).withSlot(0);
    public boolean isAutoFlywheel;
    public double rStickAxis;
   
    public FlywheelSubsystem(){
        //the PID of the flywheel
        var flywheelConfig = new Slot0Configs();
        flywheelConfig.kS = 0.1; // Add 0.1 V output to overcome static friction
        flywheelConfig.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
        flywheelConfig.kP = 0.45; // An error of 1 rps results in 0.11 V output
        flywheelConfig.kI = 0.0; // no output for integrated error
        flywheelConfig.kD = 0.0; // no output for error derivative*/
        
        //
        shooterMotorLeader = new TalonFX(Flywheel_Lead_ID);
        shooterMotorFollower = new TalonFX(Flywheel_Follower_ID);
        shooterMotorLeader.getConfigurator().apply(flywheelConfig);
        shooterMotorFollower.getConfigurator().apply(flywheelConfig);
        shooterMotorFollower.setControl(new Follower(shooterMotorLeader.getDeviceID(), MotorAlignmentValue.Aligned));
    }

    public void teleopInit(){
        shooterMotorLeader.set(0.0);
    }

    public void spinFlywheel(double targetRPS){
        //System.out.println("targetRPS: " + targetRPS);
        shooterMotorLeader.setControl(shooterVelocityVoltage.withVelocity(targetRPS));
        this.targetRPS = targetRPS;
    }

    public double getShooterSpeed(){
        return shooterMotorLeader.getVelocity().getValueAsDouble();
    }

    public String getShooterState(){
        return shooterState;
    }

    @Override
    public void periodic(){
        SmartDashboard.putNumber("Turret Rotate", shooterMotorLeader.getVelocity().getValueAsDouble());
        
        if (shooterMotorLeader.getVelocity().getValueAsDouble() < targetRPS + 5 
         && shooterMotorLeader.getVelocity().getValueAsDouble() > targetRPS - 5 && targetRPS > 0) {
            shooterState = "readyToShoot";
        }
        else {
            shooterState = "notReady";
        }
        //Flywheel Override
        if (isAutoFlywheel) {
            return;
        } else if (!isAutoFlywheel){
            if (operatorController.axisGreaterThan(1, 0.3).getAsBoolean()){
                rStickAxis = operatorController.getLeftY();
                spinFlywheel(rStickAxis * 65);
            } else if (operatorController.axisLessThan(1, -0.3).getAsBoolean()){
                rStickAxis = operatorController.getLeftY();
                spinFlywheel(rStickAxis * 65);
            } else {
                spinFlywheel(0);
            }
        }
    }
}
