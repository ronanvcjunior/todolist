package br.com.ronanjunior.todolist;

import br.com.ronanjunior.todolist.domain.DateManipulacaoDomain;
import br.com.ronanjunior.todolist.domain.SetTarefaDomain;
import br.com.ronanjunior.todolist.domain.TarefaDomain;
import br.com.ronanjunior.todolist.domain.TerminalInterativoDomain;
import br.com.ronanjunior.todolist.repository.*;

import java.text.ParseException;
import java.util.*;

public class App {
    final static DateManipulacaoDomain dateManipulacao = new DateManipulacaoRepository();
    final static TerminalInterativoDomain terminalInterativo = new TerminalInterativoRepository(new Scanner(System.in));

    final static TreeSet<TarefaDomain> setPrioridade = new TreeSet<>(Comparator.comparing(TarefaDomain::getPrioridade).reversed().thenComparing(Comparator.comparing(TarefaDomain::getNome)));
    final static TreeSet<TarefaDomain> setDtTermino = new TreeSet<>(Comparator.comparing(TarefaDomain::getDtTermino).thenComparing(Comparator.comparing(TarefaDomain::getNome)));
    final static SetTarefaDomain setTarefas = new SetTarefaRepository(setPrioridade);
    public static void main(String[] args) {
        TarefaDomain tarefa1 = new TarefaRepository("Task 1", new StatusRepository("Concluído"), dateManipulacao);
        tarefa1.setCategorias(Arrays.asList(new CategoriasRepository("Dia a Dia"), new CategoriasRepository("Tarefa")));
        tarefa1.setDescricao("Tarefa numero 1");
        try {
            tarefa1.setDtTermino(dateManipulacao.converterStringParaDate("02/08/2023", "dd/MM/yyyy"));
        } catch (ParseException e) {
            System.err.println("ERROR: " + e);
        }
//        System.out.println(tarefa1);
//
//        terminalInterativo.runTerminalInterativo();
        try {
            setTarefas.adicionarTarefa(new TarefaRepository("Task 2", new StatusRepository("A Fazer"), dateManipulacao, dateManipulacao.converterStringParaDate("30/08/2023", "dd/MM/yyyy")));
        } catch (ParseException e) {
            System.err.println("ERROR:" + e);
        }

        try {
            setTarefas.adicionarTarefa(new TarefaRepository("Task 1", new StatusRepository("A Fazer"), dateManipulacao, dateManipulacao.converterStringParaDate("30/08/2023", "dd/MM/yyyy")));
        } catch (ParseException e) {
            System.err.println("ERROR:" + e);
        }
        try {
            setTarefas.adicionarTarefa(new TarefaRepository("Task 3", new StatusRepository("Feito"), dateManipulacao, dateManipulacao.converterStringParaDate("29/08/2023", "dd/MM/yyyy")));
        } catch (ParseException e) {
            System.err.println("ERROR:" + e);
        }

        setTarefas.listar(setPrioridade);
        System.out.println();
        setTarefas.listar(setDtTermino);
        System.out.println();
        setTarefas.atualizarTarefa(tarefa1);
        setTarefas.listar(setDtTermino);
        System.out.println();
        try {
            setTarefas.atualizarTarefa(new TarefaRepository("Task 1", new StatusRepository("A Fazer"), dateManipulacao, dateManipulacao.converterStringParaDate("30/08/2023", "dd/MM/yyyy")));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        setTarefas.listar(setDtTermino);
    }
}
