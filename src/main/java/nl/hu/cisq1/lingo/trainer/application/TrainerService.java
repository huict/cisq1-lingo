package nl.hu.cisq1.lingo.trainer.application;


import nl.hu.cisq1.lingo.trainer.data.GameBlob;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameStatus;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotSupportedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TrainerService {
    private final SpringGameRepository gameRepository;

    public TrainerService(SpringGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameStatus provideNewGame(String beginWord) {
        Game game = new Game(beginWord);

        GameBlob gameBlob = new GameBlob(game);
        this.gameRepository.save(gameBlob);

        return new GameStatus(gameBlob.getId(), game.getCurrentRound().getLastHint());
    }


}
