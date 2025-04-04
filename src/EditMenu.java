import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditMenu extends JMenuBar {
    private Canvas canvas;
    private JMenuItem ungroupMenuItem;
    private JMenuItem groupMenuItem;
    private JMenuItem labelMenuItem;

    public EditMenu(Canvas canvas) {
        this.canvas = canvas;
        JMenu editMenu = new JMenu("Edit");


        groupMenuItem = new JMenuItem("Group");
        groupMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                groupAppleShapes(); // Call the correct method
            }
        });

        ungroupMenuItem = new JMenuItem("Ungroup");
        ungroupMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ungroupSelectedShape(); // Call the ungroupSelectedShape method
            }
        });

        labelMenuItem = new JMenuItem("Label");

        labelMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Shape selectedShape = canvas.getSelectedShape();
                if (selectedShape != null) {
                    // Create a panel for shape editing
                    JPanel panel = new JPanel();
                    panel.setLayout(new GridLayout(4, 2));

                    // Shape Type
                    String[] shapeTypes = {"Rect", "Oval"};
                    JComboBox<String> shapeTypeComboBox = new JComboBox<>(shapeTypes);
                    shapeTypeComboBox.setSelectedItem(selectedShape instanceof Rect ? "Rect" : "Oval");

                    // Name (Text Label)
                    JTextField nameTextField = new JTextField(selectedShape.getName());

                    // Background Color
                    JButton colorButton = new JButton("Choose Color");
                    colorButton.setBackground(selectedShape.getBackgroundColor());
                    colorButton.addActionListener(e1 -> {
                        Color newColor = JColorChooser.showDialog(canvas, "Choose Background Color", selectedShape.getBackgroundColor());
                        if (newColor != null) {
                            colorButton.setBackground(newColor);
                        }
                    });

                    // Font Size
                    JTextField fontSizeTextField = new JTextField(String.valueOf(selectedShape.getFontSize()));

                    panel.add(new JLabel("Shape Type:"));
                    panel.add(shapeTypeComboBox);
                    panel.add(new JLabel("Name:"));
                    panel.add(nameTextField);
                    panel.add(new JLabel("Background Color:"));
                    panel.add(colorButton);
                    panel.add(new JLabel("Font Size:"));
                    panel.add(fontSizeTextField);

                    int result = JOptionPane.showConfirmDialog(canvas, panel, "Edit Shape", JOptionPane.OK_CANCEL_OPTION);

                    if (result == JOptionPane.OK_OPTION) {
                        String selectedShapeType = (String) shapeTypeComboBox.getSelectedItem();
                        String newName = nameTextField.getText();
                        Color newColor = colorButton.getBackground();
                        int newFontSize = Integer.parseInt(fontSizeTextField.getText());

                        // Update shape properties and change shape type
                        if (!selectedShapeType.equals(selectedShape.getClass().getSimpleName())) {
                            // Change shape type
                            if (selectedShapeType.equals("Oval")) {
                                // Create a new Oval with the same properties as the Rect
                                Oval newOval = new Oval(selectedShape.x, selectedShape.y, selectedShape.width, selectedShape.height,
                                        newColor, newName, newFontSize);

                                // Replace the old Rect with the new Oval in the Canvas
                                canvas.replaceShape(selectedShape, newOval);
                            } else if (selectedShapeType.equals("Rect")){
                                Rect newRect = new Rect(selectedShape.x, selectedShape.y, selectedShape.width, selectedShape.height,
                                        newColor, newName, newFontSize);
                                // Replace the old Oval with the new Rect in the Canvas
                                canvas.replaceShape(selectedShape, newRect);
                            }
                        } else {
                            // Only update properties if shape type didn't change
                            selectedShape.setName(newName);
                            selectedShape.setBackgroundColor(newColor);
                            selectedShape.setFontSize(newFontSize);
                        }

                        canvas.repaint();
                    }
                }
            }
        });

        editMenu.add(ungroupMenuItem);
        editMenu.add(groupMenuItem);
        editMenu.add(labelMenuItem);

        add(editMenu);
    }


    private void groupAppleShapes() {
        canvas.combineAppleShapes();
        updateMenuItemsState();
    }

    private void ungroupSelectedShape() {
        Shape selectedShape = canvas.getSelectedShape();
        if (selectedShape instanceof GroupShape) {
            canvas.uncombineShape((GroupShape) selectedShape);
        } else {
            JOptionPane.showMessageDialog(canvas, "Select a grouped shape to ungroup.");
        }
        updateMenuItemsState();
    }

    public void updateMenuItemsState() {
        boolean shapeSelected = canvas.getSelectedShape() != null;
        ungroupMenuItem.setEnabled(shapeSelected);
        groupMenuItem.setEnabled(shapeSelected);
        labelMenuItem.setEnabled(shapeSelected);
    }
}