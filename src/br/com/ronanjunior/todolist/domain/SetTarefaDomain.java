package br.com.ronanjunior.todolist.domain;

import java.util.Set;

public interface SetTarefaDomain {
    void adicionarTarefa(TarefaDomain tarefa);
    boolean contemTarefaPorNome(String nome);
    boolean removerTarefa(TarefaDomain tarefa);
    void listar(Set<TarefaDomain> set);
    void atualizarTarefa(String nomeAntigo, TarefaDomain tarefa);
    TarefaDomain buscarTarefaPorNome(String nome);
}
