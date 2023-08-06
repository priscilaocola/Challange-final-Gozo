window.addEventListener("scroll", function () {
    const navbar = document.getElementById("navbar");
    const scrollPosition = window.scrollY;
    const navbarHeight = navbar.offsetHeight;
    const headerHeight = 300;

    const opacity = Math.min(1, scrollPosition / (headerHeight - navbarHeight));
    if (scrollPosition > headerHeight) {
        navbar.classList.add("top-nav");
        navbar.classList.remove("navbar-interno_home")
    } else {
        navbar.classList.remove("top-nav");
        navbar.classList.add("navbar-interno_home")
    }
    navbar.style.backgroundColor = `rgba(0, 0, 0, ${opacity})`;
});

window.addEventListener("load", function () {
    this.document.getElementById("container-loader").classList.toggle("container-loader2")
})

const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            productos: [],
            desactivados: [],
            clientes: [],
            //formulario productos
            nombre: "",
            marca: "",
            descripcion: "",
            precio: "",
            categoria: "",
            subCategoria: "",
            cantidad: null,
            // formulario clientes
            nombreCliente: "",
            apellido: "",
            email: "",
            telefono: "",
            edad: null,
            contraseña: "",
            direccion: "",
            searchInput: "",
            //modificar productos
            Pnombre: "",
            Pmarca: "",
            Pdescripcion: "",
            Pcantidad: null,
            Pprecio: null,
            //id
            selectedProductId: null,
            cliente: []
        }
    },
    created() {
        axios.get("/api/cliente/actual")
            .then(response => {
                this.logged = true;
                this.cliente = response.data

            })
        this.getProductos()
        this.getClientes()
    },
    computed: {
        filtrarPorTitulo() {
            this.productosFiltrados = this.productos.filter((e) =>
                e.nombre.toLowerCase().includes(this.searchInput.toLowerCase())
            );
        },
    },
    methods: {

        filtrarPorTitulo() {
            return this.productos.filter((e) =>
                e.nombre.toLowerCase().includes(this.searchInput.toLowerCase())
            );
        },
        getProductos() {
            axios.get("/api/productos")
                .then(res => {
                    this.productos = res.data.filter(productos => productos.activo == true)
                    this.desactivados = res.data.filter(productos => productos.activo == false)
                    console.log(this.productos)
                })
                .catch(error => {
                    alert("no se agrego")
                });
        },

        createdProduct() {
            const fileInput = document.querySelector('input[type="file"]');
            const file = fileInput.files[0];
            const formData = new FormData();
            formData.append('file', file);
            formData.append('UPLOADCARE_PUB_KEY', 'fddd46a83c06481cde62');
            formData.append('UPLOADCARE_STORE', '1');

            axios({
                url: 'https://upload.uploadcare.com/base/',
                method: 'POST',
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
                data: formData
            })
                .then(res => {
                    // The UUID of the uploaded file is in res.data.file
                    // You can use this UUID to construct the file URL
                    const fileUUID = res.data.file;
                    const fileURL = `https://ucarecdn.com/${fileUUID}/`;

                    // Save the file URL to your database
                    const data = {
                        nombre: this.nombre,
                        marca: this.marca,
                        descripcion: this.descripcion,
                        precio: this.precio,
                        categoria: this.categoria,
                        subCategoria: this.subCategoria,
                        cantidad: this.cantidad,
                        imagen: fileURL
                    };

                    axios.post('/api/productos/agregar', data, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                        .then(res => {
                            alert("se agrego")
                        })
                        .catch(error => {
                            alert("no se agrego")
                        });
                })
                .catch(err => {
                    console.error(err);
                });
        },
        borrarProducto(id) {
            axios.patch(`/api/productos/${id}/deactivate`)
                .then(res => {
                    alert("se borro")
                    window.location.href = "/manager.html"
                })
                .catch(error => {
                    alert("no se borro")
                });
        },
        reactivar(id) {
            axios.patch(`/api/productos/${id}/activate`)
                .then(res => {
                    alert("se agrego")
                    window.location.href = "/assets/pages/manager.html"
                })
                .catch(error => {
                    alert("se agrego")
                });
        },
        getClientes() {
            axios.get("/api/clientes")
                .then(res => {
                    this.clientes = res.data
                    console.log(this.clientes)
                })
                .catch(error => {
                    alert("no se borro")
                });
        },
        register() {
            const data = {
                nombre: this.nombreCliente,
                apellido: this.apellido,
                email: this.email,
                direccion: this.direccion,
                contraseña: this.contraseña,
                telefono: this.telefono,
                edad: this.edad,

            };
            axios.post("/api/clientes", data, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(res => {
                    window.location.href = "/assets/pages/manager.html"
                })
                .catch(err => alert(err))
        },

        redirectionModal(id) {
            this.selectedProductId = id;
            window.location.href = "#openModal";
        },
        modificar() {
            const data = {
                nombre: this.Pnombre,
                marca: this.Pmarca,
                descripcion: this.Pdescripcion,
                cantidad: this.Pcantidad,
                precio: this.Pprecio,
            }
            axios.patch(`/api/productos/${this.selectedProductId}/modificar`, data, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(() => {
                    window.location.href = "#close";
                    Swal.fire('¡Éxito!', 'El producto fue modificado correctamente', 'success')
                        .then(() => {
                            window.location.href = "/assets/pages/manager.html";
                        });
                })
                .catch(err => {
                    Swal.fire('Error', err.message, 'error');
                    window.location.href = "#close";
                });
        },

        LogOut() {
            axios.post('/api/logout')
                .then(() => {
                    // Successful logout
                    // Redirect to accounts.html
                    window.location.href = '/index.html';
                })
                .catch(error => {
                    // Failed logout
                    // Show error message to user
                    swal({
                        title: 'Error',
                        text: 'Error logging out',
                        icon: 'error',
                        button: 'OK'
                    });
                });
        },

    },
})
app.mount("#app")
