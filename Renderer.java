package org.example;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Custom table header renderer with gradient background
 */
public class Renderer extends JLabel implements TableCellRenderer {

    private final Color startColor = new Color(100,192,192);
    private final Color endColor = new Color(100,50,70);

    /**
     * class constructor
     */

    public Renderer(){
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD,22));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 1, Color.YELLOW),
                BorderFactory.createEmptyBorder(2, 5, 2, 5))
        );
    }

    /**
     *
     * @param table           the <code>JTable</code> that is asking the
     *                          renderer to draw; can be <code>null</code>
     * @param value           the value of the cell to be rendered.  It is
     *                          up to the specific renderer to interpret
     *                          and draw the value.  For example, if
     *                          <code>value</code>
     *                          is the string "true", it could be rendered as a
     *                          string or it could be rendered as a check
     *                          box that is checked.  <code>null</code> is a
     *                          valid value
     * @param isSelected      true if the cell is to be rendered with the
     *                          selection highlighted; otherwise false
     * @param hasFocus        if true, render cell appropriately.  For
     *                          example, put a special border on the cell, if
     *                          the cell can be edited, render in the color used
     *                          to indicate editing
     * @param row             the row index of the cell being drawn.  When
     *                          drawing the header, the value of
     *                          <code>row</code> is -1
     * @param column          the column index of the cell being drawn
     *
     * @return
     */

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        setText(value.toString());
        return this;
    }

    /**
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        GradientPaint gradientPaint = new GradientPaint(
                0, 0, startColor,width, 0, endColor);

        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, width, height);

        super.paintComponent(g);
    }

}
