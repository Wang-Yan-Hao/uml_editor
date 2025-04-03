// EditMenu.java
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditMenu extends JMenuBar { // Ensure EditMenu extends JMenuBar
    private Canvas canvas;
    private JMenuItem ungroupMenuItem;
    private JMenuItem groupMenuItem;
    private JMenuItem labelMenuItem;

   public EditMenu(Canvas canvas) {
        this.canvas = canvas;
        JMenu editMenu = new JMenu("Edit");

        ungroupMenuItem = new JMenuItem("Ungroup");
        // ungroupMenuItem.setEnabled(false); // Enable/disable based on selection

        groupMenuItem = new JMenuItem("Group");
        // groupMenuItem.setEnabled(false);   // Enable/disable based on selection

        labelMenuItem = new JMenuItem("Label");
        labelMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Shape selectedShape = canvas.getSelectedShape();
                if (selectedShape != null) {
                    // Handle label customization logic here
                    // For example, open a dialog to set the label text and style
                    System.out.println("Label action triggered for selected shape: " + selectedShape);
                }
            }
        });

        editMenu.add(ungroupMenuItem);
        editMenu.add(groupMenuItem);
        editMenu.add(labelMenuItem);

        add(editMenu); // Add the JMenu to the JMenuBar
    }

    // You might need methods to enable/disable menu items based on selection
    // For example:
    public void updateMenuItemsState() {
        boolean shapeSelected = canvas.getSelectedShape() != null;
        ungroupMenuItem.setEnabled(shapeSelected);
        groupMenuItem.setEnabled(shapeSelected);
        labelMenuItem.setEnabled(shapeSelected);
    }
}