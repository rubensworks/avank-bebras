
package models.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import models.dbentities.UserModel;
import models.user.factory.AdministratorUserFactory;
import models.user.factory.AuthorUserFactory;
import models.user.factory.IndependentUserFactory;
import models.user.factory.OrganizerUserFactory;
import models.user.factory.PupilUserFactory;
import models.user.factory.TeacherUserFactory;
import models.user.factory.UserFactory;
import play.mvc.Http.Context;

/**
 * Class to handle UserAuthentication.
 * @author Sander Demeester
 * @author Ruben Taelman
 */
public class AuthenticationManager {
	private static AuthenticationManager _instance = null;

	//private Map<String,User> sessionIdToUser = new HashMap<String,User>();
	//private Map<String,LoginState> mappingFromSessieIDtoLoginState =	new HashMap<String,LoginState>();

	// String: value of the COOKIENAME cookie
	private Map<String, Stack<User>> users;
	public static final String COOKIENAME = "avank.auth";
	private static final Map<UserType, UserFactory> FACTORIES = new HashMap<UserType, UserFactory>();

	static {
		FACTORIES.put(UserType.ADMINISTRATOR, new AdministratorUserFactory());
		FACTORIES.put(UserType.AUTHOR, new AuthorUserFactory());
		FACTORIES.put(UserType.INDEPENDENT, new IndependentUserFactory());
		FACTORIES.put(UserType.ORGANIZER, new OrganizerUserFactory());
		FACTORIES.put(UserType.PUPIL, new PupilUserFactory());
		FACTORIES.put(UserType.TEACHER, new TeacherUserFactory());
	}

	/**
	 * AuthenticationManager constructor.
	 */
	private AuthenticationManager(){
		users = new HashMap<String, Stack<User>>();
	}

	public static AuthenticationManager getInstance() {
		if(_instance==null)
			_instance = new AuthenticationManager();
		return _instance;
	}

	/*
	 * resets the password of the user identified with userID, assuming the email-
	 * address is correct
	 */
	public void resetPassword(String userID,String emailaddress){

	}

	/**
	 * @return Gives back a list of the roles that a given user has. 
	 **/
	public List<Role> getUserRolles(User user){
		return new ArrayList<Role>();
	}

	/**
	 * 
	 * @return get userObject from sessieID
	 */
	/*public User getUserObject(String sessieID){
    	return mappingFromStringToUser.get(sessieID);
    }*/

	/**
	 * Login or mimic with a new usermodel
	 * @author Sander Demeester & Ruben Taelman
	 * @param userModel
	 * @return The logged in user.
	 */
	public User login(UserModel userModel) {
		// Check if the current user can mimic that user and login (add to stack) if that's the case
		User current = getUser();
		User user = create(userModel);
		Stack<User> stack = users.get(getAuthCookie());
		if(stack == null) { // The user is not yet logged in (would be the case if the stack is empty)
			stack = new Stack<User>();
			stack.push(user);
			users.put(getAuthCookie(), stack);
		} else if(current.canMimic(user)) { // If the current user can mimic the other user.
			stack.add(user);
		}
		if(stack.size() == 0) 
			return user;
		else
			return stack.firstElement();
	}

	/**
	 * Logout a usermodel (or pop a mimic)
	 * @param userModel
	 */
	public User logout() {
		Stack<User> stack = users.get(getAuthCookie());
		stack.pop();
		if(stack.isEmpty()) {
		    users.put(getAuthCookie(), null);
		    return null;
		} else {
		    return stack.peek();
		}
	}

	private User create(UserModel userModel) {
		return FACTORIES.get(userModel.type).create(userModel);
	}

	/**
	 * Get the current authenticated user object
	 * @return
	 */
	public User getUser() {
		Stack<User> stack = users.get(getAuthCookie());
		if(stack==null) return new Anon();
		else            return stack.firstElement();
	}

	private String getAuthCookie() {
		return Context.current().session().get(COOKIENAME);
	}
	
	public boolean isLoggedIn() {
	    return !this.getUser().getType().equals(UserType.ANON);
	}
}
