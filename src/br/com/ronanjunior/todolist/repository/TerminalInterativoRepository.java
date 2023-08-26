package br.com.ronanjunior.todolist.repository;

import br.com.ronanjunior.todolist.domain.*;

import java.text.ParseException;
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
    private List<CategoriaDomain> categorias = new ArrayList<>();
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
                        adicionarTarefa(comandos.get(1), comandos.get(2));
                    } else {
                        System.out.println("Comando 'add' incorreto");
                        System.out.println(" - add <nome> <dataTermino>: adiciona uma nova tarefa");
                    }
                    break;
                case "tarefa":
                    if (comandos.size() > 1 && comandos.get(1).equals("list")) {
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
                    } else {
                        System.out.println("Comando 'tarefa list' incorreto");
                        System.out.println(" - tarefa list: para mostrar a lista de tarefas (ordenação padrão por prioridade), segue as opções:");
                        System.out.println("                -t: para mostrar a lista de tarefas por data de termino da tarefa");
                        System.out.println("                -s: para mostrar a lista de tarefas por status da tarefa");
                    }
                    break;
                case "update":
                    if (comandos.size() == 4) {
                        if (tarefas.contemTarefaPorNome(comandos.get(1))) {
                            TarefaDomain tarefaAntiga = tarefas.buscarTarefaPorNome(comandos.get(1));
                            TarefaDomain tarefaAtualizada = new TarefaRepository(dateManipulacao);
                            tarefaAtualizada.setNome(tarefaAntiga.getNome());
                            tarefaAtualizada.setDescricao(tarefaAntiga.getDescricao());
                            tarefaAtualizada.setStatus(tarefaAntiga.getStatus());
                            tarefaAtualizada.setDtTermino(tarefaAntiga.getDtTermino());
                            tarefaAtualizada.setPrioridade(tarefaAntiga.getPrioridade());
                            tarefaAtualizada.setCategorias(tarefaAntiga.getCategorias());
                            switch (comandos.get(2)) {
                                case "-n":
                                    tarefaAtualizada.setNome(comandos.get(3));
                                    break;
                                case "-d":
                                    tarefaAtualizada.setDescricao(comandos.get(3));
                                    break;
                                case "-t":
                                    try {
                                        Date dataTermino = dateManipulacao.converterStringParaDate(comandos.get(3), "dd/MM/yyyy");
                                        tarefaAtualizada.setDtTermino(dataTermino);
                                    } catch (ParseException e) {
                                        System.out.println("O formato da data de termino deve ser dd/MM/yyyy");
                                        break;
                                    }
                                    break;
                                case "-p":
                                    try {
                                        int prioridade = Integer.parseInt(comandos.get(3));
                                        if (prioridade >= 1 && prioridade <= 5) {
                                            tarefaAtualizada.setPrioridade(prioridade);
                                        } else {
                                            System.out.println("Número inválido para prioridade, precisa ser um valor entre 1 até 5");
                                            break;
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Número inválido para prioridade, precisa ser um valor entre 1 até 5");
                                        break;
                                    }
                                    break;
                                case "-c":
                                    CategoriaDomain categoria = new CategoriasRepository(comandos.get(3));
                                    if (categorias.contains(categoria)) {
                                        tarefaAtualizada.adicionarCategoria(categoria);
                                    } else {
                                        System.out.println("Categoria não existe");
                                        break label;
                                    }
                                    break;
                                case "-s":
                                    if (comandos.get(3).equals("ToDo") || comandos.get(3).equals("Doing") || comandos.get(3).equals("Done")) {
                                        tarefaAtualizada.setStatus(comandos.get(3));
                                    } else {
                                        System.out.println("Status invalido, segue as opões (ToDo, Doing e Done)");
                                        break label;
                                    }
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
                                    break label;
                            }
                            tarefas.atualizarTarefa(comandos.get(1), tarefaAtualizada);
                        } else {
                            System.out.printf("A tarefa com nome %s não existe na lista de tarefas\n", comandos.get(1));
                        }
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
                        removerTarefa(comandos.get(1));
                    } else {
                        System.out.println("Comando 'delete' incorreto");
                        System.out.println(" - delete <nome>: para deletar uma tarefa da lista");
                    }
                    break;
                case "categoria":
                    if (comandos.size() == 2) {
                        if (comandos.get(1).equals("list")) {
                            for (CategoriaDomain categoria : categorias) {
                                System.out.println(categoria);
                            }
                        } else {
                            CategoriaDomain categoria = new CategoriasRepository(comandos.get(1));
                            if (!categorias.contains(categoria)) {
                                categorias.add(categoria);
                            } else {
                                System.out.println("Já possui uma categoria com o mesmo nome");
                            }
                        }
                    } else {
                        System.out.println("Comando 'categoria' incorreto");
                        System.out.println(" - categoria <categoria>: para criar uma nova categoria");
                        System.out.println(" - categoria list: para mostrar a lista de categorias");
                    }
                    break;
                case "status":
                    if (comandos.get(1).equals("list")) {
                        System.out.println("ToDo");
                        System.out.println("Doing");
                        System.out.println("Done");
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

    private void adicionarTarefa(String nome, String dtTermino) {
        TarefaDomain tarefa;
        try {
            Date dataTermino = dateManipulacao.converterStringParaDate(dtTermino, "dd/MM/yyyy");
            tarefa = new TarefaRepository(nome, dataTermino, dateManipulacao);
            if (!tarefas.contemTarefaPorNome(tarefa.getNome())) {
                tarefas.adicionarTarefa(tarefa);
            } else {
                System.out.println("Já possui uma tarefa com o mesmo nome");
            }
        } catch (ParseException e) {
            System.out.println("O formato da data de termino deve ser dd/MM/yyyy");
        }
    }

    private void removerTarefa(String nome) {
        if (tarefas.contemTarefaPorNome(nome)) {
            TarefaDomain tarefaRemover = tarefas.buscarTarefaPorNome(nome);
            tarefas.removerTarefa(tarefaRemover);
        } else {
            System.out.printf("A tarefa com nome %s não existe na lista de tarefas\n", nome);
        }
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
    }
}
