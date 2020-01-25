package com.skm.demo.GUI;

import com.skm.demo.App.Aplikacja;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class Informacje extends GUI {

    private ArrayList<String> nazwy;
    private ArrayList<String> wartosci;
    private String tabela;

    public Informacje(){

    }

    public Informacje(ArrayList<String> nazwy, ArrayList<String> wartosci, String tabela){

        this.nazwy = nazwy;
        this.wartosci = wartosci;
        this.tabela = tabela;
        try {
            infoWindow();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }


    public void infoWindow() throws SQLException {
        JFrame infoFrame = new JFrame("Informacje");
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.pack();

        SpringLayout layout = new SpringLayout();
        Container content = infoFrame.getContentPane();
        content.setLayout(layout);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        infoFrame.setSize(screenSize.width / 5, screenSize.height / 3);
        infoFrame.setLocationRelativeTo(null);

        int n = nazwy.size()*2;
        if(tabela.equals("sklepy")) {
            n += 4;
            nazwy.add("Marża");
            wartosci.add(Aplikacja.podajMarze(wartosci.get(4)));
            nazwy.add("Liczba pracownikow");
            wartosci.add(Aplikacja.podajLiczbePracownikow(wartosci.get(4)));
        }

        JLabel [] labels = new JLabel[n];
        labels[0] = new JLabel("\"" + nazwy.get(0) + "\":");
        labels[nazwy.size()] = new JLabel(wartosci.get(0));

        Spring xSpring = Spring.constant(5,15,25);
        Spring ySpring = Spring.constant(5,10, 15);

        SpringLayout.Constraints constr = null;

        constr = layout.getConstraints(labels[0]);
        constr.setX(xSpring);
        constr.setY(ySpring);
        layout.getConstraints(labels[nazwy.size()]).setX(xSpring);
        layout.putConstraint(SpringLayout.WEST, labels[nazwy.size()], 10, SpringLayout.EAST, labels[0]);
        layout.putConstraint(SpringLayout.NORTH, labels[nazwy.size()],10, SpringLayout.NORTH, content);

        content.add(labels[0]);
        content.add(labels[nazwy.size()]);

        for(int i=1; i<n/2; i++)
        {
            labels[i] = new JLabel("\"" + nazwy.get(i) + "\":");
            labels[i + nazwy.size()] = new JLabel(wartosci.get(i));
            layout.putConstraint(SpringLayout.NORTH, labels[i], 10, SpringLayout.SOUTH, labels[i-1]);
            layout.putConstraint(SpringLayout.WEST, labels[i], 15, SpringLayout.WEST, content);
            layout.putConstraint(SpringLayout.WEST, labels[i+nazwy.size()], 10, SpringLayout.EAST, labels[i]);
            layout.putConstraint(SpringLayout.NORTH, labels[i+nazwy.size()], 10, SpringLayout.SOUTH, labels[i-1]);
            content.add(labels[i]);
            content.add(labels[i+nazwy.size()]);

        }

        JButton cofnij = new JButton("Cofnij");

        layout.putConstraint(SpringLayout.SOUTH, cofnij, -15, SpringLayout.SOUTH, content);
        layout.putConstraint(SpringLayout.EAST, cofnij, -15, SpringLayout.EAST, content);

        content.add(cofnij);

        cofnij.addActionListener(e ->
        {
            infoFrame.dispose();
        });

        infoFrame.setVisible(true);
    }
}
