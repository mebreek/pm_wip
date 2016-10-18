package net.ibrik.mai.popularmovies04;

import java.io.Serializable;

/**
 * Created by Mohamad on 16/10/2016.
 */

public class MovieDetails implements Serializable {

    private String
        strTitle,
        strPosterPath,
        strBackdropPath,
        strOverview;
    private int intID,
            intVoteCount;
    private double dblPopularity, dblVoteAvg;
    private boolean blnAdult, blnVideo;

    public String getTitle (){
        return strTitle;
    }
    public void setTitle (String strTitle_in) {
        this.strTitle = strTitle_in;
    }

    public String getPosterPath () {
        return strPosterPath;
    }
    public void setPosterPath (String strPosterPath) {
        this.strPosterPath = strPosterPath;
    }

    public String getOverview () {
        return strOverview;
    }
    public void setOverview (String strOverview) {
        this.strOverview = strOverview;
    }

    public String getBackdropPath () {
        return strBackdropPath;
    }
    public void setBackdropPath (String strBackdropPath) {
        this.strBackdropPath = strBackdropPath;
    }

    public double getPopularity () {
        return dblPopularity;
    }
    public void setPopularity (double dblPopularity ) {
        this.dblPopularity = dblPopularity;
    }

    public double getVoteAvg () {
        return dblVoteAvg;
    }
    public void setVoteAvg (double dblVoteAvg ) {
        this.dblVoteAvg = dblVoteAvg;
    }

    public int getID () {
        return intID;
    }
    public void setID (int   intID) {
        this.intID = intID;
    }

    public int getVoteCount () {
        return intVoteCount;
    }
    public void setIntVoteCount (int intVoteCount ) {
        this.intVoteCount = intVoteCount;
    }

    public boolean isAdult () {
        return blnAdult;
    }
    public void setAdult (boolean blnAdult ) {
        this.blnAdult = blnAdult;
    }


    public boolean isVideo () {
        return blnVideo;
    }
    public void setVideo (boolean blnVideo ) {
        this.blnVideo = blnVideo;
    }


}
