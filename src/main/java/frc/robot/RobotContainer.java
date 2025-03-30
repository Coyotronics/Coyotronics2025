// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.wpilibj2.command.Commands.runOnce;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.CoralScoring;
import frc.robot.constants.AutoConstants;
import frc.robot.constants.SwerveConstants;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;

public class RobotContainer {
    static boolean field_centric;

    private final CoralSubsystem coral_subsystem;
    private final DriveSubsystem drive_subsystem;
    private final ElevatorSubsystem elevator_subsystem;
    private final CoralScoring coral_scoring_commands;

    private HashMap<String, double[]> left_reef_waypoints;
    private HashMap<String, double[]> right_reef_waypoints;
    private HashMap<String, double[]> coral_intake_waypoints;
    private boolean taxi;
    CommandXboxController driver_controller;
    CommandJoystick button_board;
    CommandXboxController subsystem_controller;

    public RobotContainer() {
        field_centric = false;
        taxi = true;

        coral_subsystem = new CoralSubsystem();
        drive_subsystem = new DriveSubsystem();
        elevator_subsystem = new ElevatorSubsystem();

        coral_scoring_commands = new CoralScoring(elevator_subsystem, coral_subsystem);

        driver_controller = new CommandXboxController(0);
        button_board = new CommandJoystick(1);
        subsystem_controller = new CommandXboxController(2);

        Optional<Alliance> ally = DriverStation.getAlliance();
        if (ally.isPresent()) {
            switch (ally.get()) {
                case Red:
                    left_reef_waypoints = AutoConstants.get_reef_waypoints("red_left");
                    right_reef_waypoints = AutoConstants.get_reef_waypoints("red_right");
                    coral_intake_waypoints = AutoConstants.get_intake_waypoints("red");

                    break;

                case Blue:
                    left_reef_waypoints = AutoConstants.get_reef_waypoints("blue_left");
                    right_reef_waypoints = AutoConstants.get_reef_waypoints("blue_right");
                    coral_intake_waypoints = AutoConstants.get_intake_waypoints("blue");

                    break;

                default:
                    left_reef_waypoints = new HashMap<>();
                    right_reef_waypoints = new HashMap<>();
                    coral_intake_waypoints = new HashMap<>();
                    DriverStation.reportError("Alliance not set or invalid", new Exception().getStackTrace());

                    break;
            }
        }

        configureButtonBindings();
    }

    private void configureButtonBindings() {
        driver_controller.start().onTrue(new InstantCommand(() -> drive_subsystem.zero_heading()));

        drive_subsystem.setDefaultCommand(
                new RunCommand(
                        () -> {
                            drive_subsystem.drive(
                                    -MathUtil.applyDeadband(driver_controller.getLeftY() * 0.75,
                                            SwerveConstants.DRIVE_DEADBAND),
                                    -MathUtil.applyDeadband(driver_controller.getLeftX() * 0.75,
                                            SwerveConstants.DRIVE_DEADBAND),
                                    -MathUtil.applyDeadband(driver_controller.getRightX() * 0.75,
                                            SwerveConstants.DRIVE_DEADBAND),
                                    field_centric, true);
                        },

                        drive_subsystem));

        elevator_subsystem.setDefaultCommand(new RunCommand(() -> {
            if (driver_controller.rightBumper().getAsBoolean()) {
                elevator_subsystem.move_up();
            } else if (driver_controller.leftBumper().getAsBoolean()) {
                elevator_subsystem.move_down();
            } else {
                elevator_subsystem.stop();
            }
        }, elevator_subsystem));

        driver_controller.a().onTrue(coral_scoring_commands.shoot_command());
        driver_controller.b().onTrue(coral_scoring_commands.get_coral());
        driver_controller.x().onTrue(coral_subsystem.pivot_to_shoot().andThen(coral_subsystem.coral_intake()));

        // reset gyro button
        driver_controller.y().onTrue(runOnce(() -> drive_subsystem.zero_heading()));

        button_board.button(2).negate().and(button_board.button(3)).onTrue(coral_scoring_commands.l2_score());
        button_board.button(4).onTrue(coral_scoring_commands.l3_score());

        button_board.button(5)
                .onTrue(go_to_path(coral_intake_waypoints.get("right")).andThen(coral_scoring_commands.get_coral()));
        button_board.button(6)
                .onTrue(go_to_path(coral_intake_waypoints.get("left")).andThen(coral_scoring_commands.get_coral()));

        button_board.button(2).negate().and(button_board.button(7)).onTrue(go_to_path(left_reef_waypoints.get("7")));
        button_board.button(2).negate().and(button_board.button(8)).onTrue(go_to_path(left_reef_waypoints.get("8")));
        button_board.button(2).negate().and(button_board.button(9)).onTrue(go_to_path(left_reef_waypoints.get("9")));
        button_board.button(2).negate().and(button_board.button(10)).onTrue(go_to_path(left_reef_waypoints.get("10")));
        button_board.button(2).negate().and(button_board.button(11)).onTrue(go_to_path(left_reef_waypoints.get("11")));
        button_board.button(2).negate().and(button_board.button(12)).onTrue(go_to_path(left_reef_waypoints.get("12")));

        button_board.button(2).and(button_board.button(7)).onTrue(go_to_path(right_reef_waypoints.get("7")));
        button_board.button(2).and(button_board.button(8)).onTrue(go_to_path(right_reef_waypoints.get("8")));
        button_board.button(2).and(button_board.button(9)).onTrue(go_to_path(right_reef_waypoints.get("9")));
        button_board.button(2).and(button_board.button(10)).onTrue(go_to_path(right_reef_waypoints.get("10")));
        button_board.button(2).and(button_board.button(11)).onTrue(go_to_path(right_reef_waypoints.get("11")));
        button_board.button(2).and(button_board.button(12)).onTrue(go_to_path(right_reef_waypoints.get("12")));

        button_board.button(2).and(button_board.button(3)).onTrue(runOnce(() -> cancel_all()));
        button_board.button(5).onTrue(runOnce(() -> drive_subsystem.set_x()));
    }

    public void cancel_all() {
        CommandScheduler.getInstance().cancelAll();
    }

    public Command getAutonomousCommand() {
        if (taxi) {
            Command taxi_command = new SequentialCommandGroup(

                    new InstantCommand(() -> {
                        drive_subsystem.drive_robot_relative(new ChassisSpeeds(0.5, 0.0, 0.0));
                    }, drive_subsystem),

                    new WaitCommand(1.0),

                    new InstantCommand(() -> {
                        drive_subsystem.drive_robot_relative(new ChassisSpeeds(0.0, 0.0, 0.0));
                    }, drive_subsystem));

            return taxi_command;

        } else {
            Optional<Alliance> ally = DriverStation.getAlliance();

            if (ally.get() == Alliance.Red) {
                Pose2d selectedPose = new Pose2d(11.553, 4.161, new Rotation2d(0));

                try {
                    List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
                            drive_subsystem.get_pose(),
                            selectedPose);

                    PathConstraints constraints = new PathConstraints(1.5, 1.0, 2 * Math.PI, 4 *
                            Math.PI);
                    PathPlannerPath path = new PathPlannerPath(
                            waypoints,
                            constraints,
                            null,
                            new GoalEndState(0.0, Rotation2d.fromDegrees(0)));

                    return new SequentialCommandGroup(AutoBuilder.followPath(path), coral_scoring_commands.l2_score(),
                            coral_scoring_commands.shoot_command());
                } catch (Exception e) {
                    DriverStation.reportError("Big oops: " + e.getMessage(), e.getStackTrace());
                    return Commands.none();
                }
            } else {
                Pose2d selectedPose = new Pose2d(5.977, 4.161, new Rotation2d(0));

                try {
                    List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
                            drive_subsystem.get_pose(),
                            selectedPose);

                    PathConstraints constraints = new PathConstraints(1.5, 1.0, 2 * Math.PI, 4 *
                            Math.PI);
                    PathPlannerPath path = new PathPlannerPath(
                            waypoints,
                            constraints,
                            null,
                            new GoalEndState(0.0, Rotation2d.fromDegrees(180)));

                    return new SequentialCommandGroup(AutoBuilder.followPath(path), coral_scoring_commands.l2_score());
                } catch (Exception e) {
                    DriverStation.reportError("Big oops: " + e.getMessage(), e.getStackTrace());
                    return Commands.none();
                }
            }
        }

    }

    public Command go_to_path(double[] waypoints) {
        Pose2d pose = new Pose2d(waypoints[0], waypoints[1], new Rotation2d(0));
        try {
            List<Waypoint> path_waypoints = PathPlannerPath.waypointsFromPoses(
                    drive_subsystem.get_pose(),
                    pose);

            PathConstraints constraints = new PathConstraints(1.5, 1.0, 2 * Math.PI, 4 *
                    Math.PI);
            PathPlannerPath path = new PathPlannerPath(
                    path_waypoints,
                    constraints,
                    null,
                    new GoalEndState(0.0, Rotation2d.fromDegrees(waypoints[2])));

            return AutoBuilder.followPath(path);
        } catch (Exception e) {
            DriverStation.reportError("Big oops: " + e.getMessage(), e.getStackTrace());
            return Commands.none();
        }
    }
}