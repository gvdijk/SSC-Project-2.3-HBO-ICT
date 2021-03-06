package models.minimax;

import models.config.ReversiIndicatorSet;
import models.minimax.weight.CornerWeight;
import models.minimax.weight.LineWeight;
import models.game.Field;
import models.game.GameInfo;

import java.util.ArrayList;
import java.util.function.Function;

public class ReversiMinimax extends Minimax<ReversiIndicatorSet>
{

    protected ArrayList<Function<Integer, Double>> weightStatements = new ArrayList<>();

    /**
     * Score matrix for calculation the score based off positions on the board.
     */
    private final int[] scoreMatrix = {
            200,  -25,  25,  10,  10,   25,  -25,  200,
            -25,  -50,  -5,   0,   0,   -5,  -50,  -25,
            25,   -5,   10,   2,   2,   10,   -5,   25,
            10,    0,    2,   5,   5,    2,    0,   10,
            10,    0,    2,   5,   5,    2,    0,   10,
            25,   -5,   10,   2,   2,   10,   -5,   25,
            -25,  -50,  -5,   0,   0,   -5,  -50,  -25,
            200,  -25,  25,  10,  10,   25,  -25,  200
    };

    /**
     * Configure this minimax to a player
     * @param field the maximizing Player
     * @param indicatorSet
     */
    public ReversiMinimax(Field field, ReversiIndicatorSet indicatorSet){
        super(GameInfo.REVERSI, field);
        this.indicatorSet = indicatorSet;
    }

    /**
     * Calculate the score for the current player on the current board for Reversi
     * @param board the board to calculate the score onn
     * @param self the player type
     * @return the score from the maximizing player's perspective
     */
    @Override
    int calculateScore(Field[] board, Field self)
    {
        double[] weightMatrix = this.createWeightMatrix(board, this.indicatorSet);

        int white = 0;
        int black = 0;
        for (int i =0; i < board.length; i++) {
            boolean negative = scoreMatrix[i] < 0;
            double score = scoreMatrix[i] * (negative ? 1 / weightMatrix[i] : weightMatrix[i]);
            if(board[i] == Field.WHITE) white += score;
            if(board[i] == Field.BLACK) black += score;
        }

        return self == Field.BLACK ? (black - white) : (white - black);
    }



    protected Double getScore(Integer field)
    {
        Double weight = 1.0;

        for (int i = 0; i < weightStatements.size(); i++) {
            weight = weight * weightStatements.get(i).apply(field);
        }

        return weight;
    }

    public void addWeightStatement(Function<Integer, Double> weightStatement) {
        this.weightStatements.add(weightStatement);
    }

    public double[] createWeightMatrix(Field[] board, ReversiIndicatorSet indicatorSet) {
        // Line Algorithm
        if (indicatorSet.hasLineIndicator()) {
            LineWeight lineWeight = new LineWeight(board);
            lineWeight.setIndicator(indicatorSet.getLineIndicator());
            this.addWeightStatement(lineWeight::execute);
        }

        // Corner Algorithm
        if (indicatorSet.hasCornerIndicator()) {
            CornerWeight cornerWeight = new CornerWeight(board);
            cornerWeight.setIndicator(indicatorSet.getCornerIndicator());
            this.addWeightStatement(cornerWeight::execute);
        }

        double[] weightMatrix = new double[64];

        for (int i = 0; i < board.length; i++) {
            weightMatrix[i] = this.getScore(i);
        }

        this.weightStatements.clear();

        return weightMatrix;
    }
}
