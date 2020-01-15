package com.skm.demo.Entities;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Collections;

@Getter
@Setter
public class Dzial extends Entity{
    private String nazwa_dzialu;
    private int id_dzialu;
    private int id_sklepu;
    public static int kolejnaLiczba = 1;
    public static ArrayList<Integer> wolnyNr = new ArrayList<Integer>();


    public Dzial(String nazwa_dzialu, int id_dzialu, int id_sklepu)
    {
        this.nazwa_dzialu = nazwa_dzialu;
        this.id_dzialu = id_dzialu;
        this.id_sklepu = id_sklepu;
    }
}
