package br.com.ronanjunior.todolist.domain;

import java.util.List;

public interface ManipulacaoArquivoDomain {
    public void carregarDiretorioTodolist(SetTarefaDomain tarefas, List<CategoriaDomain> categorias);
    boolean verificarExistenciaDiretorio(String nomeDiretorio);
    boolean verificarExistenciaArquivo(String nomeArquivo, String complementoPath);
    List<List<String>> lerArquivo(String nomeArquivo, String complementoPath);
    void escreverNoFimDoArquivo(String nomeArquivo, String complementoPath, List<String> linha);
    void escreverEmLinhaEspecifica(String nomeArquivo, String complementoPath, String filtroPrimeiraPalavra, List<String> novaLinha);
    void excluirLinhaEspecifica(String nomeArquivo, String complementoPath, String filtroPrimeiraPalavra);
}
