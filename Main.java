import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\n=== PETJOY ===");
            System.out.println("1. Menu de Pets");
            System.out.println("2. Menu de Materiais");
            System.out.println("3. Menu de Atividades");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> MenuPets.exibir(sc);
                case 2 -> MenuMateriais.exibir(sc);
                case 3 -> MenuAtividades.exibir(sc);
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }
}
