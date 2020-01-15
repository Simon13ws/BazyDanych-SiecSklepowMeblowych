package com.skm.demo.Entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;

@Getter
@Setter
public class Sklep extends Entity{
    private String adres;
    private String miasto;
    private int kod_pocztowy;
    private int id_kierownika;
    private int id_sklepu;

    public Sklep(String adres, String miasto, int kod_pocztowy, int id_kierownika, int id_sklepu)
    {
        this.adres = adres;
        this.miasto = miasto;
        this.kod_pocztowy = kod_pocztowy;
        this.id_kierownika = id_kierownika;
        this.id_sklepu = id_sklepu;
    }
}
