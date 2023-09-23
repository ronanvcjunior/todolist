window.addEventListener("load", () => {
  const localStorageCategorias = localStorage.getItem("categorias");

  if (localStorageCategorias) {
    parseCategorias = JSON.parse(localStorageCategorias);

    const tabelaCategorias = document.getElementById("tabela-categorias");

    parseCategorias.forEach((categoria) => {
      const tabelaCategoria = document.createElement("tr");
      tabelaCategoria.innerHTML = `
        <td data-label="Nome">${categoria.nome}</td>
      `;

      tabelaCategorias.appendChild(tabelaCategoria);
    });
  }
});