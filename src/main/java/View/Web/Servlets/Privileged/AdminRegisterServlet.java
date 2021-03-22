package View.Web.Servlets.Privileged;

import Control.ConfigClass;
import Model.Database.Interaction.AccessPrivilegeJournal;
import Model.Database.Interaction.Hash;
import Model.Database.Interaction.User;
import Model.Database.Support.UserAccessHelper;
import Model.Database.Tables.Table.T_AccessPrivilegeJournal;
import Model.Database.Tables.Table.T_Hash;
import Model.Database.Tables.Table.T_User;
import View.Configuration.TemplateEngineUtil;
import View.Support.CustomExceptions.InvalidOperationException;
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

@WebServlet(name = "AdminRegisterServlet", urlPatterns = AdminRegisterServlet.SERVLET_URL)
public class AdminRegisterServlet extends ConnectionServlet {
    public static final String SERVLET_URL =  "/admin-register";
    public static final String TEMPLATE_NAME = "admin-register.html";

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
        String beforeTitle = request.getParameter("beforetitle");
        String firstName = request.getParameter("firstname");
        String middleName = request.getParameter("middlename");
        String lastName = request.getParameter("lastname");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String permanentResidence = request.getParameter("permanentresidence");
        String privilege = request.getParameter("privilege");


        try {
            dbProvider.beforeSqlExecution();

            T_User userToRegister = User.retrieveByEmail(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs(), email);
            if (userToRegister != null) { //If there already is a user with the entered email
                //writer.println("This email is already registered.");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                throw new InvalidOperationException("The entered email is already registered.");
            }

            Dictionary dict = new Hashtable();
            dict.put(T_User.DBNAME_BEFORETITLE, beforeTitle);
            dict.put(T_User.DBNAME_FIRSTNAME, firstName);
            dict.put(T_User.DBNAME_MIDDLENAME, middleName);
            dict.put(T_User.DBNAME_LASTNAME, lastName);
            dict.put(T_User.DBNAME_PHONE, phone);
            dict.put(T_User.DBNAME_EMAIL, email);
            dict.put(T_User.DBNAME_PERMANENTRESIDENCE, permanentResidence);
            dict.put(T_User.DBNAME_BLOCKED, false);

            userToRegister = T_User.CreateFromScratch(dict);

            boolean canEnterUser = userToRegister.IsTableOkForDatabaseEnter();
            if(!canEnterUser){ //The entered data is not acceptable
                //writer.println("The entered data is not acceptable.");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                throw new InvalidOperationException("The entered data is not acceptable.");
            }

            User.insert(dbProvider.getConn(), dbProvider.getPs(), userToRegister);
            int userID = User.retrieveLatestID(dbProvider.getConn(), dbProvider.getPs(), dbProvider.getRs());
            String verificationCode = UserAccessHelper.generateVerification(14);

            String hashedVerificationCode = UserAccessHelper.hashPassword(verificationCode);
            Dictionary verificationDict = new Hashtable();
            verificationDict.put(T_Hash.DBNAME_VALUE, hashedVerificationCode);
            verificationDict.put(T_Hash.DBNAME_USER_ID, userID);

            T_Hash hashToInsert = T_Hash.CreateFromScratch(verificationDict);
            Hash.insert(dbProvider.getConn(), dbProvider.getPs(), hashToInsert);

            int adminID = 1; // TODO ziskat realne ID admina ktory pridava pouzivatela zo sessionu
            java.util.Date date = new java.util.Date();
            java.sql.Date dateCreated = new java.sql.Date(date.getTime());
            int privilegeID = UserAccessHelper.getAccessPrivilegeType(privilege);

            Dictionary journalDict = new Hashtable(); //TODO Spravit funkciu na vytvaranie Dictionaries pre tabulky, opakovany kod
            journalDict.put(T_AccessPrivilegeJournal.DBNAME_CREATED_AT, dateCreated);
            journalDict.put(T_AccessPrivilegeJournal.DBNAME_USER_ID, userID);
            journalDict.put(T_AccessPrivilegeJournal.DBNAME_ACCESS_PRIVILEGE_ID, privilegeID);
            journalDict.put(T_AccessPrivilegeJournal.DBNAME_CREATED_BY_USER_ID, adminID);
            T_AccessPrivilegeJournal journalEntryToSave = T_AccessPrivilegeJournal.CreateFromScratch(journalDict);
            AccessPrivilegeJournal.insert(dbProvider.getConn(), dbProvider.getPs(), journalEntryToSave);

            request.getSession().setAttribute("verificationCode", verificationCode);

            dbProvider.afterSuccessfulSqlExecution();
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException throwables) {
            dbProvider.transactionRollback();
        } catch (InvalidOperationException e) {
            dbProvider.transactionRollback();
        } finally {
            dbProvider.restartAutoCommit();
        }
    }

}
