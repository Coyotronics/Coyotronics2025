package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

public class AlgaeIntake extends SubsystemBase {
   SparkMax Algae_Master = new SparkMax(36, MotorType.kBrushless);
   SparkMax Algae_Slave = new SparkMax(37, MotorType.kBrushless);
   SparkMaxConfig Algae_Config = new SparkMaxConfig();
   public int state = 0;
    public AlgaeIntake() {
        Algae_Config.idleMode(IdleMode.kCoast);
        Algae_Master.configure(Algae_Config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        Algae_Slave.configure(Algae_Config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void intake(int state) {
        if(state==0)
        {
            Algae_Master.set(0.5);
            Algae_Slave.set(-0.5);
        }
        else if(state==1)
        {
            Algae_Master.set(0);
            Algae_Slave.set(0);
        }
        else
        {
            Algae_Master.set(-0.5);
            Algae_Slave.set(-0.5);
        }
    }

    

   

    @Override
    public void periodic() {
       
    }
}
