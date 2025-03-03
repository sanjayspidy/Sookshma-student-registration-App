package com.controller;

import java.io.IOException;
import java.io.PrintWriter;

import com.registrationpackage.Registration;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet(name ="register", urlPatterns = {"/register"})
public class register extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // type of the response sent to the client or browser
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        Registration reg = new Registration(session);
        try {
            if (request.getParameter("register")!= null) {
                String name = request.getParameter("name");
                String phone = request.getParameter("phone");
                String email = request.getParameter("email");
                String pw = request.getParameter("pw");
                String cp = request.getParameter("cp");

                if (pw.equals(cp)) {
                    String status = reg.Register(name, phone, email, pw);
                    if (status.equals("existed")){
                        request.setAttribute("status","Existed record");
                        RequestDispatcher rd1 = request.getRequestDispatcher("registration.jsp");
                        rd1.forward(request, response);
                    } else if (status.equals("success")) {
                        request.setAttribute("status","Successfully Registered");
                        RequestDispatcher rd1 = request.getRequestDispatcher("Login.jsp");
                        rd1.forward(request, response);

                    } else if (status.equals("failure")) {
                        request.setAttribute("status","Registration failed");
                        RequestDispatcher rd1 = request.getRequestDispatcher("registration.jsp");
                        rd1.forward(request, response);

                    }
                }
            }
            
            else if (request.getParameter("login")!= null) {
                String email1 = request.getParameter("email");
                String pass = request.getParameter("pw");
                String status = reg.login(email1, pass);
                if (status.equals("success")) {

                    RequestDispatcher rd1 =request.getRequestDispatcher("index.jsp");

                    rd1.forward(request,response);

                } else if (status.equals("failure")){
                    request.setAttribute("status","Login failed");
                    RequestDispatcher rd1 = request.getRequestDispatcher("Login.jsp");
                    rd1.forward(request,response);
                }
            } else if (request.getParameter("logout") != null) {
                session.invalidate();
                RequestDispatcher rd1 = request.getRequestDispatcher("index.jsp");
                rd1.forward(request,response);
            }
            
         // Forgot Password ----------------------
            else if (request.getParameter("forgotPass") != null) {
                String mail = request.getParameter("email");
                String pw11 = request.getParameter("pw");
                String cp11 = request.getParameter("cpw");
                if (pw11.equals(cp11)) {
                    String status = reg.ForgotPassword(mail,pw11);
                    if (status.equals("success")) {
                        request.setAttribute("status", "Password Reset Successfully");
                        RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
                        rd.forward(request,response);
                        
                    } else if (status.equals("failure")) {
                        request.setAttribute("status", "Password Reset Failed");
                        RequestDispatcher rd = request.getRequestDispatcher("forgot.jsp");
                        rd.forward(request,response);
                    }
                } else {
                    request.setAttribute("status", "Password mismatch");
                    RequestDispatcher rd = request.getRequestDispatcher("forgot.jsp");
                    rd.forward(request,response);
                }
                
            }
            
            //edit
            
            else if (session.getAttribute("uname") != null && request.getParameter("submit") != null) {
                String name11 = request.getParameter("name");
                String pno = request.getParameter("pno");
                String email22 = request.getParameter("email");
                Registration u = new Registration(session);
                String status = u.update(name11, pno, email22);
                if (status.equals("success")) {
                    request.setAttribute("status", "Profile successfully Updated");
                    RequestDispatcher rd1 = request.getRequestDispatcher("index.jsp");
                    rd1.forward(request,response);
                } else {
                    request.setAttribute("status","Updation failure");
                    RequestDispatcher rd1 = request.getRequestDispatcher("index.jsp");
                    rd1.forward(request,response);
                }
            }
            else if (request.getParameter("reset") != null) {
                String emaill = request.getParameter("email");
                String opw = request.getParameter("opw");
                String npw = request.getParameter("npw");
                if (opw.equals(npw)) {

                    String s = reg.getPassword(emaill, opw);
                    //System.out.println("Hi");
                    if (s.equals("success") && (opw.equals(npw))) {
                        //if (opw.equals(npw)) {
                        request.setAttribute("status","New Passwordis same as old Password");
                        RequestDispatcher rd = request.getRequestDispatcher("changePass.jsp");
                        rd.forward(request,response);
                        //} 
                    }
                }
                else {
                    System.out.println("Inside reset");
                    String status = reg.resetPassword(emaill,npw);
                    System.out.println(status);
                     if (status.equals("success")) 
                        request.setAttribute("status","Password changed successfully");
                        RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
                        rd.forward(request,response);
//                             } else  {
//                                 request.setAttribute("status", "Both Are same ");
//                                 RequestDispatcher rd = request.getRequestDispatcher("changePass.jsp");
//                                 rd.forward(request, response);
//                             }
                }

            }

            
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    

  
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}



