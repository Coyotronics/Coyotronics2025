package frc.robot.ManualCommands;


//import frc.robot.subsystems.ElevatorSubsystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSubsystem;

public class ManualElevatorCommand /*extends CommandBase*/ extends Command{


    //CommandBase is deprecated as of 2025. Thus all the overrides commented out and the add requirements is gone.
    //Could I replace these with command? Yes. Will I be doing so? ...Fine, I will
    //For reference CommandBase was replaced with Command


    private final ElevatorSubsystem elevator;
    private final XboxController controller;
    private final int axis;

    public ManualElevatorCommand(ElevatorSubsystem elevator, XboxController controller, int axis) {
        this.elevator = elevator;
        this.controller = controller;
        this.axis = axis;
        addRequirements(elevator);
    }

    @Override
    public void execute() {
        double speed = controller.getRawAxis(axis); // Read joystick input
        elevator.manualControl(speed);
    }

    @Override
    public void end(boolean interrupted) {
        elevator.stop();
    } //Yet another killswitch  

    @Override
    public boolean isFinished() {
        return false; // Runs until interrupted. According to Tharun I don't need this
    }
}