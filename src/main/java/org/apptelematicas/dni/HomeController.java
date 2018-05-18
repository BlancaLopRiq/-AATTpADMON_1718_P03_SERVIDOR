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
	
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Autowired
	private DAOInterfaz dao;
	
	//private ObtenerDatos obda = new ObtenerDatos();
	
	
	
	@RequestMapping(value = "/Login", method = { RequestMethod.GET, RequestMethod.POST})
	public String Autentificar(Model model, HttpServletRequest req ) {
		
		/*Usuario user = obda.LeerNIF();
		String usuario_dni = user.getPrimeraLetraNombre() + user.getApellido1() + user.getPrimeraLetra2Apellido();
		String clave_dni = user.getNif();
		user.setNombreUsuario(usuario_dni);
		 */
		String usuario_dni = req.getParameter("usuario");
		String clave_dni = req.getParameter("clave");
		System.out.println(usuario_dni + clave_dni);
		
		if (dao.comprobarDatos(usuario_dni, clave_dni ) == null) {
			return "NoAutentificado";
		}else if(dao.comprobarDatos(usuario_dni, clave_dni)!=null) {
			return "Autentificado";
		}
		 return "Index";
	}
	
//Este Servlet ya no se utiliza
	@RequestMapping(value="/Index", method= {RequestMethod.GET, RequestMethod.POST})
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
