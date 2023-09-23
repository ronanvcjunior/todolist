$(document).ready(function() {
    $('#prioridadeSelect').select2();
    $('#statusSelect').select2();
    $('#CategoriasSelect').select2();
});

window.addEventListener("load", () => {
    const url = new URL(window.location.href);
    const tarefaId = url.searchParams.get("tarefa");
    if (tarefaId) {
        const localStorageTarefas = localStorage.getItem("tarefas");

        if (localStorageTarefas) {
            parseTarefas = JSON.parse(localStorageTarefas);

            const tarefaParaEditar = parseTarefas.find((tarefa) => tarefa.id == tarefaId);

            if (tarefaParaEditar) {
                const nome = document.getElementById("nomeInput");
                const dataTermino = document.getElementById("dataTerminoInput");
                const prioridade = document.getElementById("prioridadeSelect");
                const status = document.getElementById("statusSelect");
                const descricao = document.getElementById("descricaoTextArea");

                nome.setAttribute("value",tarefaParaEditar.nome);
                dataTermino.setAttribute("value",tarefaParaEditar.dataTermino);
                descricao.innerText = tarefaParaEditar.descricao;

                $(prioridade).val(tarefaParaEditar.prioridade).trigger('change');
                $(status).val(tarefaParaEditar.status).trigger('change');

                const categoriasSelecionadas = tarefaParaEditar.categorias;
                montarSelect2(categoriasSelecionadas);
            } else {
                montarSelect2();
            }
        }
    } else {
        montarSelect2();
    }

});

function montarSelect2(categoriasSelecionadas = []) {
    const localStorageCategorias = localStorage.getItem("categorias");

    if (localStorageCategorias) {
        parseCategorias = JSON.parse(localStorageCategorias);

        const CategoriasSelect = document.getElementById("CategoriasSelect");

        parseCategorias.forEach((categoria) => {
            const optionCategoria = document.createElement("option");
            optionCategoria.setAttribute("value", categoria.id);
            optionCategoria.innerText = categoria.nome;

            categoriasSelecionadas.forEach((categoriaId) => {
                if (categoriaId == categoria.id)
                    optionCategoria.setAttribute("selected", "");
            });

            CategoriasSelect.appendChild(optionCategoria);
        });
    }
}

const formulario = document.getElementById("formulario");
formulario.addEventListener("submit", (event) => {
    event.preventDefault();

    let parseTarefas = [];

    const localStorageTarefas = localStorage.getItem("tarefas");

    if (localStorageTarefas) {
        parseTarefas = JSON.parse(localStorageTarefas);
    }

    let id = 1;
    console.log(parseTarefas.length)
    if (parseTarefas.length > 0)
        id = parseTarefas[parseTarefas.length - 1].id + 1;

    const url = new URL(window.location.href);
    const tarefaId = url.searchParams.get("tarefa");
    if (tarefaId)
        id = parseInt(tarefaId);


    const nome = document.getElementById("nomeInput").value;
    const dataTermino = document.getElementById("dataTerminoInput").value;
    const prioridade = document.getElementById("prioridadeSelect").value;
    const status = document.getElementById("statusSelect").value;
    const descricao = document.getElementById("descricaoTextArea").value;
    
    const CategoriasSelect = document.getElementById("CategoriasSelect");
    const categorias = Array.from(CategoriasSelect.selectedOptions).map(option => option.value);

    const tarefa = {
        id: id,
        nome: nome,
        dataTermino: dataTermino,
        prioridade: prioridade,
        status: status,
        categorias: categorias,
        descricao: descricao
    }

    if (tarefaId)
        parseTarefas = parseTarefas.map((tarefaDaLista) => {
            if (tarefaDaLista.id == id) {
                return tarefa;
            }
            return tarefaDaLista;
        });
    else
        parseTarefas.push(tarefa);


    localStorage.setItem("tarefas", JSON.stringify(parseTarefas));

    window.location.href = "./index.html"
});
