package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.*;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView playerOneScore, playerTwoScore, playerStatus;
    private Button [] buttons = new Button[9];
    private Button resetGame;

    private int playerOneScoreCount, playerTwoScoreCount, round;
    private boolean activePlayer;

    //keeps track of wether each of 9 buttons is a x or 0
    //p1 => 0
    //p2 => 1
    //empty => 2
    int [] gameState = {2,2,2,2,2,2,2,2,2};

    //Different position cobinations a player must hold in order to win
    int [][] winningPositions = {
            {0,1,2}, {3,4,5}, {6,7,8}, //rows,
            {0,3,6}, {1,4,7}, {2,5,8}, //col
            {0,4,8},{2,4,6} //diag
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerOneScore = (TextView) findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);

        resetGame = (Button) findViewById(R.id.resetGame);

        for(int i =0; i < buttons.length; i++){
            String buttonId = "btn_" + i;

            //used because we cant use R.id since the buttonID changes for each button
            int resourseID = getResources().getIdentifier(buttonId, "id",getPackageName());
            buttons[i] = (Button) findViewById(resourseID);
            buttons[i].setOnClickListener(this);
        }

        //initiates the round
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;
        round = 0;
    }

    @Override
    public void onClick(View view) {
        if( !((Button) view).getText().toString().equals("")){

            return;
        }

        //get id of button that was pressed
        String buttonID= view.getResources().getResourceEntryName(view.getId());
        int gameStateID = Integer.parseInt(buttonID.substring(buttonID.length()-1, buttonID.length())); //returns n for "btn_n"

        //checked if player 1 is playing
        if(activePlayer){
            //update button to show it was selected by player 1
            ((Button) view).setText("X");
            ((Button) view).setTextColor(Color.parseColor("#6A70FD"));
            gameState[gameStateID] = 0;
        }else{
            //update button to show it was selected by player 2
            ((Button) view).setText("O");
            ((Button) view).setTextColor(Color.parseColor("#FCB74E"));
            gameState[gameStateID] = 1;
        }
        round++;
        if(checkWinner()){
            if(activePlayer){
                playerOneScoreCount++;
                updatePlayerScore();
                Toast.makeText(this, "Player One Won!", Toast.LENGTH_SHORT).show();
                playAgain();;
            }else{
                playerTwoScoreCount++;
                updatePlayerScore();
                Toast.makeText(this, "Player Two Won!", Toast.LENGTH_SHORT).show();
                playAgain();;
            }
        }else if(round == 9){
            Toast.makeText(this, "No Winner!", Toast.LENGTH_SHORT).show();
            playAgain();;
        }else{
            //switch to other player turn
            activePlayer = !activePlayer;
        }

        resetGame.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                playAgain();
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                updatePlayerScore();
            }
        });
    }

    //check each winning position combination to see if any players holds them
    public boolean checkWinner(){
        for(int [] winningPosition: winningPositions){
            //check if any winningPosition group has the same value (but not all empty! ie == 2)
            if(gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2){
                return true;
            }
        }

        return false;

    }

    public void updatePlayerScore(){
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }

    public void playAgain(){
        round = 0;
        activePlayer = true;
        for(int i = 0; i < buttons.length; i ++){
            gameState[i] = 2;
            buttons[i].setText("");
            System.out.println(i);
        }
    }


}