package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;

public class SetElevatorPositionCommand extends Command {
    private final ElevatorSubsystem elevator;
    private final double targetPosition;

    public SetElevatorPositionCommand(ElevatorSubsystem elevator, double targetPosition) {
        this.elevator = elevator;
        this.targetPosition = targetPosition;
        addRequirements(elevator);
    }

    @Override
    public void initialize() {
        elevator.setPosition(targetPosition);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(elevator.getPosition() - targetPosition) < 1.0; // Within tolerance
    }
}
