package frc.robot.commands;

import static edu.wpi.first.wpilibj2.command.Commands.sequence;
import static edu.wpi.first.wpilibj2.command.Commands.waitSeconds;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AlgaeSubsystem;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;

public class CoralScoring {
    ElevatorSubsystem elevator_subsystem;
    CoralSubsystem coral_subsystem;
    AlgaeSubsystem algae_subsystem;

    public CoralScoring(ElevatorSubsystem elevator_subsystem, CoralSubsystem coral_subsystem, AlgaeSubsystem algae_subsystem) {
        this.elevator_subsystem = elevator_subsystem;
        this.coral_subsystem = coral_subsystem;
        this.algae_subsystem = algae_subsystem;
    }

    public Command l2_score() {
        return sequence(
            coral_subsystem.coral_intake(),
            coral_subsystem.pivot_to_shoot(),
            elevator_subsystem.pid_controll(21),
            coral_subsystem.coral_outtake(),
            waitSeconds(1),
            coral_subsystem.coral_stop()
        );
    }

    public Command l3_score() {
        return sequence(
            coral_subsystem.coral_intake(),
            coral_subsystem.pivot_to_shoot(),
            elevator_subsystem.pid_controll(61.5),
            coral_subsystem.coral_outtake(),
            waitSeconds(1),
            coral_subsystem.coral_stop()
        );
    }

    public Command get_coral() {
        return sequence(
            coral_subsystem.pivot_to_intake(),
            coral_subsystem.coral_intake(),
            waitSeconds(2),
            coral_subsystem.coral_stop()
        );
    }
}
