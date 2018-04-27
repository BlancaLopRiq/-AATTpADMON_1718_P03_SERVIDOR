package org.apptelematicas.dni;

import java.util.List;

import javax.sql.DataSource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class DAO implements DAOInterfaz {
	
	private JdbcTemplate jdbcTemplate;
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
	}


	public DTO_BBDD comprobarDatos(String Usuario, String Clave) {

    	String sql = "select * from dnie where Usuario=? and Clave=?";
    	Object [] param1 = {Usuario, Clave};
		MapperDTO mapper = new MapperDTO();
		List<DTO_BBDD> dtobbdd = this.jdbcTemplate.query(sql, param1, mapper);
		if(dtobbdd.isEmpty()) return null;
		else return dtobbdd.get(0);
	
	
}

}
