let nro_partidas;
let cantidadSelectedPartidas = 0;

let id = null;

// toast boostrap
const toastLiveExample = document.getElementById("liveToast");

// buttons
const backBtn = document.getElementById("back-btn");
const generarBtn = document.getElementById("generar-btn");

// inputs
const inputDNI = document.getElementById("DNI");
const inputCodigo = document.getElementById("codigo");
const dataInput = document.getElementById("data");
const importeTotalInput = document.getElementById("importe-total");

const cbPartidas = [];
const valueImportes = [];

ZOHO.embeddedApp.on("PageLoad", async function(ctx) {
	backBtn.onclick = handleClose;

	id = typeof ctx.EntityId === "string"
		? ctx.EntityId
		: ctx.EntityId[0]

	// This oportunidad record
	const oportunidadRecord = await ZOHO.CRM.API.getRecord({
		Entity: "Deals",
		RecordID: id
	});

	/* Contacto de oportunidad */
	const contactRecord = await ZOHO.CRM.API.getRecord({
		Entity: "Contacts",
		RecordID: oportunidadRecord.data[0].Contact_Name.id
	});

	/* CODIGO SAP*/
	const cotizaciones = await ZOHO.CRM.API.getRelatedRecords({
		Entity:"Deals",
		RecordID:id,
		RelatedList:"Oportunidad"
	});

	if (cotizaciones.status === 204) {
		alert("Error: No se encontró ninguna cotización");
		dataInput.innerHTML = "";
		return;
	}

	const cotizacionRecord = await ZOHO.CRM.API.getRecord({
		Entity: "Proformas",
		RecordID: cotizaciones.data.find(el => el["Estado"] === "Aprobado").id
	});

	const productoRecord = await ZOHO.CRM.API.getRecord({
		Entity: "Products",
		RecordID: cotizacionRecord.data[0].Cotizaci_n[0].N_mero.id
	});

	// let nroDocumento = oportunidadRecord.data[0].Numero_de_Documento;
	const nroDocumento = contactRecord.data[0].N_mero_de_Documento;
	const titular = oportunidadRecord.data[0].Contact_Name.id; // el nombre del campo que es `Search` se llena con el id del registro correspondiente
	const nombre_producto = productoRecord.data[0].C_digo_SAP1; //codigoSAP del producto
	
	inputDNI.value = nroDocumento;
	inputCodigo.value= nombre_producto;

	const partidasSAP = await getPartidasSAP(nroDocumento);
	mostrarData(partidasSAP.dataRet);
	
	generarBtn.onclick = () => crearCash(partidasSAP.dataRet, titular);
});

ZOHO.embeddedApp.init().then(() => {
	ZOHO.CRM.UI.Resize({
		height: "1500",
		width: "1500"
	});
});

// exec fn API CALL
const getPartidasSAP = async (nroDocumento) => {
	const reqData = {
		"arguments": JSON.stringify({
			campo: "PART-001",
			tipo_documento: "01",
			num_documento: nroDocumento
		})
	}
	
	const response = await ZOHO.CRM.FUNCTIONS.execute("fn_test_generar_partidas", reqData);
	const result = JSON.parse(response.details.output);
	
	return result;
	
	// LOCAL / TEST
	// fetch("http://localhost:8000/api/v1/projects/test/dataRet").then(resp => resp.json()).then(res => {
	// 	mostrarData(res.dataRet);
	// });
}

const mostrarData = (dataRet) => {
	nro_partidas = dataRet.length / 13;

	let data = "";
	for (let i = 0; i < nro_partidas; i++) {
		data += `
			<tr class="text-center">
				<td>${dataRet[i * 13].campo.slice(5)}</td>
				<td>${dataRet[0 + (i * 13)].valor}</td>
				<td>${dataRet[1 + (i * 13)].valor}</td>
				<td>${dataRet[2 + (i * 13)].valor}</td>
				<td>${dataRet[3 + (i * 13)].valor}</td>
				<td>${dataRet[4 + (i * 13)].valor}</td>
				<td id="importe-${i}">${parseFloat(dataRet[5 + (i * 13)].valor.replaceAll(",", ""))}</td>
				<td>${dataRet[6 + (i * 13)].valor}</td>
				<td>${dataRet[7 + (i * 13)].valor}</td>
				<td>${dataRet[8 + (i * 13)].valor}</td>
				<td>${dataRet[9 + (i * 13)].valor}</td>
				<td>${dataRet[10 + (i * 13)].valor}</td>
				<td>${dataRet[11 + (i * 13)].valor}</td>
				<td>${dataRet[12 + (i * 13)].valor}</td>
				<td><input id="cb-${i}" type="checkbox" class="col-sm-12"></td>
			</tr>
		`;
	}

	dataInput.innerHTML = data;

	// if (data) {
	// 	dataInput.innerHTML = data;
	// } else {
	// 	dataInput.innerHTML = `
	// 		<tr class="text-center">
	// 			<td colspan="14" class="fs-6">No hay regitros</td>
	// 		</tr>
	// 	`;
	// }
	
	for (let i = 0; i < nro_partidas; i++) {
		cbPartidas.push(document.getElementById("cb-" + i));
		valueImportes.push(document.getElementById("importe-" + i));
	}

	dataInput.addEventListener("change", () => {
		importeTotalInput.value = sumarImportes();
	});

	const sumarImportes = () => {
		let importes = [];
		
		for (let i = 0; i < nro_partidas; i++) {
			cbPartidas[i].checked && importes.push(parseFloat(valueImportes[i].textContent) || 0);
		}
		
		cantidadSelectedPartidas = importes.length;

		return importes.reduce((a, b) => a + b, 0);
	}
}

//implementando la funcionnnnnn
const crearCash = async (dataRet, titular) => {
	$('#generar-btn').prop('disabled', true);

    var func_name = "generar_cash_con_nuevas_validaciones";

	const req_data = {
		"arguments": JSON.stringify({
			idOportunidad: id,
			concatenarPartidas: concatPartidasCode(dataRet),
			importePartidas: importeTotalInput.value,
			cantidadPartidas: +cantidadSelectedPartidas,
			titularContacto: titular
		})
    }

	ZOHO.CRM.BLUEPRINT.proceed();
    
	try {
		const data = await ZOHO.CRM.FUNCTIONS.execute(func_name, req_data);

		ZOHO.CRM.BLUEPRINT.proceed();

		if (data.code === "INVALID_DATA") {
			console.log("Error: " + data.message);
			alert("Error: " + "INVALID_DATA, " + "Message: " + data.message);
		} else if (data.code === "success") {
			console.log(data);
			const toast = new bootstrap.Toast(toastLiveExample);
			toast.show();
			$('#generar-btn').prop("disabled", false);
		} else {
			console.log(data);
		}
	} catch (err) {
		console.log("Error: " + err);
		alert("Error: " + err);
	}

	ZOHO.CRM.BLUEPRINT.proceed();
}

const concatPartidasCode = (dataRet) => {
	const partidasSAP = [];
	
	for (let i = 0; i < nro_partidas; i++) {
		cbPartidas[i].checked && partidasSAP.push(dataRet[0 + (i * 13)].valor + "-" + dataRet[1 + (i * 13)].valor + "-" + dataRet[2 + (i * 13)].valor);
	}
	
	return partidasSAP.join("/");
}

const handleClose = () => {
	ZOHO.CRM.UI.Popup.close();
}
