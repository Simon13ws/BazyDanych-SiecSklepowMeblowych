package com.skm.demo.RowMappers;

import com.skm.demo.Entities.Dzial;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DzialRowMapper implements RowMapper<Dzial> {

    @Override
    public Dzial mapRow(ResultSet rs, int rowNum) throws SQLException {

        Dzial dzial = new Dzial(rs.getString("nazwa_dzialu"),
                rs.getInt("id_dzialu"),
                rs.getInt("id_sklepu")
        );
        return dzial;

    }
}