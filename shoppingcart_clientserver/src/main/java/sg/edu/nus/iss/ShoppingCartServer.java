package sg.edu.nus.iss;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShoppingCartServer {
	public static void main(String args[]) {
		ObjectOutputStream objectOutputStream = null;
		ObjectInputStream objectInputStream = null;
		RequestVO requestVO = null;
		String dirName = null;
		List<String> cartItemList = null;
		String serverPort = null;
		ServerSocket serverSocket = null;
		ResponseVO responseVO = null;
		try {

			cartItemList = new ArrayList<String>();
			if (args != null && args.length > 0) {
				dirName = args[0];
				serverPort = args[1];
				File fileDir = new File("/" + args[0]);
				if (!fileDir.exists()) {
					fileDir.mkdir();
				}
				System.out.println("Starting shopping cart server on port " + serverPort + "  \nUsing " + dirName
						+ " directory for persistence");
			} else {
				dirName = "shoppingcart";
				serverPort = "3000";
				File fileDir = new File("/" + dirName);
				if (!fileDir.exists()) {
					fileDir.mkdir();
				}
				System.out.println("Starting shopping cart server on port " + serverPort + "  \nUsing " + dirName
						+ " directory for persistence");
			}

			serverSocket = new ServerSocket(Integer.parseInt(serverPort));

			do {
				Socket socket = serverSocket.accept();
				objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				objectInputStream = new ObjectInputStream(socket.getInputStream());
				requestVO = new RequestVO();
				responseVO = new ResponseVO();
				requestVO = (RequestVO) objectInputStream.readObject();

				switch (requestVO.getRequestName()) {
				case "LOAD":
					System.out.println("Connection received...");
					responseVO = loadCartItems(requestVO.getCartName(), dirName, cartItemList, responseVO);
					responseVO.setResponseMessage(
							"Connected to shopping cart server at localhost on " + requestVO.getCartName() + " port "
									+ serverPort + "\n" + requestVO.getCartName() + " shopping cart loaded");
					objectOutputStream.writeObject(responseVO);
					break;

				case "SAVE":
					saveCart(requestVO.getItemList(), requestVO.getCartName(), dirName);
					objectOutputStream.writeUTF("Cart contents saved successfully to " + requestVO.getCartName());
					break;

				case "CREATE":
					saveCart(requestVO.getItemList(), requestVO.getCartName(), dirName);
					objectOutputStream.writeUTF(requestVO.getCartName() + " cart created successfully.");
					break;

				case "EXIT":
					objectOutputStream.writeUTF("Thank you for visiting our shopping cart.");
					break;
				}
				objectOutputStream.close();
				objectInputStream.close();
				socket.close();
			} while (!"EXIT".equalsIgnoreCase(requestVO.getRequestName()));
			serverSocket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static ResponseVO loadCartItems(String cartName, String dirName, List<String> cartItemList,
			ResponseVO responseVO) {
		String fileName =("/" + dirName + "/" + cartName + ".cart");
		
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			responseVO.setCartItems(stream.collect(Collectors.toList()));
			responseVO.setResponseStatus("0");
		} catch (Exception e) {
			responseVO.setResponseStatus("1");
			responseVO.setResponseMessage("Cart name does not exist. Do you want to create cart?");
		}
		return responseVO;
	}

	private static void writeToCartFile(Path path, List<String> list) {
		try {
			if (Files.exists(path)) {
				Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);
			}
			Files.write(path, list, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveCart(List<String> cartItemList, String cartName, String dirName) {
		Path path = Paths.get("/" + dirName + "/" + cartName + ".cart");
		if (cartItemList != null && cartItemList.size() > 0) {
			writeToCartFile(path, cartItemList);
		}
	}

}
