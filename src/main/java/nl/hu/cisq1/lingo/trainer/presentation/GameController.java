package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.GameService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public Game startGame() {
        return gameService.startNewGame();
    }

    @PostMapping(value = "/{id}")
    public Game makeGuess(@PathVariable int id, @RequestParam String guess) {
        return gameService.guess(id, guess);
    }

    @PostMapping(value = "/{id}/round")
    public Game newRound(@PathVariable int id) {
        return gameService.startNewRound(id);
    }

    @GetMapping(value = "/{id}")
    public Game getGame(@PathVariable int id) {
        return gameService.getGame(id);
    }

}
