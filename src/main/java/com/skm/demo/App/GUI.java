package com.skm.demo.App;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame {

    private Aplikacja a;
    private JTable t;
    private JScrollPane jsp;
    String [] nazwy = {"Sklepy", "Działy", "Produkty", "Promocje", "Promocje produktów", "Pracownicy", "Etaty", "Zespoly", "Dostawcy", "Sprzęt pracowniczy"};
    String [] tabele = {"sklepy", "dzialy", "produkty","promocje","promocja_produktu","pracownicy","etaty","zespoly","dostawcy","sprzet_pracowniczy"};
    int [] autoInc;
    ArrayList<JLabel> komunikaty = new ArrayList<JLabel>();
    Boolean modyfikacja = false;

    public GUI(Aplikacja a) throws SQLException {
        this.a = a;
        try {
            autoInc = Aplikacja.getNextIncs(tabele, a);
        }catch(SQLException e) {
            e.printStackTrace();
        }
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
                        //menuFrame.dispose();
                        try {
                            Entity(nazwy[j], tabele[j], j);

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


    public void Entity(String nazwa, String tabela, int inc) throws ClassNotFoundException, SQLException {
            JFrame entityFrame = new JFrame(nazwa);
            entityFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            entityFrame.pack();

            SpringLayout layout = new SpringLayout();
            Container content = entityFrame.getContentPane();
            content.setLayout(layout);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            entityFrame.setSize(screenSize.width / 2, screenSize.height / 2);
            entityFrame.setLocationRelativeTo(null);

            ArrayList<String> listaNazw = Aplikacja.getColumnNames(tabela);
            String[] nazwy = listaNazw.toArray(new String[listaNazw.size()]);
            LinkedHashSet<ArrayList<String>> zbiorWierszy = Aplikacja.selectAll(tabela);

            String[][] wiersze = new String[zbiorWierszy.size()][listaNazw.size()];//listaWierszy.toArray(new String[listaWierszy.size()][listaNazw.size()]);

            int x = 0;
            for(ArrayList<String> e: zbiorWierszy)
            {
                int j = 0;
                for(String s: e)
                {
                    wiersze[x][j] = s;
                    j++;
                }
                x++;
            }

            t = new JTable(new DefaultTableModel(wiersze, nazwy){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            });
            jsp = new JScrollPane(t);
            content.add(jsp);

            JTextField szukajF = new JTextField(20);
            szukajF.setEditable(true);
            szukajF.setSize(180, 60);

            content.add(szukajF);

            JButton informacje = new JButton("Informacje");
            JButton dodaj = new JButton("Dodaj");
            JButton edytuj = new JButton("Edytuj");
            JButton usun = new JButton("Usuń");
            JButton cofnij = new JButton("Cofnij");
            JButton szukajB = new JButton("Szukaj");
            ArrayList<String> nazwyKolumn = listaNazw;
            nazwyKolumn.add(0,null);
            JComboBox szukajC = new JComboBox(nazwyKolumn.toArray());

            informacje.setEnabled(false);
            edytuj.setEnabled(false);
            usun.setEnabled(false);
            content.add(szukajC);
            content.add(informacje);
            content.add(dodaj);
            content.add(edytuj);
            content.add(usun);
            content.add(cofnij);
            content.add(szukajB);

            layout.putConstraint(SpringLayout.WEST, szukajF, 5, SpringLayout.EAST, jsp);
            layout.putConstraint(SpringLayout.NORTH, szukajF, 5, SpringLayout.NORTH, content);

            layout.putConstraint(SpringLayout.WEST, szukajB, 5, SpringLayout.EAST, szukajF);
            layout.putConstraint(SpringLayout.NORTH, szukajB, 5, SpringLayout.NORTH, content);

            layout.putConstraint(SpringLayout.WEST, szukajC, 5, SpringLayout.EAST, jsp);
            layout.putConstraint(SpringLayout.NORTH, szukajC, 5, SpringLayout.SOUTH, szukajF);

            layout.putConstraint(SpringLayout.WEST, informacje, 5, SpringLayout.EAST, jsp);
            layout.putConstraint(SpringLayout.NORTH, informacje, 5, SpringLayout.SOUTH, szukajC);

            layout.putConstraint(SpringLayout.WEST, dodaj, 5, SpringLayout.EAST, jsp);
            layout.putConstraint(SpringLayout.NORTH, dodaj, 5, SpringLayout.SOUTH, informacje);

            layout.putConstraint(SpringLayout.WEST, edytuj, 5, SpringLayout.EAST, jsp);
            layout.putConstraint(SpringLayout.NORTH, edytuj, 5, SpringLayout.SOUTH, dodaj);

            layout.putConstraint(SpringLayout.WEST, usun, 5, SpringLayout.EAST, jsp);
            layout.putConstraint(SpringLayout.NORTH, usun, 5, SpringLayout.SOUTH, edytuj);

            layout.putConstraint(SpringLayout.WEST, cofnij, 5, SpringLayout.EAST, jsp);
            layout.putConstraint(SpringLayout.NORTH, cofnij, 5, SpringLayout.SOUTH, usun);

            szukajB.addActionListener(e -> {
                String wyrazenie = szukajF.getText();
                LinkedHashSet<ArrayList<String>> listaWierszy2 = new LinkedHashSet<ArrayList<String>>();
                try {
                    if(szukajC.getSelectedItem() != null)
                        listaWierszy2.addAll(Aplikacja.szukaj(tabela, szukajC.getSelectedItem().toString(), wyrazenie));
                    else {
                        for (int i = 0; i < nazwy.length; i++)
                            listaWierszy2.addAll(Aplikacja.szukaj(tabela, nazwy[i], wyrazenie));
                    }
                }catch (SQLException ex) {
                    ex.printStackTrace();
                    }


                String[][] wiersze2 = new String[listaWierszy2.size()][listaNazw.size()];
                int i = 0;
                for(ArrayList<String> l: listaWierszy2)
                {
                    int j = 0;
                    for(String s: l)
                    {
                        wiersze2[i][j] = s;
                        j++;
                    }
                    i++;
                }

                content.remove(jsp);
                t = new JTable(new DefaultTableModel(wiersze2, nazwy){
                    @Override
                    public boolean isCellEditable(int row, int column){
                        return false;
                    }
                });
                if (wiersze.length > 0)
                    t.setRowSelectionInterval(0, 0);
                else
                    t.clearSelection();
                jsp = new JScrollPane(t);
                jsp.repaint();
                content.add(jsp);
                entityFrame.setVisible(true);
            });

            informacje.addActionListener(e -> {
                        ArrayList<String> wartosci = new ArrayList<String>();
                        int row = t.getSelectedRow();
                        int columnsCount = t.getColumnCount();
                        for (int i = 0; i < columnsCount; i++)
                            wartosci.add(t.getValueAt(row, i).toString());
                        try {
                            Informacje(listaNazw, wartosci, tabela);
                        }catch(SQLException exc)
                        {
                            exc.printStackTrace();
                        }

                    }
            );

            dodaj.addActionListener(e -> {
                JLabel nieMozna;
                        if (modyfikacja) {
                            nieMozna = new JLabel("Nie można wykonać tej akcji, gdy inny element systemu jest dodawany/edytowany");
                            nieMozna.setForeground(Color.RED);
                            layout.putConstraint(SpringLayout.WEST, nieMozna, 5, SpringLayout.EAST, jsp);
                            layout.putConstraint(SpringLayout.NORTH, nieMozna, 5, SpringLayout.SOUTH, cofnij);
                            content.add(nieMozna);
                            entityFrame.setVisible(true);
                        }
                        else{
                            modyfikacja = true;
                            entityFrame.dispose();
                            try {
                                entityFrame.dispose();
                                ArrayList<String> wartosci = new ArrayList<String>();
                                int columnsCount = t.getColumnCount();
                                for (int i = 0; i < columnsCount; i++)
                                    wartosci.add("");
                                AddEd(nazwa, tabela, wartosci, inc, false);
                            } catch (Exception exc) {
                                exc.printStackTrace();
                            }
                        }
                    }
            );

            cofnij.addActionListener(e -> {
                        entityFrame.dispose();
                        /*try {
                            Menu();
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }*/
                    }
            );

            edytuj.addActionListener(e -> {
                JLabel nieMozna;
                        if (modyfikacja) {
                            nieMozna = new JLabel("Nie można wykonać tej akcji, gdy inny element systemu jest dodawany/edytowany");
                            nieMozna.setForeground(Color.RED);
                            layout.putConstraint(SpringLayout.WEST, nieMozna, 5, SpringLayout.EAST, jsp);
                            layout.putConstraint(SpringLayout.NORTH, nieMozna, 5, SpringLayout.SOUTH, cofnij);
                            content.add(nieMozna);
                            entityFrame.setVisible(true);
                        } else {
                            modyfikacja = true;
                            try {
                                entityFrame.dispose();
                                ArrayList<String> wartosci = new ArrayList<String>();
                                int row = t.getSelectedRow();
                                int columnsCount = t.getColumnCount();
                                for (int i = 0; i < columnsCount; i++)
                                    wartosci.add(t.getValueAt(row, i).toString());
                                AddEd(nazwa, tabela, wartosci, inc, true);

                            } catch (Exception exc) {
                                exc.printStackTrace();
                            }
                        }
                    }
            );

            usun.addActionListener(e -> {
                JLabel nieMozna;
                    if (modyfikacja) {
                            nieMozna = new JLabel("Nie można wykonać tej akcji, gdy inny element systemu jest dodawany/edytowany");
                            nieMozna.setForeground(Color.RED);
                            layout.putConstraint(SpringLayout.WEST, nieMozna, 5, SpringLayout.EAST, jsp);
                            layout.putConstraint(SpringLayout.NORTH, nieMozna, 5, SpringLayout.SOUTH, cofnij);
                            content.add(nieMozna);
                            entityFrame.repaint();
                        } else {
                            int row = t.getSelectedRow();
                            try {
                                LinkedHashMap<Integer, String> pkID = a.getKeys(tabela, a, "primary", 0);
                                LinkedHashMap<String, Object> wartosci = new LinkedHashMap<String, Object>();
                                for (Map.Entry<Integer, String> k : pkID.entrySet()) {
                                    wartosci.put(k.getValue(), t.getValueAt(row, k.getKey() - 1));
                                }

                                Aplikacja.deleteRow(tabela, wartosci, a);

                                LinkedHashSet<ArrayList<String>> listaWierszy2 = Aplikacja.selectAll(tabela);
                                String[][] wiersze2 = new String[listaWierszy2.size()][listaNazw.size()];
                                int i = 0;
                                for (ArrayList<String> l : listaWierszy2) {
                                    int j = 0;
                                    for (String s : l) {
                                        wiersze2[i][j] = s;
                                        j++;
                                    }
                                    i++;
                                }
                                content.remove(jsp);
                                t = new JTable(new DefaultTableModel(wiersze2, nazwy) {
                                    @Override
                                    public boolean isCellEditable(int row, int column) {
                                        return false;
                                    }
                                });
                                if (wiersze2.length > 0)
                                    t.setRowSelectionInterval(0, 0);
                                else {
                                    informacje.setEnabled(false);
                                    edytuj.setEnabled(false);
                                    usun.setEnabled(false);
                                    t.clearSelection();
                                }
                                jsp = new JScrollPane(t);
                                jsp.repaint();
                                content.add(jsp);
                                entityFrame.setVisible(true);

                            } catch (Exception exc) {
                                exc.printStackTrace();
                            }

                        }
                    }
            );

            t.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    informacje.setEnabled(true);
                    edytuj.setEnabled(true);
                    usun.setEnabled(true);
                }
            });
            entityFrame.setVisible(true);
    }

    public void AddEd(String nazwa, String tabela, ArrayList<String> w, int inc, boolean edycja) throws SQLException {

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

        ArrayList<JTextField> areas = new ArrayList<>();
        ArrayList<JComponent> fields = new ArrayList<>();
        LinkedHashMap<Integer, String> fkTable = Aplikacja.getKeys(tabela,a,"foreign", 1);
        LinkedHashMap<Integer, String> fkName = Aplikacja.getKeys(tabela,a,"foreign", 0);

        SpringLayout.Constraints constr = null;
        for(int i = 0; i < pola.size(); i++) {
            areas.add(new JTextField(pola.get(i)));
            areas.get(i).setEditable(false);
            areas.get(i).setHorizontalAlignment(JTextField.CENTER);
            areas.get(i).setColumns(pola.size());
            content.add(areas.get(i));
            try {
                if (fkName.containsKey(i + 1)) {
                    ArrayList<String> kolumna = Aplikacja.selectColumn(fkTable.get(i + 1), fkName.get(i + 1));
                    kolumna.add(0, null);
                    String[] wartosci = kolumna.toArray(new String[kolumna.size()]);
                    JComboBox box = new JComboBox(wartosci);
                    if (edycja)
                        box.setSelectedItem(w.get(i));
                    ((JLabel) box.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
                    box.setPrototypeDisplayValue("XXXXXXXXx");
                    fields.add(box);
                    content.add(fields.get(i));

                } else {
                    if (pola.get(i).contains("numer") || pola.get(i).contains("id")) {
                        try {
                            Integer n;
                            if (edycja)
                                n = Integer.parseInt(w.get(i));
                            else
                                n = autoInc[inc];
                            JTextField field = new JTextField(n.toString(), areas.get(i).getColumns());
                            field.setEditable(false);
                            field.setHorizontalAlignment(JTextField.CENTER);
                            fields.add(field);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else
                        fields.add(new JTextField(w.get(i), areas.get(i).getColumns()));
                    content.add(fields.get(i));
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        Spring xSpring = Spring.constant(5,15,25);
        Spring ySpring = Spring.constant(5,10, 15);

        constr = layout.getConstraints(areas.get(0));
        constr.setX(xSpring);
        constr.setY(ySpring);
        layout.getConstraints(fields.get(0)).setX(xSpring);
        layout.putConstraint(SpringLayout.NORTH, fields.get(0),ySpring,SpringLayout.SOUTH,areas.get(0));
        layout.putConstraint(SpringLayout.SOUTH, fields.get(0),25,SpringLayout.NORTH,fields.get(0));

        for(int i = 1 ; i< areas.size() ; i++) {
            layout.getConstraints(areas.get(i)).setY(ySpring);
            layout.putConstraint(SpringLayout.WEST, areas.get(i), xSpring,SpringLayout.EAST, areas.get(i-1));
            layout.putConstraint(SpringLayout.NORTH, fields.get(i),ySpring,SpringLayout.SOUTH,areas.get(i));
            layout.putConstraint(SpringLayout.SOUTH, fields.get(i),25,SpringLayout.NORTH,fields.get(i));
            layout.putConstraint(SpringLayout.WEST, fields.get(i),xSpring,SpringLayout.EAST,areas.get(i-1));
        }

        JButton zatwierdz = new JButton("Zatwierdz");
        JButton anuluj = new JButton("Anuluj");

        content.add(zatwierdz);
        content.add(anuluj);

        layout.putConstraint(SpringLayout.NORTH, zatwierdz, ySpring,SpringLayout.SOUTH,fields.get(0));
        layout.putConstraint(SpringLayout.NORTH, anuluj,ySpring,SpringLayout.SOUTH,zatwierdz);
        layout.putConstraint(SpringLayout.WEST, zatwierdz, xSpring,SpringLayout.WEST,content);
        layout.putConstraint(SpringLayout.WEST, anuluj, xSpring,SpringLayout.WEST,content);

        zatwierdz.addActionListener(e -> {
            for(JLabel l: komunikaty)
                content.remove(l);
            frame.repaint();
            komunikaty = new ArrayList<JLabel>();
                    try {
                        ArrayList<String> wartosci = new ArrayList<String>();
                        ArrayList<String> pola2 = new ArrayList<>();
                        pola2.addAll(pola);
                        int x=-1;
                        for(int i=0; i<fields.size(); i++) {
                            x++;
                            if(fkName.containsKey(i+1)){
                                JComboBox box = (JComboBox) fields.get(i);
                                if(box.getSelectedItem() != null)
                                    wartosci.add(box.getSelectedItem().toString());
                                else
                                {
                                    wartosci.add("");
                                    continue;
                                }
                            }
                            else {
                                if(pola2.get(x).contains("numer") || pola2.get(x).contains("id"))
                                {
                                    pola2.remove(x);
                                    x--;
                                    continue;
                                }
                                else
                                    {
                                        JTextField field = (JTextField) fields.get(i);
                                        wartosci.add(field.getText().trim());
                                    }
                            }
                        }
                        for (int i=0; i<wartosci.size(); i++) {
                            String odp = Aplikacja.checkType(tabela, pola2.get(i), wartosci.get(i), a);
                            if(odp.length() > 0)
                                komunikaty.add(new JLabel(odp));
                        }

                        if(komunikaty.size() != 0) {
                            komunikaty.get(0).setForeground(Color.RED);
                            layout.putConstraint(SpringLayout.WEST, komunikaty.get(0), xSpring, SpringLayout.EAST, zatwierdz);
                            layout.putConstraint(SpringLayout.NORTH, komunikaty.get(0), xSpring, SpringLayout.SOUTH, fields.get(0));
                            content.add(komunikaty.get(0));
                            for (int i=1; i<komunikaty.size(); i++) {
                                komunikaty.get(i).setForeground(Color.RED);
                                layout.putConstraint(SpringLayout.WEST, komunikaty.get(i), xSpring, SpringLayout.EAST, zatwierdz);
                                layout.putConstraint(SpringLayout.NORTH, komunikaty.get(i), xSpring, SpringLayout.SOUTH, komunikaty.get(i-1));
                                content.add(komunikaty.get(i));
                                }
                            frame.setVisible(true);
                        }
                        else {
                            if (edycja) {
                                LinkedHashMap<Integer, String> pkID = a.getKeys(tabela, a, "primary", 0);
                                LinkedHashMap<String, String> pkPrzedEdycja = new LinkedHashMap<String, String>();

                                for (Map.Entry<Integer, String> k : pkID.entrySet()) {
                                    pkPrzedEdycja.put(k.getValue(), w.get(k.getKey() - 1));
                                }

                                Aplikacja.updateRow(pola2, wartosci, tabela, pkPrzedEdycja, a);
                            } else {
                                Aplikacja.addRow(pola2, wartosci, tabela, a);
                                autoInc[inc]++;
                            }
                            frame.dispose();
                            modyfikacja = false;
                            Entity(nazwa, tabela, inc);
                        }

                    }catch(Exception exc)
                    {
                        exc.printStackTrace();
                    }
                }
        );

        anuluj.addActionListener(e -> {
                    frame.dispose();
                    try {
                        Entity(nazwa, tabela, inc);
                        modyfikacja = false;
                    }catch(Exception exc)
                    {
                        exc.printStackTrace();
                    }
                }
        );
        frame.setVisible(true);
    }

    public void Informacje(ArrayList<String> nazwy, ArrayList<String> wartosci, String tabela) throws SQLException {
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

