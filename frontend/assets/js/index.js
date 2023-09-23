window.addEventListener("load", () => {
    const localStorageTarefas = localStorage.getItem("tarefas");

    if (localStorageTarefas) {
        parseTarefas = JSON.parse(localStorageTarefas);

        const tabelaTarefas = document.getElementById("tabela-tarefas");

        parseTarefas.forEach((tarefa) => {
            let dataFormatada = "";
            if (tarefa.dataTermino) {
                const data = new Date(tarefa.dataTermino);

                const dia = String(data.getDate()).padStart(2, '0');
                const mes = String(data.getMonth() + 1).padStart(2, '0');
                const ano = data.getFullYear();

                dataFormatada = `${dia}/${mes}/${ano}`;
            }

            let categorias = "";

            const localStorageCategorias = localStorage.getItem("categorias");
            
            if (localStorageCategorias) {
                parseCategorias = JSON.parse(localStorageCategorias);

                tarefa.categorias.forEach((categoriaTarefa) => {
                    console.log(categoriaTarefa)
                    const categoria = parseCategorias.find((categoria) => categoria.id == categoriaTarefa);
                    console.log(categoria)
                    categorias += (categoria.nome + " ")
                })
            }

            categorias = categorias.trim().replace(" ", ", ");
            
            const tabelaTarefa = document.createElement("tr");
            tabelaTarefa.innerHTML = `
                <td data-label="Nome">${tarefa.nome}</td>
                <td data-label="Descrição">${tarefa.descricao}</td>
                <td data-label="Data de Término">${dataFormatada}</td>
                <td data-label="Prioridade">${tarefa.prioridade}</td>
                <td data-label="Categorias">${categorias}</td>
                <td data-label="Status">${tarefa.status}</td>
                <td data-label="Opções">
                    <div class="btns">
                        <a href="./cadastrarTarefa.html?tarefa=${tarefa.id}" class="btn btn-editar">Editar</a>
                        <a href="#" value="${tarefa.id}" class="btn btn-excluir">Excluir</a>
                    </div>
                </td>
              `;

            tabelaTarefas.appendChild(tabelaTarefa);
        });
    }

    const btnsExcluir = document.getElementsByClassName("btn-excluir");

    Array.from(btnsExcluir).forEach(btn => {
        btn.addEventListener("click", function(event) {
            event.preventDefault();
            const tarefaParaRemoverId = btn.getAttribute("value");

            let parseTarefas = [];

            const localStorageTarefas = localStorage.getItem("tarefas");

            if (localStorageTarefas) {
                parseTarefas = JSON.parse(localStorageTarefas);
            }

            const tarefas = parseTarefas.filter((objeto) => objeto.id != tarefaParaRemoverId);

            localStorage.setItem("tarefas", JSON.stringify(tarefas));

            window.location.href = "./index.html"
        });
    });

});
