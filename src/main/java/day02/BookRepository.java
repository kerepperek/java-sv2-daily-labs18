package day02;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

public class BookRepository {

    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    public BookRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public void insertBook(String writer, String title, int price, int pieces) {
        jdbcTemplate.update("insert into books(writer,title,price,pieces) values(?,?,?,?)", writer, title, price, pieces);
    }

    public List<Book> findBooksByWriter(String prefix) {
        SqlParameterSource parameters = new MapSqlParameterSource("writer", prefix+ "%");
        return namedJdbcTemplate.query("select * from books where writer like (:writer)"
                 ,parameters
                ,(rs, rowNum) ->
                        new Book(rs.getLong("id"), rs.getString("writer"), rs.getString("title"), rs.getInt("price"), rs.getInt("pieces"))
                );
    }

    public List<Book> findBooksByWriter_old(String prefix) {
        return jdbcTemplate.query("select * from books where writer like ?",
                (rs, rowNum) ->
                        new Book(rs.getLong("id"), rs.getString("writer"), rs.getString("title"), rs.getInt("price"), rs.getInt("pieces"))
                , prefix + "%");
    }
    public void updatePieces(Long id, int pieces) {
        jdbcTemplate.update("update books SET pieces=pieces+? where id=?"
                , pieces, id);

    }
}
