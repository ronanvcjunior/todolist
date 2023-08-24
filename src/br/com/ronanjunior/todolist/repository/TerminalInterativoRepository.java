package br.com.ronanjunior.todolist.repository;

import br.com.ronanjunior.todolist.domain.TerminalInterativoDomain;

import java.util.Scanner;

public class TerminalInterativoRepository implements TerminalInterativoDomain {
    private Scanner scanner;
    private Boolean running;

    public TerminalInterativoRepository(Scanner scanner) {
        this.scanner = scanner;
        this.running = true;
    }

    @Override
    public void runTerminalInterativo() {
        System.out.println("Digite um comando ('help' para ajuda, 'exit' para sair): ");

        while (running) {
            System.out.print("todolist> ");
            String command = scanner.nextLine();

            switch (command) {
                case "help":
                    displayHelp();
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

    private void displayHelp() {
        System.out.println("Opções disponíveis:");
        System.out.println(" - help: Mostra todas as opções disponíveis");
        System.out.println(" - exit: Sai do programa");
    }
}
