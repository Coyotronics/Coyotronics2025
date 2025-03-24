// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.wpilibj2.command.Commands.runOnce;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.CoralScoring;
import frc.robot.constants.SwerveConstants;
import frc.robot.subsystems.AlgaeSubsystem;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.ElevatorSubsystem;

public class RobotContainer {
    static boolean field_centric = false;
    private final CoralSubsystem coral_subsystem = new CoralSubsystem();
    private final AlgaeSubsystem algae_subsystem = new AlgaeSubsystem();
    private final DriveSubsystem drive_subsystem = new DriveSubsystem();
    private final ElevatorSubsystem elevator_subsystem = new ElevatorSubsystem();
    private final CoralScoring coral_scoring_commands = new CoralScoring(elevator_subsystem, coral_subsystem,
            algae_subsystem);

    // Joystick button_board = new Joystick(2);
    CommandXboxController driver_controller = new CommandXboxController(0);
    CommandXboxController subsystem_controller = new CommandXboxController(1);

    public RobotContainer() {
        configureButtonBindings();
    }

    private void configureButtonBindings() {
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

                            driver_controller.x().onTrue(runOnce(() -> {
                                drive_subsystem.zero_heading();
                            }, drive_subsystem));
                        },

                        drive_subsystem));

        driver_controller.rightBumper().onTrue(coral_scoring_commands.l3_score());
        driver_controller.leftBumper().onTrue(coral_scoring_commands.l2_score());
        driver_controller.a().onTrue(coral_scoring_commands.get_coral());
    }

    public DriveSubsystem getAutonomousCommand() {
        return drive_subsystem;
    }

    public ElevatorSubsystem getElevator_subsystem() {
        return elevator_subsystem;
    }

    public CoralSubsystem getCoralIntak() {
        return coral_subsystem;
    }
}