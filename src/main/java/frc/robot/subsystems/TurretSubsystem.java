// get an encoder
// get the values of the encoder and convert them into comparable units, degrees
// set a setpoint ex. 20 degrees
// use the (optional pid) to move from the current position to the setpoint

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import static frc.robot.Constants.*;

public class TurretSubsystem extends SubsystemBase{
    public TalonFX turretMotor; //Turret Spin is the motor name for the turret
    public static DigitalInput rightSwitch = new DigitalInput(0);
    public static DigitalInput leftSwitch = new DigitalInput(1);
    public double TurretGearRatio = 41.667; // the number of times the motor has to spin for the turret to go one rotation
    public double RotationsPerDegree = TurretGearRatio/360;
    final PositionVoltage turretPosition = new PositionVoltage(0).withSlot(0);
    public double rStickAxis;
    public boolean isAutoAiming;
    public boolean stopbutton = false;
    public boolean flipButton = false;

    public TurretSubsystem(){
        turretMotor = new TalonFX(Turret_ID); 
         
        turretMotor.setVoltage(0);
        turretMotor.setPosition(0);
        
        var turretSpinConfig = new Slot0Configs();
        
        turretSpinConfig.kS = 0.1;
        turretSpinConfig.kV = 0.2;
        turretSpinConfig.kP = 0.9;
        turretSpinConfig.kI = 0;
        turretSpinConfig.kD = 0;
        turretMotor.getConfigurator().apply(turretSpinConfig);
    }

    public void teleopInit(){
        turretMotor.setControl(turretPosition.withPosition(0));
        turretMotor.setVoltage(0);
    }

    public void turretRotationPID(double targetTurretPos){
        turretMotor.setControl(turretPosition.withPosition(targetTurretPos));
    }

    public double convertAngleRotation(double angle){
        return angle*RotationsPerDegree;
    }

    //When called it turns the motor to the right
    public void turnRight(double voltage) {
        turretMotor.setVoltage(voltage*-1);
    }

    //When called it turns the motor to the left
    public void turnLeft(double voltage) {
        turretMotor.setVoltage(voltage);
    }

    public void lockedOn(double voltage){
        turretMotor.setVoltage(voltage);
    }

    //When called it stops the motor
    public void stopTurn() {
        turretMotor.setVoltage(0);
    }

    //Returns the value of the right limit switch
    public boolean getRightSwitch(){
        return rightSwitch.get();
    }

    //Returns the value of the left limit switch
    public boolean getLeftSwitch(){
        return leftSwitch.get();
    }

    // gets the value of the turret encoder
    public double getEncoderValue(){
        //SmartDashboard.putNumber("turretEncoderValue", turretMotor.getPosition().getValueAsDouble());
        return turretMotor.getPosition().getValueAsDouble();
    }

    // returns the stopButton to be true
    public boolean getStopCommand(){
        return stopbutton = true;
    }

    public boolean forceTurretFlip(){
        return flipButton = true;
    }

    public TalonFX returnMotor(){
        return turretMotor;
    }

    @Override
    public void periodic(){
        /*
        var turretSpinConfig = new Slot0Configs();

        turretSpinConfig.kS = SmartDashboard.getNumber("turret.kS", 0.1); // Add 0.1 V output to overcome static friction
        turretSpinConfig.kV = SmartDashboard.getNumber("turret.kV", 0.12); // A velocity target of 1 rps results in 0.12 V output
        turretSpinConfig.kP = SmartDashboard.getNumber("turret.kP", 0.1); // An error of 1 rps results in 0.11 V output
        turretSpinConfig.kI = SmartDashboard.getNumber("turret.kI", 0); // no output for integrated error
        turretSpinConfig.kD = SmartDashboard.getNumber("turret.kD", 0); // no output for error derivative
        //SmartDashboard.putNumber("turretEncoderValue", turretMotor.getPosition().getValueAsDouble());
        */


        //Check for Hitting Limit Switch Before the Override
        if (!rightSwitch.get() || getEncoderValue() < turretLimitRight){
            turnLeft(0);
            return;
        } else if (!leftSwitch.get() || getEncoderValue() > Constants.turretLimitLeft) {
            turnLeft(0);
            return;
        }

        //Turret Override
        if (isAutoAiming) {
            return;
        } else if (!isAutoAiming){
            if (operatorController.axisGreaterThan(4, 0.3).getAsBoolean()){
                rStickAxis = operatorController.getRightX();
                turnLeft(rStickAxis * -2);
            } else if (operatorController.axisLessThan(4, -0.3).getAsBoolean()){
                rStickAxis = operatorController.getRightX();
                turnLeft(rStickAxis * -2);
            } else {
                turnLeft(0);
            }
        }
        

    }

}
