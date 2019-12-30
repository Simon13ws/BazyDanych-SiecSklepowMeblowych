package com.skm.demo.App;

import ch.qos.logback.core.Layout;
import com.skm.demo.Entities.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;

public class GUI extends JFrame {

    Aplikacja a;

    public GUI(Aplikacja a) {
        this.a = a;
    }

    public void Menu(){

        JFrame menuFrame = new JFrame("Menu");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.pack();

        SpringLayout layout = new SpringLayout();
        Container content = menuFrame.getContentPane();
        content.setLayout(layout);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        menuFrame.setSize(screenSize.width/4,screenSize.height/2);
        menuFrame.setLocationRelativeTo(null);

        String [] nazwy = {"Sklepy", "Działy", "Produkty", "Promocje", "Promocje produktów", "Pracownicy", "Etaty", "Zespoly", "Dostawcy", "Sprzęt pracowniczy"};
        String [] tabele = {"sklepy", "dzialy", "produkty","promocje","promocja_produktu","pracownicy","etaty","zespoly","dostawcy","sprzet_pracowniczy"};
        Entity [] typy = {new Sklep(), new Dzial(), new Produkt(), new Promocja(), new PromocjaProduktu(), new Pracownik(), new Etat(), new Zespol(), new Dostawca(), new SprzetPracowniczy()};
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

            int j = i;
            przyciski[i].addActionListener(e -> {
                        menuFrame.dispose();
                        try {
                            Entity(nazwy[j], tabele[j], typy[j]);

                        }catch(Exception exc)
                        {
                            System.out.println(exc);
                        }
                    }
            );

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


    public void Entity(String nazwa, String tabela, Entity typ) throws ClassNotFoundException, SQLException {

        JFrame entityFrame = new JFrame(nazwa);
        entityFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        entityFrame.pack();

        SpringLayout layout = new SpringLayout();
        Container content = entityFrame.getContentPane();
        content.setLayout(layout);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        entityFrame.setSize(screenSize.width/2,screenSize.height/2);
        entityFrame.setLocationRelativeTo(null);

        String [] nazwy = typ.podajPola();
        String [][] rows;

        rows = Aplikacja.SelectAll(tabela);
        JTable t = new JTable(rows, nazwy);
        content.add(new JScrollPane(t));
        //TODO pozycja tabeli + przyciski

        JButton dodaj = new JButton("Dodaj");
        JButton edytuj = new JButton("Edytuj");
        JButton cofnij = new JButton("Cofnij");

        content.add(dodaj);
        content.add(edytuj);
        content.add(cofnij);

        layout.putConstraint(SpringLayout.EAST, dodaj, 5, SpringLayout.EAST, t);
        layout.putConstraint(SpringLayout.NORTH, dodaj, 5, SpringLayout.NORTH, content);

        layout.putConstraint(SpringLayout.EAST, edytuj, 5, SpringLayout.EAST, t);
        layout.putConstraint(SpringLayout.NORTH, edytuj, 5, SpringLayout.SOUTH, dodaj);

        layout.putConstraint(SpringLayout.EAST, cofnij, 5, SpringLayout.EAST, t);
        layout.putConstraint(SpringLayout.NORTH, cofnij, 5, SpringLayout.SOUTH, edytuj);

        dodaj.addActionListener(e -> {
                    entityFrame.dispose();
                    try {
                        Addition(nazwa, tabela, typ);

                    }catch(Exception exc)
                    {
                        System.out.println(exc);
                    }
                }
        );


        cofnij.addActionListener(e -> {
                    entityFrame.dispose();
                    try {
                        Menu();

                    }catch(Exception exc)
                    {
                        System.out.println(exc);
                    }
                }
        );



        entityFrame.setVisible(true);

    }

    public void Addition(String nazwa, String tabela, Entity typ) throws SQLException {

        JFrame addFrame = new JFrame("Dodawanie");
        addFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addFrame.pack();

        SpringLayout layout = new SpringLayout();
        Container content = addFrame.getContentPane();
        content.setLayout(layout);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        addFrame.setSize(screenSize.width/2,screenSize.height/2);
        addFrame.setLocationRelativeTo(null);

        String [] pola = typ.podajPola();

        JTextField[] areas = new JTextField[pola.length];
        JTextField[] fields = new JTextField[pola.length];

        SpringLayout.Constraints constr = null;

        for(int i = 0; i < pola.length; i++) {
            areas[i] = new JTextField(pola[i]);
            areas[i].setEditable(false);
            areas[i].setHorizontalAlignment(JTextField.CENTER);
            areas[i].setColumns(pola.length);
            //System.out.println(areas[i].getColumns());
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

        JButton zatwierdz = new JButton("Zatwierdz");
        JButton anuluj = new JButton("Anuluj");

        content.add(zatwierdz);
        content.add(anuluj);

        layout.putConstraint(SpringLayout.NORTH, zatwierdz, ySpring,SpringLayout.SOUTH,fields[0]);
        layout.putConstraint(SpringLayout.NORTH, anuluj,ySpring,SpringLayout.SOUTH,zatwierdz);

        String [] wartosci = new String[pola.length];

        zatwierdz.addActionListener(e -> {
                    try {
                        for(int i=0; i<fields.length; i++)
                            wartosci[i] = fields[i].getText();
                        Aplikacja.dodajWiersz(wartosci,tabela,a);
                        addFrame.dispose();
                        Entity(nazwa, tabela, typ);

                    }catch(Exception exc)
                    {
                        System.out.println(exc);
                    }
                }
        );

        anuluj.addActionListener(e -> {
                    addFrame.dispose();
                    try {
                        Entity(nazwa, tabela, typ);

                    }catch(Exception exc)
                    {
                        System.out.println(exc);
                    }
                }
        );
        addFrame.setVisible(true);
    }

    //NA RAZIE JEST TO SAMO CO W OKNIE DODAWANIA - pytanie czy nie polączyc, zeby nie duplikowac
    public void Edition(String nazwa, String tabela, Entity typ) throws SQLException {

        JFrame editFrame = new JFrame("Edycja");
        editFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        editFrame.pack();

        SpringLayout layout = new SpringLayout();
        Container content = editFrame.getContentPane();
        content.setLayout(layout);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        editFrame.setSize(screenSize.width/2,screenSize.height/2);
        editFrame.setLocationRelativeTo(null);

        String [] pola = typ.podajPola();

        JTextField[] areas = new JTextField[pola.length];
        JTextField[] fields = new JTextField[pola.length];

        SpringLayout.Constraints constr = null;

        for(int i = 0; i < areas.length; i++) {
            areas[i] = new JTextField(pola[i]);
            areas[i].setEditable(false);
            areas[i].setHorizontalAlignment(JTextField.CENTER);
            areas[i].setColumns(7);
            //System.out.println(areas[i].getColumns());
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


        JButton zatwierdz = new JButton("Zatwierdz");
        JButton anuluj = new JButton("Anuluj");

        content.add(zatwierdz);
        content.add(anuluj);

        layout.putConstraint(SpringLayout.NORTH, zatwierdz, ySpring,SpringLayout.SOUTH,fields[0]);
        layout.putConstraint(SpringLayout.NORTH, anuluj,ySpring,SpringLayout.SOUTH,zatwierdz);

        zatwierdz.addActionListener(e -> {
                    //TUTAJ WYWOLANIE FUNKCJI EDYTUJACEJ
                    editFrame.dispose();
                    try {
                        Entity(nazwa, tabela, typ);

                    }catch(Exception exc)
                    {
                        System.out.println(exc);
                    }
                }
        );

        anuluj.addActionListener(e -> {
                    editFrame.dispose();
                    try {
                        Entity(nazwa, tabela, typ);

                    }catch(Exception exc)
                    {
                        System.out.println(exc);
                    }
                }
        );

        editFrame.setVisible(true);

    }

}

