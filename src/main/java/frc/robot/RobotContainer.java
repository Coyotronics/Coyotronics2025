// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.CoralScoring;
import frc.robot.constants.AutoConstants;
import frc.robot.constants.SwerveConstants;
import frc.robot.subsystems.AlgaeSubsystem;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;

public class RobotContainer {
    static boolean field_centric;

    private final CoralSubsystem coral_subsystem;
    private final AlgaeSubsystem algae_subsystem;
    private final DriveSubsystem drive_subsystem;
    private final ElevatorSubsystem elevator_subsystem;
    private final CoralScoring coral_scoring_commands;

    private HashMap<String, double[]> left_reef_waypoints;
    private HashMap<String, double[]> right_reef_waypoints;
    private HashMap<String, double[]> coral_intake_waypoints;

    CommandXboxController driver_controller;
    CommandJoystick button_board;
    CommandXboxController subsystem_controller;

    public RobotContainer() {
        field_centric = false;

        coral_subsystem = new CoralSubsystem();
        algae_subsystem = new AlgaeSubsystem();
        drive_subsystem = new DriveSubsystem();
        elevator_subsystem = new ElevatorSubsystem();

        coral_scoring_commands = new CoralScoring(elevator_subsystem, coral_subsystem,
                algae_subsystem);

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

        // driver_controller.rightBumper().onTrue(coral_scoring_commands.l3_score());
        // driver_controller.leftBumper().onTrue(coral_scoring_commands.l2_score());
        driver_controller.a().onTrue(coral_scoring_commands.get_coral());
        // driver_controller.b().onTrue(
        //         new AlignAprilTag(drive_subsystem).onlyIf(() -> LimelightHelpers.getFiducialID("limelight") == 18.0));

        button_board.button(3).onTrue(coral_scoring_commands.l2_score());
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
    }

    public Command getAutonomousCommand() {
        Pose2d selectedPose = new Pose2d(0, 0, new Rotation2d(0));

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
                    new GoalEndState(0.0, Rotation2d.fromDegrees(-90)));

            return new SequentialCommandGroup(AutoBuilder.followPath(path), coral_scoring_commands.l2_score());
        } catch (Exception e) {
            DriverStation.reportError("Big oops: " + e.getMessage(), e.getStackTrace());
            return Commands.none();
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