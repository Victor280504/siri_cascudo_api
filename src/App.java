import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.CategoryDAO;
import model.Category;

public class App {
    public static void main(String[] args) throws Exception {
        boolean exit = false;
        Scanner response = new Scanner(System.in);

        while (!exit) {
            showMenu();
            int option = response.nextInt();
            System.out.println();

            exit = action(option, response);

            if (!exit) {
                System.out.print("\nPressione Enter para voltar ao menu...");
                clearBuffer(response);
                response.nextLine(); // Aguarda o Enter do usuário
            }

            try {
                clearConsole();
            } catch (Exception e) {
                System.out.println("Erro ao limpar o console: " + e.getMessage());
            }
        }

        response.close();
    }

    public static void showMenu() {
        System.out.println("--------------------------------------");
        System.out.println("1 - Listar todas as categorias");
        System.out.println("2 - Listar apenas uma categoria");
        System.out.println("3 - Excluir categoria");
        System.out.println("4 - Cadastrar categoria");
        System.out.println("5 - Alterar categoria");
        System.out.println("6 - Sair");
        System.out.println("--------------------------------------");
        System.out.print("Digite a opção que deseja realizar: ");
    }

    public static boolean action(int option, Scanner response) throws SQLException {
        Category category = new Category();
        CategoryDAO categoryDAO = new CategoryDAO();
        int id;

        switch (option) {
            case 1:
                System.out.println("1 - Todas as categorias\n");
                ArrayList<Category> categories = categoryDAO.getAllCategories();
                if (categories != null && !categories.isEmpty()) {
                    for (Category objCategory : categories) {
                        System.out.println(objCategory.getToString());
                    }
                } else {
                    System.out.println("Nenhuma categoria encontrada.");
                }
                break;

            case 2:
                System.out.println("2 - Categoria\n");
                System.out.print("Digite o id da categoria: ");
                id = response.nextInt();
                category.setId(id);
                Category objCategory = categoryDAO.getCategory(category);
                if (objCategory != null) {
                    System.out.println(objCategory.getToString());
                } else {
                    System.out.println("Categoria não encontrada.");
                }
                break;

            case 3:
                System.out.println("3 - Excluir categoria\n");
                System.out.print("Digite o id da categoria: ");
                id = response.nextInt();
                category.setId(id);
                categoryDAO.delete(category);
                System.out.println("Categoria excluída (se encontrada).");
                break;

            case 4:
                System.out.println("4 - Cadastrar categoria\n");
                clearBuffer(response);
                System.out.print("Nome: ");
                category.setName(response.nextLine());
                categoryDAO.add(category);
                System.out.println("Categoria cadastrada.");
                break;

            case 5:
                System.out.println("5 - Editar categoria");
                System.out.print("Digite o id da categoria que deseja editar: ");
                id = response.nextInt();
                clearBuffer(response);
                category.setId(id);

                Category oldCategory = categoryDAO.getCategory(category);
                if (oldCategory != null) {
                    System.out.print("Nome (" + oldCategory.getName() + "): ");
                    category.setName(response.nextLine());
                    categoryDAO.update(category);
                    System.out.println("Categoria atualizada.");
                } else {
                    System.out.println("Categoria não encontrada.");
                }
                break;

            case 6:
                return true;

            default:
                System.out.println("Opção inválida! Tente novamente.");
        }
        return false;
    }

    public static void clearConsole() throws IOException, InterruptedException {
        if (System.getProperty("os.name").contains("Windows"))
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        else
            Runtime.getRuntime().exec("clear");
    }

    private static void clearBuffer(Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}