package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    public final BoardRepository boardRepository;
    @GetMapping({ "/", "/board"})
    public String index(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
        List<Board> boardList= boardRepository.findAll(page);
        request.setAttribute("boardList", boardList);
        System.out.println(boardList);
        int currentPage =page;
        int nextPage = currentPage+1;
        int prevPage = currentPage-1;
        request.setAttribute("nextPage", nextPage);
        request.setAttribute("prevPage", prevPage);

        int totalCount=boardRepository.totalCount();
        int totalCountPage=totalCount/5+((totalCount%5>0)?1:0);
        boolean first =(currentPage==0) ? true:false;
        boolean last =(currentPage+1==totalCountPage) ? true:false;

        List<Integer> pageList = new ArrayList<>();
        for (int i = 0; i < totalCountPage ; i++) {
            pageList.add(i);
        }

        request.setAttribute("pageList", pageList);
        request.setAttribute("first", first);
        request.setAttribute("last", last);



        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request) {
        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);
        return "board/updateForm";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO,HttpServletRequest request){
        System.out.println(requestDTO);
        System.out.println(requestDTO.getTitle().length());
        if(requestDTO.getTitle().length()>30){
            request.setAttribute("status", 400);
            request.setAttribute("msg", "title의 길이가 30자를 초과하면 안돼요");
            return "error/40x";
        }
        System.out.println(requestDTO.getContent().length());
        if(requestDTO.getContent().length()>30){
            request.setAttribute("status", 400);
            request.setAttribute("msg", "content의 길이가 30자를 초과하면 안돼요");
            return "error/40x";
        }

        boardRepository.save(requestDTO);
        return "redirect:/";
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO, HttpServletRequest request){
        System.out.println(requestDTO);
        //TODO : 유효성검사
        System.out.println(requestDTO);
        System.out.println(requestDTO.getTitle().length());
        if(requestDTO.getTitle().length()>30){
            request.setAttribute("status", 400);
            request.setAttribute("msg", "title의 길이가 30자를 초과하면 안돼요");
            return "error/40x";
        }
        System.out.println(requestDTO.getContent().length());
        if(requestDTO.getContent().length()>30){
            request.setAttribute("status", 400);
            request.setAttribute("msg", "content의 길이가 30자를 초과하면 안돼요");
            return "error/40x";
        }
        boardRepository.update(requestDTO, id);
        return "redirect:/";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id){
        System.out.println(id);
        boardRepository.delete(id);

        return "redirect:/";
    }
}
