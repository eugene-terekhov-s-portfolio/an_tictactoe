package ru.eterekhov.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private enum Players {
        NONE,
        RED,
        GREEN
    }

    Players activePlayer = Players.RED;
    Players[] cellState;

    int[][] winningCombos = {
            {0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}
    };
    boolean gameIsActive = true;

    public void makeAMove(View view) {
        ImageView activeCell = (ImageView)view;
        if (isMovePossible(activeCell.getTag())) {
            updateCellState(activeCell.getTag());
            animateChipAppearing(activeCell);
            if (activePlayerHasWon()) {
                showWinningMessage();
            } else {
                if (isItDraw()) {
                    showDrawMessage();
                }
            }

            switchActivePlayer();
        }
    }

    private void showDrawMessage() {
        TextView tvGameResult = findViewById(R.id.tvGameResult);
        tvGameResult.setText("It is a draw!");
        findViewById(R.id.messageLayout).setVisibility(View.VISIBLE);
    }

    private boolean isItDraw() {
        for (Players state : cellState) {
            if (state == Players.NONE) {
                return false;
            }
        }
        return true;
    }

    private void showWinningMessage() {
        String winningPlayer = activePlayer == Players.RED ? "Red" : "Green";
        TextView tvGameResult = findViewById(R.id.tvGameResult);
        tvGameResult.setText(winningPlayer + " has won!");
        findViewById(R.id.messageLayout).setVisibility(View.VISIBLE);
    }

    private void updateCellState(Object tag) {
        cellState[Integer.parseInt(tag.toString())] = activePlayer;
    }

    private boolean isMovePossible(Object tag) {
        return cellState[Integer.parseInt(tag.toString())] == Players.NONE && gameIsActive;
    }

    private boolean activePlayerHasWon() {
        for (int[] winningCombo : winningCombos) {
            if (cellState[winningCombo[0]] == activePlayer &&
                    cellState[winningCombo[1]] == activePlayer &&
                    cellState[winningCombo[2]] == activePlayer) {
                gameIsActive = false;
                return true;
            }
        }
        return false;
    }

    private void switchActivePlayer() {
        activePlayer = activePlayer == Players.RED ? Players.GREEN : Players.RED;
    }

    private void animateChipAppearing(ImageView activeCell) {
        int chipRes = activePlayer == Players.RED ? R.drawable.tictactoe_cross : R.drawable.tictactoe_nought;
        activeCell.setScaleX(0f);
        activeCell.setScaleY(0f);
        activeCell.setImageResource(chipRes);
        activeCell.animate().scaleXBy(1f).scaleYBy(1f).rotationBy(180f).setDuration(250);

    }

    public void startNewGame(View view) {
        startNewGame();
    }

    private void startNewGame() {
        resetGameField();
        resetCellStates();
        resetGameState();
        hideResultMessage();
    }

    private void hideResultMessage() {
        LinearLayout messageLayout = findViewById(R.id.messageLayout);
        messageLayout.setVisibility(View.INVISIBLE);
    }

    private void resetGameField() {
        GridLayout grid = findViewById(R.id.gridLayout);
        for(int i=0; i<grid.getChildCount(); i++) {
            ((ImageView) grid.getChildAt(i)).setImageResource(0);
        }
    }

    private void resetGameState() {
        activePlayer = Players.RED;
        gameIsActive = true;
    }

    private void resetCellStates() {
        cellState = new Players[] {
                Players.NONE, Players.NONE, Players.NONE,
                Players.NONE, Players.NONE, Players.NONE,
                Players.NONE, Players.NONE, Players.NONE
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startNewGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
