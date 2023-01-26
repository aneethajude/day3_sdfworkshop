package sg.edu.nus.iss;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ShoppingCartClient {

	public static void main(String[] args) {
		Socket socket = null;
		ObjectInputStream objectInputStream = null;
		ObjectOutputStream objectOutputStream = null;
		RequestVO requestVO = null;
		ResponseVO responseVO = null;
		Scanner strInput = null;
		List<String> cartItemList = null;
		String[] arg1 = null;
		String[] arg2 = null;
		String hostName = "";
		int serverPort = 0;
		String cartName = null;
		boolean isCartExist = true;
		try {
			strInput = new Scanner(System.in);
			cartItemList = new ArrayList<String>();
			requestVO = new RequestVO();
			responseVO = new ResponseVO();
			if (args != null && args.length > 0) {
				arg1 = args[0].split("@");
				arg2 = arg1[1].split(":");
				cartName = arg1[0];
				hostName = arg2[0];
				serverPort = Integer.parseInt(arg2[1]);
			} else {
				cartName = "fred";
				hostName = "localhost";
				serverPort = 3000;
			}

			socket = new Socket(hostName, serverPort);
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			requestVO.setRequestName("LOAD");
			requestVO.setCartName(cartName);
			objectOutputStream.writeObject(requestVO);
			responseVO = (ResponseVO) objectInputStream.readObject();
			socket.close();
			if ("0".equals(responseVO.getResponseStatus())) {
				cartItemList = responseVO.getCartItems();
				System.out.println(responseVO.getResponseMessage());
			} else {
				System.out.println(responseVO.getResponseMessage());
				socket = new Socket(hostName, serverPort);
				objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				objectInputStream = new ObjectInputStream(socket.getInputStream());
				
				if ("Y".equalsIgnoreCase(strInput.nextLine())) {
					requestVO.setCartName(cartName);
					requestVO.setRequestName("CREATE");
					objectOutputStream.writeObject(requestVO);
					System.out.println(objectInputStream.readUTF());
				} else {
					isCartExist = false;
					requestVO.setRequestName("EXIT");
					objectOutputStream.writeObject(requestVO);
					System.out.println(objectInputStream.readUTF());
				}
				
				objectInputStream.close();
				objectOutputStream.close();
				socket.close();
			}
			
			if (isCartExist) 
				shoppingCartSelectionOperation(cartItemList, strInput, cartName, objectOutputStream, requestVO, socket, hostName, serverPort, objectInputStream);

			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Something went wrong");
		}
	}

	public static void shoppingCartSelectionOperation(List<String> cartItems, Scanner scannerObj,
			String cartName, ObjectOutputStream objectOutputStream,
			RequestVO requestVO, Socket socket, String hostName, int serverPort, ObjectInputStream objectInputStream) {
		String ch = "";
		String[] cartItemList = null;
		ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
		try {
			do {
				cartItemList = scannerObj.nextLine().split(" ");
				if (cartItemList != null && cartItemList.length > 0) {
					ch = cartItemList[0].toUpperCase();
					switch (ch) {
					case "ADD":
						if (cartItemList.length > 1) {
							List<String> itemlist = Arrays.stream(cartItemList).collect(Collectors.toList());
							itemlist.remove(0);
							cartItems.addAll(itemlist);
							System.out.println(itemlist.stream().collect(Collectors.joining(",")) + " added to the cart");
						}
						break;

					case "LIST":
						shoppingCartItem.itemList(cartItems, cartName);
						break;

					case "DELETE":
						if (cartItemList.length > 1) {
							int inputIndex = Integer.parseInt(cartItemList[1]);
							shoppingCartItem.deleteCartItem(cartItems, inputIndex);
						} else
							System.out.println("Please enter the item index");
						break;

					case "SAVE":
						socket = new Socket(hostName, serverPort);
						objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
						objectInputStream = new ObjectInputStream(socket.getInputStream());
						requestVO.setRequestName("SAVE");
						requestVO.setCartName(cartName);
						requestVO.setItemList(cartItems);
						objectOutputStream.writeObject(requestVO);
						System.out.println(objectInputStream.readUTF());
						objectInputStream.close();
						objectOutputStream.close();
						socket.close();
						break;

					case "EXIT":
						socket = new Socket(hostName, serverPort);
						objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
						objectInputStream = new ObjectInputStream(socket.getInputStream());
						requestVO.setRequestName("EXIT");
						objectOutputStream.writeObject(requestVO);
						System.out.println(objectInputStream.readUTF());
						objectInputStream.close();
						objectOutputStream.close();
						socket.close();
						break;

					default:
						System.out.println(
								"Please choose the valid choice for list of below.\nCreate\nLoad\nList\nAdd\nDelete\nSave\nExit");
						break;
					}
				}
			} while (!"EXIT".equalsIgnoreCase(ch));

		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
	}
}
