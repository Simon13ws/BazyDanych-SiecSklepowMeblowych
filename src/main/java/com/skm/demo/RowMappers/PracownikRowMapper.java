package com.skm.demo.RowMappers;

import com.skm.demo.Entities.Etat;
import com.skm.demo.Entities.Pracownik;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PracownikRowMapper implements RowMapper<Pracownik> {

    @Override
    public Pracownik mapRow(ResultSet rs, int rowNum) throws SQLException {

        Pracownik pracownik = new Pracownik(rs.getString("imie"),
                rs.getString("nazwisko"),
                rs.getInt("id_pracownika"),
                rs.getInt("id_szefa"),
                (Etat)rs.getObject("etaty_nazwa_etatu"),
                rs.getFloat("placa"),
                rs.getDate("zatrudniony"),
                rs.getInt("zespoly_id_zesp"),
                rs.getInt("sklepy_id_sklepu")
        );
        return pracownik;

    }
}