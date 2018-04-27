package org.apptelematicas.dni;

import java.io.Serializable;

public class Usuario implements Serializable {
	 private String nombre;
	    private String apellido1;
	    private String apellido2;
	    private String nif;
	    private char PrimeraLetraNombre;
	    private char PrimeraLetra2Apellido;
	    private String nombreUsuario;
	  
	    
	    public Usuario( String no,String ape1,String ape2,String ni, char primeraLetraNombre2, char primeraLetra2Apellido2, String NombreUsuario){
	        nombre=no;
	        apellido1=ape1;
	        apellido2=ape2;
	        nif=ni;
	        PrimeraLetraNombre = primeraLetraNombre2;
	        PrimeraLetra2Apellido =primeraLetra2Apellido2;
	        nombreUsuario = NombreUsuario;
	        
	    }
	    public Usuario(){
	    	nombre="";
	    	apellido1="";
	    	apellido2="";
	    	nif="";
	    	PrimeraLetraNombre= 0;
	    	PrimeraLetra2Apellido= 0;
	    	nombreUsuario="";
	    	
	    	
	    	
	    }
	    
	    
		public String getNombreUsuario() {
			return nombreUsuario;
		}
		public void setNombreUsuario(String nombreUsuario) {
			nombreUsuario = nombreUsuario;
		}
		public char getPrimeraLetra2Apellido() {
			return PrimeraLetra2Apellido;
		}

		public void setPrimeraLetra2Apellido(char primeraLetra2Apellido) {
			PrimeraLetra2Apellido = primeraLetra2Apellido;
		}

		public char getPrimeraLetraNombre() {
			return PrimeraLetraNombre;
		}

		public void setPrimeraLetraNombre(char primeraLetraNombre) {
			PrimeraLetraNombre = primeraLetraNombre;
		}

		@Override
	    public String toString(){
	        return nombre+" "+apellido1+" "+apellido2+" "+nif;
	    }

	    public String getNombre() {
	        return nombre;
	    }

	    public void setNombre(String nombre) {
	        this.nombre = nombre;
	    }

	    public String getApellido1() {
	        return apellido1;
	    }

	    public void setApellido1(String apellido1) {
	        this.apellido1 = apellido1;
	    }

	    public String getApellido2() {
	        return apellido2;
	    }

	    public void setApellido2(String apellido2) {
	        this.apellido2 = apellido2;
	    }

	    public String getNif() {
	        return nif;
	    }

	    public void setNif(String nif) {
	        this.nif = nif;
	    }
	          
	}


