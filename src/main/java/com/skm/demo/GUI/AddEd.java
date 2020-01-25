package com.skm.demo.GUI;

import com.skm.demo.App.Aplikacja;
import org.springframework.jdbc.UncategorizedSQLException;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddEd extends GUI {

    private String nazwa;
    private String tabela;
    private int inc;
    private ArrayList<String> w;
    boolean edycja;
    private ArrayList<JLabel> komunikaty = new ArrayList<JLabel>();

    public AddEd(){

    }

    public AddEd(String nazwa, String tabela, ArrayList<String> w, int inc, boolean edycja){
        this.nazwa = nazwa;
        this.tabela = tabela;
        this.inc = inc;
        this.w = w;
        this.edycja = edycja;

        try {
            addOrEditWindow();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void addOrEditWindow() throws SQLException {

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
        LinkedHashMap<Integer, String> fkTable = Aplikacja.getKeys(tabela,GUI.a,"foreign", 1);
        LinkedHashMap<Integer, String> fkName = Aplikacja.getKeys(tabela,GUI.a,"foreign", 0);

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
                                n = GUI.autoInc[inc];
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
                        String etat = new String();
                        for (int i=0; i<wartosci.size(); i++) {
                            if(tabela.equals("pracownicy") && pola2.get(i).equals("etat"))
                                etat = wartosci.get(i);

                            String odp = Aplikacja.checkType(tabela, pola2.get(i), wartosci.get(i), GUI.a);
                            if(odp.length() > 0)
                                komunikaty.add(new JLabel(odp));
                            else if(wartosci.get(i).isEmpty()) {
                                wartosci.set(i, null);
                            }
                            else if(pola2.get(i).equals("placa"))
                            {
                                odp = Aplikacja.checkPlaca(etat, wartosci.get(i),GUI.a);
                                if(odp.length() > 0)
                                    komunikaty.add(new JLabel(odp));
                            }
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

                                Aplikacja.updateRow(pola2, wartosci, tabela, pkPrzedEdycja, GUI.a);
                            } else {
                                Aplikacja.addRow(pola2, wartosci, tabela, GUI.a);
                                GUI.autoInc[inc]++;
                            }
                            frame.dispose();
                            GUI.modyfikacja = false;
                            new Tabela(nazwa, tabela, inc);
                        }

                    }catch(UncategorizedSQLException exce)
                    {
                        String komunikat = new String();
                        switch(tabela) {
                            case "pracownicy":
                                komunikat = "Placa powinna mieć wartość z przedziału odpowiedniego dla wybranego etatu!";
                                break;
                            case "promocje":
                                komunikat = "Promocja powinna mieć wartość z przedziału 0.00-0.99!";
                                break;
                            case "promocja_produktu":
                                komunikat = "Data zakończenia promocji musi być późniejsza od daty rozpoczęcia!";
                                break;
                            case "etaty":
                                komunikat = "Płaca maksymalna musi być większa od płacy minimalnej!";
                                break;
                        }
                        JLabel naruszenie = new JLabel(komunikat);
                        naruszenie.setForeground(Color.RED);
                        layout.putConstraint(SpringLayout.WEST, naruszenie, xSpring, SpringLayout.EAST, zatwierdz);
                        layout.putConstraint(SpringLayout.NORTH, naruszenie, xSpring, SpringLayout.SOUTH, fields.get(0));
                        content.add(naruszenie);
                        frame.setVisible(true);
                    }
                    catch(Exception exc)
                    {
                        exc.printStackTrace();
                    }
                }
        );

        anuluj.addActionListener(e -> {
                    frame.dispose();
                    try {
                        new Tabela(nazwa, tabela, inc);
                        GUI.modyfikacja = false;
                    }catch(Exception exc)
                    {
                        exc.printStackTrace();
                    }
                }
        );
        frame.setVisible(true);
    }

}
