package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.UserMapper;
import model.Order;
import model.validator.Notification;
import service.report.PDFGenerateReportService;
import service.book.BookService;
import service.user.AuthenticationService;
import service.user.UserService;
import view.AdminView;
import view.model.UserDTO;
import view.model.builder.UserDTOBuilder;

import java.util.List;



public class AdminController {

    private final AdminView adminView;
    private final UserService userService;
    private final BookService bookService;
    private final AuthenticationService authenticationService;

    private final PDFGenerateReportService pdfGenerateReportService;

    public AdminController(AdminView adminView, UserService userService, BookService bookService, AuthenticationService authenticationService, PDFGenerateReportService pdfGenerateReportService) {
        this.adminView = adminView;
        this.userService = userService;
        this.bookService = bookService;
        this.authenticationService=authenticationService;
        this.pdfGenerateReportService=pdfGenerateReportService;

        this.adminView.addAddUserButtonListener(new AddUserButtonListener());
        this.adminView.addGenerateReportButtonListener(new GenerateReportButtonListener());
        this.adminView.addDeleteUserButtonListener(new DeleteUserButtonListener());

    }


    private class AddUserButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = adminView.getUsername();
            String password = adminView.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                adminView.addDisplayAlertMessage("Error", "Missing Fields", "Username and Password cannot be empty.");
            } else {

                Notification<Boolean> registerEmployeeNotification = authenticationService.register(username, password, "employee");

                if (registerEmployeeNotification.hasErrors()) {
                    adminView.addDisplayAlertMessage("Error", "Problem adding Employee", registerEmployeeNotification.getFormattedErrors());
                } else {
                    adminView.addDisplayAlertMessage("Success", "Employee Added", "The employee was successfully added to the database.");
                    UserDTO userDTO = new UserDTOBuilder().setUsername(username).build();
                    adminView.addUserToObservableList(userDTO);
                }
            }
        }
    }

    private class DeleteUserButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            UserDTO userDTO = (UserDTO) adminView.getUserTableView().getSelectionModel().getSelectedItem();

            if (userDTO != null){
                boolean deleteSuccessful= userService.delete(UserMapper.convertUserDTOToUser(userDTO));

                if(deleteSuccessful){
                    adminView.addDisplayAlertMessage("Delete Successful", "User Deleted", "User was successfully deleted from the database.");
                    adminView.removeUserFromObservableList(userDTO);
                } else {
                    adminView.addDisplayAlertMessage("Delete Error", "Problem at deleting User", "There was a problem with the database. Please try again.");
                }

            } else {
                adminView.addDisplayAlertMessage("Delete Error", "Problem at deleting user", "You must select a user before pressing the delete button.");
            }
        }
        }


    private class GenerateReportButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {

            List<Order> orders = bookService.findOrdersForLastMonth();

            if (!orders.isEmpty()) {

                pdfGenerateReportService.generatePdfReport(orders);
                adminView.addDisplayAlertMessage("Report Generated", "Report Generated Successfully",
                        "The report for all orders has been successfully generated.");
            } else {
                adminView.addDisplayAlertMessage("No Orders", "No Orders Found",
                        "No orders found in the last month.");
            }
        }

    }
}