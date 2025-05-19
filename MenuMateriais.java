import java.sql.*;
import java.util.Scanner;

public class MenuMateriais {
    public static void exibir(Scanner sc) {
        try (Connection conn = DriverManager.getConnection(
            "jdbc:mysql://petjoy-db:3306/petjoy?useSSL=false&serverTimezone=UTC",
            "root", "1234");) {
            int opcao;
            do {
                System.out.println("\n--- MENU MATERIAIS ---");
                System.out.println("1. Cadastrar material");
                System.out.println("2. Listar materiais");
                System.out.println("3. Atualizar material");
                System.out.println("4. Remover material");
                System.out.println("0. Voltar");
                System.out.print("Escolha: ");
                opcao = sc.nextInt();
                sc.nextLine();

                switch (opcao) {
                    case 1 -> cadastrarMaterial(conn, sc);
                    case 2 -> listarMateriais(conn);
                    case 3 -> atualizarMaterial(conn, sc);
                    case 4 -> removerMaterial(conn, sc);
                }
            } while (opcao != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void cadastrarMaterial(Connection conn, Scanner sc) throws SQLException {
        System.out.print("ID do material: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Nome do material: ");
        String nome = sc.nextLine();

        String sql = "INSERT INTO materiais (id, nome) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, nome);
            stmt.executeUpdate();
            System.out.println("Material cadastrado com sucesso!");
        }
    }

    private static void listarMateriais(Connection conn) throws SQLException {
        String sql = "SELECT * FROM materiais";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Lista de Materiais ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s\n", rs.getInt("id"), rs.getString("nome"));
            }
        }
    }

    private static void atualizarMaterial(Connection conn, Scanner sc) throws SQLException {
        System.out.print("ID do material a atualizar: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Novo nome do material: ");
        String novoNome = sc.nextLine();

        String sql = "UPDATE materiais SET nome = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoNome);
            stmt.setInt(2, id);
            int linhas = stmt.executeUpdate();
            if (linhas > 0)
                System.out.println("Material atualizado com sucesso!");
            else
                System.out.println("Material não encontrado.");
        }
    }

    private static void removerMaterial(Connection conn, Scanner sc) throws SQLException {
        System.out.print("ID do material a remover: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM materiais WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();
            if (linhas > 0)
                System.out.println("Material removido com sucesso!");
            else
                System.out.println("Material não encontrado.");
        }
    }
}