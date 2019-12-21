package com.skm.demo.Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dostawca {
    private String nazwa;
    private String email;
    private int id_dostawcy;

    public Dostawca()
    {

    }

    public Dostawca(String nazwa, String email, int id_dostawcy)
    {
        this.nazwa = nazwa;
        this.email = email;
        this.id_dostawcy = id_dostawcy;
    }
}
