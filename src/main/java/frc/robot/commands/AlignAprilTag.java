package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.DriveSubsystem;

public class AlignAprilTag extends Command {
    private DriveSubsystem drive_subsystem;

    private PIDController rotation_pid;

    double x_error;
    double y_error;
    double theta_error;
    double tag_angle;
    double[] tag_distance;

    boolean x_done;
    boolean y_done;
    boolean angle_done;

    public AlignAprilTag(DriveSubsystem drive_subsystem) {
        this.drive_subsystem = drive_subsystem;
        rotation_pid = new PIDController(0.2, 0, 10);
        rotation_pid.setTolerance(0.1);
        rotation_pid.enableContinuousInput(0, 360);
        rotation_pid.setSetpoint(0);

        addRequirements(drive_subsystem);
    }

    @Override
    public void initialize() {
        x_done = false;
        y_done = false;
        angle_done = false;
    }

    @Override
    public void execute() {
        tag_angle = LimelightHelpers.getTX("limelight");
        double rotation = rotation_pid.calculate(tag_angle);

        SmartDashboard.putNumber("Align Rotation", rotation);
        
        drive_subsystem.drive(0, 0, rotation, false, true);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return rotation_pid.atSetpoint();
    }
}
