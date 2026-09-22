const API_URL = "/api/productos";

const form = document.getElementById("producto-form");
const formTitle = document.getElementById("form-title");
const idInput = document.getElementById("producto-id");
const nombreInput = document.getElementById("nombre");
const descripcionInput = document.getElementById("descripcion");
const precioInput = document.getElementById("precio");
const stockInput = document.getElementById("stock");
const submitBtn = document.getElementById("submit-btn");
const cancelBtn = document.getElementById("cancel-btn");
const refreshBtn = document.getElementById("refresh-btn");
const tbody = document.getElementById("productos-tbody");
const emptyMsg = document.getElementById("empty-msg");
const statusMsg = document.getElementById("status-msg");

function mostrarEstado(mensaje, tipo) {
  statusMsg.textContent = mensaje;
  statusMsg.className = tipo || "";
  if (mensaje) {
    setTimeout(() => {
      statusMsg.textContent = "";
      statusMsg.className = "";
    }, 4000);
  }
}

function limpiarFormulario() {
  form.reset();
  idInput.value = "";
  formTitle.textContent = "Nuevo producto";
  submitBtn.textContent = "Crear";
  cancelBtn.hidden = true;
}

async function cargarProductos() {
  try {
    const respuesta = await fetch(API_URL);
    if (!respuesta.ok) {
      throw new Error("No se pudo obtener la lista de productos");
    }
    const productos = await respuesta.json();
    renderizarTabla(productos);
  } catch (error) {
    mostrarEstado(error.message, "error");
  }
}

function renderizarTabla(productos) {
  tbody.innerHTML = "";
  emptyMsg.hidden = productos.length > 0;

  productos.forEach((producto) => {
    const fila = document.createElement("tr");

    fila.innerHTML = `
      <td>${producto.id}</td>
      <td>${producto.nombre ?? ""}</td>
      <td>${producto.descripcion ?? ""}</td>
      <td>${(producto.precio ?? 0).toLocaleString("es-CO")}</td>
      <td>${producto.stock ?? 0}</td>
    `;

    const celdaAcciones = document.createElement("td");
    celdaAcciones.className = "actions";

    const btnEditar = document.createElement("button");
    btnEditar.className = "secondary";
    btnEditar.textContent = "Editar";
    btnEditar.addEventListener("click", () => cargarEnFormulario(producto));

    const btnEliminar = document.createElement("button");
    btnEliminar.className = "danger";
    btnEliminar.textContent = "Eliminar";
    btnEliminar.addEventListener("click", () => eliminarProducto(producto.id));

    celdaAcciones.append(btnEditar, btnEliminar);
    fila.appendChild(celdaAcciones);
    tbody.appendChild(fila);
  });
}

function cargarEnFormulario(producto) {
  idInput.value = producto.id;
  nombreInput.value = producto.nombre ?? "";
  descripcionInput.value = producto.descripcion ?? "";
  precioInput.value = producto.precio ?? "";
  stockInput.value = producto.stock ?? "";
  formTitle.textContent = `Editando producto #${producto.id}`;
  submitBtn.textContent = "Guardar cambios";
  cancelBtn.hidden = false;
  nombreInput.focus();
}

async function guardarProducto(event) {
  event.preventDefault();

  const producto = {
    nombre: nombreInput.value.trim(),
    descripcion: descripcionInput.value.trim(),
    precio: parseFloat(precioInput.value),
    stock: parseInt(stockInput.value, 10),
  };

  const id = idInput.value;
  const esEdicion = Boolean(id);
  const url = esEdicion ? `${API_URL}/${id}` : API_URL;
  const metodo = esEdicion ? "PUT" : "POST";

  try {
    const respuesta = await fetch(url, {
      method: metodo,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(producto),
    });

    if (!respuesta.ok) {
      throw new Error("No se pudo guardar el producto");
    }

    mostrarEstado(esEdicion ? "Producto actualizado" : "Producto creado", "success");
    limpiarFormulario();
    cargarProductos();
  } catch (error) {
    mostrarEstado(error.message, "error");
  }
}

async function eliminarProducto(id) {
  if (!confirm(`¿Eliminar el producto #${id}?`)) {
    return;
  }

  try {
    const respuesta = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (!respuesta.ok) {
      throw new Error("No se pudo eliminar el producto");
    }
    mostrarEstado("Producto eliminado", "success");
    cargarProductos();
  } catch (error) {
    mostrarEstado(error.message, "error");
  }
}

form.addEventListener("submit", guardarProducto);
cancelBtn.addEventListener("click", limpiarFormulario);
refreshBtn.addEventListener("click", cargarProductos);

cargarProductos();
