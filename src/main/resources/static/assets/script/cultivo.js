window.addEventListener("scroll", function () {
  const navbar = document.getElementById("navbar");
  const scrollPosition = window.scrollY;
  const navbarHeight = navbar.offsetHeight;
  const headerHeight = 200;

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


const { createApp } = Vue

createApp({
  data() {
    return {
      productos: [],
      cultivo: [],
      cultivoFiltrado: [],
      filtroCultivo: [],
      checkedCheckbox: [],
      seleccionadas: [],
      tabacosFiltrados: [],
      categoriasCultivo: [],
      productoSeleccionado: {},
      logged: false,
      cliente: [],
      cantidadProductosCarrito: this.getCantidadProductosCarrito(),
      format: [],
      totalPrecioProductos: this.getMontoTotalProductos(),

    };
  },
  created() {
    axios.get("/api/cliente/actual")
      .then(response => {
        this.logged = true;
        this.cliente = response.data

      })
      .catch(err => console.log(err))
    this.traerProductosCultivo();
    this.seleccionadas = JSON.parse(localStorage.getItem("seleccionadas")) ?? [];
    this.format = new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    });
  },
  methods: {
    logout() {
      axios.post("/api/logout")
        .then(response => {

          window.location.href = "/index.html";
        })
    },
    mostrarModal(producto) {
      if (producto) {
        this.productoSeleccionado = producto;
      }
    },
    traerProductosCultivo() {
      axios
        .get('/api/productos')
        .then(response => {
          this.productos = response.data

          //CULTIVO
          this.cultivo = this.productos.filter(producto => producto.categoria == "CULTIVO")
          console.log(this.cultivo);
          let categoriasDeCultivo = this.cultivo.map(el => el.subCategoria)
          const catCultivos = [...new Set(categoriasDeCultivo)]
          this.categoriasCultivo = catCultivos;
          console.log(this.categoriasCultivo)
        })
        .catch(exception => {
          console.log(exception);
        })
    },

    // localstorage
    toggleSeleccion(id) {
      const producto = this.productos.find((e) => e.id == id);
      swal({
        title: "Agregar al carrito",
        text: `Ingrese la cantidad que desea agregar (Stock disponible: ${producto.cantidad})`,
        content: {
          element: "input",
          attributes: {
            type: "number",
            min: 1,
            max: producto.cantidad,
          },
        },
        buttons: {
          cancel: true,
          confirm: "Agregar",
        },
      }).then((value) => {
        if (value) {
          const cantidad = parseInt(value);
          if (cantidad > 0 && cantidad <= producto.cantidad) {
            const item = this.seleccionadas.find((e) => e.id == id);
            if (item) {
              item.cantidad += cantidad;
            } else {
              this.seleccionadas.push({
                ...producto,
                cantidad,
              });
            }
            this.cantidadProductosCarrito += cantidad;
            this.totalPrecioProductos = this.seleccionadas.reduce((total, producto) => {
              return total + producto.precio * producto.cantidad;
            }, 0);
            localStorage.setItem("totalPrecioProductos", this.totalPrecioProductos);
            const jsonProductos = JSON.stringify(this.cantidadProductosCarrito)
            localStorage.setItem("cantidadProductosCarrito", jsonProductos);

            const json = JSON.stringify(this.seleccionadas);
            localStorage.setItem("seleccionadas", json);
            swal("Success", "Producto agregado al carrito", "success");
          } else {
            swal("Error", "Cantidad inválida", "error");
          }
        }
      });
    },

    // Verificar si hay productos en el carrito
    getCantidadProductosCarrito() {
      const storedCantidadProductosCarrito = localStorage.getItem("cantidadProductosCarrito");
      if (storedCantidadProductosCarrito) {
        return parseInt(storedCantidadProductosCarrito);
      }
      return 0; // Valor predeterminado si no se encuentra en el LocalStorage
    },
    getMontoTotalProductos() {
      const storedMontoTotalProductos = localStorage.getItem("totalPrecioProductos");
      if (storedMontoTotalProductos) {
        return storedMontoTotalProductos;
      }
      return 0; // Valor predeterminado si no se encuentra en el LocalStorage
    },
    comprarEnElModal(id) {
      const producto = this.productos.find((e) => e.id == id);
      const cantidad = parseInt(this.cantidadEscogida);

      if (cantidad > 0 && cantidad <= producto.cantidad) {
        const item = this.seleccionadas.find((e) => e.id == id);
        if (item) {
          item.cantidad += cantidad;
        } else {
          this.seleccionadas.push({
            ...producto,
            cantidad,
          });
        }
      }


    },
  },
  computed: {
    filtroBusquedaCultivo() {
      if (this.checkedCheckbox.length != 0) {
        this.filtroCultivo = this.cultivo.filter(producto => this.checkedCheckbox.includes(producto.subCategoria))
        console.log(this.filtroCultivo)
      } else {
        this.filtroCultivo = this.cultivo;
      }
    },
  }
}
).mount("#app")





