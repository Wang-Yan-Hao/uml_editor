// LabelStyleDialog.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LabelStyleDialog extends JDialog {
    private Shape shape; // Accept a Shape object
    private JTextField labelTextField;
    private JSpinner fontSizeSpinner;
    private String labelText;
    private int fontSize;

    public LabelStyleDialog(Frame owner, Shape shape) {
        super(owner, "Customize Label", true);
        this.shape = shape; // Store the Shape object
        initComponents();
        populateFields();
    }

    private void initComponents() {
        // ... (Initialize UI components: labelTextField, fontSizeSpinner, buttons)

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                labelText = labelTextField.getText();
                fontSize = (Integer) fontSizeSpinner.getValue();
                dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        // ... (Layout components)
    }

    private void populateFields() {
        labelTextField.setText(shape.getName());
        fontSizeSpinner.setValue(shape.getFontSize());
    }

    public String getLabelText() {
        return labelText;
    }

    public int getFontSize() {
        return fontSize;
    }
}