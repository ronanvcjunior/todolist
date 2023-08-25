package br.com.ronanjunior.todolist.domain;

import java.util.Set;

public interface SetTarefaDomain {
    void adicionarTarefa(TarefaDomain tarefa);
    boolean removerTarefa(TarefaDomain tarefa);
    void listar(Set<TarefaDomain> set);
    void atualizarTarefa(TarefaDomain tarefa);

}
