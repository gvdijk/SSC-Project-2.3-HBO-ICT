package algorithm;

import helper.PositionHelper;
import game.Field;
import game.Game;
import player.Player;

import java.util.function.Function;

public class LineWeight
{

    private Double indicator;

    private Algorithm algorithm;

    private Player me;

    private Function<Integer, Boolean> hasFrom;
    private Function<Integer, Integer> from;
    private Function<Integer, Boolean> inversedHasFrom;
    private Function<Integer, Integer> inversedFrom;

    public LineWeight(Algorithm algorithm, Player me)
    {
        this.algorithm = algorithm;
        this.me = me;
    }

    public void setIndicator(Double indicator)
    {
        this.indicator = indicator;
    }

    public Double execute(int field)
    {
        Double score = 1.0;

        // Left

        this.hasFrom = PositionHelper::hasLeftFrom;
        this.inversedHasFrom = PositionHelper::hasRightFrom;

        this.from = PositionHelper::leftFrom;
        this.inversedFrom = PositionHelper::rightFrom;

        score = score * this.doExecute(field);

        // right

        this.hasFrom = PositionHelper::hasRightFrom;
        this.inversedHasFrom = PositionHelper::hasLeftFrom;

        this.from = PositionHelper::rightFrom;
        this.inversedFrom = PositionHelper::leftFrom;

        score = score * this.doExecute(field);

        // up

        this.hasFrom = PositionHelper::hasUpFrom;
        this.inversedHasFrom = PositionHelper::hasDownFrom;

        this.from = PositionHelper::upFrom;
        this.inversedFrom = PositionHelper::downFrom;

        score = score * this.doExecute(field);

        // down

        this.hasFrom = PositionHelper::hasDownFrom;
        this.inversedHasFrom = PositionHelper::hasUpFrom;

        this.from = PositionHelper::downFrom;
        this.inversedFrom = PositionHelper::upFrom;

        score = score * this.doExecute(field);

        // upper left

        this.hasFrom = PositionHelper::hasUpperLeftFrom;
        this.inversedHasFrom = PositionHelper::hasDownRightFrom;

        this.from = PositionHelper::upperLeftFrom;
        this.inversedFrom = PositionHelper::downRightFrom;

        score = score * this.doExecute(field);

        // upper right

        this.hasFrom = PositionHelper::hasUpperRightFrom;
        this.inversedHasFrom = PositionHelper::hasDownLeftFrom;

        this.from = PositionHelper::upperRightFrom;
        this.inversedFrom = PositionHelper::downLeftFrom;

        score = score * this.doExecute(field);

        // down left

        this.hasFrom = PositionHelper::hasDownLeftFrom;
        this.inversedHasFrom = PositionHelper::hasUpperRightFrom;

        this.from = PositionHelper::downLeftFrom;
        this.inversedFrom = PositionHelper::upperRightFrom;

        score = score * this.doExecute(field);

        // down right

        this.hasFrom = PositionHelper::hasDownRightFrom;
        this.inversedHasFrom = PositionHelper::hasUpperLeftFrom;

        this.from = PositionHelper::downRightFrom;
        this.inversedFrom = PositionHelper::upperLeftFrom;

        score = score * this.doExecute(field);

        return score;
    }

    private Double doExecute(Integer field)
    {
        Boolean valid = true;

        Integer pos = field;
        while(this.hasFrom.apply(pos)) {
            pos = this.from.apply(pos);

            if (this.algorithm.getBoard()[pos].equals(Field.EMPTY)) {
                break;
            }

            if (! valid) {
                if (this.algorithm.getBoard()[pos].equals(this.me.getColor())) {
                    valid = true;
                    break;
                }
            }

            if (this.algorithm.getBoard()[pos].equals(this.algorithm.getGame().getOpponent(this.me).getColor())) {
                valid = false;
            }
        }

        if (! valid) {
            while (this.inversedHasFrom.apply(pos)) {
                pos = this.inversedFrom.apply(pos);

                if (this.algorithm.getBoard()[pos].equals(this.algorithm.getGame().getOpponent(this.me).getColor())) {
                    break;
                }

                if (this.algorithm.getBoard()[pos].equals(Field.EMPTY)) {
                    return this.indicator;
                }
            }
        }

        return 1.0;
    }

}
