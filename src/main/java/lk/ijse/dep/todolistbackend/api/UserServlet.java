package lk.ijse.dep.todolistbackend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import lk.ijse.dep.todolistbackend.dto.UserDTO;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "UserServlet", value = "/*")
public class UserServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool_ToDoApp")
    private volatile DataSource pool;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() == null ||
                !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }
        if (req.getPathInfo().equals("/login")){
            try {
                Jsonb jsonb = JsonbBuilder.create();
                UserDTO user = jsonb.fromJson(req.getReader(), UserDTO.class);
                String name = user.getName();
                String password = user.getPassword();
                try(Connection connection = pool.getConnection()){
                    PreparedStatement stm = connection.prepareStatement("SELECT * FROM to_do_list_backend.user WHERE name=? AND password=?");
                    stm.setString(1,name);
                    stm.setString(2,password);
                    if (!stm.executeQuery().next()){
                        System.out.println("User is not registered!");
                        return;
                    }
                    /* Todo: Handle user for future process*/
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }catch (JsonbException e){

            }

        }
    }
}
