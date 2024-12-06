import database.DatabaseConnectionFactory;
import model.Book;
import model.User;
import model.builder.BookBuilder;
import model.builder.UserBuilder;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;

import java.sql.Connection;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World");

      /*  Book book = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(LocalDate.of(1910, 10, 20))
                .build();

        System.out.println(book);*/

        /*BookRepository bookRepository=new BookRepositoryMock();
        bookRepository.save(book);
        bookRepository.save(new BookBuilder().setAuthor("Ioan Slavici").setTitle("Moara cu noroc").setPublishedDate(LocalDate.of(1950,2,10)).build());
        System.out.println(bookRepository.findAll());
        bookRepository.removeAll();
        System.out.println(bookRepository.findAll());*/

      Connection connection=DatabaseConnectionFactory.getConnectionWrapper(false).getConnection();
//        RightsRolesRepositoryMySQL rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
//
//
//        UserRepositoryMySQL userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
//
//
//        User newUser = new UserBuilder()
//                .setUsername("testuser")
//                .setPassword("testpassword")
//                .build();
//
//
//        if (userRepository.save(newUser)) {
//            System.out.println("User added successfully: " + newUser.getUsername());
//        } else {
//            System.out.println("Error adding user.");
//
//    }


       BookRepository bookRepository= new BookRepositoryCacheDecorator(new BookRepositoryMySQL(connection), new Cache<>(), new Cache<>());

//        BookService bookService=new BookServiceImpl(bookRepository);
//
//        RightsRolesRepository rightsRolesRepository=new RightsRolesRepositoryMySQL(connection);
//        UserRepository userRepository=new UserRepositoryMySQL(connection, rightsRolesRepository);
//        AuthenticationService authenticationService=new AuthenticationServiceImpl(userRepository,rightsRolesRepository);
//        if(userRepository.existsByUsername("Andra")){
//            System.out.println("Username already present into the user table");
//        } else {
//            authenticationService.register("Andra", "parola123!");
//        }
//       System.out.println(authenticationService.login("Andra", "parola123!"));

        // System.out.println(bookRepository.findAll());
       /* BookService bookService=new BookServiceImpl(bookRepository);


        bookService.save(book);
       System.out.println(bookService.findAll());
       Book bookMoaraCuNoroc= new BookBuilder().setAuthor("Ioan Slavici").setTitle("Moara cu noroc").setPublishedDate(LocalDate.of(1950,2,10)).build();
        bookService.save(bookMoaraCuNoroc);
        System.out.println(bookService.findAll());
        bookService.delete(bookMoaraCuNoroc);
        bookService.delete(book);
        System.out.println(bookService.findAll());*/


    }
}