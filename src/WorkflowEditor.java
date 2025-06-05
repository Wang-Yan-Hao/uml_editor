// WorkflowEditor.java
import javax.swing.*;
import java.awt.*;

public class WorkflowEditor extends JFrame {
    // Singelton design pattern
    private static WorkflowEditor instance;

    private Canvas canvas;
    private ToolBar toolBar;
    private EditMenu editMenu;

    private WorkflowEditor() {
        setTitle("Workflow Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 1200);
        setLocationRelativeTo(null);

        canvas = new Canvas(); // Initialize Canvas first
        toolBar = new ToolBar(canvas); // Initialize toolBar, passing the Canvas
        editMenu = new EditMenu(canvas);
        setJMenuBar(editMenu);

        add(canvas, BorderLayout.CENTER); // Canvas in the center
        add(toolBar, BorderLayout.WEST);  // Toolbar on the left

        // 設定 toolBar 大小
        toolBar.setPreferredSize(new Dimension(150, getHeight()));

        setVisible(true);
    }

    public static WorkflowEditor getInstance() {
        if (instance == null) {
            instance = new WorkflowEditor();
        }
        return instance;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> WorkflowEditor.getInstance());
    }
}