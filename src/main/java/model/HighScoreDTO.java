/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author fpittner
 */

public class HighScoreDTO implements Serializable{
    private String username;
    private double highscore;
    
    public HighScoreDTO(){
    }
    
    public HighScoreDTO(String name, double highscore){
        this.username = name;
        this.highscore = highscore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getHighscore() {
        return highscore;
    }

    public void setHighscore(double highscore) {
        this.highscore = highscore;
    }
    
    
}
