let transportista = [
    {
        nombre: "Eddy Palomino Diaz",
        licencia: "Q10619186",
        placa: "D6S748",
        marca: "TOYOTA"
    },
    {
        nombre: "Cristhian Larrea Carerra",
        licencia: "Q16799074",
        placa: "C4Q881",
        marca: "TOYOTA"
    },
    {
        nombre: "Richard Carlos Aquino Pari",
        licencia: "Q4031",
        placa: "AAG-877",
        marca: "HYUNDAI"
    }
];
let marca_y_placa_input = document.querySelector("#marca_y_placa");

let nombre_select = document.querySelector("#nombre");

nombre_select.onchange = (evt) => {
    
    console.log(evt.target.value);
}
