package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.constants.LimelightConstants;
import frc.robot.subsystems.DriveSubsystem;

public class AlignAprilTag extends Command {
    private DriveSubsystem drive_subsystem;

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
        tag_distance = LimelightHelpers.getBotPose_TargetSpace("limelight");

        x_error = LimelightConstants.x_goal - tag_distance[0];
        y_error = LimelightConstants.y_goal - tag_distance[1];
        theta_error = tag_angle;

        drive_subsystem.drive(LimelightConstants.x_p * x_error, LimelightConstants.y_p * y_error,
                LimelightConstants.theta_p * theta_error, false, false);

        if (Math.abs(x_error) < LimelightConstants.x_tolerance_meters) {
            x_done = true;
        }
        if (Math.abs(y_error) < LimelightConstants.y_tolerance_meters) {
            y_done = true;
        }
        if (Math.abs(theta_error) < LimelightConstants.theta_tolerance_radians) {
            angle_done = true;
        }
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return x_done && y_done && angle_done;
    }
}
