const {createApp} = Vue

createApp({
    data(){
        return{
            registro: false,
            email: "",
            contraseña: "",
            nombre: "",
            apellido: "",
            telefono: "",
            direccion: "",
            edad: ""
        }
    },
    computed(){

    },
    methods: {
        ingreso() { 
            axios.post('/api/login','email=' + this.email + '&contraseña=' + this.contraseña ,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(response => {
                  console.log(response);
                  Swal.fire(
                    'Bienvenidx a Gozo!',
                    'Ahora podés realizar compras.',
                    'success'
                  ).then(() => {
                      window.location.href = "/index.html";
                  });
                })
                .catch(error => {
                    console.log(error.response);
                    Swal.fire(
                    'Login falló, intente nuevamente!',
                    error.response.data,
                    'error'
                  );
                  this.email = "";
                  this.password = "";
                });
        },
        registrar(){
            let registroDTO = {
                nombre : this.nombre,
                apellido : this.apellido,
                email : this.email,
                contraseña : this.contraseña,
                edad : this.edad,
                telefono : this.telefono,
                direccion : this.direccion
            }
            axios.post('/api/clientes', registroDTO, {headers:{'content-type':'application/json'}})
                .then(response => {
                  console.log(response);
                  Swal.fire({
                    title: 'Registro exitoso!',
                    text: 'Espera a ser logueado y podes comprar lo que quieras!.',
                    timer: 2000,
                    showConfirmButton: false,
                    timerProgressBar: true,
                    onBeforeOpen: () => {
                      Swal.showLoading();
                    }
                  }).then(() => {
                    this.ingreso();
                  });
                })
                .catch(error => {
                  console.log(error.response);
                      Swal.fire(
                      'Registro falló!',
                      error.response.data,
                      'error'
                    );
                    this.nombre = "";
                    this.apellido = "";
                    this.email = "";
                    this.contraseña = "";
                    this.edad = "";
                    this.telefono = "";
                    this.direccion = "";
                })
      
          },

        mostrarRegistro(){
            this.registro = true
        },
        mostrarLogin(){
            this.registro = false
        }
        

    }
}).mount("#app")