package se.atg.test.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.atg.test.dto.GameEvent;
import se.atg.test.service.GamesSortService;

import java.util.List;

@RestController
public class ButiksandelarController {

    final private GamesSortService gamesSortService;

    @Autowired
    public ButiksandelarController(GamesSortService gamesSortService) {
        this.gamesSortService = gamesSortService;
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String getStatus() {
        return "ok";
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.POST, consumes = {"application/json"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String getStatus(@RequestBody List<GameEvent> games) {
        return gamesSortService.processGameList(games);
    }


}
