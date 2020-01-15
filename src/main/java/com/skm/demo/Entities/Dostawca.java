package com.skm.demo.Entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;

@Getter
@Setter
public class Dostawca extends Entity{

    private String nazwa;
    private String email;
    private int id_dostawcy;

    public Dostawca(String nazwa, String email, int id_dostawcy)
    {
        this.nazwa = nazwa;
        this.email = email;
        this.id_dostawcy = id_dostawcy;
    }
}
