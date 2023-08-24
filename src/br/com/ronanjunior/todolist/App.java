package br.com.ronanjunior.todolist;

import br.com.ronanjunior.todolist.domain.TarefaDomain;
import br.com.ronanjunior.todolist.repository.CategoriasRepository;
import br.com.ronanjunior.todolist.repository.StatusRepository;
import br.com.ronanjunior.todolist.repository.TarefaRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class App {
    public static void main(String[] args) {
        TarefaDomain tarefa1 = new TarefaRepository("Task 1", new StatusRepository("A Fazer"));
        tarefa1.setCategorias(Arrays.asList(new CategoriasRepository("Dia a Dia"), new CategoriasRepository("Tarefa")));
        tarefa1.setDescricao("Tarefa numero 1");
        tarefa1.setDtTermino(new Date());
        System.out.println(tarefa1);
    }
}
