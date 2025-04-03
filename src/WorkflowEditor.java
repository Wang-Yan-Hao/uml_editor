// WorkflowEditor.java
import javax.swing.*;
import java.awt.*;

public class WorkflowEditor extends JFrame {
    private Canvas canvas;
    private ToolBar toolBar;
    private EditMenu editMenu;

    public WorkflowEditor() {
        setTitle("Workflow Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        canvas = new Canvas(); // Initialize Canvas first
        toolBar = new ToolBar(canvas); // Initialize toolBar, passing the Canvas

        editMenu = new EditMenu(canvas);
        setJMenuBar(editMenu);

        add(canvas, BorderLayout.CENTER); // Canvas in the center
        add(toolBar, BorderLayout.WEST);  // Toolbar on the left

        // Set a preferred size for the toolbar to control its width
        toolBar.setPreferredSize(new Dimension(150, getHeight())); // Adjust width as needed

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WorkflowEditor());
    }
}