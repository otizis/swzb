package com.yz.zwzb.domain;

public class PlayerResultOneMatch
{
    String account;
    int score;
    boolean isWin;

    public boolean isWin()
    {
        return isWin;
    }

    public void setWin(boolean win)
    {
        isWin = win;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }
}
