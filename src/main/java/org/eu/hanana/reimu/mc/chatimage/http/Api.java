package org.eu.hanana.reimu.mc.chatimage.http;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Api extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应内容类型
        response.setContentType("text/html");

        // 获取输出流
        PrintWriter out = response.getWriter();

        // 生成动态内容
        out.println("<html>");
        out.println("<head><title>Dynamic Page</title></head>");
        out.println("<body>");
        out.println("<h1>Hello, this is a dynamic page generated by Java!</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}

