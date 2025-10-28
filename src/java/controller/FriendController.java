
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.FriendList; // FriendList entity එක import කරගන්න
import entity.Status;
import entity.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
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

@MultipartConfig
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

        // ✅ 2. Validate the retrieved strings
        if (nickName == null || nickName.trim().isEmpty()) {
            responseObject.addProperty("message", "Nickname is required");
        } else if (countryCode == null || countryCode.trim().isEmpty()) {
            responseObject.addProperty("message", "Country code is required");
        } else if (contactNo == null || contactNo.trim().isEmpty()) {
            responseObject.addProperty("message", "Contact number is required");
        } else {
            Session s = HibernateUtil.getSessionFactory().openSession();
            
        
            User mainUser = (User) s.get(User.class, 1); // IMPORTANT: Replace '1' with actual logged-in user's ID
            
            if(mainUser == null){
               responseObject.addProperty("status", true);
            } else {
                 // Check if the friend to be added exists in the User table
                Criteria c = s.createCriteria(User.class);
                c.add(Restrictions.eq("country_code", countryCode));
                c.add(Restrictions.eq("contact_number", contactNo));
                User friendUser = (User) c.uniqueResult();

                if (friendUser == null) {
                    responseObject.addProperty("message", "The contact you are trying to add is not a registered user.");
                } else {
                    // ✅ 3. Save the new friend to the FriendList table
                    FriendList newFriend = new FriendList();
                    newFriend.setNick_name(nickName);
                    newFriend.setUser_id(mainUser); // The user who is adding the friend
                    newFriend.setFriend_id(friendUser); // The user who is being added as a friend
                    newFriend.setStatus(Status.FRIENDS); // Set a default status

                   Transaction tr = s.beginTransaction();
                    s.save(newFriend);
                  tr.commit();

                    responseObject.addProperty("status", true);
                    responseObject.addProperty("message", "Friend added successfully!");
                }
            }
            s.close();
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    }
}