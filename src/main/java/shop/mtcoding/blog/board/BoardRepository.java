package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.awt.desktop.QuitResponse;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
    private final EntityManager em;


    public List<Board> findAll(int page) {
        int count = 5;
        int value = page * count;

        Query query = em.createNativeQuery("select * from board_tb order by id desc limit ?,?", Board.class);
        query.setParameter(1, value);
        query.setParameter(2, count);

        List<Board> boardList = query.getResultList();
        return boardList;

    }

    @Transactional
    public void save(BoardRequest.SaveDTO requestDTO) {
        Query query = em.createNativeQuery("INSERT INTO board_tb(title,content,author) values(?,?,?)", Board.class);
        query.setParameter(1, requestDTO.getTitle());
        query.setParameter(2, requestDTO.getContent());
        query.setParameter(3, requestDTO.getAuthor());

        query.executeUpdate();


    }

    @Transactional
    public void delete(int id) {
        Query query = em.createNativeQuery("delete from board_tb where id=?", Board.class);
        query.setParameter(1, id);

        query.executeUpdate();
    }

    @Transactional
    public void update(BoardRequest.UpdateDTO requestDTO, int id) {
        Query query = em.createNativeQuery("update board_tb set author = ?, title = ?, content = ? where id = ?");
        query.setParameter(1, requestDTO.getAuthor());
        query.setParameter(2, requestDTO.getTitle());
        query.setParameter(3, requestDTO.getContent());
        query.setParameter(4, id);

        query.executeUpdate();
    }

    public Board findById(int id) {
        Query query = em.createNativeQuery("select *from board_tb where id=?", Board.class);
        query.setParameter(1, id);
        Board board = (Board) query.getSingleResult();
        return board;
    }

    public int totalCount() {
        Query query = em.createNativeQuery("select count(*) from board_tb");

        int count= ((Number)query.getSingleResult()).intValue();
        return count;
    }
}
