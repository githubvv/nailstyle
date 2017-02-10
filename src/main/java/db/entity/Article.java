package db.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Article implements Serializable {
	private Integer id;
	private String name;
	private String text;
	private Date date;
    private String description;
    private List<ArticleImages> articleImgList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Article() {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		java.util.Date utilDate = cal.getTime();
		this.date = new Date(utilDate.getTime());
        this.articleImgList = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

    public List<ArticleImages> getArticleImgList() {
        return articleImgList;
    }

    public void setArticleImgList(List<ArticleImages> articleImgList) {
        this.articleImgList = articleImgList;
    }

    public void addArtImg(ArticleImages artImg){
        articleImgList.add(artImg);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Article article = (Article) obj;
		if (this.id != article.id)
			return false;

		return true;
	}

}
