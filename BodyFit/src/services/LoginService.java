package services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import beans.Admin;
import beans.Customer;
import beans.Manager;
import beans.Trainer;
import beans.User;
import dao.AdminDao;
import dao.CustomerDao;
import dao.ManagerDao;
import dao.TrainerDao;

@Path("login")
public class LoginService   {
	CustomerDao customerDao = new CustomerDao();
	ManagerDao managerDao = new ManagerDao();
	AdminDao adminDao = new AdminDao();
	TrainerDao trainerDao = new TrainerDao();
	List<String> usernames;
	
	@Context
	ServletContext ctx;
	
	@SuppressWarnings("unused")
	public void init() {
		if (ctx.getAttribute("username") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("username", "");
		}
	}
	public String getContext() {
		return (ctx.getRealPath("") + "WEB-INF" + File.separator + "classes" + File.separator + "jsonData"
				+ File.separator);
		//return "src/jsonData/";
	}
	private void setLoggedInUser(String username) {
		ctx.setAttribute("username", username);
	}
	@GET
	@Path("loggedUser")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUsername() {
		return (String)ctx.getAttribute("username");
	}
	
	@GET
	@Path("logOut")
	@Produces(MediaType.TEXT_PLAIN)
	public String userLogOut() {
		System.out.println(ctx.getRealPath(""));
		ctx.setAttribute("username", "");
		return "Log Out Successful";
	}
	@POST
	@Path("logInStatus")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean SuccesLoginIn(User user) {
		System.out.println(ctx.getRealPath(""));
		UserState loginState = customerLogIn(user);
		boolean loginStatus= false;
		if(loginState == UserState.SUCCESS) {
			loginStatus =  true;
		}
		loginState = adminLogIn(user);
		if(loginState == UserState.SUCCESS) {
			loginStatus = true;
		}
		loginState = managerLogIn(user);
		if(loginState == UserState.SUCCESS) {
			loginStatus =  true;
		}
		loginState = trainerLogIn(user);
		if(loginState == UserState.SUCCESS) {
			loginStatus = true;
		}
		return loginStatus;
		
	}
	@GET
	@Path("loggedUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getCurrentUser() {
		adminDao.setBasePath(getContext());
		customerDao.setBasePath(getContext());
		managerDao.setBasePath(getContext());
		trainerDao.setBasePath(getContext());
		String username = (String) ctx.getAttribute("username");
		User trainer = trainerDao.getById(username);
		if(trainer != null) {
			return trainer;
		}
		User manager = managerDao.getById(username);
		if(manager != null) {
			return manager;
		}
		User customer = customerDao.getById(username);
		if(customer != null) {
			return customer;
		}
		User admin = adminDao.getById(username);
		if(admin != null) {
			return admin;
		}
		return null;
		
	}
	@GET
	@Path("getUsernames")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> getAllUserNames(){
		customerDao.setBasePath(getContext());
		managerDao.setBasePath(getContext());
		adminDao.setBasePath(getContext());
		trainerDao.setBasePath(getContext());
		ArrayList<String> usernames = new ArrayList<String>();
		for (User user : customerDao.getAllToList()) {
			usernames.add(user.getUsername());
		}
		for (User user : managerDao.getAllToList()) {
			usernames.add(user.getUsername());
		}
		for (User user : adminDao.getAllToList()) {
			usernames.add(user.getUsername());
		}
		for (User user : trainerDao.getAllToList()) {
			usernames.add(user.getUsername());
		}
		this.usernames = usernames;
		return usernames;

	}
	private UserState customerLogIn(User user) {
		UserState state = UserState.ERROR;
		customerDao.setBasePath(getContext());
		for (Customer u : customerDao.getAllToList()) {
			if (u.getUsername().equals(user.getUsername())) {
				if (u.getPassword().equals(user.getPassword())) {
					if(u.isBanned()) {
						state = UserState.BANNED;
					}
					else {
						setLoggedInUser(user.getUsername());
						state = UserState.SUCCESS;
					}
			}
		}
		}
		return state;
	}

	private UserState adminLogIn(User user) {
		adminDao.setBasePath(getContext());
		UserState state = UserState.ERROR;
		for (Admin u : adminDao.getAllToList()) {
			if (u.getUsername().equals(user.getUsername())) {
				if (u.getPassword().equals(user.getPassword())) {
					setLoggedInUser(user.getUsername());
					return UserState.SUCCESS;
			
			}
		}
		}
		return state;
	}

	private UserState managerLogIn(User user) {
		managerDao.setBasePath(getContext());
		UserState state = UserState.ERROR;
		for (Manager u : managerDao.getAllToList()) {
			if (u.getUsername().equals(user.getUsername())) {
				if (u.getPassword().equals(user.getPassword())) {
					if(u.isBanned()) {
						state =  UserState.BANNED;
					}
					else {
						setLoggedInUser(u.getUsername());
						state = UserState.SUCCESS;
					}
				}
			}
		}
		return state;
	}

	private UserState trainerLogIn(User user) {
		trainerDao.setBasePath(getContext());
		UserState state = UserState.ERROR;
		for (Trainer u : trainerDao.getAllToList()) {
			if (u.getUsername().equals(user.getUsername())) {
				if (u.getPassword().equals(user.getPassword())) {
					if(u.isBanned()) {
						state = UserState.BANNED;
					}
					else {
						setLoggedInUser(user.getUsername());
						state = UserState.SUCCESS;
					}
				}
			}
		}
		return state;
	}
	
//	public String getCustomerStateString(User user) {
//		UserState state  = customerLogIn(user);
//		String userState = "";
//		switch (state) {
//		case SUCCESS:
//			userState = "Success login";
//			break;
//		case ERROR:
//			userState = "Wrong credential.Wrong username or password!";
//			break;
//		case BANNED:
//			userState = "Your are banned!";
//			break;
//
//		default:
//			break;
//		}
//		return userState;
//	}
//	public String getTrainerStateString(User user) {
//		UserState state  = trainerLogIn(user);
//		String userState = "";
//		switch (state) {
//		case SUCCESS:
//			userState = "Success login";
//			break;
//		case ERROR:
//			userState = "Wrong credential.Wrong username or password!";
//			break;
//		case BANNED:
//			userState = "Your are banned!";
//			break;
//
//		default:
//			break;
//		}
//		return userState;
//	}
//	public String getManagerStateString(User user) {
//		UserState state  = managerLogIn(user);
//		String userState = "";
//		switch (state) {
//		case SUCCESS:
//			userState = "Success login";
//			break;
//		case ERROR:
//			userState = "Wrong credential.Wrong username or password!";
//			break;
//		case BANNED:
//			userState = "Your are banned!";
//			break;
//
//		default:
//			break;
//		}
//		return userState;
//	}
//	public String getAdminStateString(User user) {
//		UserState state  = adminLogIn(user);
//		String userState = "";
//		switch (state) {
//		case SUCCESS:
//			userState = "Success login";
//			break;
//		case ERROR:
//			userState = "Wrong credential.Wrong username or password!";
//			break;
//		case BANNED:
//			userState = "Your are banned!";
//			break;
//
//		default:
//			break;
//		}
//		return userState;
//	}	
	


}
