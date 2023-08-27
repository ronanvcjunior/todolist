package br.com.ronanjunior.todolist;

import br.com.ronanjunior.todolist.domain.*;
import br.com.ronanjunior.todolist.repository.*;

import java.util.*;

public class App {
    final static TreeSet<TarefaDomain> setPrioridade = new TreeSet<>(Comparator.comparing(TarefaDomain::getPrioridade).reversed().thenComparing(Comparator.comparing(TarefaDomain::getNome)));
    final static SetTarefaDomain setTarefas = new SetTarefaRepository(setPrioridade);
    final static TerminalInterativoDomain terminalInterativo = new TerminalInterativoRepository(new Scanner(System.in), setTarefas);
    public static void main(String[] args) {
        terminalInterativo.runTerminalInterativo();
    }
}
