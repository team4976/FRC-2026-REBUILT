package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Value;

import java.net.SocketImpl;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pneumatics extends SubsystemBase {
    public static final PneumaticsControlModule pneumaticsControlModule = new PneumaticsControlModule(30);
    private final Compressor compressor = new Compressor(30, PneumaticsModuleType.CTREPCM);
    private  Solenoid solenoid; //= new Solenoid(PneumaticsModuleType.CTREPCM, 4);
            /* 
    private final DoubleSolenoid solenoid2 = new DoubleSolenoid(2,
            PneumaticsModuleType.CTREPCM, 2, 3);
            */
    public Pneumatics() {
        // Enable closed-loop control for compressor
        compressor.enableDigital(); 
        solenoid = pneumaticsControlModule.makeSolenoid(2);
        solenoid.set(false);
    }

    public void forwardSolenoid() {
        //solenoid.toggle(); 
            System.out.println("solenoid on");   
            solenoid.set(true);

        }
    
        public void reverseSolenoid() {
            //solenoidTest.set(false);
                System.out.println("solenoid off");
                solenoid.set(false);
             }

    }

