package com.skm.demo.App;

import ch.qos.logback.core.Layout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.*;

public class GUI extends JFrame {

    static JFrame frame;

    public GUI() {
        frame = new JFrame("Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();



    }
    public static void Addition(ResultSetMetaData rsmd) throws SQLException {
        SpringLayout layout = new SpringLayout();
        Container content = frame.getContentPane();
        content.setLayout(layout);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width/2,screenSize.height/2);
        frame.setLocationRelativeTo(null);

        JTextField[] areas = new JTextField[rsmd.getColumnCount()];
        JTextField[] fields = new JTextField[rsmd.getColumnCount()];
        SpringLayout.Constraints constr = null;

        for(int i = 0; i < areas.length; i++) {
            areas[i] = new JTextField(rsmd.getColumnName(i+1));
            areas[i].setEditable(false);
            areas[i].setHorizontalAlignment(JTextField.CENTER);
            areas[i].setColumns(7);
            System.out.println(areas[i].getColumns());
            content.add(areas[i]);

            fields[i] = new JTextField(areas[i].getColumns());
            content.add(fields[i]);

        }

        Spring xSpring = Spring.constant(5,15,25);
        Spring ySpring = Spring.constant(5,10, 15);

        constr = layout.getConstraints(areas[0]);
        constr.setX(xSpring);
        constr.setY(ySpring);
        layout.getConstraints(fields[0]).setX(xSpring);
        layout.putConstraint(SpringLayout.NORTH, fields[0],ySpring,SpringLayout.SOUTH,areas[0]);

        for(int i = 1 ; i< areas.length ; i++) {
            layout.getConstraints(areas[i]).setY(ySpring);
            layout.putConstraint(SpringLayout.WEST, areas[i], xSpring,SpringLayout.EAST, areas[i-1]);
            layout.putConstraint(SpringLayout.NORTH, fields[i],ySpring,SpringLayout.SOUTH,areas[i]);
            layout.putConstraint(SpringLayout.WEST, fields[i],xSpring,SpringLayout.EAST,areas[i-1]);

        }
        frame.setVisible(true);

    }

}

