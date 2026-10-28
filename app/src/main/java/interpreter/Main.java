package interpreter;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.Repository;
import interpreter.view.Examples;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== TOY LANGUAGE INTERPRETER ===");
            System.out.println("1. Run Example 1: int v; v=2; print(v)");
            System.out.println("2. Run Example 2: int a; int b; a=2+3*5; b=a+1; print(b)");
            System.out.println("3. Run Example 3: bool a; int v; a=true; (if a then v=2 else v=3); print(v)");
            System.out.println("4. Run Example 4 (Seminar 4 B3)");

            System.out.println("0. Exit");
            System.out.print("Alege o optiune: ");

            String option = scanner.nextLine();
            if (option.equals("0")) {
                System.out.println("Iesire...");
                break;
            }

            IStmt selectedProgram;
            switch (option) {
                case "1" -> selectedProgram = Examples.EX1;
                case "2" -> selectedProgram = Examples.EX2;
                case "3" -> selectedProgram = Examples.EX3;
                case "4" -> selectedProgram = Examples.EX4;

                default -> {
                    System.out.println("Optiune invalida!");
                    continue;
                }
            }

            try {
                // Initializam starea programului cu ADT-uri goale
                PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), selectedProgram);
                Repository repo = new Repository(state);

                // Flag-ul true afiseaza pasii intermediari
                Controller controller = new Controller(repo, true);

                System.out.println("\n--- Executie ---");
                controller.allStep();

            } catch (MyException e) {
                System.out.println("EROARE: " + e.getMessage());
            }
        }
    }
}
