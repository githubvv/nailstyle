package db.entity;

public class ArticleImages{
	private Integer id;
	private int articleId;
	private String name;
	
	public ArticleImages() {}

	public ArticleImages(int articleId, String name) {
		this.articleId = articleId;
		this.name = name;
	}

    public ArticleImages(int id, int articleId, String name) {
        this.id = id;
        this.articleId = articleId;
        this.name = name;
    }
	
	public Integer getId() {
		return id;
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
        ArticleImages articleImages = (ArticleImages) obj;
        if (this.id != articleImages.id)
            return false;

        return true;
    }



}
