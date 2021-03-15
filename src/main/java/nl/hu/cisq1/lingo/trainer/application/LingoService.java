package nl.hu.cisq1.lingo.trainer.application;

import lombok.RequiredArgsConstructor;
import nl.hu.cisq1.lingo.trainer.data.Game;
import nl.hu.cisq1.lingo.trainer.data.GameRepository;
import nl.hu.cisq1.lingo.trainer.domain.LingoGame;
import nl.hu.cisq1.lingo.trainer.exception.GameNotFoundException;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GuessDTO;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LingoService {
    private final WordService wordService;
    private final GameRepository gameRepository;

    public LingoGame startGame() {
        LingoGame lingoGame = new LingoGame();
        lingoGame.newRound(wordService.provideRandomWord(lingoGame.generateType().number()));

        return this.saveGame(lingoGame, false);
    }

    public LingoGame makeGuess(String guess) {
        LingoGame lingoGame = this.retrieveLingoGame().getLingoGame();

        lingoGame.getLastRound().makeGuessAndGiveHint(guess, this.wordService.wordExistsByValue(guess));

        switch (lingoGame.getLastRound().getRoundState()) {
            case LOST : return this.saveGame(lingoGame, true);
            case WON : {
                lingoGame.newRound(wordService.provideRandomWord(lingoGame.generateType().number()));
                return this.saveGame(lingoGame, false);
            }
            default : return this.saveGame(lingoGame, false);
        }
    }

    private LingoGame saveGame(LingoGame lingoGame, boolean gameDone) {
        this.gameRepository.save(new Game(lingoGame, gameDone));
        return lingoGame;
    }

    private Game retrieveLingoGame() {
        return this.gameRepository.findTopByGameDoneOrderByIdDesc(false)
                .orElseThrow(() -> new GameNotFoundException("⏺ ⏺ ⏺ ⏺ Game has not started yet! ⏺ ⏺ ⏺ ⏺"));
    }
}
