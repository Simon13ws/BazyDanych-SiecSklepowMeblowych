package com.skm.demo.Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Zespol {
    private String nazwa_zesp;
    private int id_zesp;


    public Zespol()
    {

    }

    public Zespol(String nazwa_zesp, int id_zesp)
    {
        this.id_zesp = id_zesp;
        this.nazwa_zesp = nazwa_zesp;
    }
}
