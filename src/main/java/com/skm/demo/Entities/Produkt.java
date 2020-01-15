package com.skm.demo.Entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;

@Getter
@Setter
public class Produkt extends Entity{

    private String nazwa;
    private int nr_serii;
    private float cena_bazowa;
    private int liczba;
    private int id_dzialu;
    private int id_dostawcy;
    private float cena_aktualna;

    public Produkt(String nazwa, int nr_serii, float cena_bazowa, int liczba, int id_dzialu, int id_dostawcy, float cena_aktualna)
    {
        this.nazwa = nazwa;
        this.nr_serii = nr_serii;
        this.cena_bazowa = cena_bazowa;
        this.liczba = liczba;
        this.id_dzialu = id_dzialu;
        this.id_dostawcy = id_dostawcy;
        this.cena_aktualna = cena_aktualna;
    }
}
