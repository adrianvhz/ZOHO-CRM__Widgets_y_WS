const inputPartida1 = document.getElementById("partida-1"); 
const inputPartida2 = document.getElementById("partida-2");  
const inputPartida3 = document.getElementById("partida-3"); 
const inputPartida4 = document.getElementById("partida-4");  
const inputPartida5 = document.getElementById("partida-5"); 

let inputImporteTotal = document.getElementById("importe-total");

let cbImporte = [
	document.getElementById("cb-1"),
	document.getElementById("cb-2"),
	document.getElementById("cb-3"),
	document.getElementById("cb-4"),
	document.getElementById("cb-5"),
];

cbImporte.forEach(el => {
	el.onchange = (evt) => {
		inputImporteTotal.innerHTML = sumarImportes();
	}
});

function sumarImportes() {
	let inputImporte = [
		document.getElementById("importe-1"),
		document.getElementById("importe-2"),
		document.getElementById("importe-3"),
		document.getElementById("importe-4"),
		document.getElementById("importe-5")
	];

	let importes = [];

	for (let i = 0; i < 5; i++) {
		cbImporte[i].checked && importes.push(parseInt(inputImporte[i].value) || 0);
	}

	return importes.reduce((a, b) => a + b, 0);

}
