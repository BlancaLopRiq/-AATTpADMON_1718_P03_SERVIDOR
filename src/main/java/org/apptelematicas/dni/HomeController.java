package org.apptelematicas.dni;




import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Autowired
	private DAOInterfaz dao;
	
	private ObtenerDatos obda = new ObtenerDatos();
	
	
	
	@RequestMapping(value = "/Autentificacion", method = { RequestMethod.GET})
	public String Autentificar(Model model, HttpServletRequest req) {
		try {
		Usuario user = obda.LeerNIF();
		String usuario_dni = user.getPrimeraLetraNombre() + user.getApellido1() + user.getPrimeraLetra2Apellido();
		String clave_dni = user.getNif();
		user.setNombreUsuario(usuario_dni);
		System.out.println(usuario_dni + clave_dni); //Lee los datos correctamente, el fallo está abajo
		
		if (dao.comprobarDatos(usuario_dni, clave_dni ) == null) {
			return "NoAutentificado";
		}else 
			return "Autentificado";
		

	}catch(NullPointerException e) {
		model.addAttribute("NoAutentificado", "No ha insertado ninguna tarjeta");
	}
		return "Inicio";
}
	@RequestMapping(value="/IntroduceDatos", method= {RequestMethod.GET, RequestMethod.POST})
	public String AutentiManual (HttpServletRequest req, Model model) {
		String us,cla;
		us= req.getParameter("nombre");
		cla= req.getParameter("nif");
		try {
		if(dao.comprobarDatos(us, cla) == null) {
			return "NoAutentificado";
		}else return "Autentificado";
	}catch(NullPointerException e) {
	}
		return null;
		
	}
}
