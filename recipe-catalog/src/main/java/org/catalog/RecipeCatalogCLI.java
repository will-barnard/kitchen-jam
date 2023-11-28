package org.catalog;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;

import org.catalog.dao.*;
import org.catalog.exception.DaoException;
import org.catalog.view.Menu;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import javax.sql.DataSource;

public class RecipeCatalogCLI {

    private static final String MAIN_MENU_OPTION_MEAL = "View and Manage Employees";
    private static final String MAIN_MENU_OPTION_RECIPE = "View and Manage Departments";
    private static final String MAIN_MENU_OPTION_CATEGORY = "View and Manage Projects";
    private static final String MAIN_MENU_OPTION_EXIT = "Exit";
    private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_MEAL,
            MAIN_MENU_OPTION_RECIPE,
            MAIN_MENU_OPTION_CATEGORY,
            MAIN_MENU_OPTION_EXIT };

    private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";

    private static final String RECIPE_MENU_OPTION_ALL_RECIPES = "Show all departments";
    private static final String RECIPE_MENU_OPTION_CREATE_RECIPE = "Create new department";
    private static final String RECIPE_MENU_OPTION_UPDATE_RECIPE = "Update department name";
    private static final String RECIPE_MENU_OPTION_DELETE_RECIPE = "Delete department";
    private static final String[] RECIPE_MENU_OPTIONS = new String[] { RECIPE_MENU_OPTION_ALL_RECIPES,
            RECIPE_MENU_OPTION_CREATE_RECIPE,
            RECIPE_MENU_OPTION_UPDATE_RECIPE,
            RECIPE_MENU_OPTION_DELETE_RECIPE,
            MENU_OPTION_RETURN_TO_MAIN};



    private static final String MEAL_MENU_OPTION_ALL_MEALS = "Show all employees";
    private static final String MEAL_MENU_OPTION_SEARCH_BY_NAME = "Employee search by name";
    private static final String MEAL_MENU_OPTION_NOT_MADE = "Show employees without projects";
    private static final String MEAL_MENU_OPTION_LOG_MEAL = "Create new employee";
    private static final String MEAL_MENU_OPTION_UPDATE_MEAL = "Update employee";
    private static final String MEAL_MENU_OPTION_DELETE_MEAL = "Delete employee";
    private static final String[] MEAL_MENU_OPTIONS = new String[] { MEAL_MENU_OPTION_ALL_MEALS,
            MEAL_MENU_OPTION_SEARCH_BY_NAME,
            MEAL_MENU_OPTION_NOT_MADE,
            MEAL_MENU_OPTION_LOG_MEAL,
            MEAL_MENU_OPTION_UPDATE_MEAL,
            MEAL_MENU_OPTION_DELETE_MEAL,
            MENU_OPTION_RETURN_TO_MAIN};

    private static final String CAT_MENU_OPTION_VIEW_CATEGORIES = "Show all projects";
    private static final String CAT_MENU_OPTION_CAT_RECIPES = "Show project employees";
    private static final String CAT_MENU_OPTION_ASSIGN_RECIPE_CATEGORY = "Assign an employee to a project";
    private static final String CAT_MENU_OPTION_REMOVE_RECIPE_FROM_CAT = "Remove employee from project";
    private static final String CAT_MENU_OPTION_CREATE_CAT = "Create new project";
    private static final String CAT_MENU_OPTION_UPDATE_CAT = "Update project information";
    private static final String CAT_MENU_OPTION_DELETE_CAT = "Delete project";
    private static final String[] CAT_MENU_OPTIONS = new String[] { CAT_MENU_OPTION_VIEW_CATEGORIES,
            CAT_MENU_OPTION_CAT_RECIPES,
            CAT_MENU_OPTION_ASSIGN_RECIPE_CATEGORY,
            CAT_MENU_OPTION_REMOVE_RECIPE_FROM_CAT,
            CAT_MENU_OPTION_CREATE_CAT,
            CAT_MENU_OPTION_UPDATE_CAT,
            CAT_MENU_OPTION_DELETE_CAT,
            MENU_OPTION_RETURN_TO_MAIN };

    private final Menu menu;
    private final RecipeDao recipeDao;
    private final MealDao mealDao;
    private final CategoryDao categoryDao;

    public static void main(String[] args) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/CookingDB");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        RecipeCatalogCLI application = new RecipeCatalogCLI(dataSource);
        application.run();
    }

    public RecipeCatalogCLI(DataSource dataSource) {
        this.menu = new Menu(System.in, System.out);

        recipeDao = new JbdcRecipeDao(dataSource);
        mealDao = new JbdcMealDao(dataSource);
        categoryDao = new JbdcCategoryDao(dataSource);
    }

    private void run() {
        displayApplicationBanner();
        boolean running = true;
        while(running) {
            printHeading("Main Menu");
            String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if(choice.equals(MAIN_MENU_OPTION_MEAL)) {
                handleDepartments();
            } else if(choice.equals(MAIN_MENU_OPTION_RECIPE)) {
                handleEmployees();
            } else if(choice.equals(MAIN_MENU_OPTION_CATEGORY)) {
                handleProjects();
            } else if(choice.equals(MAIN_MENU_OPTION_EXIT)) {
                running = false;
            }
        }
    }

    private void handleDepartments() {
        try {
            printHeading("Departments");
            String choice = (String)menu.getChoiceFromOptions(RECIPE_MENU_OPTIONS);
            if(choice.equals(RECIPE_MENU_OPTION_ALL_RECIPES)) {
                handleListAllDepartments();
            } else if(choice.equals(RECIPE_MENU_OPTION_CREATE_RECIPE)) {
                handleCreateDepartment();
            } else if(choice.equals(RECIPE_MENU_OPTION_UPDATE_RECIPE)) {
                handleUpdateDepartmentName();
            } else if(choice.equals(RECIPE_MENU_OPTION_DELETE_RECIPE)){
                handleDeleteDepartment();
            }
        }
        catch (DaoException ex) {
            displayError("Error occurred: " + ex.getMessage());
        }
    }

    private void handleListAllDepartments() {
//        printHeading("All Departments");
//        List<Department> allDepartments = departmentDao.getDepartments();
//        listDepartments(allDepartments);
    }

    private void listDepartments(List<Department> departments) {
//        System.out.println();
//        if(departments.size() > 0) {
//            for(Department dept : departments) {
//                System.out.println(dept);
//            }
//        } else {
//            System.out.println("\n*** No results ***");
//        }
    }

    private void handleCreateDepartment() {
//        printHeading("Create New Department");
//        String newDepartmentName = getUserInput("Enter new Department name");
//        Department newDepartment = new Department();
//        newDepartment.setName(newDepartmentName);
//        newDepartment = departmentDao.createDepartment(newDepartment);
//        System.out.println("\n*** "+newDepartment.getName()+" created ***");
    }

    private void handleUpdateDepartmentName() {
//        printHeading("Update Department Name");
//        List<Department> allDepartments = departmentDao.getDepartments();
//        if(allDepartments.size() > 0) {
//            System.out.println("\n*** Choose a Department ***");
//            Department selectedDepartment = (Department)menu.getChoiceFromOptions(allDepartments.toArray());
//            String newDepartmentName = getUserInput("Enter new Department name");
//            selectedDepartment.setName(newDepartmentName);
//            departmentDao.updateDepartment(selectedDepartment);
//        } else {
//            System.out.println("\n*** No results ***");
//        }
    }

    private void handleDeleteDepartment() {
//        printHeading("Delete Department");
//        Department selectedDepartment = getDepartmentSelectionFromUser();
//
//        departmentDao.deleteDepartmentById(selectedDepartment.getId());
//        System.out.println("\n*** " + selectedDepartment.getName() + " deleted ***");
    }

    private Department getDepartmentSelectionFromUser() {
//        System.out.println("Choose a project:");
//        List<Department> allDepartments = departmentDao.getDepartments();
//        return (Department)menu.getChoiceFromOptions(allDepartments.toArray());
    }

    private void handleEmployees() {
//        try {
//            printHeading("Employees");
//            String choice = (String)menu.getChoiceFromOptions(MEAL_MENU_OPTIONS);
//            if(choice.equals(MEAL_MENU_OPTION_ALL_MEALS)) {
//                handleListAllEmployees();
//            } else if(choice.equals(MEAL_MENU_OPTION_LOG_MEAL)) {
//                handleEmployeeSearch();
//            } else if(choice.equals(MEAL_MENU_OPTION_SEARCH_BY_NAME)) {
//                handleUnassignedEmployeeSearch();
//            } else if(choice.equals(MEAL_MENU_OPTION_NOT_MADE)) {
//                handleCreateEmployee();
//            } else if(choice.equals(MEAL_MENU_OPTION_UPDATE_MEAL)) {
//                handleUpdateEmployee();
//            } else if(choice.equals(MEAL_MENU_OPTION_DELETE_MEAL)) {
//                handleDeleteEmployee();
//            }
//        }
//        catch (DaoException ex) {
//            displayError("Error occurred: " + ex.getMessage());
//        }
    }

    private void handleListAllEmployees() {
//        printHeading("All Employees");
//        List<Employee> allEmployees = employeeDao.getEmployees();
//        listEmployees(allEmployees);
    }

    private void handleEmployeeSearch() {
//        printHeading("Employee Search");
//        String firstNameSearch = getUserInput("Enter first name to search for");
//        String lastNameSearch = getUserInput("Enter last name to search for");
//        List<Employee> employees = employeeDao.getEmployeesByFirstNameLastName(firstNameSearch, lastNameSearch);
//        listEmployees(employees);
    }

    private void handleUnassignedEmployeeSearch() {
//        printHeading("Employees Without Projects");
//        List<Employee> employees = employeeDao.getEmployeesWithoutProjects();
//        listEmployees(employees);
    }

    private void handleCreateEmployee() {
//        printHeading("Create New Employee");
//        Employee newEmployee = new Employee();
//        List<Department> allDepartments = departmentDao.getDepartments();
//        if(allDepartments.size() > 0) {
//            System.out.println("\n*** Choose a department ***");
//            Department selectedDepartment = (Department)menu.getChoiceFromOptions(allDepartments.toArray());
//            newEmployee.setDepartmentId(selectedDepartment.getId());
//        }
//        String newFirstName = getUserInput("Enter employee's first name");
//        String newLastName = getUserInput("Enter employee's first name");
//        String birthDate = getUserInput("Enter birth date (YYYY-MM-DD)");
//        String hireDate = getUserInput("Enter hire date (YYYY-MM-DD)");
//        newEmployee.setFirstName(newFirstName);
//        newEmployee.setLastName(newLastName);
//        newEmployee.setBirthDate(LocalDate.parse(birthDate));
//        newEmployee.setHireDate(LocalDate.parse(hireDate));
//        newEmployee = employeeDao.createEmployee(newEmployee);
//        System.out.println("\n*** " + newEmployee.getFirstName() + " " + newEmployee.getLastName() + " created ***");
    }

    private void handleUpdateEmployee() {
//        printHeading("Update Employee");
//        List<Employee> allEmployees = employeeDao.getEmployees();
//        if(allEmployees.size() > 0) {
//            System.out.println("\n*** Choose an employee ***");
//            Employee selectedEmployee = (Employee)menu.getChoiceFromOptions(allEmployees.toArray());
//
//            List<Department> allDepartments = departmentDao.getDepartments();
//            if(allDepartments.size() > 0) {
//                System.out.println("\n*** Choose a department ***");
//                Department selectedDepartment = (Department)menu.getChoiceFromOptions(allDepartments.toArray());
//                selectedEmployee.setDepartmentId(selectedDepartment.getId());
//            }
//            String newFirstName = getUserInput("Enter employee's updated first name (leave blank to skip)");
//            String newLastName = getUserInput("Enter employee's updated first name (leave blank to skip)");
//            String birthDate = getUserInput("Enter updated birth date (YYYY-MM-DD)(leave blank to skip)");
//            String hireDate = getUserInput("Enter updated hire date (YYYY-MM-DD) (leave blank to skip)");
//            if(!newFirstName.equals("")) {
//                selectedEmployee.setFirstName(newFirstName);
//            }
//            if(!newLastName.equals("")) {
//                selectedEmployee.setLastName(newLastName);
//            }
//            if(!birthDate.equals("")) {
//                selectedEmployee.setBirthDate(LocalDate.parse(birthDate));
//            }
//            if(!hireDate.equals("")) {
//                selectedEmployee.setHireDate(LocalDate.parse(hireDate));
//            }
//            selectedEmployee = employeeDao.updateEmployee(selectedEmployee);
//            System.out.println("\n*** " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName() + " updated ***");
//        } else {
//            System.out.println("\n*** No results ***");
//        }
    }

    private void handleDeleteEmployee() {
//        printHeading("Delete Employee");
//        List<Employee> allEmployees = employeeDao.getEmployees();
//        if(allEmployees.size() > 0) {
//            System.out.println("\n*** Choose an employee ***");
//            Employee selectedEmployee = (Employee)menu.getChoiceFromOptions(allEmployees.toArray());
//            employeeDao.deleteEmployeeById(selectedEmployee.getId());
//            System.out.println("\n*** " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName() + " deleted ***");
//        } else {
//            System.out.println("\n*** No results ***");
//        }
    }

    private void handleDeleteEmployeesFromDepartment() {
//        List<Department> allDepartments = departmentDao.getDepartments();
//        if(allDepartments.size() > 0) {
//            System.out.println("\n*** Choose a department ***");
//            Department selectedDepartment = (Department)menu.getChoiceFromOptions(allDepartments.toArray());
//            int deletedCount = employeeDao.deleteEmployeesByDepartmentId(selectedDepartment.getId());
//            System.out.println("\n*** " + deletedCount + " employees deleted ***");
//        } else {
//            System.out.println("\n*** No results ***");
//        }
    }

    private void listEmployees(List<Employee> employees) {
//        System.out.println();
//        if(employees.size() > 0) {
//            for(Employee employee : employees) {
//                System.out.println(employee);
//            }
//        } else {
//            System.out.println("\n*** No results ***");
//        }
    }

    private void handleProjects() {
//        try {
//            printHeading("Projects");
//            String choice = (String)menu.getChoiceFromOptions(CAT_MENU_OPTIONS);
//            if(choice.equals(CAT_MENU_OPTION_VIEW_CATEGORIES)) {
//                handleListActiveProjects();
//            } else if(choice.equals(CAT_MENU_OPTION_CAT_RECIPES)) {
//                handleProjectEmployeeList();
//            } else if(choice.equals(CAT_MENU_OPTION_ASSIGN_RECIPE_CATEGORY)) {
//                handleEmployeeProjectAssignment();
//            }  else if(choice.equals(CAT_MENU_OPTION_REMOVE_RECIPE_FROM_CAT)) {
//                handleEmployeeProjectRemoval();
//            } else if(choice.equals(CAT_MENU_OPTION_CREATE_CAT)) {
//                handleCreateProject();
//            } else if(choice.equals(CAT_MENU_OPTION_UPDATE_CAT)) {
//                handleUpdateProject();
//            } else if(choice.equals(CAT_MENU_OPTION_DELETE_CAT)) {
//                handleDeleteProject();
//            }
//        }
//        catch (DaoException ex) {
//            displayError("Error occurred: " + ex.getMessage());
//        }
    }

    private void handleListActiveProjects() {
//        printHeading("Active Projects");
//        List<Project> projects = projectDao.getProjects();
//        listProjects(projects);
    }

    private void handleCreateProject() {
//        printHeading("Create New Project");
//        String newProjectName = getUserInput("Enter new Project name");
//        String startDate = getUserInput("Enter start date (YYYY-MM-DD)");
//        String endDate = getUserInput("Enter end date (YYYY-MM-DD)");
//        Project newProject = new Project();
//        newProject.setName(newProjectName);
//        if(!startDate.equals("")) {
//            newProject.setFromDate(LocalDate.parse(startDate));
//        }
//        if(!endDate.equals("")) {
//            newProject.setToDate(LocalDate.parse(endDate));
//        }
//        newProject = projectDao.createProject(newProject);
//        System.out.println("\n*** "+newProject.getName()+" created ***");
    }

    private void handleUpdateProject() {
//        printHeading("Update Project");
//        Project selectedProject = getProjectSelectionFromUser();
//        String newProjectName = getUserInput("Enter updated project name (leave blank to skip)");
//        String startDate = getUserInput("Enter updated start date (YYYY-MM-DD) (leave blank to skip)");
//        String endDate = getUserInput("Enter updated end date (YYYY-MM-DD) (leave blank to skip)");
//        if(!newProjectName.equals("")) {
//            selectedProject.setName(newProjectName);
//        }
//        if(!startDate.equals("")) {
//            selectedProject.setFromDate(LocalDate.parse(startDate));
//        }
//        if(!endDate.equals("")) {
//            selectedProject.setToDate(LocalDate.parse(endDate));
//        }
//        selectedProject = projectDao.updateProject(selectedProject);
//        System.out.println("\n*** "+selectedProject.getName()+" updated ***");
    }

    private void handleEmployeeProjectRemoval() {
//        printHeading("Remove Employee From Project");
//
//        Project selectedProject = getProjectSelectionFromUser();
//
//        System.out.println("Choose an employee to remove:");
//        List<Employee> projectEmployees = employeeDao.getEmployeesByProjectId(selectedProject.getId());
//        if(projectEmployees.size() > 0) {
//            Employee selectedEmployee = (Employee)menu.getChoiceFromOptions(projectEmployees.toArray());
//            projectDao.unlinkProjectEmployee(selectedProject.getId(), selectedEmployee.getId());
//            System.out.println("\n*** "+selectedEmployee+" removed from "+selectedProject+" ***");
//        } else {
//            System.out.println("\n*** No results ***");
//        }
    }

    private void handleEmployeeProjectAssignment() {
//        printHeading("Assign Employee To Project");
//
//        Project selectedProject = getProjectSelectionFromUser();
//
//        System.out.println("Choose an employee to add:");
//        List<Employee> allEmployees = employeeDao.getEmployees();
//        Employee selectedEmployee = (Employee)menu.getChoiceFromOptions(allEmployees.toArray());
//
//        projectDao.linkProjectEmployee(selectedProject.getId(), selectedEmployee.getId());
//        System.out.println("\n*** "+selectedEmployee+" added to "+selectedProject+" ***");
    }

    private void handleDeleteProject() {
//        printHeading("Delete Project");
//        Project selectedProject = getProjectSelectionFromUser();
//
//        projectDao.deleteProjectById(selectedProject.getId());
//        System.out.println("\n*** " + selectedProject.getName() + " deleted ***");
    }

    private void handleProjectEmployeeList() {
//        Project selectedProject = getProjectSelectionFromUser();
//        List<Employee> projectEmployees = employeeDao.getEmployeesByProjectId(selectedProject.getId());
//        listEmployees(projectEmployees);
    }

    private Project getProjectSelectionFromUser() {
//        System.out.println("Choose a project:");
//        List<Project> allProjects = projectDao.getProjects();
//        return (Project)menu.getChoiceFromOptions(allProjects.toArray());
    }

    private void listProjects(List<Project> projects) {
//        System.out.println();
//        if(projects.size() > 0) {
//            for(Project proj : projects) {
//                System.out.println(proj);
//            }
//        } else {
//            System.out.println("\n*** No results ***");
//        }
    }

    private void printHeading(String headingText) {
        System.out.println("\n"+headingText);
        for(int i = 0; i < headingText.length(); i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    private String getUserInput(String prompt) {
        System.out.print(prompt + " >>> ");
        return new Scanner(System.in).nextLine();
    }

    private void displayError(String message) {
        System.out.println();
        System.out.println("***" + message + "***");
    }


    private void displayApplicationBanner() {

    }

}