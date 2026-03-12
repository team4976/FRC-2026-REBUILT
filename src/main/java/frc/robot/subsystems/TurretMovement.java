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

public class TurretMovement extends SubsystemBase{
    public TalonFX turretSpin; //Turret Spin is the motor name for the turret
    public static DigitalInput RightSwitch = new DigitalInput(0);
    public static DigitalInput LeftSwitch = new DigitalInput(1);
    public double TurretGearRatio = 41.667; // the number of times the motor has to spin for the turret to go one rotation
    public double RotationsPerDegree = TurretGearRatio/360;
    final PositionVoltage turretPosition = new PositionVoltage(0).withSlot(0);

    public TurretMovement(){
    
        turretSpin = new TalonFX(Constants.Turret_ID); 
         
        turretSpin.setVoltage(0);
        turretSpin.setPosition(0);
        
        var turretSpinConfig = new Slot0Configs();
        
        SmartDashboard.putNumber("turret.kS",0.1); // Add 0.1 V output to overcome static friction
        SmartDashboard.putNumber("turret.kV",0.2); // A velocity target of 1 rps results in 0.12 V output
        SmartDashboard.putNumber("turret.kP",0.9);; // An error of 1 rps results in 0.11 V output
        SmartDashboard.putNumber("turret.kI",0);; // no output for integrated error
        SmartDashboard.putNumber("turret.kD",0);; // no output for error derivative*/
   
        turretSpinConfig.kS = SmartDashboard.getNumber("turret.kS", 0.1); // Add 0.1 V output to overcome static friction
        turretSpinConfig.kV = SmartDashboard.getNumber("turret.kV", 0.12); // A velocity target of 1 rps results in 0.12 V output
        turretSpinConfig.kP = SmartDashboard.getNumber("turret.kP", 0.3); // An error of 1 rps results in 0.11 V output
        turretSpinConfig.kI = SmartDashboard.getNumber("turret.kI", 0); // no output for integrated error
        turretSpinConfig.kD = SmartDashboard.getNumber("turret.kD", 0); // no output for error derivative
        turretSpin.getConfigurator().apply(turretSpinConfig);
    }

    public void turretRotationPID(double targetTurretPos){
        System.out.println("turret target position" + targetTurretPos);
        // check if target position is in turret deadzone
       /*  if(targetTurretPos > Constants.turretLimitLeft && targetTurretPos < Constants.turretLimitLeft + Constants.turretDeadzoneSize ||
         targetTurretPos < Constants.turretLimitRight && targetTurretPos > Constants.turretLimitRight - Constants.turretDeadzoneSize){
            turretSpin.setControl(turretPosition.withPosition(turretSpin.getPosition().getValueAsDouble())); 
        }
        // if targetTurretPos is on the other side of the dead zone subtract 41.667 from the targetPos (causes turret to whiparound left)
        else if(targetTurretPos > Constants.turretLimitLeft + Constants.turretDeadzoneSize){
            turretSpin.setControl(turretPosition.withPosition(targetTurretPos - 41.667));
        }
        // if targetTurretPos is on the other side of the dead zone add 41.667 from the targetPos (causes turret to whiparound right)
        else if(targetTurretPos < Constants.turretLimitRight - Constants.turretDeadzoneSize){
            turretSpin.setControl(turretPosition.withPosition(targetTurretPos + 41.667));
        }
        // go to targetTurretPos
        else{ */            
        turretSpin.setControl(turretPosition.withPosition(targetTurretPos));
       // }
    }

    public double convertAngleRotation(double angle){
        return angle*RotationsPerDegree;
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
    public boolean forceTurretFlip(){
        return Constants.flipButton = true;
    }

    public TalonFX returnMotor(){
        return turretSpin;
    }

    @Override
    public void periodic(){
         var turretSpinConfig = new Slot0Configs();
        turretSpinConfig.kS = SmartDashboard.getNumber("turret.kS", 0.1); // Add 0.1 V output to overcome static friction
        turretSpinConfig.kV = SmartDashboard.getNumber("turret.kV", 0.12); // A velocity target of 1 rps results in 0.12 V output
        turretSpinConfig.kP = SmartDashboard.getNumber("turret.kP", 0.1); // An error of 1 rps results in 0.11 V output
        turretSpinConfig.kI = SmartDashboard.getNumber("turret.kI", 0); // no output for integrated error
        turretSpinConfig.kD = SmartDashboard.getNumber("turret.kD", 0); // no output for error derivative
        SmartDashboard.putNumber("turretEncoderValue", turretSpin.getPosition().getValueAsDouble());

    }

}
