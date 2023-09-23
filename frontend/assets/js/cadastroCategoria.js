const formulario = document.getElementById("formulario");
formulario.addEventListener("submit", (event) => {
  event.preventDefault();

  let parseCategorias = [];

  const localStorageCategorias = localStorage.getItem("categorias");

  if (localStorageCategorias) {
    parseCategorias = JSON.parse(localStorageCategorias);
  }

  let id = 1;
  if (parseCategorias.length > 0)
    id = parseCategorias[parseCategorias.length - 1].id + 1;

  const nome = document.getElementById("nomeInput").value;

  const categoria = {
    id: id,
    nome: nome
  }

  parseCategorias.push(categoria);

  localStorage.setItem("categorias", JSON.stringify(parseCategorias));

  window.location.href = "./categorias.html";
});
