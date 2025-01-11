import dao.CategoryDAO;
import dao.IngredientDAO;
import dao.ProductDAO;
import dao.RecipeDAO;
import dao.SaleDAO;
import dao.UserDAO;
import dao.UserRoleDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import model.Category;
import model.Ingredient;
import model.Product;
import model.Recipe;
import model.Sale;
import model.User;
import model.UserRole;

public class App {
    public static void main(String[] args) throws Exception {
        boolean exit = false;
        Scanner response = new Scanner(System.in);
        int option = 0;

        while (!exit) {
            // CATEGORY

            // showMenuCategory();
            // option = response.nextInt();
            // System.out.println();
            // exit = actionCategory(option, response);

            // INGREDIENT

            // showMenuIngredient();
            // option = response.nextInt();
            // System.out.println();
            // exit = actionIngredient(option, response);

            // PRODUCT

            // showMenuProduct();
            // option = response.nextInt();
            // System.out.println();
            // exit = actionProduct(option, response);

            // RECIPE

            // showMenuRecipe();
            // option = response.nextInt();
            // System.out.println();
            // exit = actionRecipe(option, response);

            // SALE

            // showMenuSale();
            // option = response.nextInt();
            // System.out.println();
            // exit = actionSale(option, response);

            // SALE PRODUCT

            // USER

            // showMenuUser();
            // option = response.nextInt();
            // System.out.println();
            // exit = actionUser(option, response);

            // USER ROLES

            showMenuUserRole();
            option = response.nextInt();
            System.out.println();
            exit = actionUserRole(option, response);

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

    // Test CategoryDAO

    public static void showMenuCategory() {

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

    public static boolean actionCategory(int option, Scanner response) throws SQLException {

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

    // Test IngredientDAO

    public static void showMenuIngredient() {
        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println("1 - Listar todos os ingredientes");
        System.out.println("2 - Listar um ingrediente específico");
        System.out.println("3 - Excluir ingrediente");
        System.out.println("4 - Cadastrar ingrediente");
        System.out.println("5 - Alterar ingrediente");
        System.out.println("6 - Sair");
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.print("Digite a opção que deseja realizar: ");
    }

    public static boolean actionIngredient(int option, Scanner response) throws SQLException {
        Ingredient ingredient = new Ingredient();
        IngredientDAO ingredientDAO = new IngredientDAO();
        int id = 0;

        switch (option) {
            case 1:
                System.out.println("1 - Todos os ingredientes\n");

                ArrayList<Ingredient> ingredients = ingredientDAO.getAllIngredients();
                for (Ingredient objIngredient : ingredients) {
                    System.out.println(objIngredient.getToString());
                }
                return false;

            case 2:
                System.out.println("2 - Ingrediente específico\n");
                System.out.print("Digite o ID do ingrediente: ");
                id = response.nextInt();

                ingredient.setId(id);
                Ingredient objIngredient = ingredientDAO.getIngredient(ingredient);
                if (objIngredient != null) {
                    System.out.println(objIngredient.getToString());
                } else {
                    System.out.println("Nenhum objeto encontrado.");
                }
                return false;

            case 3:
                System.out.println("3 - Excluir ingrediente\n");
                System.out.print("Digite o ID do ingrediente: ");
                id = response.nextInt();

                ingredient.setId(id);
                ingredientDAO.delete(ingredient);
                return false;

            case 4:
                System.out.println("4 - Cadastrar ingrediente\n");

                System.out.print("Descrição: ");
                response.nextLine(); // Consumir quebra de linha
                ingredient.setDescription(response.nextLine());

                System.out.print("Quantidade: ");
                ingredient.setQuantity(response.nextInt());

                System.out.print("Preço: ");
                ingredient.setPrice(response.nextDouble());

                ingredientDAO.add(ingredient);
                return false;

            case 5:
                System.out.println("5 - Editar ingrediente\n");

                System.out.print("Digite o ID do ingrediente que deseja editar: ");
                id = response.nextInt();
                response.nextLine(); // Consumir quebra de linha

                ingredient.setId(id);
                Ingredient oldIngredient = ingredientDAO.getIngredient(ingredient);

                if (oldIngredient != null) {
                    System.out.print("Descrição (" + oldIngredient.getDescription() + "): ");
                    String newDescription = response.nextLine();
                    ingredient
                            .setDescription(newDescription.isEmpty() ? oldIngredient.getDescription() : newDescription);

                    System.out.print("Quantidade (" + oldIngredient.getQuantity() + "): ");
                    String newQuantity = response.nextLine();
                    ingredient.setQuantity(
                            newQuantity.isEmpty() ? oldIngredient.getQuantity() : Integer.parseInt(newQuantity));

                    System.out.print("Preço (" + oldIngredient.getPrice() + "): ");
                    String newPrice = response.nextLine();
                    ingredient.setPrice(newPrice.isEmpty() ? oldIngredient.getPrice() : Double.parseDouble(newPrice));

                    ingredientDAO.update(ingredient);
                }
                return false;

            case 6:
                return true;

            default:
                System.out.println("Opção inválida. Tente novamente.");
                return false;
        }
    }

    // Test ProductDAO

    public static void showMenuProduct() {
        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Buscar um produto específico");
        System.out.println("3 - Excluir um produto");
        System.out.println("4 - Adicionar um produto");
        System.out.println("5 - Atualizar um produto");
        System.out.println("6 - Sair");
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.print("Digite a opção que deseja realizar: ");
    }

    public static boolean actionProduct(int option, Scanner scanner) throws SQLException {
        ProductDAO productDAO = new ProductDAO();
        Product product;
        int id, quantity, idCategory;
        String description, image;
        double price;

        switch (option) {
            case 1:
                System.out.println("1 - Listar todos os produtos\n");
                ArrayList<Product> products = productDAO.getAllProducts();
                for (Product objProduct : products) {
                    System.out.println(objProduct.getToString());
                }
                return false;

            case 2:
                System.out.println("2 - Buscar um produto específico\n");
                System.out.print("Digite o ID do produto: ");
                id = scanner.nextInt();

                product = new Product();
                product.setId(id);

                Product foundProduct = productDAO.getProduct(product);
                if (foundProduct != null) {
                    System.out.println(foundProduct.getToString());
                }
                return false;

            case 3:
                System.out.println("3 - Excluir um produto\n");
                System.out.print("Digite o ID do produto: ");
                id = scanner.nextInt();

                product = new Product();
                product.setId(id);

                productDAO.delete(product);
                return false;

            case 4:
                System.out.println("4 - Adicionar um produto\n");
                System.out.print("Digite a descrição do produto: ");
                scanner.nextLine(); // Consumir quebra de linha
                description = scanner.nextLine();
                System.out.print("Digite o caminho da imagem: ");
                image = scanner.nextLine();
                System.out.print("Digite a quantidade: ");
                quantity = scanner.nextInt();
                System.out.print("Digite o preço: ");
                price = scanner.nextDouble();
                System.out.print("Digite o ID da categoria: ");
                idCategory = scanner.nextInt();

                product = new Product(0, description, image, quantity, price, idCategory);
                productDAO.add(product);
                return false;

            case 5:
                System.out.println("5 - Atualizar um produto\n");
                System.out.print("Digite o ID do produto: ");
                id = scanner.nextInt();
                System.out.print("Digite a nova descrição (ou deixe vazio): ");
                scanner.nextLine(); // Consumir quebra de linha
                description = scanner.nextLine();
                System.out.print("Digite o novo caminho da imagem (ou deixe vazio): ");
                image = scanner.nextLine();
                System.out.print("Digite a nova quantidade (ou 0 para manter): ");
                quantity = scanner.nextInt();
                System.out.print("Digite o novo preço (ou 0.0 para manter): ");
                price = scanner.nextDouble();
                System.out.print("Digite o novo ID da categoria (ou 0 para manter): ");
                idCategory = scanner.nextInt();

                product = new Product(id, description, image, quantity, price, idCategory);
                productDAO.update(product);
                return false;

            case 6:
                System.out.println("Encerrando...");
                return true;

            default:
                System.out.println("Opção inválida. Tente novamente.");
                return false;
        }
    }

    // Test RecipeDAO

    public static void showMenuRecipe() {
        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println("1 - Listar todas as receitas");
        System.out.println("2 - Buscar uma receita específica");
        System.out.println("3 - Excluir uma receita");
        System.out.println("4 - Adicionar uma receita");
        System.out.println("5 - Atualizar uma receita");
        System.out.println("6 - Sair");
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.print("Digite a opção que deseja realizar: ");
    }

    public static boolean actionRecipe(int option, Scanner response) throws SQLException {
        RecipeDAO recipeDAO = new RecipeDAO();
        Recipe recipe;
        int idIngredient, idProduct, quantity;

        switch (option) {
            case 1:
                System.out.println("1 - Todas as receitas\n");
                ArrayList<Recipe> recipes = recipeDAO.getAllRecipes();
                for (Recipe objRecipe : recipes) {
                    System.out.println(objRecipe.getToString());
                }
                return false;

            case 2:
                System.out.println("2 - Buscar uma receita específica\n");
                System.out.print("Digite o ID do ingrediente: ");
                idIngredient = response.nextInt();
                System.out.print("Digite o ID do produto: ");
                idProduct = response.nextInt();

                recipe = new Recipe();
                recipe.setIdIngredient(idIngredient);
                recipe.setIdProduct(idProduct);

                Recipe foundRecipe = recipeDAO.getRecipe(recipe);
                if (foundRecipe != null) {
                    System.out.println(foundRecipe.getToString());
                }
                return false;

            case 3:
                System.out.println("3 - Excluir uma receita\n");
                System.out.print("Digite o ID do ingrediente: ");
                idIngredient = response.nextInt();
                System.out.print("Digite o ID do produto: ");
                idProduct = response.nextInt();

                recipe = new Recipe();
                recipe.setIdIngredient(idIngredient);
                recipe.setIdProduct(idProduct);

                recipeDAO.delete(recipe);
                return false;

            case 4:
                System.out.println("4 - Adicionar uma receita\n");
                System.out.print("Digite o ID do ingrediente: ");
                idIngredient = response.nextInt();
                System.out.print("Digite o ID do produto: ");
                idProduct = response.nextInt();
                System.out.print("Digite a quantidade: ");
                quantity = response.nextInt();

                recipe = new Recipe(idIngredient, idProduct, quantity);
                recipeDAO.add(recipe);
                return false;

            case 5:
                System.out.println("5 - Atualizar uma receita\n");
                System.out.print("Digite o ID do ingrediente: ");
                idIngredient = response.nextInt();
                System.out.print("Digite o ID do produto: ");
                idProduct = response.nextInt();
                System.out.print("Digite a nova quantidade (ou 0 para manter): ");
                quantity = response.nextInt();

                recipe = new Recipe();
                recipe.setIdIngredient(idIngredient);
                recipe.setIdProduct(idProduct);
                recipe.setQuantity(quantity);

                recipeDAO.update(recipe);
                return false;

            case 6:
                System.out.println("Encerrando...");
                return true;

            default:
                System.out.println("Opção inválida. Tente novamente.");
                return false;
        }
    }

    // Sale DAO

    public static void showMenuSale() {
        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println("1 - Listar todas as vendas");
        System.out.println("2 - Buscar uma venda específica");
        System.out.println("3 - Excluir uma venda");
        System.out.println("4 - Adicionar uma venda");
        System.out.println("5 - Atualizar uma venda");
        System.out.println("6 - Sair");
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.print("Digite a opção que deseja realizar: ");
    }

    public static boolean actionSale(int option, Scanner response) throws SQLException {
        SaleDAO saleDAO = new SaleDAO();
        Sale sale;
        int id, idUser;
        String paymentMethod;
        java.sql.Date date;

        switch (option) {
            case 1 -> {
                System.out.println("1 - Todas as vendas\n");
                ArrayList<Sale> sales = saleDAO.getAllSales();
                if (sales.isEmpty()) {
                    System.out.println("Nenhuma venda encontrada.");
                } else {
                    for (Sale objSale : sales) {
                        System.out.println(objSale.getToString());
                    }
                }
                return false;
            }
            case 2 -> {
                System.out.println("2 - Buscar uma venda específica\n");
                System.out.print("Digite o ID da venda: ");
                id = response.nextInt();

                sale = new Sale();
                sale.setId(id);

                Sale foundSale = saleDAO.getSale(sale);
                if (foundSale != null && foundSale.getId() > 0) {
                    System.out.println(foundSale.getToString());
                } else {
                    System.out.println("Venda não encontrada.");
                }
                return false;
            }
            case 3 -> {
                System.out.println("3 - Excluir uma venda\n");
                System.out.print("Digite o ID da venda: ");
                id = response.nextInt();

                sale = new Sale();
                sale.setId(id);

                saleDAO.delete(sale);
                return false;
            }
            case 4 -> {
                System.out.println("4 - Adicionar uma venda\n");
                System.out.print("Digite a data da venda (formato: yyyy-MM-dd): ");
                date = java.sql.Date.valueOf(response.next());
                System.out.print("Digite o método de pagamento: ");
                paymentMethod = response.next();
                System.out.print("Digite o ID do usuário: ");
                idUser = response.nextInt();

                sale = new Sale(0, date, paymentMethod, idUser);
                saleDAO.add(sale);
                return false;
            }
            case 5 -> {
                System.out.println("5 - Atualizar uma venda\n");
                System.out.print("Digite o ID da venda: ");
                id = response.nextInt();
                System.out.print("Digite a nova data (formato: yyyy-MM-dd, ou enter para manter): ");
                String dateInput = response.next();
                date = dateInput.isEmpty() ? null : java.sql.Date.valueOf(dateInput);
                System.out.print("Digite o novo método de pagamento (ou enter para manter): ");
                paymentMethod = response.next();
                System.out.print("Digite o novo ID do usuário (ou 0 para manter): ");
                idUser = response.nextInt();

                sale = new Sale(id, date, paymentMethod, idUser);
                saleDAO.update(sale);
                return false;
            }
            case 6 -> {
                System.out.println("Encerrando...");
                return true;
            }
            default -> {
                System.out.println("Opção inválida. Tente novamente.");
                return false;
            }
        }
    }

    // SaleProduct DAO

    // User DAO

    public static void showMenuUser() {
        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println("1 - Listar todos os usuários");
        System.out.println("2 - Buscar um usuário por ID");
        System.out.println("3 - Buscar um usuário por login");
        System.out.println("4 - Adicionar um usuário");
        System.out.println("5 - Atualizar um usuário");
        System.out.println("6 - Excluir um usuário");
        System.out.println("7 - Identificar usuário por login e senha");
        System.out.println("8 - Sair");
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.print("Digite a opção que deseja realizar: ");
    }

    public static boolean actionUser(int option, Scanner response) throws SQLException {
        UserDAO userDAO = new UserDAO();
        User user;
        int id;
        String name, email, password, address, login;

        switch (option) {
            case 1:
                System.out.println("1 - Listar todos os usuários\n");
                ArrayList<User> users = userDAO.getAllUsers();
                for (User objUser : users) {
                    System.out.println(objUser.getToString());
                }
                return false;

            case 2:
                System.out.println("2 - Buscar um usuário por ID\n");
                System.out.print("Digite o ID do usuário: ");
                id = response.nextInt();

                user = new User();
                user.setId(id);
                User foundUserById = userDAO.getUser(user);

                if (foundUserById != null) {
                    System.out.println(foundUserById.getToString());
                } else {
                    System.out.println("Usuário não encontrado.");
                }
                return false;

            case 3:
                System.out.println("3 - Buscar um usuário por login\n");
                System.out.print("Digite o login do usuário: ");
                login = response.next();

                User foundUserByLogin = userDAO.getByLogin(login);
                if (foundUserByLogin != null) {
                    System.out.println(foundUserByLogin.getToString());
                } else {
                    System.out.println("Usuário não encontrado.");
                }
                return false;

            case 4:
                System.out.println("4 - Adicionar um usuário\n");
                System.out.print("Nome: ");
                name = response.next();
                System.out.print("Email: ");
                email = response.next();
                System.out.print("Senha: ");
                password = response.next();
                System.out.print("Endereço: ");
                address = response.next();
                System.out.print("Login: ");
                login = response.next();

                user = new User(0, name, email, password, address, login, null);
                userDAO.add(user);
                return false;

            case 5:
                System.out.println("5 - Atualizar um usuário\n");
                System.out.print("Digite o ID do usuário: ");
                id = response.nextInt();

                System.out.print("Novo nome (ou deixe vazio para manter): ");
                response.nextLine(); // Consumir quebra de linha
                name = response.nextLine();
                System.out.print("Novo email (ou deixe vazio para manter): ");
                email = response.nextLine();
                System.out.print("Nova senha (ou deixe vazio para manter): ");
                password = response.nextLine();
                System.out.print("Novo endereço (ou deixe vazio para manter): ");
                address = response.nextLine();
                System.out.print("Novo login (ou deixe vazio para manter): ");
                login = response.nextLine();

                user = new User(id, name, email, password, address, login, null);
                userDAO.update(user);
                return false;

            case 6:
                System.out.println("6 - Excluir um usuário\n");
                System.out.print("Digite o ID do usuário: ");
                id = response.nextInt();

                user = new User();
                user.setId(id);
                userDAO.delete(user);
                return false;

            case 7:
                System.out.println("7 - Identificar usuário por login e senha\n");
                System.out.print("Login: ");
                login = response.next();
                System.out.print("Senha: ");
                password = response.next();

                boolean isAuthenticated = userDAO.identify(login, password);
                if (isAuthenticated) {
                    System.out.println("Usuário autenticado com sucesso.");
                } else {
                    System.out.println("Login ou senha incorretos.");
                }
                return false;

            case 8:
                System.out.println("Encerrando...");
                return true;

            default:
                System.out.println("Opção inválida. Tente novamente.");
                return false;
        }
    }

    // UserRole DAO

    public static void showMenuUserRole() {
        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println("1 - Listar todos os roles");
        System.out.println("2 - Buscar um role por ID de usuário");
        System.out.println("3 - Adicionar um role");
        System.out.println("4 - Atualizar um role");
        System.out.println("5 - Excluir um role");
        System.out.println("6 - Sair");
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.print("Digite a opção que deseja realizar: ");
    }

    public static boolean actionUserRole(int option, Scanner response) throws SQLException {
        UserRoleDAO userRoleDAO = new UserRoleDAO();
        UserRole userRole;
        int idUser;
        String role;

        switch (option) {
            case 1:
                System.out.println("1 - Listar todos os roles\n");
                ArrayList<UserRole> userRoles = userRoleDAO.getAllCategories();
                for (UserRole objUserRole : userRoles) {
                    System.out.println(objUserRole.getToString());
                }
                return false;

            case 2:
                System.out.println("2 - Buscar um role por ID de usuário\n");
                System.out.print("Digite o ID do usuário: ");
                idUser = response.nextInt();

                userRole = new UserRole();
                userRole.setIdUser(idUser);
                UserRole foundUserRoleById = userRoleDAO.getRoleByUser(userRole);

                if (foundUserRoleById != null) {
                    System.out.println(foundUserRoleById.getToString());
                } else {
                    System.out.println("Role de usuário não encontrado.");
                }
                return false;

            case 3:
                System.out.println("3 - Adicionar um role\n");
                System.out.print("ID do usuário: ");
                idUser = response.nextInt();
                System.out.print("Role: ");
                role = response.next();

                userRole = new UserRole(idUser, role);
                userRoleDAO.add(userRole);
                return false;

            case 4:
                System.out.println("4 - Atualizar um role\n");
                System.out.print("Digite o ID do usuário para atualizar o role: ");
                idUser = response.nextInt();

                System.out.print("Novo role (ou deixe vazio para manter): ");
                response.nextLine(); // Consumir quebra de linha
                role = response.nextLine();

                userRole = new UserRole(idUser, role);
                userRoleDAO.update(userRole);
                return false;

            case 5:
                System.out.println("5 - Excluir um role\n");
                System.out.print("Digite o ID do usuário para excluir o role: ");
                idUser = response.nextInt();

                System.out.print("Digite o role do usuário a ser excluído: ");
                role = response.next();

                userRole = new UserRole(idUser, role);
                userRoleDAO.delete(userRole);
                return false;

            case 6:
                System.out.println("Encerrando...");
                return true;

            default:
                System.out.println("Opção inválida. Tente novamente.");
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
