// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.List;

import javax.lang.model.util.ElementScanner14;

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
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Intake;

public class RobotContainer {
    static boolean field_centric = true;
    private final Intake intake = new Intake();
    private final DriveSubsystem robot_drive = new DriveSubsystem();
    private final Elevator elevator = new Elevator();

    XboxController driver_controller = new XboxController(JoystickConstants.DRIVER_CONTROLLER_PORT);
    public SendableChooser sendable_chooser = new SendableChooser();
    XboxController mani_controller = new XboxController(JoystickConstants.DRIVER_CONTROLLER_PORT1); // Check ports

    @SuppressWarnings("unchecked")
    public RobotContainer() {
        sendable_chooser.addOption("Red_Reef_1", "Red_Reef_1");
        sendable_chooser.addOption("Red_Reef_2", "Red_Reef_2");
        sendable_chooser.addOption("Red_Reef_3", "Red_Reef_3");
        sendable_chooser.addOption("Red_Reef_4", "Red_Reef_4");
        sendable_chooser.addOption("Red_Reef_5", "Red_Reef_5");
        sendable_chooser.addOption("Red_Reef_6", "Red_Reef_6");
        sendable_chooser.addOption("Red_Reef_7", "Red_Reef_7");
        sendable_chooser.addOption("Red_Reef_8", "Red_Reef_8");

        sendable_chooser.addOption("Blue_Reef_1", "Blue_Reef_1");
        sendable_chooser.addOption("Blue_Reef_2", "Blue_Reef_2");
        sendable_chooser.addOption("Blue_Reef_3", "Blue_Reef_3");
        sendable_chooser.addOption("Blue_Reef_4", "Blue_Reef_4");
        sendable_chooser.addOption("Blue_Reef_5", "Blue_Reef_5");
        sendable_chooser.addOption("Blue_Reef_6", "Blue_Reef_6");
        sendable_chooser.addOption("Blue_Reef_7", "Blue_Reef_7");
        sendable_chooser.addOption("Blue_Reef_8", "Blue_Reef_8");

        sendable_chooser.addOption("Blue_Intake", "Blue_Intake");
        sendable_chooser.addOption("Red_Intake", "Red_Intake");

        sendable_chooser.addOption("Processor_Blue", "Processor_Blue");
        sendable_chooser.addOption("Processor_Red", "Processor_Red");

        if (driver_controller.getLeftTriggerAxis() == 1 && driver_controller.getRightTriggerAxis() == 1) {
            field_centric = !field_centric;
        }

        configureButtonBindings();

        // Configure default commands
    }

    private void configureButtonBindings() {
        robot_drive.setDefaultCommand(
                new RunCommand(
                        () -> robot_drive.drive(
                                -MathUtil.applyDeadband(driver_controller.getLeftY(),
                                        JoystickConstants.DRIVE_DEADBAND),
                                -MathUtil.applyDeadband(driver_controller.getLeftX(),
                                        JoystickConstants.DRIVE_DEADBAND),
                                -MathUtil.applyDeadband(driver_controller.getRightX(),
                                        JoystickConstants.DRIVE_DEADBAND),
                                field_centric, true),

                        robot_drive));

        intake.setDefaultCommand(new RunCommand(
                () -> {
                    if (driver_controller.getXButton()) {
                        intake.coralIntakeOn();
                    } else if (driver_controller.getYButton()) {
                        intake.coralIntakeOut();
                    }

                }, intake));

        if (driver_controller.getLeftStickButton()) {
            sendable_chooser.setDefaultOption("Blue_Intake", "Blue_Intake");
        } else if (driver_controller.getRightStickButton()) {
            sendable_chooser.setDefaultOption("Processor_Blue", "Processor_Blue");
        }

        elevator.setDefaultCommand(new RunCommand(
                () -> {
                    if (driver_controller.getRightBumperButton()) {
                        elevator.pid_control(68);
                    } else if (driver_controller.getLeftBumperButton()) {
                        elevator.pid_control(40.5);
                    } else {
                        elevator.pid_control(0);
                    }
                }, elevator));
    }

    public Command getAutonomousCommand() {
        Pose2d selectedPose = new Pose2d();
        switch ((String) sendable_chooser.getSelected()) {
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
            try {

                List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
                        robot_drive.get_pose(),
                        selectedPose);

                PathConstraints constraints = new PathConstraints(3.0, 3.0, 2 * Math.PI, 4 *
                        Math.PI); // The constraints
                // // for this path.
                PathPlannerPath path3 = new PathPlannerPath(
                        waypoints,
                        constraints,
                        null, // The ideal starting state, this is only relevant for pre-planned
                        // paths, so can
                        // // be null for on-the-fly paths.
                        new GoalEndState(0.0, Rotation2d.fromDegrees(-90)) // Goal end state. You can
                // set a holonomic
                // // rotation here. If using a differential
                // // drivetrain, the rotation will have no effect.
                );

                // // Create a path following command using AutoBuilder. This will also trigger
                // // event markers.
                return AutoBuilder.followPath(path3);

            } catch (Exception e) {
                DriverStation.reportError("Big oops: " + e.getMessage(), e.getStackTrace());
                return Commands.none();
            }
        }
        return null;
    }

}