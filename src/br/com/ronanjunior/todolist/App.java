package br.com.ronanjunior.todolist;

import br.com.ronanjunior.todolist.domain.DateManipulacaoDomain;
import br.com.ronanjunior.todolist.domain.TarefaDomain;
import br.com.ronanjunior.todolist.domain.TerminalInterativoDomain;
import br.com.ronanjunior.todolist.repository.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class App {
    final static DateManipulacaoDomain dateManipulacao = new DateManipulacaoRepository();
    final static TerminalInterativoDomain terminalInterativo = new TerminalInterativoRepository(new Scanner(System.in));
    public static void main(String[] args) {
        TarefaDomain tarefa1 = new TarefaRepository("Task 1", new StatusRepository("A Fazer"), dateManipulacao);
        tarefa1.setCategorias(Arrays.asList(new CategoriasRepository("Dia a Dia"), new CategoriasRepository("Tarefa")));
        tarefa1.setDescricao("Tarefa numero 1");
        try {
            tarefa1.setDtTermino(dateManipulacao.converterStringParaDate("30/08/2023", "dd/MM/yyyy"));
        } catch (ParseException e) {
            System.err.println("ERROR: " + e);
        }
        System.out.println(tarefa1);

        terminalInterativo.runTerminalInterativo();
    }
}
