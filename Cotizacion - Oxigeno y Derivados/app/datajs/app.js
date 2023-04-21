var stringsnam1 = "WelcomeBack";
var id_registro = "";


const app = new Vue({
    el: '#app',
    data: {
      message: 'Hello!',
      entity: {
        id: null,
        module: null,
      },
      Datos_principal: {
        fecha_mostrar: null,
        cod_cotizacion: null,
        pngvacio: null,
        nameCliente: null,
        Productos: [
          {
            posicion_lista: 0,
            SKU: null,
            m_unidades: null,
            cantidad: 0,
            precio: null,
            total_precio: null,
            ImgOfItem_CrmProduct: {
                tipoFoto: null,
                Base64DataImg: null
            }
          }
        ],
        sumaTotal_Sunbform: null,
      },
      cargado: false,
      Imagenes: []
    },
    methods: {
      onLoad: function () {
        const self = this
        ZOHO.embeddedApp.on('PageLoad', async function (data) {
          console.log(data)
          self.entity.id = data.EntityId[0]
          self.entity.module = data.Entity
  
          await self.getDataFromZoho(self.entity.id);

        //  ZOHO.CRM.API.attachFile({Entity:"Deals",RecordID:data.EntityId[0],File:{Name: ArchivoPDF_Generado,Content:blob}}).then(function(data){
        //    console.log(data);
        //  });
        });
      },
      initZSDK: function () {
        const self = this
        ZOHO.embeddedApp.init().then(function () {
          ZOHO.CRM.UI.Resize({
            height: '780',
            width: '840',
          })
        })
      },
      getDataFromZoho: async function (id) {
        // https://www.zohoapis.com/crm/v2/functions/sa_get_all_data/actions/execute?auth_type=oauth
        //OBTENEMOS LA DATA
        const self = this;
        var func_name = "WidgetDatos";
        var req_data = {
          "arguments": JSON.stringify({
            // "id": id,
            "id_registro": id,
          })
        };
        console.log("holitas");
        console.log(req_data);
        let datosFromZoho = await ZOHO.CRM.FUNCTIONS.execute(func_name, req_data);
        let datos = datosFromZoho.details.output;
        let datos_object = JSON.parse(datos);
        console.log(datos_object);
        //-------------------------------
  
        console.log("hola");
  
        console.log(datosFromZoho);
        return datosFromZoho;
  
  
      },
      GenerarPDf: async function () {
        var paramPDf = {
          margin: [0.2, 0.2],
          filename: 'Cotizacion.pdf',
          image: {
            type: 'jpeg',
            quality: 0.98
          },
          html2canvas: {
            scale: 4, // A mayor escala, mejores gráficos, pero más peso
            letterRendering: true,
            userCORS: true,
          },
          jsPDF: {
            unit: "in",
            format: "a4",
            orientation: 'portrait' // landscape o portrait
          }
        };

        var ArchivoPDF_Generado;
        try {
          //ArchivoPDF_Generado = await html2pdf().set(paramPDf).from(document.getElementById('element')).outputPdf();
          //Removiendo Modal para que no aparezca en PDF
          //modal.parentNode.removeChild(modal);
          ArchivoPDF_Generado = await html2pdf().set(paramPDf).from(document.getElementById('container')).outputPdf().save();
        } catch (error1) {
          console.log("Generador PDF Fallo =>" + error1); 
        }

        console.log(paramPDf.filename);

        /*const forPDF = document.getElementById('container');
  
        const pdfResponse = await html2canvas(forPDF).then(async (canvas) => {
          const imgData = canvas.toDataURL('image/png');
          const imgWidth = 190;
          const pageHeight = 295;
          let imgHeight = canvas.height * imgWidth / canvas.width;
          let heightLeft = imgHeight;
          const doc = new jsPDF('p', 'mm', "a4");
          let position = 0;
          doc.addImage(imgData, 'PNG', 8, position, imgWidth, imgHeight + 10);
          heightLeft -= pageHeight;
          while (heightLeft >= 0) {
            position = heightLeft - imgHeight;
            doc.addPage();
            doc.addImage(imgData, 'PNG', 8, position, imgWidth, imgHeight + 10);
            heightLeft -= pageHeight;
          }
          return doc.output('blob');
        })*/
        let fileVar = new Blob([ArchivoPDF_Generado], { type: "application/octet-stream" });
        let formdata = new FormData();
        let headers = new Headers();
        formdata.append('archivo', fileVar);
        headers.append('Access-Control-Allow-Origin', 'https://0df37860-54ed-4d6f-8175-ecf7400cbbdb.zappsusercontent.com');
        const requestOptions = {
          method: 'POST',
          body: formdata,
          redirect: 'follow',
          headers: headers
        };
        let objeto;
        let message = '';

      
  
        //try {
          await fetch("https://solucionesm4g.site:8443/files/api-touring-people/uploadPdf", requestOptions)
            .then((response) => response.json())
            .then(data => {
              objeto = data?.id;
              message = data?.mensaje;
            })
            console.log("archisvos adjutnos URL => respuesta: "+message +"\n"+"objeto: "+objeto);
        //} catch (DocumentoLinkError) {
          message = "no_data";
       // }
        if (objeto) {
          const model = message;
          const data = {
            "arguments": JSON.stringify({
              "param1": "Omar EL mensaje si llego",
              "accion": "send_mail",
              "id_file": objeto
            })
          };
          await ZOHO.CRM.FUNCTIONS.execute(model, data).then(res => {
            message = res.details?.output
          })

          //modal.style.display = "none";
        }
        //swal('', message, "success");
      },
    },
    created: async function () {
      this.onLoad()
      this.initZSDK()
      ZOHO.embeddedApp.init()
    },
  })

  let button = document.getElementById("btnDownload");

  button.addEventListener("click", function () {
    button.style.display = "none";
    modal.style.display="block"
  });

  document.getElementById("btnDownload").onclick = function tempAlert(msg, duration) {
    var el = document.createElement("div");
    el.setAttribute("style", "position:absolute;top:40%;left:20%;background-color:white;");
    el.innerHTML = msg;
    setTimeout(function () {
      el.parentNode.removeChild(el);
    }, duration);
    document.body.appendChild(el);
  }



  // Get the modal
  var modal = document.getElementById("miModal");

  // Get the <span> element that closes the modal
  var span = document.getElementsByClassName("close")[0];

  // When the user clicks the button, open the modal  
  button.onclick = function () {
    modal.style.display="block";
  }


  // When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}
// Obtener la referencia al modal
var modal = document.getElementById("miModal");

// Programar el cierre del modal después de 5 segundos (5000 milisegundos)
setTimeout(function () {
  modal.style.display = "none";

}, 5100);


button.addEventListener('click', function () {

  setTimeout(function () {
    // Código para cerrar el widget aquí
    ZOHO.CRM.UI.Popup.close()
      .then(function (data) {
        console.log(data)
      })
  }, 5000);
  //4100
})


const dataInput = document.getElementById("data");//igualito


  Vue.filter('toCurrency', function (value) {
    if (typeof value !== 'number') {
      return value
    }
    var formatter = new Intl.NumberFormat('es-PE', {
      style: 'currency',
      currency: 'USD'
    })
    return formatter.format(value).replace('USD', '')
  })