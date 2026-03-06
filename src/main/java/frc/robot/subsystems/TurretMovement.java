// get an encoder
// get the values of the encoder and convert them into comparable units, degrees
// set a setpoint ex. 20 degrees
// use the (optional pid) to move from the current position to the setpoint

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class TurretMovement extends SubsystemBase{
    public TalonFX turretSpin; //Turret Spin is the motor name for the turret
    public static DigitalInput RightSwitch = new DigitalInput(11);
    public static DigitalInput LeftSwitch = new DigitalInput(12);
    public final MotionMagicVoltage turretPosVolt = new MotionMagicVoltage(0).withSlot(1);
    public double TurretGearRatio = 41.667;  //41.667 motor rotations for 1 rotation of turret
    public double RotationsperDegree = TurretGearRatio/360;

    public TurretMovement(){
        turretSpin = new TalonFX(Constants.Turret_ID);  
        turretSpin.setVoltage(0);
        //Magic Motion Setup
         var turretConfig = new TalonFXConfiguration();

        var slot1Configs = turretConfig.Slot1;        
        slot1Configs.kS = 0.1; // Add 0.1 V output to overcome static friction
        slot1Configs.kV = 0.12;
        slot1Configs.kP = 0.1; // An error of 1 rotation results in 0.1 V output
        slot1Configs.kI = 0.0; // no output for integrated error
        slot1Configs.kD = 0.0; // no output for error derivative

        var motionMagicConfigs = turretConfig.MotionMagic;
        motionMagicConfigs.MotionMagicCruiseVelocity =TurretGearRatio/2; // Complete spin of turret takes 2 seconds
        motionMagicConfigs.MotionMagicAcceleration = TurretGearRatio*2; // Hit cruise velocity in 0.25 s
        motionMagicConfigs.MotionMagicJerk = 0; //No jerk control applied
        turretSpin.getConfigurator().apply(turretConfig, 0.050);
    }

    //When called it turns the motor to the right
    public void turnRight(double voltage) {
    turretSpin.setVoltage(voltage*-1);
    //System.out.println("right switch: " + RightSwitch.get());
    }

    //When called it turns the motor to the left
    public void turnLeft(double voltage) {
    turretSpin.setVoltage(voltage);
    //System.out.println("left switch: " + LeftSwitch.get());
    }

    public void lockedOn(double voltage){
        turretSpin.setVoltage(voltage);
    }

    public void ApplyPositionControler(double targetTurretPos){
      turretSpin.setControl(turretPosVolt.withPosition(targetTurretPos));
    }

    public double convertAngletoRotation(double angle){
     return angle*RotationsperDegree;
    }


    //When called it stops the motor
    public void stopTurn() {
        turretSpin.setVoltage(0);
        //System.out.println("STOP MOVING");
    }

    //Returns the value of the right limit switch
    public boolean getRightSwitch(){
        return RightSwitch.get();
    }

    //Returns the value of the left limit switch
    public boolean getLeftSwitch(){
        return LeftSwitch.get();
    }

    // gets the value of the turret encoder
    public double getEncoderValue(){
        SmartDashboard.putNumber("turretEncoderValue", turretSpin.getPosition().getValueAsDouble());
        return turretSpin.getPosition().getValueAsDouble();
    }

    // returns the stopButton to be true
    public boolean getStopCommand(){
        return Constants.stopbutton = true;
    }

    public TalonFX returnMotor(){
        return turretSpin;
    }

    @Override
    public void periodic(){
        SmartDashboard.putNumber("turretEncoderValue", turretSpin.getPosition().getValueAsDouble());
    }

}
