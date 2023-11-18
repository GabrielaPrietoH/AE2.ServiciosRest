package servicio.rest.cliente.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServicioProxyMensaje {

	public static final String URL = "http://localhost:8080/";
	
	@Autowired
	private RestTemplate restTemplate;
	
	public String obtener(String path) {
		
		String pathFinal = URL + path;
		System.out.println("obtener -> Ruta final: " + pathFinal);
		String mensaje = restTemplate.getForObject(pathFinal, String.class);
		return mensaje;
	}
}
