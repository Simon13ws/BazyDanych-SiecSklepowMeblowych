package com.skm.demo.Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Etat extends Entity{
    private String nazwa;
    private float minPlaca;
    private float maxPlaca;
    private int liczbaGodz;


    public Etat()
    {

    }

    public Etat(String nazwa, float minPlaca, float maxPlaca, int liczbaGodz)
    {
        this.nazwa = nazwa;
        this.minPlaca = minPlaca;
        this.maxPlaca = maxPlaca;
        this.liczbaGodz = liczbaGodz;
    }
}
