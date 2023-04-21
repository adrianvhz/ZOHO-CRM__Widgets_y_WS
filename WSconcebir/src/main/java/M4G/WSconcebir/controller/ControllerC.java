package M4G.WSconcebir.controller;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.apache.http.HttpException;
import org.json.JSONObject;
import org.springframework.boot.origin.Origin;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.zoho.crm.library.setup.restclient.ZCRMRestClient;
import com.zoho.oauth.client.ZohoOAuthClient;
import com.zoho.oauth.common.ZohoOAuthException;
import com.zoho.oauth.contract.ZohoOAuthTokens;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST })
@RestController
public class ControllerC {
//origins = "*", methods = { RequestMethod.POST }
	//Concebir sysmedical WS
	
	
	//cracion para un registro
	//creacion de cita de un registro
	//actualizaicon solo de registro
	//actulaicion solo de cita
	//eliminacion
	//eliminacion solo de cita
	
	
//	PARA CREACION:
//		Si  campos 'Tipo de Citas Sysmedical' es "fertilidad" y tambien que el 'motivo de la consulta' sea "nuevo 1ra consulta virtual" o "nuevo 1ra consulta"
//		
//		PARA ACTUALIZACION:
//		Si campos:
//		 'motivo de la consulta' es "nuevo 1ra consulta virtual" campo 'Tipo de Consulta - tiene que actualizarse' a "Virtual" 
//		 'motivo de la consulta' es "nuevo 1ra consulta" 'Tipo de Consulta' a "Presencial"
//		
//		PARA ACTUALIZACION como cita perdida:
//		Si Campos:
//		 Cuando campo "Estado de la Cita Sysmedical" sea 'Falta con aviso' o 'Falta sin aviso' enviar al CRM para cambiar la 'fase' a "oportunidad perdida"
//		 Siempre y cuando el cliente de esta oportunidad solo tenga una cita, de tener mas de 1 solo marcar en el subform y no en la fase
//		 
//		when el campo account_ (en crm usuario sismedical)cuando sea "dJoya"=> fuente de posible paciente debe ser digital
//		y cualquiera de las otras "central telefonica"

	
	@PostMapping("/Create/record")
	public HashMap<String, Object> createRecord(
		@RequestBody RecibidorDatas jsonData, @RequestHeader String user, @RequestHeader String password
	) throws Exception {
		HashMap<String, Object> mapReturn = new HashMap<String, Object>();
		
		if (!(user.equalsIgnoreCase("ws_concebirM4G") && password.equalsIgnoreCase("ZohoCrm_75a205e5cd5da8ba6dff47f2b3f85a67d5679f555ad043429ee018359e82c85f"))) {
			mapReturn.put("code", "820");
			mapReturn.put("msg", "Usuario invalido");
			return mapReturn;
		}
		
		HashMap<String, String> zcrmConfigurations = new HashMap<String, String>();
		zcrmConfigurations.put("minLogLevel", "ALL");
		zcrmConfigurations.put("currentUserEmail", "antonio.benavides@serprosa.com.pe");
		zcrmConfigurations.put("client_id", "1000.XYS010L81NWVVL8DNXSPI1OU3OKF7C");
		zcrmConfigurations.put("client_secret", "d8d032722e2da9ffcd2efada772faf430c9e0e6a87");
		zcrmConfigurations.put("redirect_uri", "https://www.m4g.com.pe/response");
		zcrmConfigurations.put("persistence_handler_class", "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence"); // for database. "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence" for file, user can implement his own persistence and provide the path here
		
		//zcrmConfigurations.put("oauth_tokens_file_path", "/opt/propertiesConcebirCrm/TokenAccesConcebirCRM.properties");
		zcrmConfigurations.put("oauth_tokens_file_path","E:\\Workspace_EclipseFullTrabajo\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
//		zcrmConfigurations.put("oauth_tokens_file_path","C:\\Users\\USER\\Desktop\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
		
		zcrmConfigurations.put("domainSuffix", "com"); // optional. Default is com. "cn" and "eu" supported
		zcrmConfigurations.put("accessType", "Production"); // Production->www(default), Development->developer, Sandbox->sandbox(optional)
		
		ZCRMRestClient.initialize(zcrmConfigurations);
		ZohoOAuthClient cli = ZohoOAuthClient.getInstance();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		System.out.println(
			"WS Concebir, Init procesos path#Create# Hora de Ejecucion: " + dtf.format(now) + "\n" +
			"Token de acceso: " + cli.getAccessToken("antonio.benavides@serprosa.com.pe") + "\n" +
			"Datos recibidos: |" + jsonData + "|"
		);
		
		// deluge call api settings
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Zoho-oauthtoken " + cli.getAccessToken("antonio.benavides@serprosa.com.pe"));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("action", "create/record");
		map.add("param_one",
			"{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
			"\",\"name_completo\":\""+jsonData.getNombre_completo()+"\",\"num_documento\":\""+jsonData.getNumero_de_documento()+
			"\",\"edad_paciente\":\""+jsonData.getEdad_paciente()+"\",\"refe_medio_contacto\":\""+jsonData.getReferencia_medio_de_contacto()+
			"\",\"fecha_horade_cita\":\""+jsonData.getFecha_hora_de_cita()+"\",\"tipo_d_documento\":\""+jsonData.getTipo_d_documento()+
			"\",\"fecha_nacimiento\":\""+jsonData.getFecha_d_nacimiento()+"\",\"num_celular\":\""+jsonData.getNumero_celular()+
			"\",\"num_telefono\":\""+jsonData.getNumero_telefono()+"\",\"name_doctor\":\""+jsonData.getName_doctor()+
			"\",\"name_sede\":\""+jsonData.getName_sede()+"\",\"tipo_cita\":\""+jsonData.getTipo_cita()+
			"\",\"motivo_consulta\":\""+jsonData.getMotivo_consulta()+"\",\"estado_cita\":\""+jsonData.getStatus_cita()+
			"\",\"activ_codigo\":\""+jsonData.getActividadx_codigo()+"\",\"activ_codigo\":\""+jsonData.getZ_account_create()+"\"}"
		);
		
		System.out.println("-> " + map.get("param_one"));
		
		HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> ConsultaDeluge = restTemplate.postForEntity(
			// "https://sandbox.zohoapis.com/crm/v2/functions/bd_sysmedical_callaccess_test/actions/execute?auth_type=oauth",
			"https://www.zohoapis.com/crm/v2/functions/bd_sysmedical_callaccess/actions/execute?auth_type=oauth",
			request2,
			String.class
		);
		
		try {
			String EstadoCallDeluge = "";
			JSONObject json = new JSONObject(ConsultaDeluge.getBody());
			System.out.println(ConsultaDeluge.getBody());
			
			//
			System.out.println("Extracion de data deluge zoho: \nDeluge respuesta de envio=> " + json + "\n Fin de procesos\n************");
			//
			
			EstadoCallDeluge = json.getString("code");
			String Response = json.getJSONObject("details").get("output") + "";

			if (EstadoCallDeluge.contains("success")) {
				mapReturn.put("response", Response);
			}
			else {
				mapReturn.put("code", "420");
			}
		}
		catch(Exception e1) {
			mapReturn.put("code","450");
			mapReturn.put("msg", "Durante el tratamiento de informacion hubo un problema");
			mapReturn.put("DataEnviada", "{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
					   "\",\"name_completo\":\""+jsonData.getNombre_completo()+"\",\"num_documento\":\""+jsonData.getNumero_de_documento()+
					   "\",\"edad_paciente\":\""+jsonData.getEdad_paciente()+"\",\"refe_medio_contacto\":\""+jsonData.getReferencia_medio_de_contacto()+
					   "\",\"fecha_horade_cita\":\""+jsonData.getFecha_hora_de_cita()+"\",\"tipo_d_documento\":\""+jsonData.getTipo_d_documento()+
					   "\",\"fecha_nacimiento\":\""+jsonData.getFecha_d_nacimiento()+"\",\"num_celular\":\""+jsonData.getNumero_celular()+
					   "\",\"num_telefono\":\""+jsonData.getNumero_telefono()+"\",\"name_doctor\":\""+jsonData.getName_doctor()+
					   "\",\"name_sede\":\""+jsonData.getName_sede()+"\",\"tipo_cita\":\""+jsonData.getTipo_cita()+
					   "\",\"motivo_consulta\":\""+jsonData.getMotivo_consulta()+"\",\"estado_cita\":\""+jsonData.getStatus_cita()+
					   "\",\"activ_codigo\":\""+jsonData.getActividadx_codigo()+"\",\"activ_codigo\":\""+jsonData.getZ_account_create()+"\"}");
			System.out.println(e1.getMessage());
			System.out.println(e1.getCause().getLocalizedMessage());
			System.out.println(e1.getLocalizedMessage());
		}
		
		System.out.println("Fin de proceso Ws concebir ***");
		
		return mapReturn;
	}
	
	@PostMapping("/Create/cita")
	public HashMap<String, Object> createCita(
		@RequestBody RecibidorDatas jsonData, @RequestHeader String user, @RequestHeader String password
	) throws Exception {
		HashMap<String, Object> mapReturn = new HashMap<String, Object>();
		
		if (!(user.equalsIgnoreCase("ws_concebirM4G") && password.equalsIgnoreCase("ZohoCrm_75a205e5cd5da8ba6dff47f2b3f85a67d5679f555ad043429ee018359e82c85f"))) {
			mapReturn.put("code", "820");
			mapReturn.put("msg", "Usuario invalido");
			return mapReturn;
		}
		
		HashMap<String, String> zcrmConfigurations = new HashMap<String, String>();
		zcrmConfigurations.put("minLogLevel", "ALL");
		zcrmConfigurations.put("currentUserEmail", "antonio.benavides@serprosa.com.pe");
		zcrmConfigurations.put("client_id", "1000.XYS010L81NWVVL8DNXSPI1OU3OKF7C");
		zcrmConfigurations.put("client_secret", "d8d032722e2da9ffcd2efada772faf430c9e0e6a87");
		zcrmConfigurations.put("redirect_uri", "https://www.m4g.com.pe/response");
		zcrmConfigurations.put("persistence_handler_class", "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence"); // for database. "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence" for file, user can implement his own persistence and provide the path here
		
		//zcrmConfigurations.put("oauth_tokens_file_path", "/opt/propertiesConcebirCrm/TokenAccesConcebirCRM.properties");
		zcrmConfigurations.put("oauth_tokens_file_path","E:\\Workspace_EclipseFullTrabajo\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
//		zcrmConfigurations.put("oauth_tokens_file_path","C:\\Users\\USER\\Desktop\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
		zcrmConfigurations.put("domainSuffix", "com"); // optional. Default is com. "cn" and "eu" supported
		zcrmConfigurations.put("accessType", "Production"); // Production->www(default), Development->developer, Sandbox->sandbox(optional)
		
		ZCRMRestClient.initialize(zcrmConfigurations);
		ZohoOAuthClient cli = ZohoOAuthClient.getInstance();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		System.out.println(
			"WS Concebir, Init procesos path#Create# Hora de Ejecucion: " + dtf.format(now) + "\n" +
			"Token de acceso: " + cli.getAccessToken("antonio.benavides@serprosa.com.pe") + "\n" +
			"Datos recibidos: |" + jsonData + "|"
		);
		
		// deluge call api settings
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Zoho-oauthtoken " + cli.getAccessToken("antonio.benavides@serprosa.com.pe"));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("action", "create/cita");
		map.add("param_one",
			"{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
			"\",\"name_completo\":"+jsonData.getNombre_completo()+",\"num_documento\":"+jsonData.getNumero_de_documento()+
			",\"edad_paciente\":"+jsonData.getEdad_paciente()+",\"refe_medio_contacto\":"+jsonData.getReferencia_medio_de_contacto()+
			",\"fecha_horade_cita\":"+jsonData.getFecha_hora_de_cita()+",\"tipo_d_documento\":"+jsonData.getTipo_d_documento()+
			",\"fecha_nacimiento\":"+jsonData.getFecha_d_nacimiento()+",\"num_celular\":"+jsonData.getNumero_celular()+
			",\"num_telefono\":"+jsonData.getNumero_telefono()+",\"name_doctor\":"+jsonData.getName_doctor()+
			",\"name_sede\":"+jsonData.getName_sede()+",\"tipo_cita\":"+jsonData.getTipo_cita()+
			",\"motivo_consulta\":"+jsonData.getMotivo_consulta()+",\"estado_cita\":"+jsonData.getStatus_cita()+
			",\"activ_codigo\":"+jsonData.getActividadx_codigo()+",\"z_account_create\":"+jsonData.getZ_account_create()+ "}"
		);
		
		System.out.println("-> " + map.get("param_one"));
		
//		return mapReturn;
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> ConsultaDeluge = restTemplate.postForEntity(
			"https://www.zohoapis.com/crm/v2/functions/bd_sysmedical_callaccess/actions/execute?auth_type=oauth",
			request,
			String.class
		);
		
		try {
			String EstadoCallDeluge = "";
			JSONObject json = new JSONObject(ConsultaDeluge.getBody());
			
			System.out.println("Extracion de data deluge zoho: \nDeluge respuesta de envio=> " + json + "\n Fin de procesos\n************");
			
			EstadoCallDeluge = json.getString("code");
			String Response = json.getJSONObject("details").get("output") + "";
			
		
			if (EstadoCallDeluge.contains("success")) {
				mapReturn.put("response", Response);
			}
			else {
				mapReturn.put("code", "420");
			}
		}
		catch(Exception e1) {
			mapReturn.put("code", "450");
			mapReturn.put("msg", "Durante el tratamiento de informacion hubo un problema");
			mapReturn.put("DataEnviada", "{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
					   "\",\"name_completo\":\""+jsonData.getNombre_completo()+"\",\"num_documento\":\""+jsonData.getNumero_de_documento()+
					   "\",\"edad_paciente\":\""+jsonData.getEdad_paciente()+"\",\"refe_medio_contacto\":\""+jsonData.getReferencia_medio_de_contacto()+
					   "\",\"fecha_horade_cita\":\""+jsonData.getFecha_hora_de_cita()+"\",\"tipo_d_documento\":\""+jsonData.getTipo_d_documento()+
					   "\",\"fecha_nacimiento\":\""+jsonData.getFecha_d_nacimiento()+"\",\"num_celular\":\""+jsonData.getNumero_celular()+
					   "\",\"num_telefono\":\""+jsonData.getNumero_telefono()+"\",\"name_doctor\":\""+jsonData.getName_doctor()+
					   "\",\"name_sede\":\""+jsonData.getName_sede()+"\",\"tipo_cita\":\""+jsonData.getTipo_cita()+
					   "\",\"motivo_consulta\":\""+jsonData.getMotivo_consulta()+"\",\"estado_cita\":\""+jsonData.getStatus_cita()+
					   "\",\"activ_codigo\":\""+jsonData.getActividadx_codigo()+"\",\"activ_codigo\":\""+jsonData.getZ_account_create()+"\"}");
			System.out.println(e1.getMessage());
			System.out.println(e1.getCause().getLocalizedMessage());
			System.out.println(e1.getLocalizedMessage());
		}
		
		System.out.println("Fin de proceso Ws concebir ***");
		
		return mapReturn;
	}
		
	@PostMapping("/Create")
	public HashMap<String,Object> Creacion_1 ( @RequestBody RecibidorDatas jsonData,@RequestHeader String user,@RequestHeader String password ) throws Exception{
		
		HashMap <String, Object> MapReturn = new HashMap <String, Object>();
		HashMap <String, String> MensajesReturn = new HashMap <String, String>();
		
		if ( user.equalsIgnoreCase("ws_concebirM4G") && password.equalsIgnoreCase("ZohoCrm_75a205e5cd5da8ba6dff47f2b3f85a67d5679f555ad043429ee018359e82c85f") ) {
			//HashMap <String, String> MapReturn_fail = new HashMap <String, String>();
			
			//MapReturn.put("code", "820");
			//MapReturn.put("msg", "Usuario invalido");
			
			//return MapReturn;
		}
		else {
			MapReturn.put("code", "820");
			MapReturn.put("msg", "Usuario invalido");
			
			return MapReturn;
		}
		
		HashMap <String, String> zcrmConfigurations = new HashMap <String, String >();
		zcrmConfigurations.put("minLogLevel", "ALL");
		zcrmConfigurations.put("currentUserEmail","antonio.benavides@serprosa.com.pe");
		zcrmConfigurations.put("client_id","1000.XYS010L81NWVVL8DNXSPI1OU3OKF7C");
		zcrmConfigurations.put("client_secret","d8d032722e2da9ffcd2efada772faf430c9e0e6a87");
		zcrmConfigurations.put("redirect_uri","https://www.m4g.com.pe/response");
		zcrmConfigurations.put("persistence_handler_class","com.zoho.oauth.clientapp.ZohoOAuthFilePersistence");//for database. "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence" for file, user can implement his own persistence and provide the path here
		
		//zcrmConfigurations.put("oauth_tokens_file_path", "/opt/propertiesConcebirCrm/TokenAccesConcebirCRM.properties");
		zcrmConfigurations.put("oauth_tokens_file_path","E:\\Workspace_EclipseFullTrabajo\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
		zcrmConfigurations.put("domainSuffix","com");//optional. Default is com. "cn" and "eu" supported
		zcrmConfigurations.put("accessType","Production");//Production->www(default), Development->developer, Sandbox->sandbox(optional)
		
		
		//https://accounts.zoho.com/oauth/v2/auth?scope=ZohoCRM.users.ALL,ZohoCRM.modules.ALL,Aaaserver.profile.read,
		//ZohoCRM.functions.execute.READ,ZohoCRM.functions.execute.CREATE,ZohoBooks.invoices.READ,ZohoBooks.purchaseorders.READ,
		//ZohoBooks.salesorders.READ,ZohoBooks.bills.READ
		//&client_id=1000.8FJYVHSIQ5YTD7JJQ5A84WQ9597AIH
		//&response_type=code&access_type=offline&redirect_uri=https://m4g.com.pe/response
			
			
			/*
			 * 
			 * CAMPOS SYSMEDICAL	CRM		
Id del paciente	Id de la Oportunidad (paciente)						LISTO
Tipo de documento	Tipo de documento	Enviar las opciones del Sysmedical	"DNI LISTO
+Carnet de Extranjeria
+Pasaporte
+Otros
+Sin Identificar

Pacientes::DNI	DNI													LISTO
Pacientes::Name_Full	Nombres y apellidos del paciente			LISTO	
Pacientes::FechaNacimiento	Fecha de nacimiento						LISTO
Pacientes::Edad	Edad												LISTO
Pacientes::FonoCelular	Celular										LISTO
Pacientes::Email	Correo											LISTO
Pacientes::Referencia	Medio por el que se enteró de la clínica	LISTO	
_FK_Fecha	Fecha/hora de la Cita	"Formato  dd/mm/aaaa hh:mm:ss"	=> HoraInicio	= LISTO
		
		PARA CREACION:
		Si  campos 'Tipo de Citas Sysmedical' es "fertilidad" y tambien que el 'motivo de la consulta' sea "nuevo 1ra consulta virtual" o "nuevo 1ra consulta"
		
		PARA ACTUALIZACION:
		Si campos:
		 'motivo de la consulta' es "nuevo 1ra consulta virtual" campo 'Tipo de Consulta - tiene que actualizarse' a "Virtual" 
		 'motivo de la consulta' es "nuevo 1ra consulta" 'Tipo de Consulta' a "Presencial"
		
		PARA ACTUALIZACION como cita perdida:
		Si Campos:
		 Cuando campo "Estado de la Cita Sysmedical" sea 'Falta con aviso' o 'Falta sin aviso' enviar al CRM para cambiar la 'fase' a "oportunidad perdida"
		 Siempre y cuando el cliente de esta oportunidad solo tenga una cita, de tener mas de 1 solo marcar en el subform y no en la fase
		 
		when el campo account_ (en crm usuario sismedical)cuando sea "dJoya"=> fuente de posible paciente debe ser digital
		y cualquiera de las otras "central telefonica" 

		
		
Profesional::Name_Full	Médicos		tipo busqueda(datos estan alineados)
Sede::Nombre	Sede	tipo busqueda
TipoCita	Tipo de Cita Sysmedical		
MotivoConsulta	Motivo de Cita Sysmedical		
Status Cita::Nombre	Estado de Cita		
ActividadxCodigo::Nombre	ActividadxCódigo		
Id de la Cita	Id de la Cita (subformulario)	
*/
		
			
		ZCRMRestClient.initialize(zcrmConfigurations);
		
		ZohoOAuthClient cli = ZohoOAuthClient.getInstance();
		//String grantToken = "1000.f107ea0fba6b8eb58f8c6be0493468f3.761168cdc0265d57984735edf8c5b3b3";
		//ZohoOAuthTokens tokens = cli.generateAccessToken(grantToken);
		//String accessToken = tokens.getAccessToken();
		//String refreshToken = tokens.getRefreshToken();
		//System.out.println("access token = " + accessToken + " & refresh token = " + refreshToken);
		ZCRMRestClient cliente2 = ZCRMRestClient.getInstance();
		//
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		 LocalDateTime now = LocalDateTime.now();  
		
		System.out.println("WS Concebir, Init procesos path#Create# Hora de Ejecucion: "+dtf.format(now)+"\n"+
						   "Token de acceso: "+ cli.getAccessToken("antonio.benavides@serprosa.com.pe")+"\n"+
						   "Datos recibidos: |"+jsonData+"|");
		//Aqui hacemos el envio a deluge zoho:
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Zoho-oauthtoken "+cli.getAccessToken("antonio.benavides@serprosa.com.pe"));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		
			map.add("action", "create");
			map.add("param_one", "{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
					   "\",\"name_completo\":\""+jsonData.getNombre_completo()+"\",\"num_documento\":\""+jsonData.getNumero_de_documento()+
					   "\",\"edad_paciente\":\""+jsonData.getEdad_paciente()+"\",\"refe_medio_contacto\":\""+jsonData.getReferencia_medio_de_contacto()+
					   "\",\"fecha_horade_cita\":\""+jsonData.getFecha_hora_de_cita()+"\",\"tipo_d_documento\":\""+jsonData.getTipo_d_documento()+
					   "\",\"fecha_nacimiento\":\""+jsonData.getFecha_d_nacimiento()+"\",\"num_celular\":\""+jsonData.getNumero_celular()+
					   "\",\"num_telefono\":\""+jsonData.getNumero_telefono()+"\",\"name_doctor\":\""+jsonData.getName_doctor()+
					   "\",\"name_sede\":\""+jsonData.getName_sede()+"\",\"tipo_cita\":\""+jsonData.getTipo_cita()+
					   "\",\"motivo_consulta\":\""+jsonData.getMotivo_consulta()+"\",\"estado_cita\":\""+jsonData.getStatus_cita()+
					   "\",\"activ_codigo\":\""+jsonData.getActividadx_codigo()+"\",\"activ_codigo\":\""+jsonData.getZ_account_create()+"\"}");

			
			
		HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> ConsultaDeluge = restTemplate.postForEntity("https://www.zohoapis.com/crm/v2/functions/bd_sysmedical_callaccess/actions/execute?auth_type=oauth", request2 , String.class );
		
		//este try maneja la informacion que llego de la funcion llamada:
		try
		{
			String EstadoCallDeluge ="";
			JSONObject json = new JSONObject(ConsultaDeluge.getBody());
			
			//
			System.out.println("Extracion de data deluge zoho: \nDeluge respuesta de envio=> "+json+"\n Fin de procesos\n************");
			//
			EstadoCallDeluge = json.getString("code");
			String Response =  json.getJSONObject("details").get("output")+"";
			
			
			//JSONObject Response_11 = new JSONObject(Response);
			//String sadasdas =  Response_11.getJSONObject("details").get("output")+"";
			
			//System.out.println("===> mensaje deluge return => "+ json);
			//System.out.println("mensaje deluge return => "+ json);
			JSONObject RespuestaDeveloper = new JSONObject(Response);
			//System.out.println("Response: "+Response);
			//System.out.println();
			
			//MensajesReturn.put("msg", RespuestaDeveloper+"");
			if ( EstadoCallDeluge.contains("success") ) {
				//System.out.println("** Fin de mensajes. Exito en Envio, Tratado y Recepcion de DATA **");
				MapReturn.put("response", Response);
			}
			else {
				//System.out.println("Hubo problemas en el llamado respuesta completa:");
				//System.out.println(json);
				//System.out.println("**Fin Respuesta**");
				MapReturn.put("code", "420");
			}
		}
		catch( Exception e1 )
		{
			//System.out.println("Fallo al tratar el json response deluge");
			//System.out.println("dataList: "+"variable de tipó lista");
			//System.out.println("** Fin de muestreo informacion enviada al enviar a Zoho **");
			MapReturn.put("code","450");
			MapReturn.put("msg", "Durante el tratamiento de informacion hubo un problema");
			MapReturn.put("DataEnviada", "{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
					   "\",\"name_completo\":\""+jsonData.getNombre_completo()+"\",\"num_documento\":\""+jsonData.getNumero_de_documento()+
					   "\",\"edad_paciente\":\""+jsonData.getEdad_paciente()+"\",\"refe_medio_contacto\":\""+jsonData.getReferencia_medio_de_contacto()+
					   "\",\"fecha_horade_cita\":\""+jsonData.getFecha_hora_de_cita()+"\",\"tipo_d_documento\":\""+jsonData.getTipo_d_documento()+
					   "\",\"fecha_nacimiento\":\""+jsonData.getFecha_d_nacimiento()+"\",\"num_celular\":\""+jsonData.getNumero_celular()+
					   "\",\"num_telefono\":\""+jsonData.getNumero_telefono()+"\",\"name_doctor\":\""+jsonData.getName_doctor()+
					   "\",\"name_sede\":\""+jsonData.getName_sede()+"\",\"tipo_cita\":\""+jsonData.getTipo_cita()+
					   "\",\"motivo_consulta\":\""+jsonData.getMotivo_consulta()+"\",\"estado_cita\":\""+jsonData.getStatus_cita()+
					   "\",\"activ_codigo\":\""+jsonData.getActividadx_codigo()+"\",\"activ_codigo\":\""+jsonData.getZ_account_create()+"\"}");
		}
		
		/*//para seguir usando esta solucion unix
		String Comprobador = "1672689332000";
		String contadorCaracteres = "";
		System.out.println("Numero de caracteres: "+Comprobador.length());
		long unix_seconds = 1672689332;
		for (int i = 0; i < (Comprobador.length()-3); i++) {
			System.out.println("iterqador tamaño=> "+i);
			contadorCaracteres = contadorCaracteres+Comprobador.charAt(i);
		}
		System.out.println("Unix=> "+contadorCaracteres);
		Long PasearLong = Long.parseLong(contadorCaracteres);  

		   
		   //convert seconds to milliseconds
		   Date date = new Date(PasearLong*1000L); 
		   // format of the date
		   SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		   jdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));
		   String java_date = jdf.format(date);
		   System.out.println("\n"+java_date+"\n");
		*/
		System.out.println("Fin de proceso Ws concebir ***");
		return MapReturn;
	}
	
	
	@PostMapping("/Update/record")
	public HashMap<String, Object> updateRecord(
		@RequestBody RecibidorDatas jsonData, @RequestHeader String user, @RequestHeader String password
	) throws Exception {
		HashMap <String, Object> mapReturn = new HashMap <String, Object>();
		
		if (!(user.equalsIgnoreCase("ws_concebirM4G") && password.equalsIgnoreCase("ZohoCrm_75a205e5cd5da8ba6dff47f2b3f85a67d5679f555ad043429ee018359e82c85f"))) {
			mapReturn.put("code", "820");
			mapReturn.put("msg", "Usuario invalido");
			return mapReturn;
		}
		
		HashMap <String, String> zcrmConfigurations = new HashMap <String, String >();
		zcrmConfigurations.put("minLogLevel", "ALL");
		zcrmConfigurations.put("currentUserEmail","antonio.benavides@serprosa.com.pe");
		zcrmConfigurations.put("client_id","1000.XYS010L81NWVVL8DNXSPI1OU3OKF7C");
		zcrmConfigurations.put("client_secret","d8d032722e2da9ffcd2efada772faf430c9e0e6a87");
		zcrmConfigurations.put("redirect_uri","https://www.m4g.com.pe/response");
		zcrmConfigurations.put("persistence_handler_class","com.zoho.oauth.clientapp.ZohoOAuthFilePersistence");//for database. "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence" for file, user can implement his own persistence and provide the path here
		
		//zcrmConfigurations.put("oauth_tokens_file_path", "/opt/pacifico/ComdataPacificoBulkCrm.properties");
		zcrmConfigurations.put("oauth_tokens_file_path","E:\\Workspace_EclipseFullTrabajo\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
//		zcrmConfigurations.put("oauth_tokens_file_path","C:\\Users\\USER\\Desktop\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
		zcrmConfigurations.put("domainSuffix","com");//optional. Default is com. "cn" and "eu" supported
		zcrmConfigurations.put("accessType","Production");//Production->www(default), Development->developer, Sandbox->sandbox(optional)
		
		ZCRMRestClient.initialize(zcrmConfigurations);
		
		ZohoOAuthClient cli = ZohoOAuthClient.getInstance();
		//String grantToken = "1000.f107ea0fba6b8eb58f8c6be0493468f3.761168cdc0265d57984735edf8c5b3b3";
		//ZohoOAuthTokens tokens = cli.generateAccessToken(grantToken);
		//String accessToken = tokens.getAccessToken();
		//String refreshToken = tokens.getRefreshToken();
		//System.out.println("access token = " + accessToken + " & refresh token = " + refreshToken);
		
		ZCRMRestClient cliente2 = ZCRMRestClient.getInstance();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		System.out.println("WS Concebir, Init procesos path#Create# Hora de Ejecucion: "+dtf.format(now)+"\n"+
						   "Token de acceso: "+ cli.getAccessToken("antonio.benavides@serprosa.com.pe")+"\n"+
						   "Datos recibidos: |"+jsonData+"|");
	
		//
		System.out.println(jsonData.toString());
		System.out.println(jsonData.getId_paciente());
		//
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Zoho-oauthtoken "+cli.getAccessToken("antonio.benavides@serprosa.com.pe"));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("action", "update/record");
		map.add("param_one",
			"{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
			"\",\"name_completo\":"+jsonData.getNombre_completo()+",\"num_documento\":"+jsonData.getNumero_de_documento()+
			",\"edad_paciente\":"+jsonData.getEdad_paciente()+",\"refe_medio_contacto\":"+jsonData.getReferencia_medio_de_contacto()+
			",\"fecha_horade_cita\":"+jsonData.getFecha_hora_de_cita()+",\"tipo_d_documento\":"+jsonData.getTipo_d_documento()+
			",\"fecha_nacimiento\":"+jsonData.getFecha_d_nacimiento()+",\"num_celular\":"+jsonData.getNumero_celular()+
			",\"num_telefono\":"+jsonData.getNumero_telefono()+",\"name_doctor\":"+jsonData.getName_doctor()+
			",\"name_sede\":"+jsonData.getName_sede()+",\"tipo_cita\":"+jsonData.getTipo_cita()+
			",\"motivo_consulta\":"+jsonData.getMotivo_consulta()+",\"estado_cita\":"+jsonData.getStatus_cita()+
			",\"activ_codigo\":"+jsonData.getActividadx_codigo()+",\"z_account_create\":"+jsonData.getZ_account_create()+ "}"
		);
		
		System.out.println("-> " + map.get("param_one"));


		HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> ConsultaDeluge = restTemplate.postForEntity(
			"https://www.zohoapis.com/crm/v2/functions/bd_sysmedical_callaccess/actions/execute?auth_type=oauth",
			request2,
			String.class
		);
		
		try
		{
			String EstadoCallDeluge ="";
			JSONObject json = new JSONObject(ConsultaDeluge.getBody());
			
			//
			System.out.println("Extracion de data deluge zoho: \nDeluge respuesta de envio=> "+json+"\n Fin de procesos\n************");
			//
			
			EstadoCallDeluge = json.getString("code");
			String Response =  json.getJSONObject("details").get("output") + "";
			
			if ( EstadoCallDeluge.contains("success") ) {
				mapReturn.put("response", Response);
			}
			else {
				mapReturn.put("code", "420");
			}
		}
		catch(Exception e1)
		{
			mapReturn.put("code","450");
			mapReturn.put("msg", "Durante el tratamiento de informacion hubo un problema");
			mapReturn.put("DataEnviada", "{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
					   "\",\"name_completo\":\""+jsonData.getNombre_completo()+"\",\"num_documento\":\""+jsonData.getNumero_de_documento()+
					   "\",\"edad_paciente\":\""+jsonData.getEdad_paciente()+"\",\"refe_medio_contacto\":\""+jsonData.getReferencia_medio_de_contacto()+
					   "\",\"fecha_horade_cita\":\""+jsonData.getFecha_hora_de_cita()+"\",\"tipo_d_documento\":\""+jsonData.getTipo_d_documento()+
					   "\",\"fecha_nacimiento\":\""+jsonData.getFecha_d_nacimiento()+"\",\"num_celular\":\""+jsonData.getNumero_celular()+
					   "\",\"num_telefono\":\""+jsonData.getNumero_telefono()+"\",\"name_doctor\":\""+jsonData.getName_doctor()+
					   "\",\"name_sede\":\""+jsonData.getName_sede()+"\",\"tipo_cita\":\""+jsonData.getTipo_cita()+
					   "\",\"motivo_consulta\":\""+jsonData.getMotivo_consulta()+"\",\"estado_cita\":\""+jsonData.getStatus_cita()+
					   "\",\"activ_codigo\":\""+jsonData.getActividadx_codigo()+"\",\"activ_codigo\":\""+jsonData.getZ_account_create()+"\"}");
		}
		
		System.out.println("Fin de proceso Ws concebir ***");
		
		return mapReturn;
	}
	
	@PostMapping("/Update/cita")
	public HashMap<String, Object> updateCita(
		@RequestBody RecibidorDatas jsonData, @RequestHeader String user, @RequestHeader String password
	) throws Exception {
		HashMap <String, Object> mapReturn = new HashMap <String, Object>();
		
		if (!(user.equalsIgnoreCase("ws_concebirM4G") && password.equalsIgnoreCase("ZohoCrm_75a205e5cd5da8ba6dff47f2b3f85a67d5679f555ad043429ee018359e82c85f"))) {
			mapReturn.put("code", "820");
			mapReturn.put("msg", "Usuario invalido");
			return mapReturn;
		}
		
		HashMap <String, String> zcrmConfigurations = new HashMap <String, String >();
		zcrmConfigurations.put("minLogLevel", "ALL");
		zcrmConfigurations.put("currentUserEmail","antonio.benavides@serprosa.com.pe");
		zcrmConfigurations.put("client_id","1000.XYS010L81NWVVL8DNXSPI1OU3OKF7C");
		zcrmConfigurations.put("client_secret","d8d032722e2da9ffcd2efada772faf430c9e0e6a87");
		zcrmConfigurations.put("redirect_uri","https://www.m4g.com.pe/response");
		zcrmConfigurations.put("persistence_handler_class","com.zoho.oauth.clientapp.ZohoOAuthFilePersistence");//for database. "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence" for file, user can implement his own persistence and provide the path here
		
		//zcrmConfigurations.put("oauth_tokens_file_path", "/opt/pacifico/ComdataPacificoBulkCrm.properties");
		zcrmConfigurations.put("oauth_tokens_file_path","E:\\Workspace_EclipseFullTrabajo\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
//		zcrmConfigurations.put("oauth_tokens_file_path","C:\\Users\\USER\\Desktop\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
		zcrmConfigurations.put("domainSuffix","com");//optional. Default is com. "cn" and "eu" supported
		zcrmConfigurations.put("accessType","Production");//Production->www(default), Development->developer, Sandbox->sandbox(optional)
		
		ZCRMRestClient.initialize(zcrmConfigurations);
		
		ZohoOAuthClient cli = ZohoOAuthClient.getInstance();
		//String grantToken = "1000.f107ea0fba6b8eb58f8c6be0493468f3.761168cdc0265d57984735edf8c5b3b3";
		//ZohoOAuthTokens tokens = cli.generateAccessToken(grantToken);
		//String accessToken = tokens.getAccessToken();
		//String refreshToken = tokens.getRefreshToken();
		//System.out.println("access token = " + accessToken + " & refresh token = " + refreshToken);
		
		ZCRMRestClient cliente2 = ZCRMRestClient.getInstance();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		System.out.println("WS Concebir, Init procesos path#Create# Hora de Ejecucion: "+dtf.format(now)+"\n"+
						   "Token de acceso: "+ cli.getAccessToken("antonio.benavides@serprosa.com.pe")+"\n"+
						   "Datos recibidos: |"+jsonData+"|");
		
		//
		System.out.println(jsonData.toString());
		System.out.println(jsonData.getId_paciente());
		//
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Zoho-oauthtoken "+cli.getAccessToken("antonio.benavides@serprosa.com.pe"));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("action", "update/cita");
		map.add("param_one",
			"{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
			"\",\"name_completo\":"+jsonData.getNombre_completo()+",\"num_documento\":"+jsonData.getNumero_de_documento()+
			",\"edad_paciente\":"+jsonData.getEdad_paciente()+",\"refe_medio_contacto\":"+jsonData.getReferencia_medio_de_contacto()+
			",\"fecha_horade_cita\":"+jsonData.getFecha_hora_de_cita()+",\"tipo_d_documento\":"+jsonData.getTipo_d_documento()+
			",\"fecha_nacimiento\":"+jsonData.getFecha_d_nacimiento()+",\"num_celular\":"+jsonData.getNumero_celular()+
			",\"num_telefono\":"+jsonData.getNumero_telefono()+",\"name_doctor\":"+jsonData.getName_doctor()+
			",\"name_sede\":"+jsonData.getName_sede()+",\"tipo_cita\":"+jsonData.getTipo_cita()+
			",\"motivo_consulta\":"+jsonData.getMotivo_consulta()+",\"estado_cita\":"+jsonData.getStatus_cita()+
			",\"activ_codigo\":"+jsonData.getActividadx_codigo()+",\"z_account_create\":"+jsonData.getZ_account_create()+ "}"
		);


		HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> ConsultaDeluge = restTemplate.postForEntity(
			"https://www.zohoapis.com/crm/v2/functions/bd_sysmedical_callaccess/actions/execute?auth_type=oauth",
			request2,
			String.class
		);
		
		try
		{
			String EstadoCallDeluge = "";
			JSONObject json = new JSONObject(ConsultaDeluge.getBody());
			
			//
			System.out.println("Extracion de data deluge zoho: \nDeluge respuesta de envio=> "+json+"\n Fin de procesos\n************");
			//
			
			EstadoCallDeluge = json.getString("code");
			String Response =  json.getJSONObject("details").get("output") + "";
			
			if (EstadoCallDeluge.contains("success")) {
				mapReturn.put("response", Response);
			}
			else {
				mapReturn.put("code", "420");
			}
		}
		catch(Exception e1)
		{
			mapReturn.put("code","450");
			mapReturn.put("msg", "Durante el tratamiento de informacion hubo un problema");
			mapReturn.put("DataEnviada", "{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
					   "\",\"name_completo\":\""+jsonData.getNombre_completo()+"\",\"num_documento\":\""+jsonData.getNumero_de_documento()+
					   "\",\"edad_paciente\":\""+jsonData.getEdad_paciente()+"\",\"refe_medio_contacto\":\""+jsonData.getReferencia_medio_de_contacto()+
					   "\",\"fecha_horade_cita\":\""+jsonData.getFecha_hora_de_cita()+"\",\"tipo_d_documento\":\""+jsonData.getTipo_d_documento()+
					   "\",\"fecha_nacimiento\":\""+jsonData.getFecha_d_nacimiento()+"\",\"num_celular\":\""+jsonData.getNumero_celular()+
					   "\",\"num_telefono\":\""+jsonData.getNumero_telefono()+"\",\"name_doctor\":\""+jsonData.getName_doctor()+
					   "\",\"name_sede\":\""+jsonData.getName_sede()+"\",\"tipo_cita\":\""+jsonData.getTipo_cita()+
					   "\",\"motivo_consulta\":\""+jsonData.getMotivo_consulta()+"\",\"estado_cita\":\""+jsonData.getStatus_cita()+
					   "\",\"activ_codigo\":\""+jsonData.getActividadx_codigo()+"\",\"activ_codigo\":\""+jsonData.getZ_account_create()+"\"}");
		}
		
		System.out.println("Fin de proceso Ws concebir ***");
		
		return mapReturn;
	}
	
	//Actualizacion Modulo Deals
	@PostMapping("/Update")
	public HashMap<String,Object> Actualizacion_1 (
		@RequestBody RecibidorDatas jsonData, @RequestHeader String user, @RequestHeader String password
	) throws Exception {
		
		HashMap <String, Object> MapReturn = new HashMap <String, Object>();
		
		if (!(user.equalsIgnoreCase("ws_concebirM4G") && password.equalsIgnoreCase("ZohoCrm_75a205e5cd5da8ba6dff47f2b3f85a67d5679f555ad043429ee018359e82c85f"))) {
			MapReturn.put("code", "820");
			MapReturn.put("msg", "Usuario invalido");
			return MapReturn;
		}
		
		HashMap <String, String> MensajesReturn = new HashMap <String, String>();
		
		HashMap <String, String> zcrmConfigurations = new HashMap <String, String >();
		zcrmConfigurations.put("minLogLevel", "ALL");
		zcrmConfigurations.put("currentUserEmail","antonio.benavides@serprosa.com.pe");
		zcrmConfigurations.put("client_id","1000.XYS010L81NWVVL8DNXSPI1OU3OKF7C");
		zcrmConfigurations.put("client_secret","d8d032722e2da9ffcd2efada772faf430c9e0e6a87");
		zcrmConfigurations.put("redirect_uri","https://www.m4g.com.pe/response");
		zcrmConfigurations.put("persistence_handler_class","com.zoho.oauth.clientapp.ZohoOAuthFilePersistence");//for database. "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence" for file, user can implement his own persistence and provide the path here
		
		//zcrmConfigurations.put("oauth_tokens_file_path", "/opt/pacifico/ComdataPacificoBulkCrm.properties");
		zcrmConfigurations.put("oauth_tokens_file_path","E:\\Workspace_EclipseFullTrabajo\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
		zcrmConfigurations.put("domainSuffix","com");//optional. Default is com. "cn" and "eu" supported
		zcrmConfigurations.put("accessType","Production");//Production->www(default), Development->developer, Sandbox->sandbox(optional)
		
		
		//https://accounts.zoho.com/oauth/v2/auth?scope=ZohoCRM.users.ALL,ZohoCRM.modules.ALL,Aaaserver.profile.read,
		//ZohoCRM.functions.execute.READ,ZohoCRM.functions.execute.CREATE,ZohoBooks.invoices.READ,ZohoBooks.purchaseorders.READ,
		//ZohoBooks.salesorders.READ,ZohoBooks.bills.READ
		//&client_id=1000.8FJYVHSIQ5YTD7JJQ5A84WQ9597AIH
		//&response_type=code&access_type=offline&redirect_uri=https://m4g.com.pe/response
		
		
		
		ZCRMRestClient.initialize(zcrmConfigurations);
		
		ZohoOAuthClient cli = ZohoOAuthClient.getInstance();
		//String grantToken = "1000.f107ea0fba6b8eb58f8c6be0493468f3.761168cdc0265d57984735edf8c5b3b3";
		//ZohoOAuthTokens tokens = cli.generateAccessToken(grantToken);
		//String accessToken = tokens.getAccessToken();
		//String refreshToken = tokens.getRefreshToken();
		//System.out.println("access token = " + accessToken + " & refresh token = " + refreshToken);
		ZCRMRestClient cliente2 = ZCRMRestClient.getInstance();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		System.out.println("WS Concebir, Init procesos path#Create# Hora de Ejecucion: "+dtf.format(now)+"\n"+
						   "Token de acceso: "+ cli.getAccessToken("antonio.benavides@serprosa.com.pe")+"\n"+
						   "Datos recibidos: |"+jsonData+"|");
	
		//Aqui hacemos el envio a deluge zoho:
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Zoho-oauthtoken "+cli.getAccessToken("antonio.benavides@serprosa.com.pe"));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		System.out.println(jsonData.toString());
		System.out.println(jsonData.getId_paciente());
		map.add("action", "update");
		map.add("param_one", "{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
				   "\",\"name_completo\":\""+jsonData.getNombre_completo()+"\",\"num_documento\":\""+jsonData.getNumero_de_documento()+
				   "\",\"edad_paciente\":\""+jsonData.getEdad_paciente()+"\",\"refe_medio_contacto\":\""+jsonData.getReferencia_medio_de_contacto()+
				   "\",\"fecha_horade_cita\":\""+jsonData.getFecha_hora_de_cita()+"\",\"tipo_d_documento\":\""+jsonData.getTipo_d_documento()+
				   "\",\"fecha_nacimiento\":\""+jsonData.getFecha_d_nacimiento()+"\",\"num_celular\":\""+jsonData.getNumero_celular()+
				   "\",\"num_telefono\":\""+jsonData.getNumero_telefono()+"\",\"name_doctor\":\""+jsonData.getName_doctor()+
				   "\",\"name_sede\":\""+jsonData.getName_sede()+"\",\"tipo_cita\":\""+jsonData.getTipo_cita()+
				   "\",\"motivo_consulta\":\""+jsonData.getMotivo_consulta()+"\",\"estado_cita\":\""+jsonData.getStatus_cita()+
				   "\",\"activ_codigo\":\""+jsonData.getActividadx_codigo()+"\",\"activ_codigo\":\""+jsonData.getZ_account_create()+"\"}");


		HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> ConsultaDeluge = restTemplate.postForEntity("https://www.zohoapis.com/crm/v2/functions/bd_sysmedical_callaccess/actions/execute?auth_type=oauth", request2 , String.class );
		
		//este try maneja la informacion que llego de la funcion llamada:
		try
		{
			String EstadoCallDeluge ="";
			JSONObject json = new JSONObject(ConsultaDeluge.getBody());
			
			//
			System.out.println("Extracion de data deluge zoho: \nDeluge respuesta de envio=> "+json+"\n Fin de procesos\n************");
			//
			EstadoCallDeluge = json.getString("code");
			String Response =  json.getJSONObject("details").get("output")+"";
			
			
			//JSONObject Response_11 = new JSONObject(Response);
			//String sadasdas =  Response_11.getJSONObject("details").get("output")+"";
			
			//System.out.println("===> mensaje deluge return => "+ json);
			//System.out.println("mensaje deluge return => "+ json);
			JSONObject RespuestaDeveloper = new JSONObject(Response);
			//System.out.println("Response: "+Response);
			//System.out.println();
			
			//MensajesReturn.put("msg", RespuestaDeveloper+"");
			if ( EstadoCallDeluge.contains("success") ) {
				//System.out.println("** Fin de mensajes. Exito en Envio, Tratado y Recepcion de DATA **");
				MapReturn.put("response", Response);
			}
			else {
				//System.out.println("Hubo problemas en el llamado respuesta completa:");
				//System.out.println(json);
				//System.out.println("**Fin Respuesta**");
				MapReturn.put("code", "420");
			}
		}
		catch( Exception e1 )
		{
			//System.out.println("Fallo al tratar el json response deluge");
			//System.out.println("dataList: "+"variable de tipó lista");
			//System.out.println("** Fin de muestreo informacion enviada al enviar a Zoho **");
			MapReturn.put("code","450");
			MapReturn.put("msg", "Durante el tratamiento de informacion hubo un problema");
			MapReturn.put("DataEnviada", "{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
					   "\",\"name_completo\":\""+jsonData.getNombre_completo()+"\",\"num_documento\":\""+jsonData.getNumero_de_documento()+
					   "\",\"edad_paciente\":\""+jsonData.getEdad_paciente()+"\",\"refe_medio_contacto\":\""+jsonData.getReferencia_medio_de_contacto()+
					   "\",\"fecha_horade_cita\":\""+jsonData.getFecha_hora_de_cita()+"\",\"tipo_d_documento\":\""+jsonData.getTipo_d_documento()+
					   "\",\"fecha_nacimiento\":\""+jsonData.getFecha_d_nacimiento()+"\",\"num_celular\":\""+jsonData.getNumero_celular()+
					   "\",\"num_telefono\":\""+jsonData.getNumero_telefono()+"\",\"name_doctor\":\""+jsonData.getName_doctor()+
					   "\",\"name_sede\":\""+jsonData.getName_sede()+"\",\"tipo_cita\":\""+jsonData.getTipo_cita()+
					   "\",\"motivo_consulta\":\""+jsonData.getMotivo_consulta()+"\",\"estado_cita\":\""+jsonData.getStatus_cita()+
					   "\",\"activ_codigo\":\""+jsonData.getActividadx_codigo()+"\",\"activ_codigo\":\""+jsonData.getZ_account_create()+"\"}");
		}
		
		/*//para seguir usando esta solucion unix
		String Comprobador = "1672689332000";
		String contadorCaracteres = "";
		System.out.println("Numero de caracteres: "+Comprobador.length());
		long unix_seconds = 1672689332;
		for (int i = 0; i < (Comprobador.length()-3); i++) {
			System.out.println("iterqador tamaño=> "+i);
			contadorCaracteres = contadorCaracteres+Comprobador.charAt(i);
		}
		System.out.println("Unix=> "+contadorCaracteres);
		Long PasearLong = Long.parseLong(contadorCaracteres);  

		   
		   //convert seconds to milliseconds
		   Date date = new Date(PasearLong*1000L); 
		   // format of the date
		   SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		   jdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));
		   String java_date = jdf.format(date);
		   System.out.println("\n"+java_date+"\n");
		*/
		
		
		System.out.println("Fin de proceso Ws concebir ***");
		return MapReturn;
	}
	
	
	//Eliminacion Modulo Deals
	@PostMapping("/Delete")
	public HashMap<String,Object> Eliminar_1 (
		@RequestBody RecibidorDatas jsonData, @RequestHeader String user, @RequestHeader String password
	) throws Exception {
		HashMap <String, Object> MapReturn = new HashMap <String, Object>();
		
		if (!(user.equalsIgnoreCase("ws_concebirM4G") && password.equalsIgnoreCase("ZohoCrm_75a205e5cd5da8ba6dff47f2b3f85a67d5679f555ad043429ee018359e82c85f"))) {
			MapReturn.put("code", "820");
			MapReturn.put("msg", "Usuario invalido");
			return MapReturn;
		}
		
		HashMap <String, String> MensajesReturn = new HashMap <String, String>();
		
		HashMap <String, String> zcrmConfigurations = new HashMap <String, String >();
		zcrmConfigurations.put("minLogLevel", "ALL");
		zcrmConfigurations.put("currentUserEmail","antonio.benavides@serprosa.com.pe");
		zcrmConfigurations.put("client_id","1000.XYS010L81NWVVL8DNXSPI1OU3OKF7C");
		zcrmConfigurations.put("client_secret","d8d032722e2da9ffcd2efada772faf430c9e0e6a87");
		zcrmConfigurations.put("redirect_uri","https://www.m4g.com.pe/response");
		zcrmConfigurations.put("persistence_handler_class","com.zoho.oauth.clientapp.ZohoOAuthFilePersistence");//for database. "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence" for file, user can implement his own persistence and provide the path here
		
		//zcrmConfigurations.put("oauth_tokens_file_path", "/opt/pacifico/ComdataPacificoBulkCrm.properties");
		zcrmConfigurations.put("oauth_tokens_file_path","E:\\Workspace_EclipseFullTrabajo\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
		zcrmConfigurations.put("domainSuffix","com");//optional. Default is com. "cn" and "eu" supported
		zcrmConfigurations.put("accessType","Production");//Production->www(default), Development->developer, Sandbox->sandbox(optional)
		
		//https://accounts.zoho.com/oauth/v2/auth?scope=ZohoCRM.users.ALL,ZohoCRM.modules.ALL,Aaaserver.profile.read,
		//ZohoCRM.functions.execute.READ,ZohoCRM.functions.execute.CREATE,ZohoBooks.invoices.READ,ZohoBooks.purchaseorders.READ,
		//ZohoBooks.salesorders.READ,ZohoBooks.bills.READ
		//&client_id=1000.8FJYVHSIQ5YTD7JJQ5A84WQ9597AIH
		//&response_type=code&access_type=offline&redirect_uri=https://m4g.com.pe/response
		
		
		
		ZCRMRestClient.initialize(zcrmConfigurations);
		
		ZohoOAuthClient cli = ZohoOAuthClient.getInstance();
		//String grantToken = "1000.f107ea0fba6b8eb58f8c6be0493468f3.761168cdc0265d57984735edf8c5b3b3";
		//ZohoOAuthTokens tokens = cli.generateAccessToken(grantToken);
		//String accessToken = tokens.getAccessToken();
		//String refreshToken = tokens.getRefreshToken();
		//System.out.println("access token = " + accessToken + " & refresh token = " + refreshToken);
		ZCRMRestClient cliente2 = ZCRMRestClient.getInstance();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		System.out.println("WS Concebir, Init procesos path#Create# Hora de Ejecucion: "+dtf.format(now)+"\n"+
						   "Token de acceso: "+ cli.getAccessToken("antonio.benavides@serprosa.com.pe")+"\n"+
						   "Datos recibidos: |"+jsonData+"|");
		//Aqui hacemos el envio a deluge zoho:
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Zoho-oauthtoken "+cli.getAccessToken("antonio.benavides@serprosa.com.pe"));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		//System.out.println(jsonData.toString());
		//System.out.println(jsonData.getId_paciente());
		map.add("action", "delete");
		map.add("param_one", "{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
				   "\",\"name_completo\":\""+jsonData.getNombre_completo()+"\",\"num_documento\":\""+jsonData.getNumero_de_documento()+
				   "\",\"edad_paciente\":\""+jsonData.getEdad_paciente()+"\",\"refe_medio_contacto\":\""+jsonData.getReferencia_medio_de_contacto()+
				   "\",\"fecha_horade_cita\":\""+jsonData.getFecha_hora_de_cita()+"\",\"tipo_d_documento\":\""+jsonData.getTipo_d_documento()+
				   "\",\"fecha_nacimiento\":\""+jsonData.getFecha_d_nacimiento()+"\",\"num_celular\":\""+jsonData.getNumero_celular()+
				   "\",\"num_telefono\":\""+jsonData.getNumero_telefono()+"\",\"name_doctor\":\""+jsonData.getName_doctor()+
				   "\",\"name_sede\":\""+jsonData.getName_sede()+"\",\"tipo_cita\":\""+jsonData.getTipo_cita()+
				   "\",\"motivo_consulta\":\""+jsonData.getMotivo_consulta()+"\",\"estado_cita\":\""+jsonData.getStatus_cita()+
				   "\",\"activ_codigo\":\""+jsonData.getActividadx_codigo()+"\",\"activ_codigo\":\""+jsonData.getZ_account_create()+"\"}");
	
	
		HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> ConsultaDeluge = restTemplate.postForEntity("https://www.zohoapis.com/crm/v2/functions/bd_sysmedical_callaccess/actions/execute?auth_type=oauth", request2 , String.class );
		
		//este try maneja la informacion que llego de la funcion llamada:
		try
		{
			String EstadoCallDeluge ="";
			JSONObject json = new JSONObject(ConsultaDeluge.getBody());
			
			//
			System.out.println("Extracion de data deluge zoho: \nDeluge respuesta de envio=> "+json+"\n Fin de procesos\n************");
			//
			EstadoCallDeluge = json.getString("code");
			String Response =  json.getJSONObject("details").get("output")+"";
			
			
			//JSONObject Response_11 = new JSONObject(Response);
			//String sadasdas =  Response_11.getJSONObject("details").get("output")+"";
			
			//System.out.println("===> mensaje deluge return => "+ json);
			//System.out.println("mensaje deluge return => "+ json);
			JSONObject RespuestaDeveloper = new JSONObject(Response);
			//System.out.println("Response: "+Response);
			//System.out.println();
			
			//MensajesReturn.put("msg", RespuestaDeveloper+"");
			if ( EstadoCallDeluge.contains("success") ) {
				//System.out.println("** Fin de mensajes. Exito en Envio, Tratado y Recepcion de DATA **");
				MapReturn.put("response", Response);
			}
			else {
				//System.out.println("Hubo problemas en el llamado respuesta completa:");
				//System.out.println(json);
				//System.out.println("**Fin Respuesta**");
				MapReturn.put("code", "420");
			}
		}
		catch( Exception e1 )
		{
			//System.out.println("Fallo al tratar el json response deluge");
			//System.out.println("dataList: "+"variable de tipó lista");
			//System.out.println("** Fin de muestreo informacion enviada al enviar a Zoho **");
			MapReturn.put("code","450");
			MapReturn.put("msg", "Durante el tratamiento de informacion hubo un problema");
			MapReturn.put("DataEnviada", "{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
					   "\",\"name_completo\":\""+jsonData.getNombre_completo()+"\",\"num_documento\":\""+jsonData.getNumero_de_documento()+
					   "\",\"edad_paciente\":\""+jsonData.getEdad_paciente()+"\",\"refe_medio_contacto\":\""+jsonData.getReferencia_medio_de_contacto()+
					   "\",\"fecha_horade_cita\":\""+jsonData.getFecha_hora_de_cita()+"\",\"tipo_d_documento\":\""+jsonData.getTipo_d_documento()+
					   "\",\"fecha_nacimiento\":\""+jsonData.getFecha_d_nacimiento()+"\",\"num_celular\":\""+jsonData.getNumero_celular()+
					   "\",\"num_telefono\":\""+jsonData.getNumero_telefono()+"\",\"name_doctor\":\""+jsonData.getName_doctor()+
					   "\",\"name_sede\":\""+jsonData.getName_sede()+"\",\"tipo_cita\":\""+jsonData.getTipo_cita()+
					   "\",\"motivo_consulta\":\""+jsonData.getMotivo_consulta()+"\",\"estado_cita\":\""+jsonData.getStatus_cita()+
					   "\",\"activ_codigo\":\""+jsonData.getActividadx_codigo()+"\",\"activ_codigo\":\""+jsonData.getZ_account_create()+"\"}");
		}
		
		
		/*//para seguir usando esta solucion unix
		String Comprobador = "1672689332000";
		String contadorCaracteres = "";
		System.out.println("Numero de caracteres: "+Comprobador.length());
		long unix_seconds = 1672689332;
		for (int i = 0; i < (Comprobador.length()-3); i++) {
			System.out.println("iterqador tamaño=> "+i);
			contadorCaracteres = contadorCaracteres+Comprobador.charAt(i);
		}
		System.out.println("Unix=> "+contadorCaracteres);
		Long PasearLong = Long.parseLong(contadorCaracteres);  
	
		   
		   //convert seconds to milliseconds
		   Date date = new Date(PasearLong*1000L); 
		   // format of the date
		   SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		   jdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));
		   String java_date = jdf.format(date);
		   System.out.println("\n"+java_date+"\n");
		*/
		
		
		System.out.println("Fin de proceso Ws concebir ***");
		return MapReturn;
	}
	
	@PostMapping("/Delete/record")
	public HashMap<String, Object> deleteRecord (
		@RequestBody RecibidorDatas jsonData, @RequestHeader String user, @RequestHeader String password
	) throws Exception {
		HashMap <String, Object> mapReturn = new HashMap <String, Object>();
		
		if (!(user.equalsIgnoreCase("ws_concebirM4G") && password.equalsIgnoreCase("ZohoCrm_75a205e5cd5da8ba6dff47f2b3f85a67d5679f555ad043429ee018359e82c85f"))) {
			mapReturn.put("code", "820");
			mapReturn.put("msg", "Usuario invalido");
			return mapReturn;
		}
		
		HashMap <String, String> zcrmConfigurations = new HashMap <String, String >();
		zcrmConfigurations.put("minLogLevel", "ALL");
		zcrmConfigurations.put("currentUserEmail","antonio.benavides@serprosa.com.pe");
		zcrmConfigurations.put("client_id","1000.XYS010L81NWVVL8DNXSPI1OU3OKF7C");
		zcrmConfigurations.put("client_secret","d8d032722e2da9ffcd2efada772faf430c9e0e6a87");
		zcrmConfigurations.put("redirect_uri","https://www.m4g.com.pe/response");
		zcrmConfigurations.put("persistence_handler_class","com.zoho.oauth.clientapp.ZohoOAuthFilePersistence");//for database. "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence" for file, user can implement his own persistence and provide the path here
		
		//zcrmConfigurations.put("oauth_tokens_file_path", "/opt/pacifico/ComdataPacificoBulkCrm.properties");
		zcrmConfigurations.put("oauth_tokens_file_path","E:\\Workspace_EclipseFullTrabajo\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
//		zcrmConfigurations.put("oauth_tokens_file_path","C:\\Users\\USER\\Desktop\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
		zcrmConfigurations.put("domainSuffix","com");//optional. Default is com. "cn" and "eu" supported
		zcrmConfigurations.put("accessType","Production");//Production->www(default), Development->developer, Sandbox->sandbox(optional)
		
		//https://accounts.zoho.com/oauth/v2/auth?scope=ZohoCRM.users.ALL,ZohoCRM.modules.ALL,Aaaserver.profile.read,
		//ZohoCRM.functions.execute.READ,ZohoCRM.functions.execute.CREATE,ZohoBooks.invoices.READ,ZohoBooks.purchaseorders.READ,
		//ZohoBooks.salesorders.READ,ZohoBooks.bills.READ
		//&client_id=1000.8FJYVHSIQ5YTD7JJQ5A84WQ9597AIH
		//&response_type=code&access_type=offline&redirect_uri=https://m4g.com.pe/response
		
		
		
		ZCRMRestClient.initialize(zcrmConfigurations);
		
		ZohoOAuthClient cli = ZohoOAuthClient.getInstance();
		//String grantToken = "1000.f107ea0fba6b8eb58f8c6be0493468f3.761168cdc0265d57984735edf8c5b3b3";
		//ZohoOAuthTokens tokens = cli.generateAccessToken(grantToken);
		//String accessToken = tokens.getAccessToken();
		//String refreshToken = tokens.getRefreshToken();
		//System.out.println("access token = " + accessToken + " & refresh token = " + refreshToken);
		ZCRMRestClient cliente2 = ZCRMRestClient.getInstance();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		System.out.println("WS Concebir, Init procesos path#Create# Hora de Ejecucion: "+dtf.format(now)+"\n"+
						   "Token de acceso: "+ cli.getAccessToken("antonio.benavides@serprosa.com.pe")+"\n"+
						   "Datos recibidos: |"+jsonData+"|");
		
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Zoho-oauthtoken " + cli.getAccessToken("antonio.benavides@serprosa.com.pe"));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("action", "delete/record");
		map.add("param_one",
			"{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
			"\",\"name_completo\":"+jsonData.getNombre_completo()+",\"num_documento\":"+jsonData.getNumero_de_documento()+
			",\"edad_paciente\":"+jsonData.getEdad_paciente()+",\"refe_medio_contacto\":"+jsonData.getReferencia_medio_de_contacto()+
			",\"fecha_horade_cita\":"+jsonData.getFecha_hora_de_cita()+",\"tipo_d_documento\":"+jsonData.getTipo_d_documento()+
			",\"fecha_nacimiento\":"+jsonData.getFecha_d_nacimiento()+",\"num_celular\":"+jsonData.getNumero_celular()+
			",\"num_telefono\":"+jsonData.getNumero_telefono()+",\"name_doctor\":"+jsonData.getName_doctor()+
			",\"name_sede\":"+jsonData.getName_sede()+",\"tipo_cita\":"+jsonData.getTipo_cita()+
			",\"motivo_consulta\":"+jsonData.getMotivo_consulta()+",\"estado_cita\":"+jsonData.getStatus_cita()+
			",\"activ_codigo\":"+jsonData.getActividadx_codigo()+",\"z_account_create\":"+jsonData.getZ_account_create()+ "}"
		);
	
	
		HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> ConsultaDeluge = restTemplate.postForEntity(
			"https://www.zohoapis.com/crm/v2/functions/bd_sysmedical_callaccess/actions/execute?auth_type=oauth",
			request2,
			String.class
		);
		
		try
		{
			String EstadoCallDeluge = "";
			JSONObject json = new JSONObject(ConsultaDeluge.getBody());
			
			//
			System.out.println("Extracion de data deluge zoho: \nDeluge respuesta de envio=> "+json+"\n Fin de procesos\n************");
			//
			EstadoCallDeluge = json.getString("code");
			String Response = json.getJSONObject("details").get("output") + "";
			
			if (EstadoCallDeluge.contains("success")) {
				mapReturn.put("response", Response);
			}
			else {
				mapReturn.put("code", "420");
			}
		}
		catch(Exception e1)
		{
			mapReturn.put("code","450");
			mapReturn.put("msg", "Durante el tratamiento de informacion hubo un problema");
			mapReturn.put("DataEnviada", "{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
					   "\",\"name_completo\":\""+jsonData.getNombre_completo()+"\",\"num_documento\":\""+jsonData.getNumero_de_documento()+
					   "\",\"edad_paciente\":\""+jsonData.getEdad_paciente()+"\",\"refe_medio_contacto\":\""+jsonData.getReferencia_medio_de_contacto()+
					   "\",\"fecha_horade_cita\":\""+jsonData.getFecha_hora_de_cita()+"\",\"tipo_d_documento\":\""+jsonData.getTipo_d_documento()+
					   "\",\"fecha_nacimiento\":\""+jsonData.getFecha_d_nacimiento()+"\",\"num_celular\":\""+jsonData.getNumero_celular()+
					   "\",\"num_telefono\":\""+jsonData.getNumero_telefono()+"\",\"name_doctor\":\""+jsonData.getName_doctor()+
					   "\",\"name_sede\":\""+jsonData.getName_sede()+"\",\"tipo_cita\":\""+jsonData.getTipo_cita()+
					   "\",\"motivo_consulta\":\""+jsonData.getMotivo_consulta()+"\",\"estado_cita\":\""+jsonData.getStatus_cita()+
					   "\",\"activ_codigo\":\""+jsonData.getActividadx_codigo()+"\",\"activ_codigo\":\""+jsonData.getZ_account_create()+"\"}");
		}
	
		System.out.println("Fin de proceso Ws concebir ***");
		
		return mapReturn;
	}
	@PostMapping("/Delete/cita")
	public HashMap<String,Object> deleteCita (
		@RequestBody RecibidorDatas jsonData, @RequestHeader String user, @RequestHeader String password
	) throws Exception {
		HashMap <String, Object> mapReturn = new HashMap <String, Object>();
		
		if (!(user.equalsIgnoreCase("ws_concebirM4G") && password.equalsIgnoreCase("ZohoCrm_75a205e5cd5da8ba6dff47f2b3f85a67d5679f555ad043429ee018359e82c85f"))) {
			mapReturn.put("code", "820");
			mapReturn.put("msg", "Usuario invalido");
			return mapReturn;
		}
		
		HashMap <String, String> zcrmConfigurations = new HashMap <String, String >();
		zcrmConfigurations.put("minLogLevel", "ALL");
		zcrmConfigurations.put("currentUserEmail","antonio.benavides@serprosa.com.pe");
		zcrmConfigurations.put("client_id","1000.XYS010L81NWVVL8DNXSPI1OU3OKF7C");
		zcrmConfigurations.put("client_secret","d8d032722e2da9ffcd2efada772faf430c9e0e6a87");
		zcrmConfigurations.put("redirect_uri","https://www.m4g.com.pe/response");
		zcrmConfigurations.put("persistence_handler_class","com.zoho.oauth.clientapp.ZohoOAuthFilePersistence");//for database. "com.zoho.oauth.clientapp.ZohoOAuthFilePersistence" for file, user can implement his own persistence and provide the path here
		
		//zcrmConfigurations.put("oauth_tokens_file_path", "/opt/pacifico/ComdataPacificoBulkCrm.properties");
		zcrmConfigurations.put("oauth_tokens_file_path","E:\\Workspace_EclipseFullTrabajo\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
//		zcrmConfigurations.put("oauth_tokens_file_path","C:\\Users\\USER\\Desktop\\WSconcebir\\src\\main\\java\\M4G\\WSconcebir\\path_to_tokens\\TokenAccesConcebirCRM.properties");
		zcrmConfigurations.put("domainSuffix","com");//optional. Default is com. "cn" and "eu" supported
		zcrmConfigurations.put("accessType","Production");//Production->www(default), Development->developer, Sandbox->sandbox(optional)
		
		//https://accounts.zoho.com/oauth/v2/auth?scope=ZohoCRM.users.ALL,ZohoCRM.modules.ALL,Aaaserver.profile.read,
		//ZohoCRM.functions.execute.READ,ZohoCRM.functions.execute.CREATE,ZohoBooks.invoices.READ,ZohoBooks.purchaseorders.READ,
		//ZohoBooks.salesorders.READ,ZohoBooks.bills.READ
		//&client_id=1000.8FJYVHSIQ5YTD7JJQ5A84WQ9597AIH
		//&response_type=code&access_type=offline&redirect_uri=https://m4g.com.pe/response
		
		
		
		ZCRMRestClient.initialize(zcrmConfigurations);
		
		ZohoOAuthClient cli = ZohoOAuthClient.getInstance();
		//String grantToken = "1000.f107ea0fba6b8eb58f8c6be0493468f3.761168cdc0265d57984735edf8c5b3b3";
		//ZohoOAuthTokens tokens = cli.generateAccessToken(grantToken);
		//String accessToken = tokens.getAccessToken();
		//String refreshToken = tokens.getRefreshToken();
		//System.out.println("access token = " + accessToken + " & refresh token = " + refreshToken);
		ZCRMRestClient cliente2 = ZCRMRestClient.getInstance();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		System.out.println("WS Concebir, Init procesos path#Create# Hora de Ejecucion: "+dtf.format(now)+"\n"+
						   "Token de acceso: "+ cli.getAccessToken("antonio.benavides@serprosa.com.pe")+"\n"+
						   "Datos recibidos: |"+jsonData+"|");
		
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Zoho-oauthtoken " + cli.getAccessToken("antonio.benavides@serprosa.com.pe"));
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("action", "delete/cita");
		map.add("param_one",
			"{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
			"\",\"name_completo\":"+jsonData.getNombre_completo()+",\"num_documento\":"+jsonData.getNumero_de_documento()+
			",\"edad_paciente\":"+jsonData.getEdad_paciente()+",\"refe_medio_contacto\":"+jsonData.getReferencia_medio_de_contacto()+
			",\"fecha_horade_cita\":"+jsonData.getFecha_hora_de_cita()+",\"tipo_d_documento\":"+jsonData.getTipo_d_documento()+
			",\"fecha_nacimiento\":"+jsonData.getFecha_d_nacimiento()+",\"num_celular\":"+jsonData.getNumero_celular()+
			",\"num_telefono\":"+jsonData.getNumero_telefono()+",\"name_doctor\":"+jsonData.getName_doctor()+
			",\"name_sede\":"+jsonData.getName_sede()+",\"tipo_cita\":"+jsonData.getTipo_cita()+
			",\"motivo_consulta\":"+jsonData.getMotivo_consulta()+",\"estado_cita\":"+jsonData.getStatus_cita()+
			",\"activ_codigo\":"+jsonData.getActividadx_codigo()+",\"z_account_create\":"+jsonData.getZ_account_create()+ "}"
		);
		
		HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> ConsultaDeluge = restTemplate.postForEntity(
			"https://www.zohoapis.com/crm/v2/functions/bd_sysmedical_callaccess/actions/execute?auth_type=oauth",
			request2,
			String.class
		);
		
		try
		{
			String EstadoCallDeluge = "";
			JSONObject json = new JSONObject(ConsultaDeluge.getBody());
			
			//
			System.out.println("Extracion de data deluge zoho: \nDeluge respuesta de envio=> "+json+"\n Fin de procesos\n************");
			//
			EstadoCallDeluge = json.getString("code");
			String Response = json.getJSONObject("details").get("output") + "";
			
			if (EstadoCallDeluge.contains("success")) {
				mapReturn.put("response", Response);
			}
			else {
				mapReturn.put("code", "420");
			}
		}
		catch(Exception e1)
		{
			mapReturn.put("code","450");
			mapReturn.put("msg", "Durante el tratamiento de informacion hubo un problema");
			mapReturn.put("DataEnviada", "{\"id_paciente\":\""+jsonData.getId_paciente()+"\",\"id_cita\":\""+jsonData.getId_cita()+
					   "\",\"name_completo\":\""+jsonData.getNombre_completo()+"\",\"num_documento\":\""+jsonData.getNumero_de_documento()+
					   "\",\"edad_paciente\":\""+jsonData.getEdad_paciente()+"\",\"refe_medio_contacto\":\""+jsonData.getReferencia_medio_de_contacto()+
					   "\",\"fecha_horade_cita\":\""+jsonData.getFecha_hora_de_cita()+"\",\"tipo_d_documento\":\""+jsonData.getTipo_d_documento()+
					   "\",\"fecha_nacimiento\":\""+jsonData.getFecha_d_nacimiento()+"\",\"num_celular\":\""+jsonData.getNumero_celular()+
					   "\",\"num_telefono\":\""+jsonData.getNumero_telefono()+"\",\"name_doctor\":\""+jsonData.getName_doctor()+
					   "\",\"name_sede\":\""+jsonData.getName_sede()+"\",\"tipo_cita\":\""+jsonData.getTipo_cita()+
					   "\",\"motivo_consulta\":\""+jsonData.getMotivo_consulta()+"\",\"estado_cita\":\""+jsonData.getStatus_cita()+
					   "\",\"activ_codigo\":\""+jsonData.getActividadx_codigo()+"\",\"activ_codigo\":\""+jsonData.getZ_account_create()+"\"}");
		}
	
		System.out.println("Fin de proceso Ws concebir ***");
		
		return mapReturn;
	}
}
