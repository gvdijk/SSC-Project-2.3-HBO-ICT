package GameAR;

import GameAR.Algorithm.Algorithm;
import GameAR.Config.IndicatorSet;
import GameAR.Engine.Board;

public class Main
{

    public static void main(String[] args)
    {
        Integer depth = 5;

        IndicatorSet indicatorSet = new IndicatorSet();
        indicatorSet.setLineIndicator(0.8);
        indicatorSet.setCornerIndicator(4.0);
        indicatorSet.setBorderFirstIndicator(2.0);

        Board board = new Board(8, 8);
        Algorithm algorithm = new Algorithm(board, indicatorSet);

        try {

            Integer score = algorithm.simulate(depth);

            System.out.println(score);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}