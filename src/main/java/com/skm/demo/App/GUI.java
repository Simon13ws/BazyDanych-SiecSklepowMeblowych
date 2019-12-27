package com.skm.demo.App;

import ch.qos.logback.core.Layout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.*;

public class GUI extends JFrame {

    private static JFrame menuFrame;
    private static JFrame frame;

    public GUI() {
        menuFrame = new JFrame("Menu");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.pack();

        frame = new JFrame("Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    public static void Menu(){
        SpringLayout layout = new SpringLayout();
        Container content = menuFrame.getContentPane();
        content.setLayout(layout);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        menuFrame.setSize(screenSize.width/4,screenSize.height/2);
        menuFrame.setLocationRelativeTo(null);

        String [] nazwy = {"Sklepy", "Działy", "Produkty", "Promocje", "Promocje produktów", "Pracownicy", "Etaty", "Zespoly", "Dostawcy", "Sprzęt pracowniczy"};
        JButton[] przyciski = new JButton[nazwy.length];
        int x = 100;
        int y = 100;
        int w = 300;
        int h = 100;
        for(int i = 0; i < nazwy.length; i++)
        {
            przyciski[i] = new JButton(nazwy[i]);
            przyciski[i].setBounds(x, y, w, h);
            y+=150;
            if(i==(nazwy.length/2)-1)
            {
                y = 100;
                x = 450;
            }
            content.add(przyciski[i]);
        }
        SpringLayout.Constraints constr = null;

        Spring xSpring = Spring.constant(30,40,50);
        Spring ySpring = Spring.constant(20,30, 40);
        constr = layout.getConstraints(przyciski[0]);
        constr.setX(xSpring);
        constr.setY(ySpring);
        layout.getConstraints(przyciski[1]).setX(xSpring);
        layout.putConstraint(SpringLayout.NORTH, przyciski[1],ySpring,SpringLayout.SOUTH,przyciski[0]);

        for(int i = 1 ; i< przyciski.length ; i++) {

            layout.getConstraints(przyciski[i]).setX(xSpring);
            layout.getConstraints(przyciski[i]).setY(ySpring);
            if(i==nazwy.length/2) {
                xSpring = Spring.constant(200,210,220);
                constr.setX(xSpring);
            }
            else
                layout.putConstraint(SpringLayout.NORTH, przyciski[i], ySpring, SpringLayout.SOUTH, przyciski[i - 1]);

        }
        menuFrame.setVisible(true);
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

