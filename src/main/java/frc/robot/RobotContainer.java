// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.Constants.SwerveConstants;
import frc.robot.subsystems.AlgaeIntake;
import frc.robot.subsystems.CoralIntake;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.Elevator;

public class RobotContainer {
    static boolean field_centric = false;
    private final CoralIntake coral_intake = new CoralIntake();
    private final AlgaeIntake algae_intake = new AlgaeIntake();
    private final DriveSubsystem robot_drive = new DriveSubsystem();
    private final Elevator elevator = new Elevator();

    // Joystick button_board = new Joystick(2);
    XboxController driver_controller = new XboxController(0);
    XboxController subsystem_controller = new XboxController(1);

    public SendableChooser<Command> sendable_chooser = new SendableChooser<>();

    public RobotContainer() {
        configureButtonBindings();
    }

    private void configureButtonBindings() {
        // Drive Control
        robot_drive.setDefaultCommand(
                new RunCommand(
                        () -> {
                            robot_drive.drive(
                                    -MathUtil.applyDeadband(driver_controller.getLeftY() * 0.75,
                                            SwerveConstants.DRIVE_DEADBAND),
                                    -MathUtil.applyDeadband(driver_controller.getLeftX() * 0.75,
                                            SwerveConstants.DRIVE_DEADBAND),
                                    -MathUtil.applyDeadband(driver_controller.getRightX() * 0.75,
                                            SwerveConstants.DRIVE_DEADBAND),
                                    field_centric, true);

                            if (driver_controller.getXButtonReleased()) {
                                robot_drive.zero_heading();
                            }
                        },

                        robot_drive));

        // Coral Intake
        coral_intake.setDefaultCommand(new RunCommand(
                () -> {
                    if (driver_controller.getAButtonReleased()) {
                        coral_intake.pivot();
                    } else if (driver_controller.getBButtonReleased()) {
                        coral_intake.intake();
                    }
                }, coral_intake));

        // Algae Intake
        // algae_intake.setDefaultCommand(new RunCommand(
        // () -> {
        // if (driver_controller.getXButtonReleased()) {
        // algae_intake.intake();
        // } else if (driver_controller.getYButtonReleased()) {
        // algae_intake.pivot();
        // }
        // }, algae_intake));

        // Elevator Control
        elevator.setDefaultCommand(new RunCommand(() -> {
            if (driver_controller.getLeftBumperButton()) {
                elevator.move_down();
            } else if (driver_controller.getRightBumperButton()) {
                elevator.pid_control(68);
            } else {
                elevator.stop();
            }
        }, elevator));
    }

    public DriveSubsystem getAutonomousCommand() {
        return robot_drive;
    }

    public Elevator getElevator() {
        return elevator;
    }

    public CoralIntake getCoralIntak() {
        return coral_intake;
    }
}