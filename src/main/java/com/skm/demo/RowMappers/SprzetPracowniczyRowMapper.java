package com.skm.demo.RowMappers;

import com.skm.demo.Entities.SprzetPracowniczy;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SprzetPracowniczyRowMapper implements RowMapper<SprzetPracowniczy> {

    @Override
    public SprzetPracowniczy mapRow(ResultSet rs, int rowNum) throws SQLException {

        SprzetPracowniczy sprzet = new SprzetPracowniczy(rs.getString("nazwa"),
                rs.getInt("numer_serii"),
                rs.getInt("dzialy_id_dzialu"),
                rs.getInt("liczba")
        );
        return sprzet;

    }
}