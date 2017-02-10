package db.entity;

public class PriceItem {
	private Integer id;
	private String nameService;
	private Double price;
	private String description;
    private Integer prioritet;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNameService() {
		return nameService;
	}

	public void setNameService(String nameService) {
		this.nameService = nameService;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public Integer getPrioritet() {
        return prioritet;
    }

    public void setPrioritet(Integer prioritet) {
        this.prioritet = prioritet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceItem priceItem = (PriceItem) o;

        if (!id.equals(priceItem.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
