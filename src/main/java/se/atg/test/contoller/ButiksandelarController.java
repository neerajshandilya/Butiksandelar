package se.atg.test.contoller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.atg.test.dto.GameEvent;
import se.atg.test.service.GamesSortService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@Slf4j
public class ButiksandelarController {

    private final GamesSortService gamesSortService;

    private final Validator validator;


    @Autowired
    public ButiksandelarController(GamesSortService gamesSortService, @Qualifier("gameEventValidator") Validator validator) {
        this.gamesSortService = gamesSortService;
        this.validator = validator;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getStatus() {
        return "ok";
    }

    @PostMapping
    public List<String> getStatus(@RequestBody @Validated List<GameEvent> inputGamesList) {
        final List<GameEvent> processGamesList = new ArrayList<>(inputGamesList);
        validator.validate(processGamesList, null);
        return gamesSortService.sortGamesList(processGamesList);
    }


}
