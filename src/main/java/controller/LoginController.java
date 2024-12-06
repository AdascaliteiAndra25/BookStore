package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.stage.Stage;
import mapper.BookMapper;
import mapper.UserMapper;
import model.User;

import model.validator.Notification;
import service.book.BookService;
import service.report.PDFGenerateReportService;
import service.user.AuthenticationService;
import service.user.UserService;
import view.AdminView;
import view.BookView;
import view.CustomerView;
import view.LoginView;
import view.model.BookDTO;
import view.model.UserDTO;

import java.util.List;

import static database.Constants.Roles.CUSTOMER;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final BookService bookService;
    private final PDFGenerateReportService pdfGenerateReportService;
    private final Stage primaryStage;



    public LoginController(LoginView loginView, AuthenticationService authenticationService,  UserService userService, BookService bookService, Stage primaryStage,PDFGenerateReportService pdfGenerateReportService){
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.userService =userService;
        this.primaryStage=primaryStage;
        this.bookService=bookService;
        this.pdfGenerateReportService=pdfGenerateReportService;


        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();


            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasErrors()) {
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            } else {
                User user = loginNotification.getResult();

                if (userService.existsByUsername(username)) {
                    loginView.setActionTargetText("LogIn Successful!");

                    if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("administrator"))) {
                        //AdminComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage());
                        List<UserDTO> userDTOs = UserMapper.convertUserListToUserDTOList(userService.findAll());
                        AdminView adminView = new AdminView(loginView.getPrimaryStage(), userDTOs);
                        AdminController adminController = new AdminController(adminView, userService,bookService, authenticationService, pdfGenerateReportService);
                    } else if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("employee"))) {
                       // EmployeeComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage());
                        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(bookService.findAll());
                        List<BookDTO> soldBookDTOs=BookMapper.convertBookListToBookDTOList(bookService.findSoldBooks());
                        BookView bookView= new BookView(primaryStage, bookDTOs, soldBookDTOs);
                        BookController bookController=new BookController(bookView, bookService, authenticationService);

                    } else if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("customer"))) {
                       // CustomerComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage());
                        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(bookService.findAll());
                        CustomerView customerView=new CustomerView(primaryStage, bookDTOs);
                        CustomerController customerController = new CustomerController(customerView, bookService);

                    } else {
                        loginView.setActionTargetText("Access denied! Unknown role.");
                    }
                } else {
                    loginView.setActionTargetText("Access denied! User not found.");
                }
            }
        }
    }



    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password, CUSTOMER);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());

            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}
