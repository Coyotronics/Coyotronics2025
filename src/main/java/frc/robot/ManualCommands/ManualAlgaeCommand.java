package frc.robot.ManualCommands;


//import frc.robot.subsystems.ElevatorSubsystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AlgaeSubsystem;

public class ManualAlgaeCommand /*extends CommandBase*/ extends Command{


    //CommandBase is deprecated as of 2025. Thus all the overrides commented out and the add requirements is gone.
    //Could I replace these with command? Yes. Will I be doing so? ...Fine, I will
    //For reference CommandBase was replaced with Command


    private final AlgaeSubsystem algaeShooter;
    private final XboxController controller;
    private final int axis;

    public ManualAlgaeCommand(AlgaeSubsystem algaeShooter, XboxController controller, int axis) {
        this.algaeShooter = algaeShooter;
        this.controller = controller;
        this.axis = axis;
        addRequirements(algaeShooter);
    }

    @Override
    public void execute() {
        double speed = controller.getRawAxis(axis); // Read joystick input
        algaeShooter.manualControl(speed);
    }

    @Override
    public void end(boolean interrupted) {
        algaeShooter.stop();
    } //Yet another killswitch  

    @Override
    public boolean isFinished() {
        return false; // Runs until interrupted. According to Tharun I don't need this
    }
}