package View.Web.Servlets.Common;

import Control.ConfigClass;
import Model.Database.Interaction.Hash;
import Model.Database.Interaction.User;
import Model.Database.Support.CustomLogs;
import Model.Database.Support.UserAccessHelper;
import Model.Database.Tables.Table.T_Hash;
import Model.Database.Tables.Table.T_User;
import View.Configuration.TemplateEngineUtil;
import View.Support.CustomExceptions.BadVerificationCodeException;
import View.Support.CustomExceptions.EmailNotRegisteredException;
import View.Support.CustomExceptions.InvalidOperationException;
import View.Support.CustomExceptions.InvalidPasswordException;
import View.Support.DcsWebContext;
import View.Web.Servlets.ConnectionServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;

@WebServlet(name = "SelfRegisterServlet", urlPatterns = SelfRegisterServlet.SERVLET_URL)
public class SelfRegisterServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/register";
    public static final String TEMPLATE_NAME = "self-register.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = DcsWebContext.WebContextInitForDCS(request, response, request.getServletContext(),
                ConfigClass.HTML_VARIABLENAME_RUNNINGREMOTELY, trueIfRunningRemotely);

        engine.process(TEMPLATE_NAME, context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();

        String email = request.getParameter("email");

        try {
            //Finding user by the entered email in the database
            T_User userToRegister = User.retrieveByEmail(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), email);
            if (userToRegister == null) { //If there isn't a user with the entered email in the database
                writer.println("This email cannot be registered.");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                throw new EmailNotRegisteredException("The email entered by the user cannot be registered.");
            }

            String password = request.getParameter("password");
            if(password.length() > 15 || password.length() < 6) {
                //writer.println("Enter a password that is between 6 and 15 characters.");
                response.setStatus(HttpServletResponse.SC_LENGTH_REQUIRED);
                throw new InvalidPasswordException("Entered password must be between 6 and 15 characters.");
            }
            String hashedPassword = UserAccessHelper.hashPassword(password); //Hashing the password

            String verification = request.getParameter("verification");
            String hashedVerification = UserAccessHelper.hashPassword(verification);
            //Getting the verification code that admin generated from the database
            T_Hash verificationDB = Hash.retrieveCode(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), userToRegister.getA_pk());
            if(verificationDB == null) { //In case the admin hasn't generated a verification code yet
                //writer.println("The verification code has not been assigned yet.");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new InvalidOperationException("The verification code has not been assigned yet.");
            }

            if(!(hashedVerification.equals(verificationDB.getA_Value()))) { //Verification code entered by user is incorrect
                //writer.println("The verification code is incorrect.");
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
                throw new BadVerificationCodeException("The verification code entered by the user is incorrect.");
            }

            int hashCount = Hash.countHashesForUser(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), userToRegister.getA_pk());
            if(hashCount > 1){ //If there already is a password in the hash table, user must have registered already
                //writer.println("You can not register more than once.");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new InvalidOperationException("User can not register more than once.");
            }

            //Inserting the hashed password in the database
            Dictionary dict = new Hashtable();
            dict.put(T_Hash.DBNAME_VALUE, hashedPassword);
            dict.put(T_Hash.DBNAME_USER_ID, userToRegister.getA_pk());
            T_Hash hashToInsert = T_Hash.CreateFromScratch(dict);
            Hash.insert(dbProvider.getConn(), dbProvider.getPs(), hashToInsert);
            //writer.println("User registered.");
            response.setStatus(HttpServletResponse.SC_CREATED);

        } catch (EmailNotRegisteredException emailNotRegisteredException) {
            CustomLogs.Error(emailNotRegisteredException.getMessage());
        } catch (BadVerificationCodeException badVerificationCodeException) {
            CustomLogs.Error(badVerificationCodeException.getMessage());
        } catch (SQLException throwables) {
            CustomLogs.Error(throwables.getMessage());
        } catch (InvalidOperationException e) {
            CustomLogs.Error(e.getMessage());
        } catch (InvalidPasswordException e) {
            CustomLogs.Error(e.getMessage());
        } finally {
            writer.close();
        }
    }
}