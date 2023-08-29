package br.com.ronanjunior.todolist.repository;

import br.com.ronanjunior.todolist.domain.CategoriaDomain;
import br.com.ronanjunior.todolist.domain.ManipulacaoArquivoDomain;
import br.com.ronanjunior.todolist.domain.SetTarefaDomain;
import br.com.ronanjunior.todolist.domain.TarefaDomain;

import java.util.Collections;
import java.util.concurrent.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ManipulacaoArquivoRepository implements ManipulacaoArquivoDomain {
    private String path;

    public ManipulacaoArquivoRepository() {
        this.path = System.getProperty("user.dir");
    }

    @Override
    public void carregarDiretorioTodolist(SetTarefaDomain tarefas, List<CategoriaDomain> categorias) {
        if (!verificarExistenciaArquivo("tarefas.csv", ".todolist") || !verificarExistenciaArquivo("categorias.csv", ".todolist")) {
            montarToDoList(".todolist");
        }

        carregarToDoList(tarefas, categorias);
    }

    private void montarToDoList(String complementoPath) {
        System.out.print("montando ToDoList");

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        ScheduledFuture<?> progressFuture = executorService.scheduleAtFixedRate(() -> {
            System.out.print(".");
        }, 0, 1, TimeUnit.SECONDS);

        criarDiretorio(complementoPath);

        Thread criarArquivoTarefasThread = new Thread(() -> {
            criarArquivo("tarefas.csv", complementoPath);
            if (arquivoEstaVazio("tarefas.csv", complementoPath)) {
                escreverNoFimDoArquivo("tarefas.csv", complementoPath, Arrays.asList("Nome", "Descrição", "Data de Término", "Prioridade", "Categorias", "Status"));
            }
        });

        Thread criarArquivoCategoriasThread = new Thread(() -> {
            criarArquivo("categorias.csv", complementoPath);
            if (arquivoEstaVazio("categorias.csv", complementoPath)) {
                escreverNoFimDoArquivo("categorias.csv", complementoPath, Collections.singletonList("Nome"));
            }
        });

        criarArquivoTarefasThread.start();
        criarArquivoCategoriasThread.start();

        try {
            criarArquivoTarefasThread.join();
            criarArquivoCategoriasThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        progressFuture.cancel(true);
        executorService.shutdown();

        System.out.println(" concluído.");
    }

    private void carregarToDoList(SetTarefaDomain tarefas, List<CategoriaDomain> categorias) {
        System.out.print("carregando ToDoList");

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        ScheduledFuture<?> progressFuture = executorService.scheduleAtFixedRate(() -> {
            System.out.print(".");
        }, 0, 1, TimeUnit.SECONDS);

        Thread lerArquivoTarefasThread = new Thread(() -> {
            List<List<String>> tarefasStringList = lerArquivo("tarefas.csv", ".todolist");
            for (List<String> tarefasString : tarefasStringList) {
                TarefaDomain tarefa = new TarefaRepository(tarefasString, new DateManipulacaoRepository());
                tarefas.adicionarTarefa(tarefa);
            }
        });

        Thread lerArquivoCategoriasThread = new Thread(() -> {
            List<List<String>> categoriasStringList = lerArquivo("categorias.csv", ".todolist");
            categorias.addAll(categoriasStringList.stream().map(strings -> new CategoriasRepository(strings.get(0))).collect(Collectors.toList()));
        });

        lerArquivoTarefasThread.start();
        lerArquivoCategoriasThread.start();

        try {
            lerArquivoTarefasThread.join();
            lerArquivoCategoriasThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        progressFuture.cancel(true);
        executorService.shutdown();

        System.out.println(" concluído.");
    }

    private boolean arquivoEstaVazio(String nomeArquivo, String complementoPath) {
        File arquivo = new File(path + "/" + complementoPath, nomeArquivo);
        return arquivo.length() == 0;
    }

    private void criarDiretorio(String nomeDiretorio) {
        new File(path, nomeDiretorio).mkdirs();
    }

    private void criarArquivo(String nomeArquivo, String complementoPath) {
        File arquivo = new File(path + "/" + complementoPath, nomeArquivo);
        try {
            arquivo.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean verificarExistenciaDiretorio(String nomeDiretorio) {
        File diretorio = new File(path, nomeDiretorio);
        return diretorio.exists() && diretorio.isDirectory();
    }

    @Override
    public boolean verificarExistenciaArquivo(String nomeArquivo, String complementoPath) {
        File arquivo = new File(path + "/" + complementoPath, nomeArquivo);
        return arquivo.exists() && arquivo.isFile();
    }

    @Override
    public List<List<String>> lerArquivo(String nomeArquivo, String complementoPath) {
        List<List<String>> lines = new ArrayList<>();
        File arquivo = new File(path + "/" + complementoPath, nomeArquivo);
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String line;
            boolean primeiraLinha = true;
            while ((line = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }
                List<String> columns = Arrays.asList(line.split(","));
                lines.add(columns);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    public void escreverNoFimDoArquivo(String nomeArquivo, String complementoPath, List<String> linha) {
        File arquivo = new File(path + "/" + complementoPath, nomeArquivo);
        String linhaCSV = String.join(",", linha);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
            writer.write(linhaCSV);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void escreverEmLinhaEspecifica(String nomeArquivo, String complementoPath, String filtroPrimeiraPalavra, List<String> novaLinha) {
        File arquivo = new File(path + "/" + complementoPath, nomeArquivo);
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String palavraNaLinha = line.substring(0, line.indexOf(','));
                if (palavraNaLinha.equals(filtroPrimeiraPalavra)) {
                    lines.add(String.join(",", novaLinha));
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void excluirLinhaEspecifica(String nomeArquivo, String complementoPath, String filtroPrimeiraPalavra) {
        File arquivo = new File(path + "/" + complementoPath, nomeArquivo);
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String palavraNaLinha = line.substring(0, line.indexOf(','));
                if (!palavraNaLinha.equals(filtroPrimeiraPalavra)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
