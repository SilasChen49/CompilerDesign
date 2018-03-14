package lexical;

import java.util.HashSet;

public class DFATable {

    int height, width, puncHeight, puncWidth;
    int[][] table;
    int[][] puncTable;

    //alphabets input1 the left half table
    private HashSet<Character> nonzero;
    private HashSet<Character> zero;
    private HashSet<Character> letter;
    private HashSet<Character> alphanum;
    private HashSet<Character> spaceEnter;

    DFATable() {
        height = 18;
        width = 13;

        initAlphabets();
        initTable();
    }

    public State nextState(State state, char c) {
        int currentState = state.getState();
        int column;
        if (c == '+')
            column = 1;
        else if (c == '-')
            column = 2;
        else if (c == '.')
            column = 3;
        else if (c == 'e')
            column = 4;
        else if (nonzero.contains(c))
            column = 5;
        else if (zero.contains(c))
            column = 6;
        else if (letter.contains(c))
            column = 7;
        else if (alphanum.contains(c))
            column = 8;
        else if (spaceEnter.contains(c))
            column = 9;
        else
            column = 10;

        int nextState = table[currentState][column];
        state.setState(nextState);
        return state;
    }

    public boolean isFinalState(State state) {
        if (table[state.getState()][11] > 0)
            return true;
        else
            return false;
    }

    public boolean isRollBack(State state) {
        if (table[state.getState()][12] > 0)
            return true;
        else
            return false;
    }

    public void initAlphabets() {
        nonzero = new HashSet<>();
        zero = new HashSet<>();
        letter = new HashSet<>();
        alphanum = new HashSet<>();
        spaceEnter = new HashSet<>();

        for (int i = 1; i <= 9; i++)
            nonzero.add((char) (i + '0'));

        zero.add('0');

        for (int i = 0; i < 26; i++) {
            letter.add((char) (i + 'a'));
            letter.add((char) (i + 'A'));
        }

        alphanum.addAll(letter);
        alphanum.addAll(nonzero);
        alphanum.addAll(zero);
        alphanum.add('_');

        spaceEnter.add(' ');
        spaceEnter.add('\n');
        spaceEnter.add((char)-1);
    }

    public TokenType getTokenType(State state) {
        TokenType tokenType;
        int number = state.getState();
        if (number == 11 || number == 12 || number == 13)
            tokenType = TokenType.TOKEN_PUNCTUATION;
        else if (number == 14)
            tokenType = TokenType.TOKEN_ERROR;
        else if (number == 15)
            tokenType = TokenType.TOKEN_ID;
        else if (number == 16)
            tokenType = TokenType.TOKEN_INTEGER;
        else if (number == 17)
            tokenType = TokenType.TOKEN_FLOAT;
        else
            tokenType = TokenType.TOKEN_ERROR;
        return tokenType;
    }


    public void initTable() {
        table = new int[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                table[i][j] = 0;

        for (int i = 0; i < height; i++)
            table[i][0] = i;

        table[0][1] = 11;
        table[0][2] = 12;
        table[0][3] = 13;
        table[0][4] = 2;
        table[0][5] = 7;
        table[0][6] = 3;
        table[0][7] = 2;
        table[0][8] = 14;
        table[0][9] = 0;
        table[0][10] = 14;

        fillRow(1, 14);
        table[1][5] = 6;
        table[1][6] = 5;

        fillRow(2, 15);
        table[2][4] = 2;
        table[2][5] = 2;
        table[2][6] = 2;
        table[2][7] = 2;
        table[2][8] = 2;

        fillRow(3, 16);
        table[3][3] = 8;

        fillRow(4, 14);
        table[4][5] = 9;
        table[4][6] = 4;

        fillRow(5, 17);

        fillRow(6, 17);
        table[6][5] = 6;
        table[6][6] = 6;

        fillRow(7, 16);
        table[7][3] = 8;
        table[7][5] = 7;
        table[7][6] = 7;

        fillRow(8, 14);
        table[8][5] = 9;
        table[8][6] = 9;

        fillRow(9, 17);
        table[9][4] = 10;
        table[9][5] = 9;
        table[9][6] = 4;

        fillRow(10, 14);
        table[10][1] = 1;
        table[10][2] = 1;
        table[10][5] = 6;
        table[10][6] = 5;

        table[11][11] = 1;

        table[12][11] = 1;

        table[13][11] = 1;

        table[14][11] = 1;

        table[15][11] = 1;
        table[15][12] = 1;

        table[16][11] = 1;
        table[16][12] = 1;

        table[17][11] = 1;
        table[17][12] = 1;
    }

    public void fillRow(int row, int value) {
        for (int i = 0; i <= 10; i++)
            table[row][i] = value;
    }

    public void printTable() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++)
                System.out.print(table[i][j] + " ");
            System.out.println();
        }
    }
}
