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

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.PathPlannerConstants;
import frc.robot.subsystems.CoralIntake;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.Elevator;

public class RobotContainer {
    static boolean field_centric = true;
    private final CoralIntake coral_intake = new CoralIntake();
    private final DriveSubsystem robot_drive = new DriveSubsystem();
    private final Elevator elevator = new Elevator();

    Joystick button_board = new Joystick(2);
    XboxController driver_controller = new XboxController(0);
    XboxController mani_controller = new XboxController(1); // Check ports

    public SendableChooser<Command> sendable_chooser = new SendableChooser<>();

    @SuppressWarnings("unchecked")
    public RobotContainer() {
        // Load in all sendable chooser options when the robot code is started
       /*sendable_chooser.addOption("Red_Reef_1", "Red_Reef_1");
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
        sendable_chooser.addOption("Processor_Red", "Processor_Red"); */

        if (driver_controller.getLeftTriggerAxis() == 1 && driver_controller.getRightTriggerAxis() == 1) {
            field_centric = !field_centric;
        }

        // Configure default commands
        configureButtonBindings();
    }

    private void configureButtonBindings() {
        // Drive Control
        // robot_drive.setDefaultCommand(
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
        // Coral Intake
        coral_intake.setDefaultCommand(new RunCommand(
                () -> {
                    if (driver_controller.getAButton()) {
                        coral_intake.pivot();
                    }
                }, coral_intake));

        // Elevator Control
        // elevator.setDefaultCommand(new RunCommand(
        //         () -> {

        //             if (driver_controller.getRightBumperButton()) {
        //                 elevator.manual_elevator_rise();
        //             } else if (driver_controller.getLeftBumperButton()) {
        //                 elevator.pid_control(10.8);
        //             } else {
        //                 elevator.stop();
        //             }
        //         }, elevator));
    }

    public Command getAutonomousCommand(String selected) {
        Pose2d selectedPose = new Pose2d();

        // Select the pose based on the selected option from the sendable chooser
        //switch ((String) sendable_chooser.getSelected()) {
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