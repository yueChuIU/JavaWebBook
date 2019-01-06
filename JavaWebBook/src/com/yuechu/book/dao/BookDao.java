package com.yuechu.book.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;


import com.yuechu.books.Book;
import com.yuechu.books.BookBean;


public class BookDao {
	
	private static Gson gson = new Gson();
	private static Connection connection = null;
	
	private static void getConnection() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/yuechu", "root", "1");
	}
	
	private static void getClose(Connection connection, Statement st, PreparedStatement ps, ResultSet rs) throws SQLException {
		if (rs != null) {
			rs.close();
		}
		if (ps != null) {
			ps.close();
		}
		if (st != null) {
			st.close();
		}
		if (connection != null) {
			connection.close();
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		System.out.println(Query());
	}
	
	public static String Query() throws ClassNotFoundException, SQLException {
		PreparedStatement ps = null;
		/*
		 * n 本书的集合列表 booksList
		 * 声明 Book 的 POJO 实体类
		 * 实际参见 top.gendseo.books.pojo.Books
		 */
		List<Book> booksList = new ArrayList<>();
		getConnection();
		
		String sql = "SELECT * FROM books;";
		System.out.println(sql);
		ps = connection.prepareStatement(sql);
		// 获得查询出来的结果集合
		ResultSet rs = ps.executeQuery();
		// 如果结果集合不为空 do while
		while (rs.next()) {
			// 声明一本书的类，并且往里添加数据，一一对应
		Book book = new Book();
		book.setBook_id(rs.getInt("book_id"));
		book.setBook_name(rs.getString("book_name"));
		book.setBook_number(rs.getInt("book_number"));
			// 最后把这本书添加到书的集合列表 booksList
//			booksList.add(book);
			System.out.println(book.getBook_id()+" "+book.getBook_name()
			+" "+book.getBook_number()+" ");
		}
		/*
		 * n 本书的集合列表 BooksBean
		 * 声明 BooksBean 的 POJO 实体类
		 * 实际参见 top.gendseo.books.pojo.BooksBean
		 */
		BookBean booksBean = new BookBean();
		// 图书的列表
		booksBean.setRows(booksList);
		// 图书的总数
		booksBean.setTotal(String.valueOf(booksList.size()));
		
		getClose(connection, null, ps, rs);
		return gson.toJson(booksBean);
	}
	public static String DELETE(String book_id) throws ClassNotFoundException, SQLException {
		Statement st = null;
		getConnection();
		
		st = connection.createStatement();
		String sql = "DELETE FROM books WHERE \"book_id\" in (" + book_id + ");";
		System.out.println(sql);
		// executeUpdate 不同于 executeQuery
		// executeUpdate 执行更新操作，不返回任何结果
		st.executeUpdate(sql);
		
		getClose(connection, st, null, null);
		return "true";
	}
	public static String UPDATE(String json) throws ClassNotFoundException, SQLException {
		PreparedStatement ps = null;
		getConnection();
		
		// 使用 Gson 将 JSON 转换成 POJO 实体类 Book
		Book book = gson.fromJson(json, Book.class);
		String sql = "UPDATE books SET \"book_id\" = ?,\"book_name\" = ?,\"book_number\" = ?;";
		System.out.println(sql);
		
		ps = connection.prepareStatement(sql);
		ps.setInt(1, book.getBook_id());
		ps.setString(2, book.getBook_name());
		ps.setInt(3, book.getBook_number());
		ps.executeUpdate();
		
		getClose(connection, null, ps, null);
		return "true";
	}
	public static String INSERT(String json) throws ClassNotFoundException, SQLException {
		PreparedStatement ps = null;
		getConnection();

		// 使用 Gson 将 JSON 转换成 POJO 实体类 Book
		Book book = gson.fromJson(json, Book.class);
		String sql = "INSERT INTO books (\"book_id\", \"book_name\", \"book_number\") VALUES (?, ?, ?);";
		 System.out.println(sql);

			
		ps = connection.prepareStatement(sql);
		ps.setInt(1, book.getBook_id());
		ps.setString(2, book.getBook_name());
		ps.setInt(3, book.getBook_number());
		ps.executeUpdate();
		
		getClose(connection, null, ps, null);
		return "true";
	}
}