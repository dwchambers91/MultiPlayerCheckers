

public class Checkers {
    private int[][] board = null;
    private String player1 = "";
    private String player2 = "";
    private String turn = "";

    public Checkers(String player1, String player2) {
        int[][] b = {
                {3, 0, 3, 0, 3, 0, 3, 0},
                {0, 3, 0, 3, 0, 3, 0, 3},
                {3, 0, 3, 0, 3, 0, 3, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1}
        };
        board = b;
        turn = player1;
        this.player1 = player1;
        this.player2 = player2;
    }

    public String getBoard() {
        return intBoardToString(board);
    }

    private String intBoardToString(int[][] b) {
        String res = "[";
        for (int[] y : b) {
            res += "[";
            for (int x: y) {
                res += x+",";
            }
            res = res.substring(0, res.length()-1);
            res += "],";
        }
        res = res.substring(0, res.length()-1);
        res += "]";
        return res;
    }

    public String getRotatedBoard(String currentBoard) {
        String res = currentBoard;
        res = res.replace("1", "-1");
        res = res.replace("3", "1");
        res = res.replace("-1", "3");
        res = res.replace("2", "-2");
        res = res.replace("4", "2");
        res = res.replace("-2", "4");
        res = new StringBuffer(res).reverse().toString();
        res = res.replace("[", "*");
        res = res.replace("]", "[");
        res = res.replace("*", "]");
        return res;
    }

    public void setBoard(int[][] newBoard) {
        if (turn.equals(player1)) {
            board = newBoard;
        } else {
            String newBoardStr = intBoardToString(newBoard);
            newBoardStr = getRotatedBoard(newBoardStr);

            int[][] realBoard = new int[8][8];
            String res = newBoardStr.substring(1, newBoardStr.length()-1);
            String[] rows = res.split("\\],\\[");
            rows[0] = rows[0].substring(1, rows[0].length());
            rows[7] = rows[7].substring(0, rows[7].length()-1);

            for (int y = 0; y < 8; y++) {
                String chars[] = rows[y].split(",");
                for (int x = 0; x < 8; x++) {
                    realBoard[y][x] = Integer.parseInt(chars[x]);
                }
            }
            board = realBoard;
        }
    }

    public String getTurn() {
        return turn;
    }

    public void changeTurns() {
        if (turn.equals(player1)) {
            turn = player2;
        } else if (turn.equals(player2)) {
            turn = player1;
        } else {
            System.err.println("Turn Error");
        }
    }
}