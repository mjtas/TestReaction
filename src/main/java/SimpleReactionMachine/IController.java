package SimpleReactionMachine;

public interface IController {
    // Connect controller to GUI
    // (This method will be called before any other methods)
    void connect(IGui gui, IRandom rng);

    // Called to initialize the controller
    void init();

    // Called whenever a coin is inserted into the machine
    void coinInserted();

    // Called whenever the go/stop button is pressed
    void goStopPressed();

    // Called to deliver a TICK to the controller
    void tick();
}
