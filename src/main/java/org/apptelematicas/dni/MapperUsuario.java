package org.apptelematicas.dni;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class MapperUsuario implements RowMapper<Usuario>{
	
	public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
		Usuario usu = new Usuario();
	usu.setNombre(rs.getString("nombre"));
	usu.setApellido1(rs.getString("apellido1"));
	usu.setApellido2(rs.getString("apellido2"));
	usu.setNif(rs.getString("nif"));
	usu.setNombreUsuario(rs.getString("nombreUsuario"));
	
	//usu.setPrimeraLetra2Apellido(rs.getString("PrimeraLetra2Apellido"));
	//usu.setPrimeraLetraNombre(rs.getString("PrimeraLetraNombre"));
		return usu;
	}

}
