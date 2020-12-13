import org.jdbf.engine.basic.ObjectMapped;


public class Product extends ObjectMapped {

	private String	name;
	private int	groupId;
	private int 	price;
	
	
    public Product(){}

	public Product(String repositoryName){
	    super(repositoryName);
	}
					    
	public String getName() { return name; }

	public int getGroupId() { return groupId; }

	public int getPrice() { return price; }

	

	public void setName(String name) {
		this.name = name;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public void setPrice(int price) {
		this.price = price;
	}	
}
