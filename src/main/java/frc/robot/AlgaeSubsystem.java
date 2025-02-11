package frc.robot;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.*;
//import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
//import com.revrobotics.spark.SparkClosedLoopController;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AlgaeSubsystem  extends SubsystemBase{
    private final SparkMax motor1;
    private final SparkMax motor2;

    private final SparkMax pivot1;
    private final SparkMax pivot2;
    private final RelativeEncoder encoder;
    private final PIDController pidController;


    //Keep in mind these use speeds. Speeds assume a constant voltage because it's a percentage of voltage.
    //If voltage is lower than expected, the motors may run slower.
    //This is better for longevity but it's a bad thing for consistent performance.
    private final double kIntakeSpeed = 0.25;
    private final double kOutakeSpeed = -0.25;

    private static final double kP = 0.05; // Adjust this for tuning
    private static final double kI = 0.0;  // Usually 0 for simple control
    private static final double kD = 0.005; // Helps prevent overshoot
    private static final double kTolerance = 2.0;  // Deadband to stop jittering, according to delphi (idk man)
   
    public AlgaeSubsystem(int motor1ID, int motor2ID, int pivot1ID, int pivot2ID) {
        motor1 = new SparkMax(motor1ID, MotorType.kBrushless);
        motor2 = new SparkMax(motor2ID, MotorType.kBrushless);

        pivot1 = new SparkMax(pivot1ID, MotorType.kBrushless);
        pivot2 = new SparkMax(pivot2ID, MotorType.kBrushless);

        encoder = pivot1.getEncoder();
        pidController = new PIDController(kP, kI, kD);
        pidController.setTolerance(kTolerance);

        // Create a configuration object
        SparkMaxConfig config1 = new SparkMaxConfig();
        SparkMaxConfig config2 = new SparkMaxConfig();

        // Set the smart current limit
        config1.smartCurrentLimit(40);
        config2.smartCurrentLimit(40);

        // Apply the configuration to the motors with a reset
        motor1.configure(config1, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        config2.inverted(true);
        //Because the other motor is inverted

        motor2.configure(config2, ResetMode.kResetSafeParameters,PersistMode.kPersistParameters);

        //Should I be making different config objects for the pivots? probably. Should it be fine anyways? I thinks so.

        pivot1.configure(config1, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        //Use config2 because inverted
        pivot2.configure(config2, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }

    public void intake(double targetPosition){
       motor1.set(kIntakeSpeed);
       motor2.set(kIntakeSpeed);

       pivot1.set(pidController.calculate(getPosition(),targetPosition));
       pivot2.set(pidController.calculate(getPosition(),targetPosition));
    }

    public void shoot(double targetPosition){
        motor1.set(kOutakeSpeed);
        motor2.set(kOutakeSpeed);

        pivot1.set(pidController.calculate(getPosition(),targetPosition));
        pivot2.set(pidController.calculate(getPosition(),targetPosition));
    }

    public void manualControl(double speed){
        //This doesn't need a PID because it's open loop.

        if ( (speed > 0 && getPosition() < (Math.PI / 2) ) || (speed < 0 && getPosition() > (-Math.PI / 2) )){
            motor1.set(speed);
            motor2.set(speed);
        }
        else{
            motor1.set(0);
            motor2.set(0);
        }


    }

    public double getPosition(){
        return (encoder.getPosition() / 20) * 2 * Math.PI;

        //You need to account for gearboxes
    }

    public void stop() { //killswtich
        motor1.set(0);
        motor2.set(0);

        pivot1.set(0);
        pivot2.set(0);
    }

}
