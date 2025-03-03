package com.registrationpackage;
import java.sql.*;
import java.util.ArrayList;

import jakarta.servlet.http.HttpSession;

public class Registration {

	    private Connection con;
	    HttpSession se;

	    public Registration(HttpSession session) {
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver"); // load the drivers
	            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/company","root","root@123");
	            // connection with data base
	            se = session;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public String Register(String name, String phone, String email, String pw) {
	        PreparedStatement ps;
	        String status = "";
	        try {
	            Statement st = null;
	            ResultSet rs = null;
	            st = con.createStatement();
	            rs = st.executeQuery("select * from sookshma where phone='" + phone + "' or email='" + email + "';");
	            boolean b = rs.next();
	            if (b) {
	                status = "existed";
	            } else {
	                ps = (PreparedStatement) con.prepareStatement("insert into sookshma values(0,?,?,?,?,now())");
	                ps.setString(1,name);
	                ps.setString(2,phone);
	                ps.setString(3,email);
	                ps.setString(4,pw);
	                int a = ps.executeUpdate();
	                if (a>0) {
	                    status = "success";
	                } 
	                else {
	                    status = "failure";
	                }
	            }

	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	        return status;
	    }

	    //login
	    public String login(String email, String pw) {
	        String status1 = "", id = "";
	        String name = "", emails = "";

	        try {
	            Statement st = null;
	            ResultSet rs = null;
	            st=con.createStatement();

	            rs=st.executeQuery("select * from sookshma where email='"+ email +"' and password='" +pw +"';");
	            boolean b =rs.next();
	            if (b==true) {
	                id=rs.getString("id");
	                name=rs.getString("name");
	                emails=rs.getString("email");
	                se.setAttribute("uname",name);
	                se.setAttribute("email", emails);
	                se.setAttribute("id",id);
	                status1="success";
	            } else {
	                status1="failure";
	            }
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }

	        return status1;
	    }
	    
	    //user deatils
	    public ArrayList<Student> getUserDetails() {
	        Statement st;
	        ResultSet rs;
	        ArrayList<Student> al = new ArrayList<Student>();
	        try {
	            st = con.createStatement();
	            String qry = "select *,"
	                    + "date_format(date,'%b %d, %Y') as date1"
	                    + " from sookshma where id not in(1);";
	            rs = st.executeQuery(qry);
	            while (rs.next()) {
	                Student p = new Student();
	                p.setId(rs.getString("id"));
	                p.setName(rs.getString("name"));
	                p.setEmail(rs.getString("email"));
	                p.setPhone(rs.getString("phone"));
	                p.setDate(rs.getString("date"));
	                al.add(p);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return al;
	    }

//delete

	 public String delete(int id) {
	        int count=0;
	        Statement st = null;
	        String status ="";
	        try {
	            st = con.createStatement();
	            count = st.executeUpdate("delete from sookshma where"+"id='"+id+"'");
	            if (count > 0) {
	                status = "success";
	            } else {
	                status = "failure";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return status;
	    }

	public String ForgotPassword(String mail, String pw) {
		String status = "";
	       try {
	           Statement st = con.createStatement();

	           int rspw = st.executeUpdate("update sookshma  set password='" + pw + "' where email='" + mail +"';");
	           if (rspw > 0) {
	               status = "success";
	           } else {
	               status = "failure";
	           }
	       } catch (Exception e) {
	           e.printStackTrace();
	       }

		return status;
	}
	
	//info
	
	
	public Student getInfo() {
        Statement st = null;
        ResultSet rs = null;
        Student s = null;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select * from sookshma where id='"+se.getAttribute("id") +"'");
            boolean b = rs.next();
            if (b==true) {
                s = new Student();
                s.setName(rs.getString("name"));
                s.setPhone(rs.getString("phone"));
                s.setEmail(rs.getString("email"));
            } else {
                s = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }

	public String update(String name, String phone, String email) {
		String status = "";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = con.createStatement();
            st.executeUpdate("update sookshma set name='" + name + "',phone='" + phone + "',email='" + email + "' where id= '" + se.getAttribute("id") + "' ");
            se.setAttribute("uname",name);
            status = "success";
        } catch (Exception e) {
            status = "failure";
            e.printStackTrace();
        }

        return status;
    }
// reset pass
	public String getPassword(String email,String password) {
		// TODO Auto-generated method stub
		       String status = "";
		       PreparedStatement ps = null;
		       ResultSet rs = null;
		       String query = "select * from sookshma where email=? and password=?";
		       try {
		           ps = con.prepareStatement(query);
		           ps.setString(1, email);
		           ps.setString(2,password);
		           rs = ps.executeQuery();
		           if (rs.next()) {
		               //se.setAttribute("pwd", rs.getString(5));
		               status = "success";
		           } else {
		               status = "failed";
		           }
		       } catch (SQLException e) {
		           e.printStackTrace();
		       }
		       //System.out.println(status);
		       return status;
		   }

	// It Reset the user Password

	   public String resetPassword(String email, String pwd) {
	// TODO Auto-generated method stub
	       String status = "";
	       PreparedStatement ps = null;
	       boolean res;
	       try {
	           ps = con.prepareStatement("update sookshma set password =? where email=?");
	           ps.setString(1,pwd);
	           ps.setString(2,email);
	           int rc = ps.executeUpdate();
	           if (rc>0) {
	               status = "success";
	           } else {
	               status = "failed";
	           }
	       } catch (SQLException e) {
	// TODO Auto-generated catch block
	           e.printStackTrace();
	       }
	       return status;
	   }

	   public ArrayList<Student> getUserinfo(String id) { 
			Statement st = null; 
			ResultSet rs = null; 
			ArrayList<Student> al = new ArrayList<Student>(); 
			try { 
			st = con.createStatement(); 
			String qry = "select * from sookshma where id ='"+ id +"';"; 
			rs = st.executeQuery(qry); 
			while (rs.next()) { 
			Student p = new Student(); 
			p.setId(rs.getString("id")); 
			p.setName(rs.getString("name")); 
			p.setEmail(rs.getString("email")); 
			p.setPhone(rs.getString("phone")); 
			p.setDate(rs.getString("date")); 
			al.add(p); 
			} 
			} catch (Exception e) { 
			e.printStackTrace(); 
			} 
			return al; 
			}
	}
