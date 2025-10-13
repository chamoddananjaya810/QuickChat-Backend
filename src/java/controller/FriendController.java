/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

/**
 *
 * @author User
 */
@WebServlet(name = "FriendController", urlPatterns = {"/FriendController"})
public class FriendController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nickName = request.getParameter("nickName");
        String countryCode = request.getParameter("countryCode");
        String contactNo = request.getParameter("contactNo");
  
        System.out.println(nickName);
        System.out.println(countryCode);
        System.out.println(contactNo);
       
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();

        responseObject.addProperty("status", false);

        if (nickName.isEmpty()) {
            responseObject.addProperty("message", "Nick Name is required");
        } else if (countryCode.isEmpty()) {

            responseObject.addProperty("message", "Country is required");
        } else if (contactNo.isEmpty()) {

            responseObject.addProperty("message", "contact no  is required");
      
           
        } else {
            Session s = HibernateUtil.getSessionFactory().openSession();
            Criteria c = s.createCriteria(User.class);
            c.add(Restrictions.eq("country_code", countryCode));
            c.add(Restrictions.eq("contact_number", contactNo));

            User user = (User) c.uniqueResult();
            if (user != null) {
               
            } else {
// responseObject.addProperty("message", "This contact no is already axists");
 
 
 
//                user=new User(firstName, lastName, contryCode, contactNo);
//                user.setCreated_at(new Date());
//                user.setUpdated_at(new Date());
//           
//
//                Transaction tr= s.beginTransaction();
//                     int id = (int) s.save(user);
//                     tr.commit();
//               
//                responseObject.add("user", gson.toJsonTree(user));
//
//               
//                responseObject.addProperty("status", true);
//                 s.close();
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    
    }

}
