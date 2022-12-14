package se.atg.test.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.atg.test.dto.GameEvent;
import se.atg.test.service.GamesSortService;

import java.util.List;

@RestController
@RequestMapping("/")
public class ButiksandelarController {

    final private GamesSortService gamesSortService;

    @Autowired
    public ButiksandelarController(GamesSortService gamesSortService) {
        this.gamesSortService = gamesSortService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getStatus() {
        return "ok";
    }

    @PostMapping
    public List<String> getStatus(@RequestBody List<GameEvent> games) {
        return gamesSortService.processGameList(games);
    }


}
