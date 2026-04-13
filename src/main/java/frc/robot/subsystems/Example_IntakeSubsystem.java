package frc.robot.subsystems;

import static frc.robot.Constants.Intake_Arm_Left_ID;
import static frc.robot.Constants.Intake_Arm_Right_ID;
import static frc.robot.Constants.Intake_ID;
import static frc.robot.Constants.intakeSpeed;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/* 
Robot Container:
    public final Temp_IntakeSubsystem s_intake = new Temp_IntakeSubsystem();
Binding Example:
    driverController.x().onTrue(new IntakeSwap(s_intake));
*/


public class Example_IntakeSubsystem extends SubsystemBase{
    public enum IntakeStates{
        extending,
        retracting,
        intaking,
        ejecting,
        idle
    }

    //Intake Wheel
    SparkMax m_Intake;
    DoubleSupplier currentIntakeSpeed = () -> m_Intake.getAppliedOutput();


    //Intake Arms
    SparkMax m_LeftArm, m_RightArm;
    double armsSeed = 0.45;
    double jitterSpeed = 0.25;
    boolean isJittering = false;

    //TODO: validate Left and right Min
    //Left Arm
    final double encoder_LeftMax = -0.025;
    final double encoder_LeftMin = -0.005; // Home Location
    final DoubleSupplier encoder_LeftArm = () -> m_LeftArm.getEncoder().getPosition();
    public BooleanSupplier armLimit_LeftMax = () -> encoder_LeftArm.getAsDouble() <= encoder_LeftMax;
    public BooleanSupplier armLimit_LeftMin = () -> encoder_LeftArm.getAsDouble() >= encoder_LeftMin;
        final double encoder_LeftArm () {
        return m_LeftArm.getEncoder().getPosition();
    }
    /*
    final double encoder_LeftMax = -5.5;
    final double encoder_LeftMin = -0.15; // Home Location
    public BooleanSupplier armLimit_LeftMax = () -> encoder_LeftArm() <= encoder_LeftMax;
    public BooleanSupplier armLimit_LeftMin = () -> encoder_LeftArm() >= encoder_LeftMin;
     */
    public Trigger trigger_LeftMax = new Trigger(armLimit_LeftMax);
    public Trigger trigger_LeftMin = new Trigger(armLimit_LeftMin);


    //Right Arm
    final double encoder_RightMax = 0.004;
    final double encoder_RightMin = 0.001; // Home Location
    final DoubleSupplier encoder_RightArm = () -> m_RightArm.getEncoder().getPosition();
    public BooleanSupplier armLimit_RightMax = () -> encoder_RightArm.getAsDouble() >= encoder_RightMax;
    public BooleanSupplier armLimit_RightMin = () -> encoder_RightArm.getAsDouble() <= encoder_RightMin;
        final double encoder_RightArm (){
        return m_RightArm.getEncoder().getPosition();
    }
    /* 
    final double encoder_RightMax = -0.65;
    final double encoder_RightMin = -0.15; // Home Location
    public BooleanSupplier armLimit_RightMax = () -> encoder_RightArm() <= encoder_RightMax;
    public BooleanSupplier armLimit_RightMin = () -> encoder_RightArm() >= encoder_RightMin;
    */
    public Trigger trigger_RightMax = new Trigger(armLimit_RightMax);
    public Trigger trigger_RightMin = new Trigger(armLimit_RightMin);

    public BooleanSupplier bothArmsAtMax = () -> armLimit_LeftMax.getAsBoolean() && armLimit_RightMax.getAsBoolean();
    public BooleanSupplier bothArmsAtMin = () -> armLimit_LeftMin.getAsBoolean() && armLimit_RightMin.getAsBoolean();

    //Direction
    IntakeStates state_Arms = IntakeStates.idle;
    IntakeStates state_IntakeMotor = IntakeStates.idle;

    public IntakeStates getArmIntakeState (){ return state_Arms;}
    public IntakeStates getIntakeMotorState (){ return state_IntakeMotor;}

    public Example_IntakeSubsystem(){
        //NOTE: uncomment if using, cant have two Sparks with same ID
        
        m_Intake = new SparkMax(Intake_ID, MotorType.kBrushed);
        m_LeftArm = new SparkMax(Intake_Arm_Left_ID, MotorType.kBrushed);
        m_RightArm = new SparkMax(Intake_Arm_Right_ID, MotorType.kBrushed);
        
    }

    public void teleopInit(){
        isJittering = false;
        state_Arms = IntakeStates.idle;
        stopIntakeMotor();
        stopIntakeArms();
    }

    //Intake Arms
    void setIntakeArmsSpeed(double speed){
        m_LeftArm.set(speed);
        m_RightArm.set(speed);
    }
    public void extend(){
        if(trigger_LeftMax.getAsBoolean()) return;
        state_Arms = IntakeStates.extending;
        double speed = (isJittering)? jitterSpeed : armsSeed;
        if(speed > 0) speed *= -1;
        setIntakeArmsSpeed(speed);
    }
    public void extend(double speed){
        if(trigger_LeftMax.getAsBoolean()) return;
        state_Arms = IntakeStates.extending;
        setIntakeArmsSpeed(speed);
    }

    public void retract(){
        if(trigger_LeftMin.getAsBoolean()) return;
        state_Arms = IntakeStates.retracting;
        double speed = (isJittering)? jitterSpeed : armsSeed;
        if(speed < 0) speed *= -1;
        setIntakeArmsSpeed(speed);
    }
    public void retract(double speed){
        if(trigger_LeftMin.getAsBoolean()) return;
        state_Arms = IntakeStates.retracting;
        setIntakeArmsSpeed(speed);
    }

    public void stopIntakeArms(){
        state_Arms = IntakeStates.idle;
        setIntakeArmsSpeed(0);
    }    
    public void swapState(){
        if(state_Arms == IntakeStates.extending){
            retract();
        }
        else if(state_Arms == IntakeStates.retracting){
            extend();
        }
    }
     public void swapState(double speed){
        if(state_Arms == IntakeStates.extending){
            retract(speed);
        }
        else if(state_Arms == IntakeStates.extending){
            retract(speed);
        }
    }

    //Intake Motor
    public void intake(){
        state_IntakeMotor = IntakeStates.intaking;
        m_Intake.set(intakeSpeed);
    }
    public void intake(double speed){
        state_IntakeMotor = IntakeStates.intaking;
        m_Intake.set(speed);
    }
    public void eject(){
        state_IntakeMotor = IntakeStates.ejecting;
        m_Intake.set(Math.abs(intakeSpeed));
    }
    public void eject(double speed){
        state_IntakeMotor = IntakeStates.ejecting;
        m_Intake.set(speed);
    }
    public void stopIntakeMotor(){
        state_IntakeMotor = IntakeStates.idle;
        m_Intake.set(0);
    }

    
    @Override
    public void periodic(){
        try{
            stateChecking();
            if(state_IntakeMotor == IntakeStates.idle) stopIntakeMotor();
            if(state_Arms == IntakeStates.idle) stopIntakeArms();
            else if(state_Arms != IntakeStates.idle){
                if(armLimit_LeftMax.getAsBoolean() && state_Arms == IntakeStates.ejecting ) m_LeftArm.set(0);
                if(armLimit_LeftMin.getAsBoolean()&& state_Arms == IntakeStates.retracting ) m_LeftArm.set(0);
                if(armLimit_RightMax.getAsBoolean()&& state_Arms == IntakeStates.ejecting ) m_RightArm.set(0);
                if(armLimit_RightMin.getAsBoolean()&& state_Arms == IntakeStates.retracting ) m_RightArm.set(0);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        diagnostics();
    }

    void stateChecking() throws Exception{
        if( state_Arms == IntakeStates.extending &&
            (m_LeftArm.getAppliedOutput() > 0 || m_RightArm.getAppliedOutput() > 0)){
                stopIntakeArms();
                throw new Exception(
                    "Intake State Error: Arm Motors Moving wrong way."+ 
                    "\nState:"+state_Arms.name() +
                    " Output:"+m_LeftArm.getAppliedOutput()
                );
        }
        if(state_Arms == IntakeStates.retracting &&
            (m_LeftArm.getAppliedOutput() < 0 || m_RightArm.getAppliedOutput() < 0)){
                stopIntakeArms();
                throw new Exception(
                    "Intake State Error: Arm Motors Moving wrong way."+ 
                    "\nState:"+state_Arms.name() +
                    " Output:"+m_LeftArm.getAppliedOutput()
                );
        }
    }

    void diagnostics(){
        try{
        SmartDashboard.putString("SubSystems/Intake/Arms State", state_Arms.name());
        SmartDashboard.putString("SubSystems/Intake/Motor State", state_IntakeMotor.name());

        //intake Motor
        String path = "SubSystems/Intake/Intake_Motor";
        SmartDashboard.putNumber(path+"/Applied-Output", m_Intake.getAppliedOutput());
        SmartDashboard.putNumber(path+"/Voltage", m_Intake.getBusVoltage());
        SmartDashboard.putNumber(path+"/Output-Current", m_Intake.getOutputCurrent());
        
        path = "SubSystems/Intake/Left_Arm";
        SmartDashboard.putNumber(path+"/Applied-Output", m_LeftArm.getAppliedOutput());
        SmartDashboard.putNumber(path+"/Voltage", m_LeftArm.getBusVoltage());
        SmartDashboard.putNumber(path+"/Output-Current", m_LeftArm.getOutputCurrent());
        SmartDashboard.putNumber(path+"/Pos", encoder_LeftArm());
        SmartDashboard.putNumber(path+"/Max", encoder_LeftMax);
        SmartDashboard.putNumber(path+"/Min", encoder_LeftMin);
        SmartDashboard.putBoolean(path+"/Max_Hit", armLimit_LeftMax.getAsBoolean());
        SmartDashboard.putBoolean(path+"/Min_Hit", armLimit_LeftMin.getAsBoolean());

        path = "SubSystems/Intake/Right_Arm";
        SmartDashboard.putNumber(path+"/Applied-Output", m_RightArm.getAppliedOutput());
        SmartDashboard.putNumber(path+"/Voltage", m_RightArm.getBusVoltage());
        SmartDashboard.putNumber(path+"/Output-Current", m_RightArm.getOutputCurrent());
        SmartDashboard.putNumber(path+"/Pos", encoder_RightArm());
        SmartDashboard.putNumber(path+"/Max", encoder_RightMax);
        SmartDashboard.putNumber(path+"/Min", encoder_RightMin);
        SmartDashboard.putBoolean(path+"/Max_Hit", armLimit_RightMax.getAsBoolean());
        SmartDashboard.putBoolean(path+"/Min_Hit", armLimit_RightMin.getAsBoolean());
        }
        catch(Exception e){
            SmartDashboard.putString("SubSystems/Intake/Error", e.getMessage());
        }
    }
}
