package lk.ijse.dep.todolistbackend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.dep.todolistbackend.dto.ItemDTO;
import org.w3c.dom.ls.LSOutput;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "ItemServlet", value = "/items/*")
public class ItemServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool4tdl")
    private DataSource pool;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
if(request.getContentType()==null || !request.getContentType().toLowerCase().startsWith("application/json")){
    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    return;
}

    try (Connection connection = pool.getConnection()){

        String userEmail =request.getPathInfo().replaceAll("/","");
        System.out.println(userEmail);
        String q = request.getParameter("q");
        String query ="%"+((q==null) ? "":q)+"%";
        System.out.println(query);
        boolean pagination = request.getParameter("page")!=null && request.getParameter("size")!= null;

        System.out.println(pagination);

        String sql = "SELECT * FROM items WHERE email=? AND description LIKE ? "+((pagination) ? "LIMIT ? OFFSET ?":"");

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,userEmail);
        ps.setString(2,query);

        String page =request.getParameter("page");
        String size =request.getParameter("size");

        if(pagination){
            ps.setInt(3,Integer.parseInt(page));
            ps.setInt(4,(Integer.parseInt(size)-1)*Integer.parseInt("size"));
         }
        PreparedStatement ps1 = connection.prepareStatement("SELECT count (*) FROM items WHERE email=? AND description LIKE ? ");
 ps1.setString(1,userEmail);
 ps1.setString(2,query);
        ///  PreparedStatement ps = connection.prepareStatement("SELECT * FROM items");
        ResultSet rst = ps.executeQuery();
        ResultSet rst1 = ps1.executeQuery();
     /*   if(!rst.next()){
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"ItemNot Found");
            return;

        }*/
        ArrayList<ItemDTO> itemList =new ArrayList<>();
        while (rst.next()){
            ItemDTO itemDTO = new ItemDTO(rst.getString("description"), rst.getString("email"), rst.getString("status"));
            itemList.add(itemDTO);
        }

        if(rst1.next()){
            response.setHeader("X-Count",rst1.getString(1));
            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(itemList, response.getWriter());
        }

        response.setContentType("application/json");





    } catch (SQLException e) {
        throw new RuntimeException(e);
    }


}





    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

if(req.getContentType()==null || !req.getContentType().toLowerCase().startsWith("application/json")){
    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid Request ");
    return;
}

        Jsonb jsonb = JsonbBuilder.create();
        ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

        try (Connection connection = pool.getConnection()){

            PreparedStatement ps1 = connection.prepareStatement("SELECT * FROM items WHERE id=?");
            ps1.setString(1,req.getPathInfo().replaceAll("/",""));
            ResultSet rst = ps1.executeQuery();

            if(rst.next()){

                PreparedStatement ps = connection.prepareStatement("UPDATE items SET description=?,status=? WHERE id=?");

                ps.setString(1,itemDTO.getDescription());
                ps.setString(2,itemDTO.getStatus());
                ps.setString(3,req.getPathInfo().replaceAll("/",""));
                System.out.println(ps.execute());

                if(!ps.execute()){
                    resp.setStatus(HttpServletResponse.SC_CREATED);


                }else {
                    throw new RuntimeException("Unable to Update");
                }

            }else {

                PreparedStatement ps = connection.prepareStatement("INSERT INTO items ( description, email, status) VALUES (?,?,?)");
                ps.setString(1,itemDTO.getDescription());
                ps.setString(2,itemDTO.getEmail());
                ps.setString(3,itemDTO.getStatus());
                //ps.setString(4,req.getPathInfo().replaceAll("/",""));
                System.out.println("A8");

                if (ps.execute()){
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Unable to Update");
                    return;
                }
               resp.setStatus(HttpServletResponse.SC_CREATED);


            }



        } catch (SQLException e) {
            System.out.println("ASD");
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getContentType()==null || !req.getContentType().toLowerCase().startsWith("application/json")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid Type");
            return;
        }

     if (req.getPathInfo().replaceAll("/","").matches("\\d+")){

         try(Connection connection = pool.getConnection()){


             PreparedStatement ps = connection.prepareStatement("DELETE FROM items WHERE id=?");


             ps.setString(1,req.getPathInfo().replaceAll("/",""));

             if(ps.execute()){
                 System.out.println("W2");
                 throw new RuntimeException("Unable to  Delete");

             }else
             { resp.setStatus(HttpServletResponse.SC_CREATED);}





         } catch (SQLException e) {
             throw new RuntimeException(e);


         }


     }
         //
         //
         // TO DO ??????????????/
         //

         try(Connection connection = pool.getConnection()){




         } catch (SQLException e) {
             throw new RuntimeException(e);


         }
     }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if(request.getContentType()==null || !request.getContentType().toLowerCase().startsWith("application/json")){
        response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid Type");
        return;
    }
        System.out.println("a");
        Jsonb jsonb = JsonbBuilder.create();


        ItemDTO itemDTO = jsonb.fromJson(request.getReader(), ItemDTO.class);
        System.out.println("a1");
        if(!itemDTO.getEmail().matches("([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})")){
            throw new RuntimeException();
        } else if (false) {
            throw new RuntimeException("Invalid Name");
        }

        System.out.println("a2");
        try(Connection connection = pool.getConnection()){
            System.out.println("a3");
            PreparedStatement ps = connection.prepareStatement("INSERT INTO items ( description, email, status) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            System.out.println("b");
           ps.setString(1,itemDTO.getDescription());
           ps.setString(2,itemDTO.getEmail());
           ps.setString(3,itemDTO.getStatus());
            System.out.println("b1");
           if (ps.execute()){
               throw new RuntimeException("Unable to store");
           }

            System.out.println("a4");
            ResultSet rst = ps.getGeneratedKeys();
           rst.next();
            System.out.println("b3");
           itemDTO.setId(rst.getString(1));
            System.out.println("a5");
           response.setStatus(HttpServletResponse.SC_CREATED);
           response.setContentType("application/json");
           jsonb.toJson(itemDTO,response.getWriter());
            System.out.println("a6");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
