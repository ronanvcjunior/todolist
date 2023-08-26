package br.com.ronanjunior.todolist.repository;

import br.com.ronanjunior.todolist.domain.SetTarefaDomain;
import br.com.ronanjunior.todolist.domain.TarefaDomain;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class SetTarefaRepository implements SetTarefaDomain {
    private Set<TarefaDomain> tarefas;

    public SetTarefaRepository(Set<TarefaDomain> tarefas) {
        this.tarefas = tarefas;
    }

    @Override
    public void adicionarTarefa(TarefaDomain tarefa) {
        tarefas.add(tarefa);
    }

    public boolean contemTarefaPorNome(String nome) {
        return tarefas.stream().anyMatch(tarefa -> tarefa.getNome().equals(nome));
    }

    @Override
    public boolean removerTarefa(TarefaDomain tarefa) {
        return tarefas.remove(tarefa);
    }

    @Override
    public void listar(Set<TarefaDomain> set) {
        ordenar(set);
        for (TarefaDomain tarefa : tarefas) {
            System.out.println(tarefa);
        }
    }

    private void ordenar(Set<TarefaDomain> set) {
        set.addAll(this.tarefas);
        this.tarefas = set;
    }

    @Override
    public void atualizarTarefa(String nomeAntigo, TarefaDomain tarefaAtualizada) {
        TarefaDomain tarefaExistente = buscarTarefaPorNome(nomeAntigo);
        tarefas.remove(tarefaExistente);
        tarefas.add(tarefaAtualizada);
    }

    public TarefaDomain buscarTarefaPorNome(String nome) {
        return tarefas.stream()
                .filter(tarefa -> tarefa.getNome().equals(nome))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "SetTarefaRepository{" +
                "tarefas=" + tarefas +
                '}';
    }
}
