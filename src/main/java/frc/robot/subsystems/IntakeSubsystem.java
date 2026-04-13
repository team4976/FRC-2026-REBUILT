package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import static frc.robot.Constants.*;

public class IntakeSubsystem extends SubsystemBase {
  public SparkMax m_intake; 
  public SparkMax m_leftArm;
  public SparkMax m_rightArm;

  //The max/min Encoder Positions. As the intake goes down the numbers get larger in magnitude but further into the negatives
  //As such when it is fully down it is at the minimum and when its up its at its maximum.
  public final double minLeftEncoderPos = -5.5;
  public final double minRightEncoderPos = -0.65;
  public final double maxLeftEncoderPos = -2.15;
  public final double maxRightEncoderPos = -2.15;

  public boolean willIntakeDown = false;

  public DoubleSupplier currentIntakeSpeed = () -> m_intake.getAppliedOutput();
  public DoubleSupplier leftArmPose = () -> m_leftArm.getEncoder().getPosition();
  public DoubleSupplier rightArmPose = () -> m_rightArm.getEncoder().getPosition();;
  public BooleanSupplier intakeLimitSwitch = () -> {
    boolean isLeftLimit = leftArmPose.getAsDouble() >= maxLeftEncoderPos || leftArmPose.getAsDouble() <= minLeftEncoderPos;
    boolean isRightLimit = rightArmPose.getAsDouble() >= maxRightEncoderPos || rightArmPose.getAsDouble() <= minRightEncoderPos;
    return isLeftLimit || isRightLimit;
  };
  public BooleanSupplier intakeDown = () -> 
    leftArmPose.getAsDouble() <= minLeftEncoderPos || rightArmPose.getAsDouble() <= minRightEncoderPos;
  public BooleanSupplier intakeUp = () -> 
    leftArmPose.getAsDouble() >= maxLeftEncoderPos || rightArmPose.getAsDouble() >= maxRightEncoderPos;


      
  public IntakeSubsystem() {
    /* 
    m_intake = new SparkMax(Intake_ID, MotorType.kBrushed);
    m_leftArm = new SparkMax(Intake_Arm_Left_ID, MotorType.kBrushless);
    m_rightArm = new SparkMax(Intake_Arm_Right_ID, MotorType.kBrushless);
     */
  }   

  public void teleopInit(){ 
  }

  /**
   * gets and swaps the direction the intake will be moving in. the initial direction is down.
   * @return the direction as a boolean, true is down false is up.
   */
  public boolean getAndSwapDirection(){
    return willIntakeDown = !willIntakeDown;
  }
  
  //Stops motor for intake bar, alternitively you can pass 0 to runIntakeMotor
  public void stopIntakeMotor() {
    m_intake.set(0); 
  }

  //Runs the motor for the intake bar
  public void runIntakeMotor(double speed) {
    m_intake.set(speed);
  }

  //Stops the intake arms motors
  public void stopIntakeArms(){
    m_leftArm.set(0);
    m_rightArm.set(0);
  }

  /**
   * 
   * @param isJitter whether the method is being called for the jitter or manual
   * @param offset the speed offset from the default of 0.45 during jitter and 0.25 in normal. Enter a negative value to lower the speed.
   * @param isDown whether the intake should be going down. pass true if you want the intake to go down and false if not.
  */
  //Jitter speed is different than manual speed, isJitter checks which
  //Pass a negative number for offset to lower the default speed, like in auto
  public void intakeMove(boolean isJitter, double offset, boolean isDown){
    double speed = (isJitter)?0.45 + offset:0.25;
    if(isDown) speed = speed * -1;
    m_leftArm.set(speed);
    m_rightArm.set(speed);
  }

  //-----------------------
  //Intake Command Methods:
  //-----------------------
    /**
   * Constructs a command that moves the intakes arms down
   * @param isJitter whether the method is being called for the jitter or manual
   * @param offset the speed offset from the default of 0.45 during jitter and 0.25 in normal. 
   * Enter a negative value to lower the speed.
   * @return the command
   */
  //Puts the intakes arms down has the same parameters as the method, in the form of a command to provide a runnable
  public Command intakeCommand(boolean isJitter, double offset, boolean isDown){
    return runOnce(()-> 
      intakeMove(isJitter, offset, isDown)
    );
  }
  
  //---------
  //Periodic
  //---------
  @Override
  public void periodic(){
    //NOT YET WORKING, NOTES ON WHAT I CHANGED WHILE IT STILL DIDNT WORK ARE BELOW.
    //We shouldnt need the differential thing where we check whether the intake goes a little past the limit in either direction if we also use current checks.
    //This is because the hitting the limit check should only pass if the current being passed also would move the intake in the direction that it would try to go past the limit.
    /*
      if(m_leftArm.getOutputCurrent() > 0 && m_leftArm.getEncoder().getPosition() <= minLeftEncoderPos){
        m_leftArm.set(0);
        m_rightArm.set(0);
        return;
      } else if (m_leftArm.getOutputCurrent() < 0 && m_leftArm.getEncoder().getPosition() >= maxLeftEncoderPos){
        m_leftArm.set(0);
        m_rightArm.set(0);
        return;
      }
       */
  }
}


