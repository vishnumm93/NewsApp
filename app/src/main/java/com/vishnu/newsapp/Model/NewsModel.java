
package com.vishnu.newsapp.Model;

public class NewsModel {

    private Articles[] articles;

    private String status;

    public NewsModel(Articles[] articles, String status) {
        this.articles = articles;
        this.status = status;
    }

    public Articles[] getArticles ()
    {
        return articles;
    }

    public void setArticles (Articles[] articles)
    {
        this.articles = articles;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

}
