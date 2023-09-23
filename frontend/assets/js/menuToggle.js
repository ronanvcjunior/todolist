const menuToggle = document.getElementById("menu-toggle");
const navHeader = document.getElementById("nav-header");
const itensMenu = document.getElementsByClassName("item-menu");

menuToggle.addEventListener("click", () => {
  navHeader.classList.toggle("menu-ativo");
});

for (let itemMenu of itensMenu) {
  itemMenu.addEventListener("click", () => {
    navHeader.classList.toggle("menu-ativo");
  });

  itemMenu.addEventListener("mouseover", () => {
    if (!itemMenu.classList.contains("ativo")) {
      const itemAtivo = document.getElementsByClassName("ativo")[0];
      if (itemAtivo)
        itemAtivo.classList.add("ativo-hover");
    }
  });

  itemMenu.addEventListener("mouseout", () => {
    const itemAtivo = document.getElementsByClassName("ativo")[0];
    if (itemAtivo)
      itemAtivo.classList.remove("ativo-hover");
  });
}