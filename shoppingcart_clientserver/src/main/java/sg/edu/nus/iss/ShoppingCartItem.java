package sg.edu.nus.iss;

import java.util.Iterator;
import java.util.List;

public class ShoppingCartItem {

	public void itemList(List<String> cartItemList, String cartName) {
		int i = 1;
		try {
			if (cartItemList != null && cartItemList.size() > 0) {
				Iterator<String> iterator = cartItemList.iterator();

				while (iterator.hasNext()) {
					String item = iterator.next();
					System.out.println(i + ". " + item);
					i++;
				}
			} else {
				System.out.println("List is empty.");
			}
		} catch (Exception e) {
			System.err.println("Something went wrong");
		}
	}

	public void deleteCartItem(List<String> cartItemList, int itemIndex) {

		try {
			if (cartItemList != null && cartItemList.size() >= itemIndex) {

				System.out.println(cartItemList.get(itemIndex - 1) + " removed from cart");
				cartItemList.remove(itemIndex - 1);
			} else {
				System.out.println("Item not found");
			}
		} catch (Exception e) {
			System.err.println("Something went wrong");
		}

	}

}
