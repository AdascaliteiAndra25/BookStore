package repository.book;

import model.Book;
import model.builder.BookBuilder;
import repository.book.BookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository {

    private final Connection connection;

    public BookRepositoryMySQL(Connection connection){
        this.connection=connection;
    }
    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";

        List<Book> books = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> findSoldBooks() {
        String sql = "SELECT * FROM sold_books;";

        List<Book> soldBooks = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                soldBooks.add(getSoldBookFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return soldBooks;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id=?;";

        Optional<Book> book = Optional.empty();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                book = Optional.of(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }


    public boolean save(Book book) {

       //String newSql = "INSERT INTO book VALUES(null, \'" + book.getAuthor() +"\', \'" + book.getTitle()+"\', \'" + book.getPublishedDate() + "\' );";
        String newSql = "INSERT INTO book VALUES(null, ?, ?, ?,?,?);";

        try{
           PreparedStatement preparedStatement= connection.prepareStatement(newSql);
           preparedStatement.setString(1, book.getAuthor());
           preparedStatement.setString(2, book.getTitle());
           preparedStatement.setDate(3,java.sql.Date.valueOf(book.getPublishedDate()));
           preparedStatement.setDouble(4, book.getPrice());
           preparedStatement.setInt(5,book.getStock());


            System.out.println("Inserting book with price: " + book.getPrice() + " and stock: " + book.getStock());

            int inserted=preparedStatement.executeUpdate();
           return (inserted != 1) ? false:true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }





    @Override
    public boolean delete(Book book) {
       // String newSql = "DELETE FROM book WHERE author=\'" + book.getAuthor() +"\' AND title=\'" + book.getTitle()+"\';";
        String newSql = "DELETE FROM book WHERE author=? and title=?;";

        try{
            PreparedStatement preparedStatement= connection.prepareStatement(newSql);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());

            int deleted=preparedStatement.executeUpdate();
            return (deleted != 1) ? false:true;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM book WHERE id >= 0;";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Book> findByTitleAndAuthor(String title, String author) {

        String sql = "SELECT * FROM book WHERE title = ? AND author= ?;";
        Optional<Book> book = Optional.empty();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,title);
            preparedStatement.setString(2, author);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                book = Optional.of(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (book.isPresent()) {
            System.out.println("Found book: " + book.get().getTitle() + ", stock: " + book.get().getStock());
        } else {
            System.out.println("Book not found with titlee: " + title + " and author: " + author);
        }
        return book;

    }

    @Override
    public boolean sellBook(Book book) {

        System.out.println("Selling book: " + book.getTitle() + ", current stock: " + book.getStock());

        if (book.getStock() <= 0) {
            System.out.println("Cannot sell book because stock is 0 or less.");
            return false;
        }

        String insertSellTableSql="INSERT INTO sold_books VALUES (null,?,?,?,?);";
        String updateSql = "UPDATE book SET stock= stock - 1 WHERE id=? AND stock > 0";
        String deleteBookSql = "DELETE FROM book WHERE id = ? AND stock = 0;";

        try{
            PreparedStatement preparedInsertStatement= connection.prepareStatement(insertSellTableSql);
            preparedInsertStatement.setString(1, book.getAuthor());
            preparedInsertStatement.setString(2,book.getTitle());
            preparedInsertStatement.setDouble(3,book.getPrice());
            preparedInsertStatement.setInt(4,1);
            int inserted=preparedInsertStatement.executeUpdate();

            PreparedStatement preparedUpdateStatement=connection.prepareStatement(updateSql);
            preparedUpdateStatement.setLong(1,book.getId());

            int updated=preparedUpdateStatement.executeUpdate();

            PreparedStatement preparedDeleteStatement = connection.prepareStatement(deleteBookSql);
            preparedDeleteStatement.setLong(1, book.getId());
            int deleted = preparedDeleteStatement.executeUpdate();


            // return (updated == 1 && inserted ==1) ? false:true;
            return (inserted == 1 && (updated == 1 || deleted == 1));

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .setPrice(resultSet.getDouble("price"))
                .setStock(resultSet.getInt("stock"))
                        .build();
    }

    private Book getSoldBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPrice(resultSet.getDouble("price"))
                .setStock(resultSet.getInt("stock_sold"))
                .build();
    }

}
