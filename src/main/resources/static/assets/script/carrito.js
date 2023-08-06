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
      seleccionadas: [],
      totalCompra: 0,
      totalProductos: 0,
      logged: false,
      cliente: [],
      numeroCantidad: null,
      totalPorProducto: null,
    }
  },
  created() {
    this.format = new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    });

    this.seleccionadas = JSON.parse(localStorage.getItem("seleccionadas")) ?? [];
    // Detecta si el usuario ha sido redirigido desde la página de pago
    if (window.location.search.includes('payment_id')) {
      Swal.fire({
        title: 'Pago exitoso',
        icon: 'success',
        text: 'Gracias por su compra!'
      });
    this.generarOrdenPago();
    this.deleteCompras();
  }
      console.log(this.seleccionadas)
    
  },

  methods: {

    redirectToPayment() {
      const items = this.seleccionadas.map(producto => ({
        title: producto.nombre,
        description: producto.descripcion,
        quantity: producto.cantidad,
        currency_id: 'ARS',
        unit_price: producto.precio
      }));

      // Crea la preferencia de pago con los items dinámicos
      fetch('https://api.mercadopago.com/checkout/preferences', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer TEST-3626152189977637-073022-5309ede04cf2730a9112c7429c6ad1f9-1436732273'
        },
        body: JSON.stringify({
          items,
          back_urls: {
            success: `gozogrowshop.onrender.com/assets/pages/carrito.html`
          }
        })
      })
        .then(response => response.json())
        .then(data => {
          // Aquí puedes obtener el ID y la URL de la preferencia de pago
          
          const preferenceId = data.id;
          const preferenceUrl = data.init_point;

          // Redirige al usuario a la página de pago
          window.location.href = preferenceUrl;
        })
        .then(() => {
          
        });
    },


    generarOrdenPago() {
      const items = this.seleccionadas.map(producto => ({
        id: producto.id,
        totalProductos: producto.cantidad,
        total: producto.precio,
        nombre: producto.nombre
      }));
      axios.post('/api/crear/orden', items, {
        headers: {
          'Content-Type': 'application/json'
        }
      })
        .then(response => {
          console.log("hola");
        })
        .catch(error => {
          console.log(error.response)
        });
    },

    deleteCompras() {
      localStorage.clear();
      this.seleccionadas = [];
    },
    redirection() {
      return window.location.href = "/assets/pages/accesorios.html"
    },
    redirectionPay() {
      return window.location.href = "/assets/pages/accesorios.html"//especificar ruta de pago
    },
    async descartarProducto(id) {
      const result = await Swal.fire({
        title: '¿Estás seguro de que quieres descartar este producto?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí',
        cancelButtonText: 'No'
      });

      if (result.isConfirmed) {
        this.elementos = JSON.parse(localStorage.getItem('seleccionadas'));
        this.elementosFiltrados = this.elementos.filter(elemento => elemento.id !== id);
        this.json = JSON.stringify(this.elementosFiltrados);
        localStorage.setItem('seleccionadas', this.json);

        const nuevaCantidadProductos = this.elementosFiltrados.reduce((total, elemento) => total + elemento.cantidad, 0);
        localStorage.setItem('cantidadProductosCarrito', nuevaCantidadProductos.toString());
        
        
        const montoActual = localStorage.getItem('totalPrecioProductos') || 0; 
        // Calcular el nuevo monto total
        const productoAEliminar = this.elementos.find(elemento => elemento.id === id);
        const nuevoMontoTotal = montoActual - productoAEliminar.precio * productoAEliminar.cantidad;

        // Actualizar el monto total en localStorage
        localStorage.setItem("totalPrecioProductos", nuevoMontoTotal);

        await Swal.fire({
          title: '¡Producto descartado!',
          icon: 'success'
        });

        window.location.href = "/assets/pages/carrito.html";
      }
    },

    abrirSweetAlert(id) {
      Swal.fire({
        title: 'Modificar cantidad',
        html: `
          <input type="number" id="inputNumber" min="1" max="10000" value="1" v-model="numeroCantidad">
        `,
        showCancelButton: true,
        showConfirmButton: true,
        confirmButtonText: 'Modificar',
        cancelButtonText: 'Cancelar',
        preConfirm: () => {
          // Acción a realizar al hacer clic en Aceptar (Confirmar)
          this.numeroCantidad = document.getElementById('inputNumber').value;
          console.log(`Valor ingresado: ${this.numeroCantidad}`);

          this.modificarCantidad(id);
        },
        cancelButtonColor: '#d33',
        allowOutsideClick: false
      });
    },

    modificarCantidad(id) {
      const producto = this.seleccionadas.find(producto => producto.id === id);
      console.log(this.numeroCantidad);

      if (producto) {

        producto.cantidad = this.numeroCantidad
        console.log('Producto encontrado:', producto);
      } else {
        console.log('Producto no encontrado con el ID proporcionado:', id);
      }
    }
  },


  computed: {
    resultado() {
      this.totalCompra = this.seleccionadas.reduce(
        (total, articulo) => total + articulo.precio * articulo.cantidad,
        0
      );
      const json = JSON.stringify(this.totalCompra);
      localStorage.setItem("resultado", json);
    },
    cantidad() {
      this.totalProductos = this.seleccionadas.reduce(
        (total, articulo) => total + articulo.cantidad,
        0
      );
      const json = JSON.stringify(this.totalProductos);
      localStorage.setItem("cantidad", json);
    },
    crearOrden() {
      this.ordenProducto = this.seleccionadas.map(producto => ({
        nombre: producto.nombre,
        cantidadDeProductos: producto.cantidad,
        precioUnitario: producto.precio
      }))
      axios
        .post('/api/ordenes/crear-orden', this.ordenProducto, { headers: { 'content-type': 'application/json' } })
        .then(response => {
          console.log(response.data);
          this.fetch();
        })
        .catch(error => {
          console.log(error.response);
        })
    },
  }
});


app.mount("#app")