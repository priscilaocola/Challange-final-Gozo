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
            logged: false,
            cliente: [],
            cantidadProductosCarrito: this.getCantidadProductosCarrito(),

        };
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
        logout() {
            axios.post("/api/logout")
                .then(response => {
                    this.deleteCompras();
                    window.location.href = "/index.html";
                })
        },
        getCantidadProductosCarrito() {
            const storedCantidadProductosCarrito = localStorage.getItem("cantidadProductosCarrito");
            if (storedCantidadProductosCarrito) {
                return parseInt(storedCantidadProductosCarrito);
            }
            return 0; // Valor predeterminado si no se encuentra en el LocalStorage
        },
        deleteCompras() {
            localStorage.clear();
            this.seleccionadas = [];
        },
    }
});

app.mount("#app");
