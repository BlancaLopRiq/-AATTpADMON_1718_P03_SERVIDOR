package org.apptelematicas.dni;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.*;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

//@Repository
public class ObtenerDatos {

	/**
	 * La clase ObtenerDatos implementa cuatro mÃ©todos pÃºblicos que permiten obtener
	 * determinados datos de los certificados de tarjetas DNIe, Izenpe y Ona.
	 *
	 * @author tbc
	 */
	


	    private static final byte[] dnie_v_1_0_Atr = {
	        (byte) 0x3B, (byte) 0x7F, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x6A, (byte) 0x44,
	        (byte) 0x4E, (byte) 0x49, (byte) 0x65, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
	        (byte) 0x00, (byte) 0x00, (byte) 0x90, (byte) 0x00};
	    private static final byte[] dnie_v_1_0_Mask = {
	        (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
	        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
	        (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF};
	    static byte[] datos=null;
	 
	    public ObtenerDatos() {
	    }

	    public Usuario LeerNIF() {

	        Usuario user = null;
	      
	        try {
	            Card c = ConexionTarjeta();
	            if (c == null) {
	                throw new Exception("ACCESO DNIe: No se ha encontrado ninguna tarjeta");
	            }
	            byte[] atr = c.getATR().getBytes();
	            CardChannel ch = c.getBasicChannel();

	            if (esDNIe(atr)) {
	                datos = leerCertificado(ch);
	                if(datos!=null)
	                    user = leerDatosUsuario(datos);
	            }
	            c.disconnect(false);

	        } catch (Exception ex) {
	            Logger.getLogger(ObtenerDatos.class.getName()).log(Level.SEVERE, null, ex);
	            return null;
	        }
	        return user;
	    }

	    public byte[] leerCertificado(CardChannel ch) throws CardException, CertificateException {


	        int offset = 0;
	        String completName = null;

	        //[1] PRÃ�CTICA 3. Punto 1.a --> Selección directa del fichero dedicado(DF) por nombre
	        byte[] command = new byte[]{(byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00, (byte) 0x0b, (byte) 0x4D, (byte) 0x61, (byte) 0x73, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x2E, (byte) 0x46, (byte) 0x69, (byte) 0x6C, (byte) 0x65};
	        ResponseAPDU r = ch.transmit(new CommandAPDU(command));
	        if ((byte) r.getSW() != (byte) 0x9000) {
	            System.out.println("ACCESO DNIe: SW incorrcto");
	            return null;
	        }

	        //[2] PRÃ�CTICA 3. Punto 1.a --> Se selecciona fichero dedicado (DF) o fichero elemental(EF) por id: 5015
	        command = new byte[]{(byte) 0x00, (byte) 0xA4, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x50, (byte) 0x15};
	        r = ch.transmit(new CommandAPDU(command));

	        if ((byte) r.getSW() != (byte) 0x9000) {
	            System.out.println("ACCESO DNIe: SW incorrecto");
	            return null;
	        }

	        //[3] PRÃ�CTICA 3. Punto 1.a --> Se selecciona fichero dedicado (DF) o fichero elemental(EF) por id: 6004
	        command = new byte[]{(byte) 0x00, (byte) 0xA4, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x60, (byte) 0x04};
	        r = ch.transmit(new CommandAPDU(command));

	        byte[] responseData = null;
	        if ((byte) r.getSW() != (byte) 0x9000) {
	            System.out.println("ACCESO DNIe: SW incorrecto");
	            return null;
	        } else {
	            responseData = r.getData();
	        }

	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        byte[] r2 = null;
	        int bloque = 0;

	        do {
	             //[4] PRÃ�CTICA 3. Punto 1.b
	            final byte CLA = (byte) 0x00;//Buscar quÃ© valor poner aquÃ­ (0xFF no es el correcto)--> El valor de CLA es 0x00 (comando select)
	            final byte INS = (byte) 0xB0;//Buscar quÃ© valor poner aquÃ­ (0xFF no es el correcto)--> El valor de INS es 0xA4 (comando select)
	            final byte LE = (byte) 0xFF;// Identificar quÃ© significa este valor--> Este valor en el comando select está vacío.

	            //[4] PRÃ�CTICA 3. Punto 1.b --> Se leen 0xFF bytes del fichero
	            command = new byte[]{CLA, INS, (byte) bloque/*P1*/, (byte) 0x00/*P2*/, LE};//Identificar quÃ© hacen P1 y P2
	            r = ch.transmit(new CommandAPDU(command)); //P1 indica cómo se selecciona el fichero(por id o por nombre); P2 no indica nada, su valor siempre es 0x00 en este caso.

	            //System.out.println("ACCESO DNIe: Response SW1=" + String.format("%X", r.getSW1()) + " SW2=" + String.format("%X", r.getSW2()));

	            if ((byte) r.getSW() == (byte) 0x9000) {
	                r2 = r.getData();

	                baos.write(r2, 0, r2.length);

	                for (int i = 0; i < r2.length; i++) {
	                    byte[] t = new byte[1];
	                    t[0] = r2[i];
	                    System.out.println(i + (0xff * bloque) + String.format(" %2X", r2[i]) + " " + String.format(" %d", r2[i])+" "+new String(t));
	                }
	                bloque++;
	            } else {
	                return null;
	            }

	        } while (r2.length >= 0xfe);


	         ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

	      

	        
	        return baos.toByteArray();
	    }

	    
	    
	    
	    /**
	     * Este mÃ©todo establece la conexiÃ³n con la tarjeta. La funciÃ³n busca el
	     * Terminal que contenga una tarjeta, independientemente del tipo de tarjeta
	     * que sea.
	     *
	     * @return objeto Card con conexiÃ³n establecida
	     * @throws Exception
	     */
	    private Card ConexionTarjeta() throws Exception {

	        Card card = null;
	        TerminalFactory factory = TerminalFactory.getDefault();
	        List<CardTerminal> terminals = factory.terminals().list();
	        //System.out.println("Terminals: " + terminals);

	        for (int i = 0; i < terminals.size(); i++) {

	            // get terminal
	            CardTerminal terminal = terminals.get(i);

	            try {
	                if (terminal.isCardPresent()) {
	                    card = terminal.connect("*"); //T=0, T=1 or T=CL(not needed)
	                }
	            } catch (Exception e) {

	                System.out.println("Exception catched: " + e.getMessage());
	                card = null;
	            }
	        }
	        return card;
	    }

	    /**
	     * Este mÃ©todo nos permite saber el tipo de tarjeta que estamos leyendo del
	     * Terminal, a partir del ATR de Ã©sta.
	     *
	     * @param atrCard ATR de la tarjeta que estamos leyendo
	     * @return tipo de la tarjeta. 1 si es DNIe, 2 si es Starcos y 0 para los
	     * demÃ¡s tipos
	     */
	    private boolean esDNIe(byte[] atrCard) {
	        int j = 0;
	        boolean found = false;

	        //Es una tarjeta DNIe?
	        if (atrCard.length == dnie_v_1_0_Atr.length) {
	            found = true;
	            while (j < dnie_v_1_0_Atr.length && found) {
	                if ((atrCard[j] & dnie_v_1_0_Mask[j]) != (dnie_v_1_0_Atr[j] & dnie_v_1_0_Mask[j])) {
	                    found = false; //No es una tarjeta DNIe
	                }
	                j++;
	            }
	        }

	        if (found == true) {
	            return true;
	        } else {
	            return false;
	        }

	    }

	    /**
	     * Analizar los datos leÃ­dos del DNIe para obtener
	     *   - nombre
	     *   - apellidos
	     *   - NIF
	     * @param datos
	     * @return 
	     */
	 
	    public Usuario leerDatosUsuario(byte[] datos ) {
	    	
	        int offset=0;
	        int posicion_dni0=0;
	        int posicion_nombre0 = 0;
	        int posicion_apellido0 = 0;
	        String nif=null;
	        String apellido1= null;
	        String apellido2= null;
	        String nombre = null;
	        char PrimeraLetraNombre = 0;
	        char PrimeraLetra2Apellido = 0;
	        String nombreUsuario=null;
	        
	        
	        //Buscamos el oid que hay justo antes de donde se muestra el nif en la hoja de excel.
	        //Una vez encontrado este oid lo que hacemos es aplicarle un offset hasta llegar a
	        //donde empieza el nif.

	            if (datos[4] == 0x30) {
	                offset = 4;
	                offset += datos[offset + 1] + 2; //Obviamos la seccion del Label
	            }

	            if (datos[offset] == 0x30) {
	                offset += datos[offset + 1] + 2; //Obviamos la seccion de la informacion sobre la fecha de expedición etc
	            }

	            if ((byte) datos[offset] == (byte) 0xA1) {
	                //El certificado empieza aquí
	                byte[] r3 = new byte[9];
	                byte[] r4 = new byte[40];
	                byte[] r2 = new byte[6];
	              
	                
	                
	                //primero sacamos el DNI
	                for (int i=0;i<datos.length;i++){
	                    if (datos[i] == 0x06 && datos[i+1] == 0x03 && datos[i+2] == 0x55 && datos[i+3] == 0x04 && datos[i+4] == 0x05){
	                        posicion_dni0 = i+4;
	                        posicion_dni0= posicion_dni0 + 3;
	                    }
	                }
	        
	                //Nos posicionamos en el byte donde empieza el NIF y leemos sus 9 bytes
	                for (int z = 0; z < 9; z++) {
	                    r3[z] = datos[posicion_dni0 + z];
	                }
	                nif = new String(r3);
	                
	                
	                //Ahora lo volvemos a hacer para el nombre
	                for (int i=posicion_dni0+9; i<datos.length; i++){
	                    if (datos[i] == 0x06 && datos[i+1] == 0x03 && datos[i+2] == 0x55 && datos[i+3] == 0x04 && datos[i+4] == 0x2A){
	                        posicion_nombre0 = i+4;
	                        posicion_nombre0= posicion_nombre0 + 3;
	                    }
	                }
	                for (int z = 0; z<6; z++) {
	                	r2[z] = datos[posicion_nombre0 + z];
	                	
	                }
	                
	             
	                nombre = new String(r2);
	                PrimeraLetraNombre = nombre.charAt(0);

	                for (int i=posicion_nombre0; i<datos.length; i++){
	                    if (datos[i] == 0x06 && datos[i+1] == 0x03 && datos[i+2] == 0x55 && datos[i+3] == 0x04 && datos[i+4] == 0x03){
	                        posicion_apellido0= i+ 5;
	                        posicion_apellido0= posicion_apellido0 + 2;
	                        break;
	                    }
	                }
	                
	                
	                
	                //Nos posicionamos en el byte donde empiezan los apellido1 y leemos sus 5 bytes.
	                int v = 0;
	                while(true)
	                {
	                    if(datos[posicion_apellido0+v] == 0x2C){
	                        break;
	                    }
	                    r4[v] = datos[posicion_apellido0+v];
	                    v++;
	                }
	                String apellidos= new String(r4);
	                apellido1 = apellidos.split(" ")[0];
	                apellido2 = apellidos.split(" ")[1]; 
	                PrimeraLetra2Apellido = apellido2.charAt(0);
	                
	               
	            }
	            Usuario completName = new Usuario(nombre,apellido1,apellido2,nif,PrimeraLetraNombre,PrimeraLetra2Apellido, nombreUsuario);
	       return completName;
	    }
	}

	


