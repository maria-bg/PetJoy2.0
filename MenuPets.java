import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

public class MenuPets {    
    public static void exibir(Scanner sc) {
        try (Connection conn = DriverManager.getConnection(
            "jdbc:mysql://petjoy-db:3306/petjoy?useSSL=false&serverTimezone=UTC",
            "root", "1234");) {
                
            int opcao;
            do {
                System.out.println("\n--- MENU PETS ---");
                System.out.println("1. Cadastrar hóspede");
                System.out.println("2. Listar hóspede(s)");
                System.out.println("3. Apagar hóspede");
                System.out.println("4. Atualizar informações de um hóspede");
                System.out.println("0. Voltar");
                System.out.print("Escolha: ");
                opcao = sc.nextInt();
                sc.nextLine();

                switch (opcao) {
                    case 1 -> cadastrarPet(conn, sc);
                    case 2 -> listarPets(conn);
                    case 3 -> removerPet(conn, sc);
                    case 4 -> atualizarPet(conn, sc);
                }
            } while (opcao != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void cadastrarPet(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Nome do pet: ");
        String nome = sc.nextLine();

        System.out.print("Idade: ");
        int idade = sc.nextInt();
        sc.nextLine();

        System.out.print("CPF do dono: ");
        String cpf = sc.nextLine();

        String verificaDono = "SELECT * FROM cliente WHERE cpf = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(verificaDono)) {
            checkStmt.setString(1, cpf);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Dono nao encontrado. Vamos cadastra-lo agora.");
                System.out.print("Nome do dono: ");
                String nomeDono = sc.nextLine();
                System.out.print("Telefone do dono: ");
                String telefone = sc.nextLine();

                String insereDono = "INSERT INTO cliente (cpf, nome, telefone) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insereDono)) {
                    insertStmt.setString(1, cpf);
                    insertStmt.setString(2, nomeDono);
                    insertStmt.setString(3, telefone);
                    insertStmt.executeUpdate();
                    System.out.println("Dono cadastrado com sucesso.");
                }
            }
        }

        System.out.print("Tempo de estadia (em dias): ");
        int dias = sc.nextInt();
        sc.nextLine();

        LocalDate entrada = LocalDate.now();
        LocalDate saida = entrada.plusDays(dias);

        String sql = "INSERT INTO pets (nome, idade, dono, data_entrada, data_saida) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nome);
            stmt.setInt(2, idade);
            stmt.setString(3, cpf);
            stmt.setDate(4, Date.valueOf(entrada));
            stmt.setDate(5, Date.valueOf(saida));
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                System.out.println("Pet cadastrado com sucesso! ID gerado: " + generatedId);
            } else {
                System.out.println("Pet cadastrado, mas ID não pôde ser recuperado.");
            }
        }
    }

    private static void listarPets(Connection conn) throws SQLException {
        String sql = "SELECT p.id, p.nome, p.idade, c.nome AS dono_nome, p.data_entrada, p.data_saida " +
                     "FROM pets p JOIN cliente c ON p.dono = c.cpf";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Lista de Pets ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Nome: %s | Idade: %d | Dono: %s | Entrada: %s | Saida: %s\n",
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("dono_nome"),
                        rs.getDate("data_entrada"),
                        rs.getDate("data_saida"));
            }
        }
    }
    
    private static void atualizarPet(Connection conn, Scanner sc) throws SQLException {
        System.out.print("ID do pet a atualizar: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.println("O que você deseja atualizar?");
        System.out.println("1. Nome do pet");
        System.out.println("2. Idade do pet");
        System.out.println("3. Tempo de estadia (recalcula a data de saída)");
        System.out.print("Escolha: ");
        int escolha = sc.nextInt();
        sc.nextLine();

        switch (escolha) {
            case 1 -> {
                System.out.print("Novo nome do pet: ");
                String novoNome = sc.nextLine();
                String sql = "UPDATE pets SET nome = ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, novoNome);
                    stmt.setInt(2, id);
                    int linhas = stmt.executeUpdate();
                    if (linhas > 0)
                        System.out.println("Nome atualizado com sucesso!");
                    else
                        System.out.println("Pet não encontrado.");
                }
            }
            case 2 -> {
                System.out.print("Nova idade do pet: ");
                int novaIdade = sc.nextInt();
                String sql = "UPDATE pets SET idade = ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, novaIdade);
                    stmt.setInt(2, id);
                    int linhas = stmt.executeUpdate();
                    if (linhas > 0)
                        System.out.println("Idade atualizada com sucesso!");
                    else
                        System.out.println("Pet não encontrado.");
                }
            }
            case 3 -> {
                String buscaEntrada = "SELECT data_entrada FROM pets WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(buscaEntrada)) {
                    stmt.setInt(1, id);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        Date dataEntrada = rs.getDate("data_entrada");
                        System.out.print("Novo tempo de estadia (em dias): ");
                        int novosDias = sc.nextInt();
                        sc.nextLine();
                        LocalDate novaSaida = dataEntrada.toLocalDate().plusDays(novosDias);

                        String atualizaSaida = "UPDATE pets SET data_saida = ? WHERE id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(atualizaSaida)) {
                            updateStmt.setDate(1, Date.valueOf(novaSaida));
                            updateStmt.setInt(2, id);
                            updateStmt.executeUpdate();
                            System.out.println("Data de saída atualizada com sucesso!");
                        }
                    } else {
                        System.out.println("Pet não encontrado.");
                    }
                }
            }
            default -> System.out.println("Opção inválida.");
        }
    }

    
    private static void removerPet(Connection conn, Scanner sc) throws SQLException {
        System.out.print("ID do pet a remover: ");
        int id = sc.nextInt();

    
        String sql = "DELETE FROM pets WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int linhas = stmt.executeUpdate();
            if (linhas > 0)
                System.out.println("Pet removido com sucesso!");
            else
                System.out.println("Pet nao encontrado.");
        }
    }
}
