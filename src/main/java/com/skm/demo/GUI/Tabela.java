package com.skm.demo.GUI;

import com.skm.demo.App.Aplikacja;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class Tabela extends GUI{

    private JTable t;
    private JScrollPane jsp;
    private String nazwa;
    private String tabela;
    int inc;

    public Tabela(){

    }

    public Tabela(String nazwa, String tabela, int inc){
        this.nazwa = nazwa;
        this.tabela = tabela;
        this.inc = inc;
        try {
            entityWindow();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void entityWindow() throws SQLException {
        JFrame entityFrame = new JFrame(nazwa);
        entityFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        JButton zamknij = new JButton("Zamknij");
        JButton szukajB = new JButton("Szukaj");
        ArrayList<String> nazwyKolumn = new ArrayList<String>();
        nazwyKolumn.addAll(listaNazw);
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
        content.add(zamknij);
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

        layout.putConstraint(SpringLayout.WEST, zamknij, 5, SpringLayout.EAST, jsp);
        layout.putConstraint(SpringLayout.NORTH, zamknij, 5, SpringLayout.SOUTH, usun);

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
                    for (int i = 0; i < columnsCount; i++) {
                        if(t.getValueAt(row, i)==null)
                            wartosci.add("");
                        else
                            wartosci.add(t.getValueAt(row, i).toString());
                    }
                       new Informacje(listaNazw, wartosci, tabela);


                }
        );

        dodaj.addActionListener(e -> {
                    JLabel nieMozna;
                    if (GUI.modyfikacja) {
                        nieMozna = new JLabel("Nie można wykonać tej akcji, gdy inny element systemu jest dodawany/edytowany");
                        nieMozna.setForeground(Color.RED);
                        layout.putConstraint(SpringLayout.WEST, nieMozna, 5, SpringLayout.EAST, jsp);
                        layout.putConstraint(SpringLayout.NORTH, nieMozna, 5, SpringLayout.SOUTH, zamknij);
                        content.add(nieMozna);
                        entityFrame.setVisible(true);
                    }
                    else{
                        GUI.modyfikacja = true;
                        entityFrame.dispose();
                        try {
                            entityFrame.dispose();
                            ArrayList<String> wartosci = new ArrayList<String>();
                            int columnsCount = t.getColumnCount();
                            for (int i = 0; i < columnsCount; i++)
                                wartosci.add("");
                            new AddEd(nazwa, tabela, wartosci, inc, false);
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                }
        );

        zamknij.addActionListener(e -> {
                    entityFrame.dispose();
                }
        );

        edytuj.addActionListener(e -> {
                    JLabel nieMozna;
                    if (GUI.modyfikacja) {
                        nieMozna = new JLabel("Nie można wykonać tej akcji, gdy inny element systemu jest dodawany/edytowany");
                        nieMozna.setForeground(Color.RED);
                        layout.putConstraint(SpringLayout.WEST, nieMozna, 5, SpringLayout.EAST, jsp);
                        layout.putConstraint(SpringLayout.NORTH, nieMozna, 5, SpringLayout.SOUTH, zamknij);
                        content.add(nieMozna);
                        entityFrame.setVisible(true);
                    } else {
                        GUI.modyfikacja = true;
                        try {
                            entityFrame.dispose();
                            ArrayList<String> wartosci = new ArrayList<String>();
                            int row = t.getSelectedRow();
                            int columnsCount = t.getColumnCount();
                            for (int i = 0; i < columnsCount; i++)
                                wartosci.add(t.getValueAt(row, i).toString());
                             new AddEd(nazwa, tabela, wartosci, inc, true);

                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                    }
                }
        );

        usun.addActionListener(e -> {
                    JLabel nieMozna;
                    if (GUI.modyfikacja) {
                        nieMozna = new JLabel("Nie można wykonać tej akcji, gdy inny element systemu jest dodawany/edytowany");
                        nieMozna.setForeground(Color.RED);
                        layout.putConstraint(SpringLayout.WEST, nieMozna, 5, SpringLayout.EAST, jsp);
                        layout.putConstraint(SpringLayout.NORTH, nieMozna, 5, SpringLayout.SOUTH, zamknij);
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
                            String tekst = "Jesteś pewien, że chcesz usunąć ten obiekt z bazy?";
                            if(tabela.equals("sklepy"))
                            {
                                tekst += " Z bazy zostanie również usunięte: " + Aplikacja.selectColumnCount("dzialy","id_sklepu", t.getValueAt(row, 4).toString()) + " działów (wraz z wszystkimi ich produktami) i ";
                                tekst += Aplikacja.selectColumnCount("pracownicy","id_sklepu", t.getValueAt(row, 4).toString()) + " pracowników. ";
                            }
                            else if(tabela.equals("dzialy")) {
                                tekst += " Z bazy zostanie również usunięte: " + Aplikacja.selectColumnCount("produkty", "id_dzialu", t.getValueAt(row, 1).toString()) + " produktów, ";
                                tekst += Aplikacja.selectColumnCount("sprzet_pracowniczy", "id_dzialu", t.getValueAt(row, 1).toString()) + " sprzętów. ";

                            }
                            else if(tabela.equals("dostawcy"))
                                tekst += " Z bazy zostanie również usunięte: " + Aplikacja.selectColumnCount("produkty","id_dostawcy", t.getValueAt(row, 2).toString()) + " produktów.";
                            else if(tabela.equals("etaty"))
                                tekst += " Z bazy zostanie również usunięte: " + Aplikacja.selectColumnCount("pracownicy","etat", t.getValueAt(row, 0).toString()) + " pracowników.";

                            int dialogResult = JOptionPane.showConfirmDialog(null, tekst, "Potwierdzenie",JOptionPane.YES_NO_OPTION);
                            if(dialogResult==0) {
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
                            }
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



}
