

const select = document.getElementById("destino");
const precioTexto = document.getElementById("precio");
const personasInput = document.getElementById("personas");
const fechaIdaInput = document.getElementById("fechaIda");
const fechaVueltaInput = document.getElementById("fechaVuelta");

const precios = {
    paris: 165,      
    roma: 145,         
    tokio: 240,       
    bali: 195,        
    dubai: 210,       
    nuevayork: 260,    
    londres: 175,      
    tailandia: 160,    
    maldivas: 320           
};

const ofertas = {
    paris: { dias: 5, precio: 725 },    
    roma: { dias: 4, precio: 510 },     
    bali: { dias: 7, precio: 1250 },    
    tokio: { dias: 8, precio: 1780 }    
};


const params = new URLSearchParams(window.location.search);
const destinoURL = params.get("destino");

if (select) {

    if (destinoURL) {
        select.value = destinoURL;
    }

    select.addEventListener("change", calcularPrecio);
    personasInput.addEventListener("input", calcularPrecio);
    fechaIdaInput.addEventListener("change", calcularPrecio);
    fechaVueltaInput.addEventListener("change", calcularPrecio);
}

function calcularPrecio() {

    const destino = select.value;
    const personas = parseInt(personasInput.value);
    const fechaIda = fechaIdaInput.value;
    const fechaVuelta = fechaVueltaInput.value;

    if (!precios[destino] || !personas || !fechaIda || !fechaVuelta) {
        precioTexto.textContent = "";
        return;
    }

    const inicio = new Date(fechaIda);
    const fin = new Date(fechaVuelta);
    

    const dias = (fin - inicio) / (1000 * 60 * 60 * 24);

    if (dias <= 0) {
        precioTexto.textContent = "Fechas incorrectas";
        return;
    }

    let total;

    if (ofertas[destino] && dias === ofertas[destino].dias) {

        total = ofertas[destino].precio * personas;

        precioTexto.textContent = "Oferta aplicada: " + total + "€";

    } else {

        total = precios[destino] * personas * dias;

        precioTexto.textContent = "Precio total: " + total + "€";
    }
}




fetch("../../datos/XML/viajes.xml")
.then(response => response.text())
.then(data => {

    const parser = new DOMParser();
    const xml = parser.parseFromString(data, "text/xml");

    const viajes = xml.getElementsByTagName("viaje");

    let contenido = "";

    
    const destinosUnicos = {};

    for (let i = 0; i < viajes.length; i++) {

        const nombreCompleto = viajes[i].getElementsByTagName("destino")[0].textContent;

        const nombreBase = nombreCompleto.split(" ")[0];

        if (!destinosUnicos[nombreBase]) {
            destinosUnicos[nombreBase] = [];
        }

        destinosUnicos[nombreBase].push(viajes[i]);
    }

    
    const destinosArray = Object.keys(destinosUnicos);

    destinosArray.sort(() => Math.random() - 0.5);

    
    const seleccionados = destinosArray.slice(0, 6);

  
    for (let destino of seleccionados) {

        const lista = destinosUnicos[destino];

        const random = Math.floor(Math.random() * lista.length);
        const viaje = lista[random];

        const nombre = viaje.getElementsByTagName("destino")[0]?.textContent || "Destino";
        const precio = viaje.getElementsByTagName("precio")[0]?.textContent || "0";
        const dias = viaje.getElementsByTagName("dias")[0]?.textContent || "0";
        const personas = viaje.getElementsByTagName("personas")[0]?.textContent || "1";
        const imagen = viaje.getElementsByTagName("imagen")[0]?.textContent || "default.jpg";

        contenido += `
            <div class="col-md-4">
                <div class="card oferta-card h-100">

                    <img src="../img/${imagen}" class="card-img-top">

                    <div class="card-body text-center">
                        <h5>${nombre}</h5>
                        <p>${dias} días • ${personas} personas</p>
                        <p class="precio">${precio}€</p>
                    </div>

                </div>
            </div>
        `;
    }

    document.getElementById("contenedor-viajes").innerHTML = contenido;

}

); 