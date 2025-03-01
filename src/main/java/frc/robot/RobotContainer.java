// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.Constants.JoystickConstants;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.Elevator;

public class RobotContainer {
    static boolean field_centric = true;

    private final DriveSubsystem robot_drive = new DriveSubsystem();
    private final Elevator elevator = new Elevator();

    private final XboxController driver_controller = new XboxController(JoystickConstants.DRIVER_CONTROLLER_PORT);
    SendableChooser sendable_chooser = new SendableChooser();
    XboxController mani_controller = new XboxController(JoystickConstants.DRIVER_CONTROLLER_PORT1); // Check ports

    public RobotContainer() {
        sendable_chooser.addOption("Path-1", "Path-1");
        sendable_chooser.addOption("Path-2", "Path-2");
        sendable_chooser.addOption("Path-3", "Path-3");
        sendable_chooser.addOption("Path-4", "Path-4");
        sendable_chooser.addOption("Path-5", "Path-5");
        sendable_chooser.addOption("Path-6", "Path-6");
        sendable_chooser.addOption("Path-7", "Path-7");
        sendable_chooser.addOption("Path-8", "Path-8");

        if (driver_controller.getLeftTriggerAxis() == 1 && driver_controller.getRightTriggerAxis() == 1) {
            field_centric = !field_centric;
        }

        configureButtonBindings();

        // Configure default commands
        // robot_drive.setDefaultCommand(
        // // The left stick controls translation of the robot.
        // // Turning is controlled by the X axis of the right stick.
        // new RunCommand(

        // () -> robot_drive.drive(
        // -MathUtil.applyDeadband(driver_controller.getLeftY(),
        // JoystickConstants.DRIVE_DEADBAND),
        // -MathUtil.applyDeadband(driver_controller.getLeftX(),
        // JoystickConstants.DRIVE_DEADBAND),
        // -MathUtil.applyDeadband(driver_controller.getRightX(),
        // JoystickConstants.DRIVE_DEADBAND),
        // field_centric, true),

        // robot_drive));
    }


    private void configureButtonBindings() {
        elevator.setDefaultCommand(new RunCommand(
                () -> {
                    if (driver_controller.getRightBumperButton()) {
                        elevator.move_up();
                    } else if (driver_controller.getLeftBumperButton()) {
                        elevator.move_down();
                    } else if (driver_controller.getAButton()) {
                        elevator.pid_control(30);
                    } else {
                        elevator.stop();
                    }
                }, elevator));
    }

    public Command getAutonomousCommand() {
        // switch((String) sendable_chooser.getSelected()) {
        // case "Path-1":

        // case "Path-2":

        // case "Path-3":

        // case "Path-4":

        // case "Path-5":

        // case "Path-6":

        // case "Path-7":

        // case "Path-8":

        // default:
        // DriverStation.reportWarning("No path selected", false);
        // }
        // return Commands.none();
        return null;
        // try {

        // List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
        // robot_drive.get_pose(),
        // new Pose2d(5.0, 3.0, Rotation2d.fromDegrees(90)));

        // PathConstraints constraints = new PathConstraints(3.0, 3.0, 2 * Math.PI, 4 *
        // Math.PI); // The constraints
        // // for this path.
        // PathPlannerPath path3 = new PathPlannerPath(
        // waypoints,
        // constraints,
        // null, // The ideal starting state, this is only relevant for pre-planned
        // paths, so can
        // // be null for on-the-fly paths.
        // new GoalEndState(0.0, Rotation2d.fromDegrees(-90)) // Goal end state. You can
        // set a holonomic
        // // rotation here. If using a differential
        // // drivetrain, the rotation will have no effect.
        // );

        // // Load the path you want to follow using its name in the GUI
        // PathPlannerPath path = PathPlannerPath.fromPathFile("Example Path");

        // // Create a path following command using AutoBuilder. This will also trigger
        // // event markers.
        // return AutoBuilder.followPath(path3);
        // } catch (Exception e) {
        // DriverStation.reportError("Big oops: " + e.getMessage(), e.getStackTrace());
        // return Commands.none();
        // }
    }

}