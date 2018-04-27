package org.apptelematicas.dni;

import java.io.Serializable;

public class DTO_BBDD implements Serializable{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Usuario;
	    private String Clave;
	    
	    public DTO_BBDD(String usu, String cla) {
	    	Usuario=usu;
	    	Clave=cla;
	    }
	    
	    public DTO_BBDD() {
	    	Usuario = "";
	    	Clave="";
	    }
	    @Override 
	    public String toString() {
	    	return Usuario+" "+Clave;
	    }

		public String getUsuario() {
			return Usuario;
		}

		public void setUsuario(String usuario) {
			Usuario = usuario;
		}

		public String getClave() {
			return Clave;
		}

		public void setClave(String clave) {
			Clave = clave;
		}

}
