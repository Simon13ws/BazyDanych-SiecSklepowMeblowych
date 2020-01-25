package com.skm.demo.GUI;

import com.skm.demo.App.Aplikacja;
import java.sql.SQLException;
import javax.swing.*;

public class GUI extends JFrame {

    protected static Aplikacja a;
    protected static Boolean modyfikacja = false;
    protected static int [] autoInc;
    protected final String [] tabele = {"sklepy", "dzialy", "produkty","promocje","promocja_produktu","pracownicy","etaty","zespoly","dostawcy","sprzet_pracowniczy"};

    public GUI(){

    }

    public GUI(Aplikacja a){
        try {
            autoInc = Aplikacja.getNextIncs(tabele, a);
        }catch(SQLException e) {
            e.printStackTrace();
        }
        this.a = a;
        new Menu();
    }

}

