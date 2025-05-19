import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate;

public class MenuAtividades {
    public static void exibir(Scanner sc) {
        try (Connection conn = DriverManager.getConnection(
            "jdbc:mysql://petjoy-db:3306/petjoy?useSSL=false&serverTimezone=UTC",
            "root", "1234");) {
            int opcao;
            do {
                System.out.println("\n--- MENU ATIVIDADES ---");
                System.out.println("1. Registrar atividade");
                System.out.println("2. Listar atividades");
                System.out.println("3. Remover atividade");
                System.out.println("0. Voltar");
                System.out.print("Escolha: ");
                opcao = sc.nextInt();
                sc.nextLine();

                switch (opcao) {
                    case 1 -> registrarAtividade(conn, sc);
                    case 2 -> listarAtividades(conn);
                    case 3 -> removerAtividade(conn, sc);
                }
            } while (opcao != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void registrarAtividade(Connection conn, Scanner sc) throws SQLException {
        System.out.print("ID do pet: ");
        int idPet = sc.nextInt();
        sc.nextLine();

        System.out.print("Tipo de atividade (Banho, Tosa, etc.): ");
        String tipo = sc.nextLine();

        LocalDate data = LocalDate.now();

        String sqlAtividade = "INSERT INTO atividade (tipo, data, id_pet) VALUES (?, ?, ?)";
        int idAtividade = -1;
        try (PreparedStatement stmt = conn.prepareStatement(sqlAtividade, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tipo);
            stmt.setDate(2, Date.valueOf(data));
            stmt.setInt(3, idPet);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idAtividade = rs.getInt(1);
            }
        }

        System.out.print("Quantos materiais foram utilizados? ");
        int qtdeMateriais = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < qtdeMateriais; i++) {
            System.out.print("ID do material: ");
            int idMaterial = sc.nextInt();
            System.out.print("Quantidade utilizada: ");
            int quantidade = sc.nextInt();
            sc.nextLine();

            String sqlMaterial = "INSERT INTO material_utilizado (id_atividade, id_material, quantidade) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlMaterial)) {
                stmt.setInt(1, idAtividade);
                stmt.setInt(2, idMaterial);
                stmt.setInt(3, quantidade);
                stmt.executeUpdate();
            }
        }

        System.out.println("Atividade registrada com sucesso!");
    }

    private static void listarAtividades(Connection conn) throws SQLException {
        String sql = """
            SELECT a.id, a.tipo, a.data, p.nome AS nome_pet, m.nome AS nome_material, mu.quantidade
            FROM atividade a
            JOIN pets p ON a.id_pet = p.id
            LEFT JOIN material_utilizado mu ON a.id = mu.id_atividade
            LEFT JOIN materiais m ON mu.id_material = m.id
            ORDER BY a.id, m.nome
            """;

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Atividades Registradas ---");
            int ultimaAtividade = -1;

            while (rs.next()) {
                int id = rs.getInt("id");
                if (id != ultimaAtividade) {
                    System.out.printf("\nID Atividade: %d | Tipo: %s | Data: %s | Pet: %s\n",
                            id,
                            rs.getString("tipo"),
                            rs.getDate("data"),
                            rs.getString("nome_pet"));
                    System.out.println("Materiais utilizados:");
                    ultimaAtividade = id;
                }
                String nomeMaterial = rs.getString("nome_material");
                int quantidade = rs.getInt("quantidade");
                if (nomeMaterial != null) {
                    System.out.printf("  - %s: %d unidade(s)\n", nomeMaterial, quantidade);
                }
            }
        }
    }

    private static void removerAtividade(Connection conn, Scanner sc) throws SQLException {
        System.out.print("ID da atividade a remover: ");
        int id = sc.nextInt();
        sc.nextLine();

        // Primeiro remover os materiais utilizados
        String deleteMateriais = "DELETE FROM material_utilizado WHERE id_atividade = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteMateriais)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        // Depois remover a atividade
        String deleteAtividade = "DELETE FROM atividade WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteAtividade)) {
            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();
            if (linhas > 0)
                System.out.println("Atividade removida com sucesso!");
            else
                System.out.println("Atividade não encontrada.");
        }
    }
}