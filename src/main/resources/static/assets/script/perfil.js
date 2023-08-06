const { createApp } = Vue

createApp({
    data() {
        return {
            cliente: {},
            logged: false,
            datos: {
                email: "",
                nombre: "",
                apellido: "",
                telefono: "",
                edad: "",
                direccion: "",
            },
            logged: false,
        }
    },
    created() {
        axios.get("/api/cliente/actual")
            .then(response => {
                this.logged = true;
                this.cliente = response.data
                console.log(this.cliente);

            })
            .catch(err => console.log(err))
    },
    methods: {
        confirmar() {
            Swal.fire({
                title: '¿Estás seguro de que quieres editar tus datos?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Sí',
                cancelButtonText: 'No'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.editarPerfil();
                } else {
                    Swal.fire(
                        'Error intentando editar el perfil',
                        'Intente nuevamente',
                        'error')
                }
            })
        },
        editarPerfil() {
            axios.put('/api/cliente/actual/editar', this.datos, { headers: { 'content-type': 'application/json' } })
                .then(response => {
                    Swal.fire({
                        title: 'Has editado tu perfil!',
                        text: 'Volvamos al inicio así seguís navegando.',
                        icon: 'success',
                        timer: 2000,
                        showConfirmButton: false,
                        timerProgressBar: true,
                        onBeforeOpen: () => {
                            Swal.showLoading();
                        }
                    })
                        .then(() => {
                            window.location.href = "../../index.html";

                        })
                })
                .catch(error => {
                    Swal.fire(
                        'Error al intentar modificar tus datos!',
                        error.response.data,
                        'error'
                    );
                });
        },
    },
}).mount("#app");