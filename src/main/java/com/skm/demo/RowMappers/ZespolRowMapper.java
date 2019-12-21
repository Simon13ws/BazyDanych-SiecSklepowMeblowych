package com.skm.demo.RowMappers;

        import com.skm.demo.Entities.Zespol;
        import org.springframework.jdbc.core.RowMapper;

        import java.sql.ResultSet;
        import java.sql.SQLException;

public class ZespolRowMapper implements RowMapper<Zespol> {

    @Override
    public Zespol mapRow(ResultSet rs, int rowNum) throws SQLException {

        Zespol zespol = new Zespol(rs.getString("nazwa_zesp"),
                rs.getInt("id_zesp")
        );
        return zespol;

    }
}