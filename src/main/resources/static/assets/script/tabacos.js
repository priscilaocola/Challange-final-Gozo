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
const myModal = document.getElementById('exampleModal');
const myInput = myModal.querySelector('.modal-body input');

myModal.addEventListener('shown.bs.modal', () => {
  myInput.focus();
});

window.addEventListener("load", function () {
  this.document.getElementById("container-loader").classList.toggle("container-loader2")
})

const { createApp } = Vue

createApp({
  data() {
    return {
      productos: [],

      tabacos: [],
      accesorios: [],
      cultivo: [],

      tabacosFiltrados: [],
      cultivoFiltrado: [],
      accesoriosFiltrados: [],

      filtroTabacos: [],
      filtroCultivo: [],
      filtroAccesorios: [],

      checkedCheckbox: [],
      seleccionadas: [],
      tabacosFiltrados: [],
      categoriasCultivo: [],
      categoriasAccesorios: [],
      cantidadProductosCarrito: this.getCantidadProductosCarrito(),
      totalPrecioProductos: this.getMontoTotalProductos(),



      productoSeleccionado: {},
      logged: false,
      cliente: []
    };
  },
  created() {
    axios.get("/api/cliente/actual")
      .then(response => {
        this.logged = true;
        this.cliente = response.data

      })
      .catch(err => console.log(err))
    this.traerProductosTabacos();
    this.traerProductosCultivo();
    this.traerProductosAccesorios();
    this.seleccionadas = JSON.parse(localStorage.getItem("seleccionadas")) ?? [];
    this.traerProductosTabacos();
    this.format = new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    });
  },

  methods: {
    logout() {
      axios.post("/api/logout")
        .then(response => {
          this.deleteCompras();
          window.location.href = "/index.html";
        })
    },
    traerProductosTabacos() {
      axios
        .get('/api/productos')
        .then(response => {
          this.productos = response.data.filter(productos => productos.activo == true)

          //TABACOS
          this.tabacos = this.productos.filter(producto => producto.categoria == "TABACO");
          let marcas = this.tabacos.map(tabaco => tabaco.marca)
          const categorias = [...new Set(marcas)]
          this.tabacosFiltrados = categorias
          console.log(this.tabacos);
        })
        .catch(exception => {
          console.log(exception);
        })
    },

    traerProductosCultivo() {
      axios
        .get('/api/productos')
        .then(response => {
          this.productos = response.data

          this.format = new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD',
          });
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

    traerProductosAccesorios() {
      axios
        .get('/api/productos')
        .then(response => {
          this.productos = response.data

          this.format = new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD',
          });
          //ACCESORIOS
          this.accesorios = this.productos.filter(producto => producto.categoria == "ACCESORIOS")
          console.log(this.accesorios);
          let categoriasDeAccesorios = this.accesorios.map(el => el.subCategoria)
          const catAccesorios = [...new Set(categoriasDeAccesorios)]
          this.categoriasAccesorios = catAccesorios;
          console.log(this.categoriasAccesorios)
        })
        .catch(exception => {
          console.log(exception);
        })
    },
    // localstorage
    toggleSeleccion(id) {
      console.log(this.productos);
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
    mostrarModal(producto) {
      if (producto) {
        this.productoSeleccionado = producto;
      }
    },
    deleteCompras() {
      localStorage.clear();
      this.seleccionadas = [];
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
    filtroBusquedaTabacos() {
      if (this.checkedCheckbox.length != 0) {
        this.filtroTabacos = this.tabacos.filter(tabaco => this.checkedCheckbox.includes(tabaco.marca));
        console.log(this.filtroTabacos)
      } else {
        this.filtroTabacos = this.tabacos;
      }
    },
  }
}).mount("#app")