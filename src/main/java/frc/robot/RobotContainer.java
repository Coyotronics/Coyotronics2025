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
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.AlignAprilTag;
import frc.robot.commands.CoralScoring;
import frc.robot.constants.SwerveConstants;
import frc.robot.subsystems.AlgaeSubsystem;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.ElevatorSubsystem;

public class RobotContainer {
    static boolean field_centric;
    private final CoralSubsystem coral_subsystem;
    private final AlgaeSubsystem algae_subsystem;
    private final DriveSubsystem drive_subsystem;
    private final ElevatorSubsystem elevator_subsystem;
    private final CoralScoring coral_scoring_commands;

    // Joystick button_board = new Joystick(2);
    CommandXboxController driver_controller = new CommandXboxController(0);
    CommandXboxController subsystem_controller = new CommandXboxController(1);

    public RobotContainer() {
        field_centric = false;

        coral_subsystem = new CoralSubsystem();
        algae_subsystem = new AlgaeSubsystem();
        drive_subsystem = new DriveSubsystem();
        elevator_subsystem = new ElevatorSubsystem();

        coral_scoring_commands = new CoralScoring(elevator_subsystem, coral_subsystem,
                algae_subsystem);

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

        driver_controller.rightBumper().onTrue(coral_scoring_commands.l3_score());
        driver_controller.leftBumper().onTrue(coral_scoring_commands.l2_score());
        driver_controller.a().onTrue(coral_scoring_commands.get_coral());
        driver_controller.b().onTrue(
                new AlignAprilTag(drive_subsystem).onlyIf(() -> LimelightHelpers.getFiducialID("limelight") == 18.0));
    }

    public Command getAutonomousCommand() {
        Pose2d selectedPose = new Pose2d(2.926, 4.005, new Rotation2d(0));
        try {

            List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
                    drive_subsystem.get_pose(),
                    selectedPose);

            PathConstraints constraints = new PathConstraints(3.0, 3.0, 2 * Math.PI, 4 *
                    Math.PI);
            PathPlannerPath path = new PathPlannerPath(
                    waypoints,
                    constraints,
                    null,
                    new GoalEndState(0.0, Rotation2d.fromDegrees(-90)));

            return AutoBuilder.followPath(path).onlyIf(() -> LimelightHelpers.getFiducialID("limelight") != 0);

        } catch (Exception e) {
            DriverStation.reportError("Big oops: " + e.getMessage(), e.getStackTrace());
            return Commands.none();
        }

       
    }

    public ElevatorSubsystem getElevator_subsystem() {
        return elevator_subsystem;
    }

    public CoralSubsystem getCoralIntak() {
        return coral_subsystem;
    }
}