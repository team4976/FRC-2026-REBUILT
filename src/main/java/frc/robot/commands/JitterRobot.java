package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import static frc.robot.Constants.*;
public class JitterRobot extends Command {
    boolean isForward = false;
    public JitterRobot(boolean isForward){
        this.isForward = isForward;
    }
   @Override
    public void initialize(){
        drivetrain.applyRequest(()->{
            double speed = 0.5 * MaxSpeed;
            if(!isForward) speed *= -1;
            return robotCentricDrive.withVelocityX(speed);
        });
    }

    @Override
    public boolean isFinished() {
       return true;
    }
}
