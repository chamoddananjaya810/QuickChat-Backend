/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package socket;

import com.google.gson.Gson;
import entity.Chat;
import entity.FriendList;
import entity.Status;
import entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.Session;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

/**
 *
 * @author User
 */
public class ChatService {

    private static final ConcurrentHashMap<Integer, Session> SESSIONS = new ConcurrentHashMap<>();
    private static final Gson GSON = new Gson();
    private static final String URL = "https://5f2ed3b1dc19.ngrok-free.app"; // ngrok proxy url

    public static void register(int userId, Session session) {
        SESSIONS.put(userId, session);
    }

    public static void unregister(int userId) {
        SESSIONS.remove(userId);
    }

    public static void sendToUser(int userId, Object payload) {
        Session ws = SESSIONS.get(userId);
        if (ws != null && ws.isOpen()) {
            try {
                System.out.println("SENDING TO USER " + userId + ": " + GSON.toJson(payload));
                ws.getBasicRemote().sendText(GSON.toJson(payload));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<ChatSummary> getFriendChatsForUser(int userId) {
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Criteria friendListCriteria = session.createCriteria(FriendList.class);
            friendListCriteria.add(Restrictions.eq("user_id.id", userId));
            friendListCriteria.add(Restrictions.eq("status", Status.ACTIVE));
            List<FriendList> friendList = friendListCriteria.list();

            System.out.println("Found " + friendList.size() + " active friends for user " + userId);

            Map<Integer, ChatSummary> map = new LinkedHashMap<>();
            for (FriendList fl : friendList) {
                User myFriend = fl.getFriend_id();
                Criteria c1 = session.createCriteria(Chat.class);

                Criterion rest1 = Restrictions.and(Restrictions.eq("sender.id", userId),
                        Restrictions.eq("recipient.id", myFriend.getId()));

                Criterion rest2 = Restrictions.and(Restrictions.eq("sender.id", myFriend.getId()),
                        Restrictions.eq("recipient.id", userId));

                c1.add(Restrictions.or(rest1, rest2));
                c1.addOrder(Order.desc("updated_at")); // Assuming 'updateAt' is the correct field name from BaseEntity
                c1.setMaxResults(1);

                List<Chat> chats = c1.list();

                if (!chats.isEmpty()) {
                    Chat lastChat = chats.get(0);

                    int unread = 0; // Logic for unread count needs improvement, but works for now.

                    String profileImage = URL + "/QuickChat-Backend/profile-images/" + myFriend.getId() + "/image1.png";
                    map.put(myFriend.getId(), new ChatSummary(
                            myFriend.getId(),
                            myFriend.getFirst_name() + " " + myFriend.getLast_name(),
                            lastChat.getMessage(),
                            lastChat.getUpdated_at(),
                            unread,
                            profileImage
                    ));
                } else {

                    System.out.println("No chat history found with friend: " + myFriend.getId());
                }
            }
            return new ArrayList<>(map.values());
        } finally {
            session.close();
        }
    }

    public static void deliverChat(Chat chat) {
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        session.persist(chat);
        tr.commit();
        session.close();

        Map<String, Object> envelope = new HashMap<>();
        envelope.put("type", "chat");
        envelope.put("payload", chat);

        sendToUser(chat.getRecipient().getId(), envelope);
        sendToUser(chat.getSender().getId(), envelope);

        sendToUser(chat.getRecipient().getId(), friendListEnvelope(getFriendChatsForUser(chat.getRecipient().getId())));
        sendToUser(chat.getSender().getId(), friendListEnvelope(getFriendChatsForUser(chat.getSender().getId())));
    }

    public static Map<String, Object> friendListEnvelope(List<ChatSummary> list) {
        Map<String, Object> envelope = new HashMap<>();
        envelope.put("type", "friend_list");
        envelope.put("payload", list);
        return envelope;
    }

    public static List<Chat> getChatHistory(int userId, int friendId) {
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Criteria c = session.createCriteria(Chat.class);

            // ===== නිවැරදි කරන ලද Query එක =====
            Criterion userIsSender = Restrictions.and(
                    Restrictions.eq("sender.id", userId),
                    Restrictions.eq("recipient.id", friendId)
            );
            Criterion userIsReceiver = Restrictions.and(
                    Restrictions.eq("sender.id", friendId),
                    Restrictions.eq("recipient.id", userId)
            );

            c.add(Restrictions.or(userIsSender, userIsReceiver));
            c.addOrder(Order.asc("created_at")); // Assuming 'createdAt' is the correct field name

            List<Chat> list = c.list();

            Transaction tr = session.beginTransaction();
            for (Chat chat : list) {
                if (chat.getRecipient().getId() == userId && chat.getStatus() == Status.SENT) {
                    chat.setStatus(Status.DELIVERD); // "DELIVERED" විය යුතුයි, Status enum එකේ spelling බලන්න
                    session.update(chat);
                }
            }
            tr.commit();

            return list;
        } finally {
            session.close();
        }
    }
}
