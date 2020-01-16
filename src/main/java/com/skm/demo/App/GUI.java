package com.skm.demo.App;

import ch.qos.logback.core.Layout;
import com.skm.demo.Entities.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
        //Entity [] typy = {new Sklep(), new Dzial(), new Produkt(), new Promocja(), new PromocjaProduktu(), new Pracownik(), new Etat(), new Zespol(), new Dostawca(), new SprzetPracowniczy()};
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
                            Entity(nazwy[j], tabele[j]);

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


    public void Entity(String nazwa, String tabela) throws ClassNotFoundException, SQLException {

        JFrame entityFrame = new JFrame(nazwa);
        entityFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        entityFrame.pack();

        SpringLayout layout = new SpringLayout();
        Container content = entityFrame.getContentPane();
        content.setLayout(layout);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        entityFrame.setSize(screenSize.width/2,screenSize.height/2);
        entityFrame.setLocationRelativeTo(null);

        ArrayList<String> listaNazw = Aplikacja.getColumnNames(tabela);
        String [] nazwy = listaNazw.toArray(new String[listaNazw.size()]);
        ArrayList<String[]> listaWierszy = Aplikacja.selectAll(tabela);
        String [][] wiersze = listaWierszy.toArray(new String[listaWierszy.size()][listaNazw.size()]);

        JTable t = new JTable(wiersze, nazwy);
        JScrollPane jsp = new JScrollPane(t);
        content.add(jsp);


        //TODO pozycja tabeli + przyciski

        JTextField szukajF = new JTextField(20);
        szukajF.setEditable(true);
        szukajF.setSize(180, 60);

        content.add(szukajF);

        JButton dodaj = new JButton("Dodaj");
        JButton edytuj = new JButton("Edytuj");
        JButton usun = new JButton("Usuń");
        JButton cofnij = new JButton("Cofnij");
        JButton szukajB = new JButton("Szukaj");

        edytuj.setEnabled(false);
        usun.setEnabled(false);

        content.add(dodaj);
        content.add(edytuj);
        content.add(usun);
        content.add(cofnij);
        content.add(szukajB);

        layout.putConstraint(SpringLayout.WEST, szukajF, 5, SpringLayout.EAST, jsp);
        layout.putConstraint(SpringLayout.NORTH, szukajF, 5, SpringLayout.NORTH, content);

        layout.putConstraint(SpringLayout.WEST, szukajB, 5, SpringLayout.EAST, szukajF);
        layout.putConstraint(SpringLayout.NORTH, szukajB, 5, SpringLayout.NORTH, content);

        layout.putConstraint(SpringLayout.WEST, dodaj, 5, SpringLayout.EAST, jsp);
        layout.putConstraint(SpringLayout.NORTH, dodaj, 5, SpringLayout.SOUTH, szukajF);

        layout.putConstraint(SpringLayout.WEST, edytuj, 5, SpringLayout.EAST, jsp);
        layout.putConstraint(SpringLayout.NORTH, edytuj, 5, SpringLayout.SOUTH, dodaj);

        layout.putConstraint(SpringLayout.WEST, usun, 5, SpringLayout.EAST, jsp);
        layout.putConstraint(SpringLayout.NORTH, usun, 5, SpringLayout.SOUTH, edytuj);

        layout.putConstraint(SpringLayout.WEST, cofnij, 5, SpringLayout.EAST, jsp);
        layout.putConstraint(SpringLayout.NORTH, cofnij, 5, SpringLayout.SOUTH, usun);


        szukajB.addActionListener(e -> {

        });

        dodaj.addActionListener(e -> {
                    entityFrame.dispose();
                    try {
                        entityFrame.dispose();
                        ArrayList<String> wartosci = new ArrayList<String>();
                        int columnsCount = t.getColumnCount();
                        for(int i=0; i<columnsCount; i++)
                            wartosci.add("");
                        Edition(nazwa, tabela, wartosci, false);
                    }catch(Exception exc)
                    {
                        exc.printStackTrace();
                    }
                }
        );


        cofnij.addActionListener(e -> {
                    entityFrame.dispose();
                    try {
                        Menu();
                    }catch(Exception exc)
                    {
                        exc.printStackTrace();
                    }
                }
        );

        t.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                edytuj.setEnabled(true);
                usun.setEnabled(true);
                edytuj.addActionListener(e -> {
                            try {
                                entityFrame.dispose();
                                ArrayList<String> wartosci = new ArrayList<String>();
                                int row = t.getSelectedRow();
                                int columnsCount = t.getColumnCount();
                                for(int i=0; i<columnsCount; i++)
                                    wartosci.add(t.getValueAt(row,i).toString());
                                Edition(nazwa, tabela, wartosci, true);

                            }catch(Exception exc)
                            {
                                exc.printStackTrace();
                            }
                        }
                );

                usun.addActionListener(e -> {
                            try {
                                LinkedHashMap<Integer, String> pkID = a.getKeys(tabela, a, "primary",0);
                                LinkedHashMap<String, Object> wartosci = new LinkedHashMap<String, Object>();
                                int row = t.getSelectedRow();

                                for(Map.Entry<Integer, String> k: pkID.entrySet())
                                {
                                    wartosci.put(k.getValue(), t.getValueAt(row, k.getKey()-1));
                                }
                                for(Map.Entry<String, Object> w: wartosci.entrySet())
                                {
                                    System.out.println(w.getKey() + " " + w.getValue() + " " + w.getClass().getSimpleName());
                                }

                                Aplikacja.deleteRow(tabela, wartosci, a);
                                ArrayList<String[]> listaWierszy = Aplikacja.selectAll(tabela);
                                String [][] wiersze = listaWierszy.toArray(new String[listaWierszy.size()][listaNazw.size()]);
                                JTable t2 = new JTable(wiersze, nazwy);
                                content.remove(t);
                                content.add(new JScrollPane(t2));
                                entityFrame.setVisible(true);


                            }catch(Exception exc)
                            {
                                exc.printStackTrace();
                            }

                        }

                );

            }
        });

        entityFrame.setVisible(true);
    }

    public void Addition(String nazwa, String tabela) throws SQLException {

        JFrame addFrame = new JFrame("Dodawanie");
        addFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addFrame.pack();

        SpringLayout layout = new SpringLayout();
        Container content = addFrame.getContentPane();
        content.setLayout(layout);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        addFrame.setSize(screenSize.width/2,screenSize.height/2);
        addFrame.setLocationRelativeTo(null);

        ArrayList<String> pola = Aplikacja.getColumnNames(tabela);

        JTextField[] areas = new JTextField[pola.size()];
        JComponent[] fields = new JComponent[pola.size()];
        LinkedHashMap<Integer, String> fkTable = Aplikacja.getKeys(tabela,a,"foreign", 1);
        LinkedHashMap<Integer, String> fkName = Aplikacja.getKeys(tabela,a,"foreign", 0);
        JComboBox[] boxes = new JComboBox[fkName.size()];

        SpringLayout.Constraints constr = null;
        for(int i = 0; i < pola.size(); i++) {
            areas[i] = new JTextField(pola.get(i));
            areas[i].setEditable(false);
            areas[i].setHorizontalAlignment(JTextField.CENTER);
            areas[i].setColumns(pola.size());
            content.add(areas[i]);
            if(fkName.containsKey(i+1)){
                try {
                    ArrayList<String> kolumna = Aplikacja.selectColumn(fkTable.get(i+1), fkName.get(i + 1));
                    kolumna.add(0,"");
                    String [] wartosci = kolumna.toArray(new String[kolumna.size()]);
                    JComboBox box = new JComboBox(wartosci);
                    ((JLabel)box.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
                    box.setPrototypeDisplayValue("XXXXXXXXx");
                    fields[i] = box;
                    content.add(fields[i]);

                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else {
                if(pola.get(i).contains("id") || pola.get(i).contains("numer")) {
                    try {
                        Integer n = Aplikacja.getNextNumber(tabela);
                        JTextField field = new JTextField(n.toString(), areas[i].getColumns());
                        field.setEditable(false);
                        field.setHorizontalAlignment(JTextField.CENTER);
                        fields[i] = field;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                    fields[i] = new JTextField(areas[i].getColumns());
                content.add(fields[i]);
                }

        }

        Spring xSpring = Spring.constant(5,15,25);
        Spring ySpring = Spring.constant(5,10, 15);


        constr = layout.getConstraints(areas[0]);
        constr.setX(xSpring);
        constr.setY(ySpring);
        layout.getConstraints(fields[0]).setX(xSpring);
        layout.putConstraint(SpringLayout.NORTH, fields[0],ySpring,SpringLayout.SOUTH,areas[0]);
        layout.putConstraint(SpringLayout.SOUTH, fields[0],25,SpringLayout.NORTH,fields[0]);

        for(int i = 1 ; i< areas.length ; i++) {
            layout.getConstraints(areas[i]).setY(ySpring);
            layout.putConstraint(SpringLayout.WEST, areas[i], xSpring,SpringLayout.EAST, areas[i-1]);
            layout.putConstraint(SpringLayout.NORTH, fields[i],ySpring,SpringLayout.SOUTH,areas[i]);
            layout.putConstraint(SpringLayout.SOUTH, fields[i],25,SpringLayout.NORTH,fields[i]);
            layout.putConstraint(SpringLayout.WEST, fields[i],xSpring,SpringLayout.EAST,areas[i-1]);

        }

        JButton zatwierdz = new JButton("Zatwierdz");
        JButton anuluj = new JButton("Anuluj");

        content.add(zatwierdz);
        content.add(anuluj);

        layout.putConstraint(SpringLayout.NORTH, zatwierdz, ySpring,SpringLayout.SOUTH,fields[0]);
        layout.putConstraint(SpringLayout.NORTH, anuluj,ySpring,SpringLayout.SOUTH,zatwierdz);

        String [] wartosci = new String[pola.size()];

        zatwierdz.addActionListener(e -> {
                    try {
                        for(int i=0; i<fields.length; i++) {
                            if(fkName.containsKey(i+1)){
                                JComboBox box = (JComboBox) fields[i];
                                wartosci[i] = box.getSelectedItem().toString();
                            }
                            else {
                                JTextField field = (JTextField) fields[i];
                                wartosci[i] = field.getText();
                                wartosci[i] = wartosci[i].trim();
                            }
                            if(wartosci[i].isEmpty())
                                wartosci[i] = null;
                        }
                        Aplikacja.addRow(wartosci,tabela,a);
                        addFrame.dispose();
                        Entity(nazwa, tabela);

                    }catch(Exception exc)
                    {
                        System.out.println(exc);
                    }
                }
        );

        anuluj.addActionListener(e -> {
                    addFrame.dispose();
                    try {
                        Entity(nazwa, tabela);

                    }catch(Exception exc)
                    {
                        System.out.println(exc);
                    }
                }
        );
        addFrame.setVisible(true);
    }

    //NA RAZIE JEST TO SAMO CO W OKNIE DODAWANIA - pytanie czy nie polączyc, zeby nie duplikowac
    public void Edition(String nazwa, String tabela, ArrayList<String> w, boolean edycja) throws SQLException {

        String nazwaFrame= new String();
        if (edycja)
            nazwaFrame = "Edytowanie";
        else
            nazwaFrame = "Dodawanie";
        JFrame frame = new JFrame(nazwaFrame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        SpringLayout layout = new SpringLayout();
        Container content = frame.getContentPane();
        content.setLayout(layout);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width/2,screenSize.height/2);
        frame.setLocationRelativeTo(null);

        ArrayList<String> pola = Aplikacja.getColumnNames(tabela);

        JTextField[] areas = new JTextField[pola.size()];
        JComponent[] fields = new JComponent[pola.size()];
        LinkedHashMap<Integer, String> fkTable = Aplikacja.getKeys(tabela,a,"foreign", 1);
        LinkedHashMap<Integer, String> fkName = Aplikacja.getKeys(tabela,a,"foreign", 0);
        JComboBox[] boxes = new JComboBox[fkName.size()];

        SpringLayout.Constraints constr = null;
        for(int i = 0; i < pola.size(); i++) {
            areas[i] = new JTextField(pola.get(i));
            areas[i].setEditable(false);
            areas[i].setHorizontalAlignment(JTextField.CENTER);
            areas[i].setColumns(pola.size());
            content.add(areas[i]);
            if(fkName.containsKey(i+1)){
                try {
                    ArrayList<String> kolumna = Aplikacja.selectColumn(fkTable.get(i+1), fkName.get(i + 1));
                    kolumna.add(0,"");
                    String [] wartosci = kolumna.toArray(new String[kolumna.size()]);
                    JComboBox box = new JComboBox(wartosci);
                    if (edycja)
                        box.setSelectedItem(w.get(i));
                    ((JLabel)box.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
                    box.setPrototypeDisplayValue("XXXXXXXXx");
                    fields[i] = box;
                    content.add(fields[i]);

                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else {
                if(pola.get(i).contains("id") || pola.get(i).contains("numer")) {
                    try {
                        Integer n;
                        if(edycja)
                            n = Integer.parseInt(w.get(i));
                        else
                            n = Aplikacja.getNextNumber(tabela);
                        JTextField field = new JTextField(n.toString(), areas[i].getColumns());
                        field.setEditable(false);
                        field.setHorizontalAlignment(JTextField.CENTER);
                        fields[i] = field;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                    fields[i] = new JTextField(w.get(i), areas[i].getColumns());
                content.add(fields[i]);
            }

        }

        Spring xSpring = Spring.constant(5,15,25);
        Spring ySpring = Spring.constant(5,10, 15);


        constr = layout.getConstraints(areas[0]);
        constr.setX(xSpring);
        constr.setY(ySpring);
        layout.getConstraints(fields[0]).setX(xSpring);
        layout.putConstraint(SpringLayout.NORTH, fields[0],ySpring,SpringLayout.SOUTH,areas[0]);
        layout.putConstraint(SpringLayout.SOUTH, fields[0],25,SpringLayout.NORTH,fields[0]);

        for(int i = 1 ; i< areas.length ; i++) {
            layout.getConstraints(areas[i]).setY(ySpring);
            layout.putConstraint(SpringLayout.WEST, areas[i], xSpring,SpringLayout.EAST, areas[i-1]);
            layout.putConstraint(SpringLayout.NORTH, fields[i],ySpring,SpringLayout.SOUTH,areas[i]);
            layout.putConstraint(SpringLayout.SOUTH, fields[i],25,SpringLayout.NORTH,fields[i]);
            layout.putConstraint(SpringLayout.WEST, fields[i],xSpring,SpringLayout.EAST,areas[i-1]);

        }

        JButton zatwierdz = new JButton("Zatwierdz");
        JButton anuluj = new JButton("Anuluj");

        content.add(zatwierdz);
        content.add(anuluj);

        layout.putConstraint(SpringLayout.NORTH, zatwierdz, ySpring,SpringLayout.SOUTH,fields[0]);
        layout.putConstraint(SpringLayout.NORTH, anuluj,ySpring,SpringLayout.SOUTH,zatwierdz);

        String [] wartosci = new String[pola.size()];

        zatwierdz.addActionListener(e -> {
                    try {
                        for(int i=0; i<fields.length; i++) {
                            if(fkName.containsKey(i+1)){
                                JComboBox box = (JComboBox) fields[i];
                                wartosci[i] = box.getSelectedItem().toString();
                            }
                            else {
                                JTextField field = (JTextField) fields[i];
                                wartosci[i] = field.getText();
                                wartosci[i] = wartosci[i].trim();
                            }
                            if(wartosci[i].isEmpty())
                                wartosci[i] = null;
                        }
                        Aplikacja.addRow(wartosci,tabela,a);
                        frame.dispose();
                        Entity(nazwa, tabela);

                    }catch(Exception exc)
                    {
                        exc.printStackTrace();
                    }
                }
        );

        anuluj.addActionListener(e -> {
                    frame.dispose();
                    try {
                        Entity(nazwa, tabela);

                    }catch(Exception exc)
                    {
                        exc.printStackTrace();
                    }
                }
        );
        frame.setVisible(true);
    }

}

