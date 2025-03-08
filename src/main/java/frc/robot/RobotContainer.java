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
    XboxController subsystem_controller = new XboxController(1);

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
                                -MathUtil.applyDeadband(driver_controller.getLeftY() * 0.75,
                                        JoystickConstants.DRIVE_DEADBAND),
                                -MathUtil.applyDeadband(driver_controller.getLeftX() * 0.75,
                                        JoystickConstants.DRIVE_DEADBAND),
                                -MathUtil.applyDeadband(driver_controller.getRightX() * 0.75,
                                        JoystickConstants.DRIVE_DEADBAND),
                                field_centric, true),

                        robot_drive));

        // Coral Intake
        coral_intake.setDefaultCommand(new RunCommand(
                () -> {
                    if (subsystem_controller.getAButtonReleased()) {
                        coral_intake.pivot();
                    } else if (subsystem_controller.getBButtonReleased()) {
                        coral_intake.intake();
                    }
                }, coral_intake));

        // Algae Intake
        algae_intake.setDefaultCommand(new RunCommand(
                () -> {
                    if (driver_controller.getXButtonReleased()) {
                        algae_intake.intake();
                    } else if (driver_controller.getYButtonReleased()) {
                        algae_intake.pivot();
                    }
                }, algae_intake));

        // Elevator Control
        elevator.setDefaultCommand(new RunCommand(
                () -> {
                    if (subsystem_controller.getRightBumperButton()) {
                        elevator.move_up();
                    } else if (subsystem_controller.getLeftBumperButton()) {
                        elevator.move_down();
                    } else {
                        elevator.stop();
                    }
                }, elevator));
    }

    public Command getAutonomousCommand() {
        algae_intake.pivot();
        return null;

    }

}