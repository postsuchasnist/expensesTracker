package org.example;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * class for creating a custom scroll bar
 */
public class CustomScrollBar extends BasicScrollBarUI {
    private Color thumbColor = new Color(189,195,199);
    private Color trackColor = new Color(236,240,241);

    /**
     * method that sets colors for the scrollbar
     */
    @Override
    protected void configureScrollBarColors(){
        super.configureScrollBarColors();

    }

    /**
     * method that return decrease button
     * @param orientation the orientation
     * @return
     */
    @Override
    protected JButton createDecreaseButton(int orientation){
        return createEmptyButton();
    }

    /**
     * method for increase button
     * @param orientation the orientation
     * @return
     */
    @Override
    protected JButton createIncreaseButton(int orientation){
        // Create an empty button for the increase button
        return createEmptyButton();
    }

    /**
     *
     * @param g the graphics
     * @param c the component
     * @param thumbBounds the thumb bounds
     */
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds){
        g.setColor(thumbColor);
        g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
    }

    /**
     *
     * @param g the graphics
     * @param c the component
     * @param trackBounds the track bounds
     */
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds){
        g.setColor(trackColor);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    /**
     * method that creates an empty button
     * @return
     */
    private JButton createEmptyButton(){
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        return button;
    }

}
