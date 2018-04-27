package org.apptelematicas.dni;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MapperDTO implements RowMapper<DTO_BBDD>{
	
	public DTO_BBDD mapRow(ResultSet rs, int rowNum) throws SQLException {
		DTO_BBDD dto = new DTO_BBDD();
		dto.setClave(rs.getString("Clave"));
		dto.setUsuario(rs.getString("Usuario"));
		
		return dto;
		
	}


}
