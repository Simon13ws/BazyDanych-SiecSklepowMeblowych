package com.skm.demo.Entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;

@Getter
@Setter
public class SprzetPracowniczy extends Entity{
    private String nazwa;
    private int nr_serii;
    private int id_dzialu;
    private int liczba;

    public SprzetPracowniczy(String nazwa, int nr_serii, int id_dzialu, int liczba)
    {
        this.nazwa = nazwa;
        this.nr_serii = nr_serii;
        this.id_dzialu = id_dzialu;
        this.liczba = liczba;
    }
}
