package com.skm.demo.Entities;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pracownik extends Entity{
    private int id_pracownika;
    private String imie;
    private String nazwisko;
    private int id_szefa;
    private Etat etat;
    private float placa;
    private Date zatrudniony;
    private int id_zespolu;
    private int id_sklepu;


    public Pracownik()
    {

    }
    public Pracownik(String imie, String nazwisko, int id_pracownika, int id_szefa, Etat etat, float placa, Date zatrudniony, int id_zespolu, int id_sklepu){
        this.id_pracownika = id_pracownika;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.id_szefa = id_szefa;
        this.etat = etat;
        this.placa = placa;
        this.zatrudniony = zatrudniony;
        this.id_zespolu = id_zespolu;
        this.id_sklepu = id_sklepu;
    }
}
