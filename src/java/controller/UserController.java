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
import javax.servlet.annotation.MultipartConfig;
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
@MultipartConfig
@WebServlet(name = "UserController", urlPatterns = {"/UserController"})
public class UserController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("oj");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String contryCode = request.getParameter("countryCode");
        String contactNo = request.getParameter("contactNo");
        String password = request.getParameter("password");
        Part profileImage = request.getPart("profileImage");

        System.out.println(firstName);
        System.out.println(lastName);
        System.out.println(contryCode);
        System.out.println(contactNo);
        System.out.println(profileImage);
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();

        responseObject.addProperty("status", false);

        if (firstName.isEmpty()) {
            responseObject.addProperty("message", "first name is required");
        } else if (lastName.isEmpty()) {

            responseObject.addProperty("message", "last name is required");
        } else if (contryCode.isEmpty()) {

            responseObject.addProperty("message", "country code is required");
        } else if (contactNo.isEmpty()) {

            responseObject.addProperty("message", "contact no is required");
        } else if (password.isEmpty()) {

            responseObject.addProperty("message", "password no is required");
        } else if (profileImage == null) {

            responseObject.addProperty("message", "Select a Profile");
        } else {
            Session s = HibernateUtil.getSessionFactory().openSession();
            Criteria c = s.createCriteria(User.class);
            c.add(Restrictions.eq("country_code", contryCode));
            c.add(Restrictions.eq("contact_number", contactNo));

            User user = (User) c.uniqueResult();
            if (user != null) {
                responseObject.addProperty("message", "This contact ni already axists");
            } else {
//                user = new User(firstName, lastName, contryCode, contactNo);
                user = new User(firstName, lastName, contryCode, password, contactNo);
                user.setCreated_at(new Date());
                user.setUpdated_at(new Date());

                Transaction tr = s.beginTransaction();
                int id = (int) s.save(user);
                tr.commit();

                responseObject.add("user", gson.toJsonTree(user));

                //image uploading
                String appPath = getServletContext().getRealPath(""); //Full path of the Web Pages folder

                String newPath = appPath.replace("build" + File.separator + "web", "web" + File.separator + "profile-images");

                File profileFolder = new File(newPath, String.valueOf(id));
                if (!profileFolder.exists()) {
                    profileFolder.mkdirs();
                }

                File file1 = new File(profileFolder, "image1.png");

                Files.copy(profileImage.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                responseObject.addProperty("status", true);
                s.close();
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    }

}
