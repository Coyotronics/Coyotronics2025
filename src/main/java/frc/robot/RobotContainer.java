// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.List;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.Constants.JoystickConstants;
import frc.robot.Constants.PathPlannerConstants;
import frc.robot.subsystems.AlgaeIntake;
import frc.robot.subsystems.CoralIntake;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.Elevator;

public class RobotContainer {
    static boolean field_centric = true;
    private final CoralIntake coral_intake = new CoralIntake();
    private final AlgaeIntake algae_intake = new AlgaeIntake();
    private final DriveSubsystem robot_drive = new DriveSubsystem();
    private final Elevator elevator = new Elevator();

    // Joystick button_board = new Joystick(2);
    XboxController driver_controller = new XboxController(0);
    XboxController mani_controller = new XboxController(1); // Check ports

    public SendableChooser<Command> sendable_chooser = new SendableChooser<>();

    public RobotContainer() {
        if (driver_controller.getLeftTriggerAxis() == 1 && driver_controller.getRightTriggerAxis() == 1) {
            field_centric = !field_centric;
        }

        // Configure default commands
        configureButtonBindings();
    }

    private void configureButtonBindings() {
        // Drive Control
        robot_drive.setDefaultCommand(
                new RunCommand(
                        () -> robot_drive.drive(
                                -MathUtil.applyDeadband(driver_controller.getLeftY() * 0.5,
                                        JoystickConstants.DRIVE_DEADBAND),
                                -MathUtil.applyDeadband(driver_controller.getLeftX() * 0.5,
                                        JoystickConstants.DRIVE_DEADBAND),
                                -MathUtil.applyDeadband(driver_controller.getRightX() * 0.5,
                                        JoystickConstants.DRIVE_DEADBAND),
                                field_centric, true),

                        robot_drive));

        // Coral Intake
        coral_intake.setDefaultCommand(new RunCommand(
                () -> {
                    if (driver_controller.getAButton()) {
                        coral_intake.pivot();
                    } else if (driver_controller.getBButtonReleased()) {
                        coral_intake.intake();
                    }
                }, coral_intake));

        // Algae Intake
        algae_intake.setDefaultCommand(new RunCommand(
                () -> {
                    if (driver_controller.getXButtonReleased()) {
                        algae_intake.intake();
                    }
                }, algae_intake));

        // Elevator Control
        elevator.setDefaultCommand(new RunCommand(
                () -> {

                    if (driver_controller.getRightBumperButton()) {
                        elevator.manual_elevator_rise();
                    } else if (driver_controller.getLeftBumperButton()) {
                        elevator.pid_control(10.8);
                    } else if (driver_controller.getYButton()) {
                        elevator.pid_control(0.5);
                    } else {
                        elevator.stop();
                    }
                }, elevator));
    }

    public Command getAutonomousCommand(String selected) {
        Pose2d selectedPose = new Pose2d();

        // Select the pose based on the selected option from the sendable chooser
        // switch ((String) sendable_chooser.getSelected()) {
        switch (selected) {
            case "Red_Reef_1":
                selectedPose = PathPlannerConstants.pose_Red_Reef1;
                break;
            case "Red_Reef_2":
                selectedPose = PathPlannerConstants.pose_Red_Reef2;
                break;
            case "Red_Reef_3":
                selectedPose = PathPlannerConstants.pose_Red_Reef3;
                break;
            case "Red_Reef_4":
                selectedPose = PathPlannerConstants.pose_Red_Reef4;
                break;
            case "Red_Reef_5":
                selectedPose = PathPlannerConstants.pose_Red_Reef5;
                break;
            case "Red_Reef_6":
                selectedPose = PathPlannerConstants.pose_Red_Reef6;
                break;
            case "Red_Reef_7":
                selectedPose = PathPlannerConstants.pose_Red_Reef7;
                break;
            case "Red_Reef_8":
                selectedPose = PathPlannerConstants.pose_Red_Reef8;
                break;
            case "Blue_Reef_1":
                selectedPose = PathPlannerConstants.pose_Blue_Reef1;
                break;
            case "Blue_Reef_2":
                selectedPose = PathPlannerConstants.pose_Blue_Reef2;
                break;
            case "Blue_Reef_3":
                selectedPose = PathPlannerConstants.pose_Blue_Reef3;
                break;
            case "Blue_Reef_4":
                selectedPose = PathPlannerConstants.pose_Blue_Reef4;
                break;
            case "Blue_Reef_5":
                selectedPose = PathPlannerConstants.pose_Blue_Reef5;
                break;
            case "Blue_Reef_6":
                selectedPose = PathPlannerConstants.pose_Blue_Reef6;
                break;
            case "Blue_Reef_7":
                selectedPose = PathPlannerConstants.pose_Blue_Reef7;
                break;
            case "Blue_Reef_8":
                selectedPose = PathPlannerConstants.pose_Blue_Reef8;
                break;
            case "Processor_Blue":
                selectedPose = PathPlannerConstants.pose_Processor_Blue;
                break;
            case "Processor_Red":
                selectedPose = PathPlannerConstants.pose_Processor_Red;
                break;
            case "Red_Intake":
                selectedPose = PathPlannerConstants.intake_red;
                break;
            case "Blue_Intake":
                selectedPose = PathPlannerConstants.intake_blue;
                break;
            default:
                selectedPose = null;
        }

        if (selectedPose != null) {
            // Use the selectedPose to make the final path that the code shpould follow
            try {

                List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
                        robot_drive.get_pose(),
                        selectedPose);

                PathConstraints constraints = new PathConstraints(3.0, 3.0, 2 * Math.PI, 4 *
                        Math.PI);

                PathPlannerPath path = new PathPlannerPath(
                        waypoints,
                        constraints,
                        null,
                        new GoalEndState(0.0, Rotation2d.fromDegrees(-90)));

                return AutoBuilder.followPath(path);

            } catch (Exception e) {
                DriverStation.reportError("Big oops: " + e.getMessage(), e.getStackTrace());
                return Commands.none();
            }
        }
        return null;
    }

}