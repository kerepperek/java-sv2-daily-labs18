package day02;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser("actors");
            dataSource.setPassword("actors");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach DataBase!", sqle);
        }

        Flyway flyway = Flyway.configure().locations("db/migration/bookstore").dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        BookRepository bookRepository=new BookRepository(dataSource);

        bookRepository.insertBook("Fekete István","Téli berek",3400 ,10);
        bookRepository.insertBook("Fekete Pétern","Kártyatrükkök",1200 ,10);

        System.out.println(bookRepository.findBooksByWriter("Fekete"));

        bookRepository.updatePieces(1L, 30);
        System.out.println(bookRepository.findBooksByWriter("Fekete"));
    }

}
