package br.com.ronanjunior.todolist.repository;

import br.com.ronanjunior.todolist.domain.*;

import java.text.ParseException;
import java.util.concurrent.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TerminalInterativoRepository implements TerminalInterativoDomain {
    private Scanner scanner;
    private Boolean running;
    private SetTarefaDomain tarefas;
    private final DateManipulacaoDomain dateManipulacao = new DateManipulacaoRepository();
    private final TreeSet<TarefaDomain> setPrioridade = new TreeSet<>(Comparator.comparing(TarefaDomain::getPrioridade).reversed().thenComparing(Comparator.comparing(TarefaDomain::getNome)));
    private final TreeSet<TarefaDomain> setDtTermino = new TreeSet<>(Comparator.comparing(TarefaDomain::getDtTermino).thenComparing(Comparator.comparing(TarefaDomain::getNome)));
    private final List<CategoriaDomain> categorias = new ArrayList<>();
    final static ManipulacaoArquivoDomain manipulacaoArquivo = new ManipulacaoArquivoRepository();
    private final Comparator<TarefaDomain> statusComparator = Comparator.comparing(tarefa -> {
        String status = tarefa.getStatus();
        if (status.equals("ToDo")) {
            return 1;
        } else if (status.equals("Doing")) {
            return 2;
        } else if (status.equals("Done")) {
            return 3;
        }
        return 0;
    });
    private final TreeSet<TarefaDomain> setStatus = new TreeSet<>(statusComparator.thenComparing(Comparator.comparing(TarefaDomain::getNome)));

    public TerminalInterativoRepository(Scanner scanner, SetTarefaDomain tarefas) {
        this.scanner = scanner;
        this.running = true;
        this.tarefas = tarefas;
    }

    @Override
    public void runTerminalInterativo() {
        manipulacaoArquivo.carregarDiretorioTodolist(tarefas, categorias);
        System.out.println("Digite um comando ('help' para ajuda, 'exit' para sair): ");

        while (running) {
            System.out.print("todolist> ");
            List<String> comandos = lerTerminal();

            label:
            switch (comandos.get(0)) {
                case "help":
                    displayHelp();
                    break;
                case "add":
                    if (comandos.size() == 3) {
                        validarAdicionarTarefa(comandos.get(1), comandos.get(2));
                    } else {
                        System.out.println("Comando 'add' incorreto");
                        System.out.println(" - add <nome> <dataTermino>: adiciona uma nova tarefa");
                    }
                    break;
                case "tarefa":
                    if (comandos.size() > 1 && comandos.get(1).equals("list")) {
                        validarListarTarefas(comandos);
                    } else {
                        System.out.println("Comando 'tarefa list' incorreto");
                        System.out.println(" - tarefa list: para mostrar a lista de tarefas (ordenação padrão por prioridade), segue as opções:");
                        System.out.println("                -t: para mostrar a lista de tarefas por data de termino da tarefa");
                        System.out.println("                -s: para mostrar a lista de tarefas por status da tarefa");
                    }
                    break;
                case "update":
                    if (comandos.size() >= 3) {
                        validarAtualizarTarefa(comandos);
                    } else {
                        System.out.println("Comando 'update' incorreto");
                        System.out.println(" - update <nome>: para fazer update em uma determinada tarefa, segue as opções:");
                        System.out.println("                  -n <novoNome>: para renomear a tarefa");
                        System.out.println("                  -d <descricao>: para alterar a descrição da tarefa");
                        System.out.println("                  -t <dataTermino>: para alterar a data de termino da tarefa");
                        System.out.println("                  -p <prioridade>: para alterar a prioridade da tarefa (valores de 1 até 5)");
                        System.out.println("                  -c <categoria>: para adicionar uma categoria para a tarefa");
                        System.out.println("                  -s <status>: para alterar o status da tarefa para as opções (ToDo, Doing e Done)");
                    }
                    break;
                case "delete":
                    if (comandos.size() == 2) {
                        validarDeletarTarefa(comandos.get(1));
                    } else {
                        System.out.println("Comando 'delete' incorreto");
                        System.out.println(" - delete <nome>: para deletar uma tarefa da lista");
                    }
                    break;
                case "categoria":
                    if (comandos.size() >= 2) {
                        validarCategoria(comandos);
                    } else {
                        System.out.println("Comando 'categoria' incorreto");
                        System.out.println(" - categoria <categoria>: para criar uma nova categoria");
                        System.out.println(" - categoria list: para mostrar a lista de categorias");
                    }
                    break;
                case "status":
                    if (comandos.size() == 2 && comandos.get(1).equals("list")) {
                        System.out.println("\tToDo");
                        System.out.println("\tDoing");
                        System.out.println("\tDone");
                    } else {
                        System.out.println("Comando 'status list' incorreto");
                        System.out.println(" - status list: para mostrar a lista de status");
                    }
                    break;
                case "exit":
                    running = false;
                    System.out.println("Saindo do todolist...");
                    break;
                default:
                    System.out.println("Comando não reconhecido. Digite 'help' para ver as opções.");
            }
        }

        scanner.close();
    }

    private List<String> lerTerminal() {
        List<String> comandos = new ArrayList<>();

        String command = scanner.nextLine();

        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
        Matcher matcher = pattern.matcher(command);

        while (matcher.find()) {
            String comando = matcher.group(1);
            if (comando == null) {
                comando = matcher.group(2);
            }

            comandos.add(comando);
        }

        return comandos;
    }

    private void validarAdicionarTarefa(String nome, String dtTermino) {
        TarefaDomain tarefa;
        try {
            Date dataTermino = dateManipulacao.converterStringParaDate(dtTermino, "dd/MM/yyyy");
            tarefa = new TarefaRepository(nome, dataTermino, dateManipulacao);
            if (!tarefas.contemTarefaPorNome(tarefa.getNome())) {
                adicionarTarefa(tarefa);
            } else {
                System.out.println("Já possui uma tarefa com o mesmo nome");
            }
        } catch (ParseException e) {
            System.out.println("O formato da data de termino deve ser dd/MM/yyyy");
        }
    }

    private void validarListarTarefas(List<String> comandos) {
        if (comandos.size() == 2) {
            tarefas.listar(setPrioridade);
        } else if (comandos.size() == 3) {
            if (comandos.get(2).equals("-s")) {
                tarefas.listar(setStatus);
            } else if (comandos.get(2).equals("-t")) {
                tarefas.listar(setDtTermino);
            } else {
                System.out.println("Comando 'tarefa list' incorreto");
                System.out.println(" - tarefa list: para mostrar a lista de tarefas (ordenação padrão por prioridade), segue as opções:");
                System.out.println("                -t: para mostrar a lista de tarefas por data de termino da tarefa");
                System.out.println("                -s: para mostrar a lista de tarefas por status da tarefa");
            }
        } else {
            System.out.println("Comando 'tarefa list' incorreto");
            System.out.println(" - tarefa list: para mostrar a lista de tarefas (ordenação padrão por prioridade), segue as opções:");
            System.out.println("                -t: para mostrar a lista de tarefas por data de termino da tarefa");
            System.out.println("                -s: para mostrar a lista de tarefas por status da tarefa");
        }
    }


    private void validatDeletarTarefa(String nome) {
        if (tarefas.contemTarefaPorNome(nome)) {
            TarefaDomain tarefaRemover = tarefas.buscarTarefaPorNome(nome);
            deletarTarefa(tarefaRemover);
        } else {
            System.out.printf("A tarefa com nome %s não existe na lista de tarefas\n", nome);
        }
    }

    private void adicionarTarefa(TarefaDomain tarefa) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        Thread adicionarTarefaLista = new Thread(() -> {
            tarefas.adicionarTarefa(tarefa);
        });

        Thread adicionarTarefaArquivo = new Thread(() -> {
            List<String> tarefaString = tarefa.converterTarefaParaListaString();
            manipulacaoArquivo.escreverNoFimDoArquivo("tarefas.csv", ".todolist", tarefaString);
        });

        adicionarTarefaLista.start();
        adicionarTarefaArquivo.start();

        try {
            adicionarTarefaLista.join();
            adicionarTarefaArquivo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }

    private void deletarTarefa(TarefaDomain tarefa) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        Thread adicionarTarefaLista = new Thread(() -> {
            tarefas.removerTarefa(tarefa);
        });

        Thread adicionarTarefaArquivo = new Thread(() -> {
            manipulacaoArquivo.excluirLinhaEspecifica("tarefas.csv", ".todolist", tarefa.getNome());
        });

        adicionarTarefaLista.start();
        adicionarTarefaArquivo.start();

        try {
            adicionarTarefaLista.join();
            adicionarTarefaArquivo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }

    private void validarAtualizarTarefa(List<String> comandos) {
        if (tarefas.contemTarefaPorNome(comandos.get(1))) {
            TarefaDomain tarefaAntiga = tarefas.buscarTarefaPorNome(comandos.get(1));
            TarefaDomain tarefaAtualizada = criarCopiaTarefa(tarefaAntiga);

            switch (comandos.get(2)) {
                case "-n":
                    tarefaAtualizada.setNome(comandos.get(3));
                    break;
                case "-d":
                    tarefaAtualizada.setDescricao(comandos.get(3));
                    break;
                case "-t":
                    validarAtualizarDataTermino(tarefaAtualizada, comandos.get(3));
                    break;
                case "-p":
                    validarAtualizarPrioridade(tarefaAtualizada, comandos.get(3));
                    break;
                case "-c":
                    validarAdicionarCategoria(tarefaAtualizada, comandos.get(3));
                    break;
                case "-s":
                    tarefaAtualizada.setStatus(comandos.get(3));
                    break;
                default:
                    System.out.println("Comando 'update' incorreto");
                    System.out.println(" - update <nome>: para fazer update em uma determinada tarefa, segue as opções:");
                    System.out.println("                  -n <novoNome>: para renomear a tarefa");
                    System.out.println("                  -d <descricao>: para alterar a descrição da tarefa");
                    System.out.println("                  -t <dataTermino>: para alterar a data de termino da tarefa");
                    System.out.println("                  -p <prioridade>: para alterar a prioridade da tarefa (valores de 1 até 5)");
                    System.out.println("                  -c <categoria>: para adicionar uma categoria para a tarefa");
                    System.out.println("                  -s <status>: para alterar o status da tarefa para as opções (ToDo, Doing e Done)");
                    break;
            }

            atualizarTarefa(comandos.get(1), tarefaAtualizada);
        } else {
            System.out.printf("A tarefa com nome %s não existe na lista de tarefas\n", comandos.get(1));
        }
    }

    private TarefaDomain criarCopiaTarefa(TarefaDomain tarefa) {
        TarefaDomain copia = new TarefaRepository(dateManipulacao);
        copia.setNome(tarefa.getNome());
        copia.setDescricao(tarefa.getDescricao());
        copia.setStatus(tarefa.getStatus());
        copia.setDtTermino(tarefa.getDtTermino());
        copia.setPrioridade(tarefa.getPrioridade());
        copia.setCategorias(tarefa.getCategorias());
        return copia;
    }

    private void validarAtualizarDataTermino(TarefaDomain tarefa, String dataTermino) {
        try {
            Date novaDataTermino = dateManipulacao.converterStringParaDate(dataTermino, "dd/MM/yyyy");
            tarefa.setDtTermino(novaDataTermino);
        } catch (ParseException e) {
            System.out.println("O formato da data de termino deve ser dd/MM/yyyy");
        }
    }

    private void validarAtualizarPrioridade(TarefaDomain tarefa, String prioridadeStr) {
        try {
            int prioridade = Integer.parseInt(prioridadeStr);
            if (prioridade >= 1 && prioridade <= 5) {
                tarefa.setPrioridade(prioridade);
            } else {
                System.out.println("Número inválido para prioridade, precisa ser um valor entre 1 até 5");
            }
        } catch (NumberFormatException e) {
            System.out.println("Número inválido para prioridade, precisa ser um valor entre 1 até 5");
        }
    }
    private void validarAdicionarCategoria(TarefaDomain tarefa, String nomeCategoria) {
        CategoriaDomain categoria = new CategoriasRepository(nomeCategoria);
        if (categorias.contains(categoria)) {
            tarefa.adicionarCategoria(categoria);
        } else {
            System.out.println("Categoria não existe");
        }
    }

    private void atualizarTarefa(String nomeAntigo, TarefaDomain tarefa) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        Thread adicionarTarefaLista = new Thread(() -> {
            tarefas.atualizarTarefa(nomeAntigo, tarefa);
        });

        Thread adicionarTarefaArquivo = new Thread(() -> {
            List<String> tarefaString = tarefa.converterTarefaParaListaString();
            manipulacaoArquivo.escreverEmLinhaEspecifica("tarefas.csv", ".todolist", nomeAntigo, tarefaString);
        });

        adicionarTarefaLista.start();
        adicionarTarefaArquivo.start();

        try {
            adicionarTarefaLista.join();
            adicionarTarefaArquivo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }

    private void validarDeletarTarefa(String nome) {
        if (tarefas.contemTarefaPorNome(nome)) {
            TarefaDomain tarefaRemover = tarefas.buscarTarefaPorNome(nome);
            deletarTarefa(tarefaRemover);
        } else {
            System.out.printf("A tarefa com nome %s não existe na lista de tarefas\n", nome);
        }
    }

    private void validarCategoria(List<String> comandos) {
        if (comandos.get(1).equals("list")) {
            for (CategoriaDomain categoria : categorias) {
                System.out.println("\t" + categoria);
            }
        } else {
            CategoriaDomain categoria = new CategoriasRepository(comandos.get(1));
            if (!categorias.contains(categoria)) {
                adicionarCategoria(categoria);
            } else {
                System.out.println("Já possui uma categoria com o mesmo nome");
            }
        }
    }

    private void adicionarCategoria(CategoriaDomain categoria) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        Thread adicionarCategoriaLista = new Thread(() -> {
            categorias.add(categoria);
        });

        Thread adicionarCategoriaArquivo = new Thread(() -> {
            List<String> categoriaString = Collections.singletonList(categoria.getNome());
            manipulacaoArquivo.escreverNoFimDoArquivo("categorias.csv", ".todolist", categoriaString);
        });

        adicionarCategoriaLista.start();
        adicionarCategoriaArquivo.start();

        try {
            adicionarCategoriaLista.join();
            adicionarCategoriaArquivo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }

    private void displayHelp() {
        System.out.println("Opções disponíveis:");
        System.out.println(" - help: Mostra todas as opções disponíveis");
        System.out.println(" - add <nome> <dataTermino>: adiciona uma nova tarefa");
        System.out.println(" - update <nome>: para fazer update em uma determinada tarefa, segue as opções:");
        System.out.println("                  -n <novoNome>: para renomear a tarefa");
        System.out.println("                  -d <descricao>: para alterar a descrição da tarefa");
        System.out.println("                  -t <dataTermino>: para alterar a data de termino da tarefa");
        System.out.println("                  -p <prioridade>: para alterar a prioridade da tarefa (valores de 1 até 5)");
        System.out.println("                  -c <categoria>: para adicionar uma categoria para a tarefa");
        System.out.println("                  -s <status>: para alterar o status da tarefa para as opções (ToDo, Doing e Done)");
        System.out.println(" - tarefa list: para mostrar a lista de tarefas (ordenação padrão por prioridade), segue as opções:");
        System.out.println("                -t: para mostrar a lista de tarefas por data de termino da tarefa");
        System.out.println("                -s: para mostrar a lista de tarefas por status da tarefa");
        System.out.println(" - delete <nome>: para deletar uma tarefa da lista");
        System.out.println(" - status list: para mostrar a lista de status");
        System.out.println(" - categoria <categoria>: para criar uma nova categoria");
        System.out.println(" - categoria list: para mostrar a lista de categorias");
        System.out.println(" - exit: Sai do programa");
        System.out.println("Observações:");
        System.out.println(" - palavras compostas entre aspas (\"\")");
        System.out.println(" - formato da data (dd/MM/yyyy)");
    }
}
