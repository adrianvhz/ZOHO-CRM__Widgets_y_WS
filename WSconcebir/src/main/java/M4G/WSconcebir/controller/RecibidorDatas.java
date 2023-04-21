package M4G.WSconcebir.controller;

public class RecibidorDatas {
	private String id_paciente;
	private String id_cita;
	private String accion_limita;
	private String nombre_completo;
	private String numero_de_documento;
	private String edad_paciente;
	private String referencia_medio_de_contacto;
	private String fecha_hora_de_cita;
	private String tipo_d_documento;
	private String fecha_d_nacimiento;
	private String numero_celular;
	private String numero_telefono;
	private String name_doctor;
	private String name_sede;
	private String tipo_cita;
	private String motivo_consulta;
	private String status_cita;
	private String actividadx_codigo;
	private String z_account_create;
	
	//si se llegasen a agregar mas, desde aqui empiezan los segundos datos
	public RecibidorDatas(String id_paciente, String id_cita, String accion_limita, String nombre_completo,
			String numero_de_documento, String edad_paciente, String referencia_medio_de_contacto,
			String fecha_hora_de_cita, String tipo_d_documento, String fecha_d_nacimiento, String numero_celular,
			String numero_telefono, String name_doctor, String name_sede, String tipo_cita, String motivo_consulta,
			String status_cita, String actividadx_codigo, String z_account_create) {
		super();
		this.id_paciente = id_paciente;
		this.id_cita = id_cita;
		this.accion_limita = accion_limita == null ? null : "\"" + accion_limita + "\"";
		this.nombre_completo = nombre_completo == null ? null : "\"" + nombre_completo + "\"";
		this.numero_de_documento = numero_de_documento == null ? null : "\"" + numero_de_documento + "\"";
		this.edad_paciente = edad_paciente == null ? null : "\"" + edad_paciente + "\"";
		this.referencia_medio_de_contacto = referencia_medio_de_contacto == null ? null : "\"" + referencia_medio_de_contacto + "\"";
		this.fecha_hora_de_cita = fecha_hora_de_cita == null ? null : "\"" + fecha_hora_de_cita + "\"";
		this.tipo_d_documento = tipo_d_documento == null ? null : "\"" + tipo_d_documento + "\"";
		this.fecha_d_nacimiento = fecha_d_nacimiento == null ? null : "\"" + fecha_d_nacimiento + "\"";
		this.numero_celular = numero_celular == null ? null : "\"" + numero_celular + "\"";
		this.numero_telefono = numero_telefono == null ? null : "\"" + numero_telefono + "\"";
		this.name_doctor = name_doctor == null ? null : "\"" + name_doctor + "\"";
		this.name_sede = name_sede == null ? null : "\"" + name_sede + "\"";
		this.tipo_cita = tipo_cita == null ? null : "\"" + tipo_cita + "\"";
		this.motivo_consulta = motivo_consulta == null ? null : "\"" + motivo_consulta + "\"";
		this.status_cita = status_cita == null ? null : "\"" + status_cita + "\"";
		this.actividadx_codigo = actividadx_codigo == null ? null : "\"" + actividadx_codigo + "\"";
		this.z_account_create = z_account_create == null ? null : "\"" + z_account_create + "\"";
	}

	public String getId_paciente() {
		return id_paciente;
	}

	public void setId_paciente(String id_paciente) {
		this.id_paciente = id_paciente;
	}

	public String getId_cita() {
		return id_cita;
	}

	public void setId_cita(String id_cita) {
		this.id_cita = id_cita;
	}

	public String getAccion_limita() {
		return accion_limita;
	}

	public void setAccion_limita(String accion_limita) {
		this.accion_limita = accion_limita;
	}

	public String getNombre_completo() {
		return nombre_completo;
	}

	public void setNombre_completo(String nombre_completo) {
		this.nombre_completo = nombre_completo;
	}

	public String getNumero_de_documento() {
		return numero_de_documento;
	}

	public void setNumero_de_documento(String numero_de_documento) {
		this.numero_de_documento = numero_de_documento;
	}

	public String getEdad_paciente() {
		return edad_paciente;
	}

	public void setEdad_paciente(String edad_paciente) {
		this.edad_paciente = edad_paciente;
	}

	public String getReferencia_medio_de_contacto() {
		return referencia_medio_de_contacto;
	}

	public void setReferencia_medio_de_contacto(String referencia_medio_de_contacto) {
		this.referencia_medio_de_contacto = referencia_medio_de_contacto;
	}

	public String getFecha_hora_de_cita() {
		return fecha_hora_de_cita;
	}

	public void setFecha_hora_de_cita(String fecha_hora_de_cita) {
		this.fecha_hora_de_cita = fecha_hora_de_cita;
	}

	public String getTipo_d_documento() {
		return tipo_d_documento;
	}

	public void setTipo_d_documento(String tipo_d_documento) {
		this.tipo_d_documento = tipo_d_documento;
	}

	public String getFecha_d_nacimiento() {
		return fecha_d_nacimiento;
	}

	public void setFecha_d_nacimiento(String fecha_d_nacimiento) {
		this.fecha_d_nacimiento = fecha_d_nacimiento;
	}

	public String getNumero_celular() {
		return numero_celular;
	}

	public void setNumero_celular(String numero_celular) {
		this.numero_celular = numero_celular;
	}

	public String getNumero_telefono() {
		return numero_telefono;
	}

	public void setNumero_telefono(String numero_telefono) {
		this.numero_telefono = numero_telefono;
	}

	public String getName_doctor() {
		return name_doctor;
	}

	public void setName_doctor(String name_doctor) {
		this.name_doctor = name_doctor;
	}

	public String getName_sede() {
		return name_sede;
	}

	public void setName_sede(String name_sede) {
		this.name_sede = name_sede;
	}

	public String getTipo_cita() {
		return tipo_cita;
	}

	public void setTipo_cita(String tipo_cita) {
		this.tipo_cita = tipo_cita;
	}

	public String getMotivo_consulta() {
		return motivo_consulta;
	}

	public void setMotivo_consulta(String motivo_consulta) {
		this.motivo_consulta = motivo_consulta;
	}

	public String getStatus_cita() {
		return status_cita;
	}

	public void setStatus_cita(String status_cita) {
		this.status_cita = status_cita;
	}

	public String getActividadx_codigo() {
		return actividadx_codigo;
	}

	public void setActividadx_codigo(String actividadx_codigo) {
		this.actividadx_codigo = actividadx_codigo;
	}

	public String getZ_account_create() {
		return z_account_create;
	}

	public void setZ_account_create(String z_account_create) {
		this.z_account_create = z_account_create;
	}

	@Override
	public String toString() {
		return "RecibidorDatas [id_paciente=" + id_paciente + ", id_cita=" + id_cita + ", accion_limita="
				+ accion_limita + ", nombre_completo=" + nombre_completo + ", numero_de_documento="
				+ numero_de_documento + ", edad_paciente=" + edad_paciente + ", referencia_medio_de_contacto="
				+ referencia_medio_de_contacto + ", fecha_hora_de_cita=" + fecha_hora_de_cita + ", tipo_d_documento="
				+ tipo_d_documento + ", fecha_d_nacimiento=" + fecha_d_nacimiento + ", numero_celular=" + numero_celular
				+ ", numero_telefono=" + numero_telefono + ", name_doctor=" + name_doctor + ", name_sede=" + name_sede
				+ ", tipo_cita=" + tipo_cita + ", motivo_consulta=" + motivo_consulta + ", status_cita=" + status_cita
				+ ", actividadx_codigo=" + actividadx_codigo + ", z_account_create=" + z_account_create + "]";
	}
	
	
	
	
	
}
