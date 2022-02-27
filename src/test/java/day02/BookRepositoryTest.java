package day02;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryTest {
    BookRepository bookRepository;
    Flyway flyway;

    @BeforeEach
    void init(){
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser("actors");
            dataSource.setPassword("actors");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach DataBase!", sqle);
        }

       flyway = Flyway.configure().locations("db/migration/bookstore").dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

       bookRepository=new BookRepository(dataSource);
    }
    @Test
    void testInsert(){

        bookRepository.insertBook("Fekete István","Téli berek",3400 ,10);
        bookRepository.insertBook("Fekete Pétern","Kártyatrükkök",1200 ,10);

        assertEquals(2,bookRepository.findBooksByWriter("Fekete").size());

    }

}