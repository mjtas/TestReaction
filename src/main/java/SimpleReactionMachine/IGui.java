package SimpleReactionMachine;

public interface IGui {
    // Connect GUI to controller
    // (This method will be called before any other methods)
    void connect(IController controller);

    // Initialize the GUI
    void init();

    // Called to change the displayed text
    void setDisplay(String s);
}