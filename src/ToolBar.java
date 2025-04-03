// ToolBar.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBar extends JPanel {
    private Canvas canvas;
    private JButton selectButton, rectButton, ovalButton, associationButton, generalizationButton, compositionButton;
    private JButton activeButton; // To track the currently active button

    public ToolBar(Canvas canvas) {
        this.canvas = canvas;
        setLayout(new GridLayout(6, 1));

        selectButton = createButton("Select", "select");
        rectButton = createButton("Rect", "rect");
        ovalButton = createButton("Oval", "oval");
        associationButton = createButton("Association", "association");
        generalizationButton = createButton("Generalization", "generalization");
        compositionButton = createButton("Composition", "composition");

        add(selectButton);
        add(associationButton);
        add(generalizationButton);
        add(compositionButton);
        add(rectButton);
        add(ovalButton);

        // Initially select the "Select" button
        activeButton = selectButton;
        updateButtonColors(activeButton);
    }

    private JButton createButton(String text, String mode) {
        JButton button = new JButton(text);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.setCurrentMode(mode);
                activeButton = button; // Update the active button
                updateButtonColors(button); // Update button colors
            }
        });
        return button;
    }

    private void updateButtonColors(JButton selectedButton) {
        Color activeColor = Color.CYAN; // Color for the active button
        Color defaultColor = UIManager.getColor("Button.background"); // Default button color

        selectButton.setBackground(defaultColor);
        rectButton.setBackground(defaultColor);
        ovalButton.setBackground(defaultColor);
        associationButton.setBackground(defaultColor);
        generalizationButton.setBackground(defaultColor);
        compositionButton.setBackground(defaultColor);

        selectedButton.setBackground(activeColor);
    }
}