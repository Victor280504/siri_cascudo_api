import dao.CategoryDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import model.Category;

public class App {
    public static void main(String[] args) throws Exception {
        boolean exit = false;
        Scanner response = new Scanner(System.in);
        int option = 0;

        while (!exit) {
            showMenu();
            option = response.nextInt();
            System.out.println();
            exit = action(option, response);

            System.out.println();

            if (option != 5) {
                System.out.print(" Pressione qualquer tecla para voltar para o menu: ");
                response.next();
            }

            try {
                clearConsole();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        response.close();
    }

    public static void showMenu() {

        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println("1 - Listar todas as categorias");
        System.out.println("2 - Listar apenas uma categoria");
        System.out.println("3 - Exluir categoria");
        System.out.println("4 - Cadastrar categoria");
        System.out.println("5 - Alterar categoria");
        System.out.println("6 - Sair");
        System.out.println();
        System.out.println("--------------------------------------");

        System.out.print("Digite a opção que deseja realizar: ");

    }

    public static boolean action(int option, Scanner response) throws SQLException {

        try {
            clearConsole();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Category category = new Category();
        CategoryDAO categoryDAO = new CategoryDAO();
        int id = 0;

        switch (option) {
            case 1:
                System.out.println("1 - Todas as categorias\n");
                System.out.println();

                ArrayList<Category> categories = categoryDAO.getAllCategories();
                for (Category objCategory : categories) {
                    System.out.println(objCategory.getToString());
                }

                return false;

            case 2:
                System.out.println("2 - Categoria\n");
                System.out.println();
                System.out.print(" Digite o id da categoria: ");
                id = response.nextInt();

                category = new Category();
                category.setId(id);
                Category objCategory = categoryDAO.getCategory(category);
                System.out.println(objCategory.getToString());
                return false;

            case 3:
                System.out.println("3 - Excluir categoria\n");
                System.out.println();
                System.out.print(" Digite o id da categoria: ");
                id = response.nextInt();

                category = new Category();
                category.setId(id);
                categoryDAO.delete(category);
                return false;

            case 4:
                System.out.println("4 - Cadastrar categoria\n");
                System.out.println();
                category = new Category();

                System.out.print(" Descrição: ");
                clearBuffer(response);
                category.setName(response.nextLine());

                categoryDAO.add(category);

                return false;

            case 5:
                System.out.println("5 - Editar categoria");
                System.out.println();
                category = new Category();

                System.out.print(" Digite o id da categoria que deseja editar: ");
                category.setId(response.nextInt());
                clearBuffer(response);

                Category oldCategory = categoryDAO.getCategory(category);

                System.out.print(" Descrição(" + oldCategory.getName() + "): ");
                category.setName(response.nextLine());

                categoryDAO.update(category);

                return false;

            case 6:
                return true;

            default:
                return false;
        }

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
