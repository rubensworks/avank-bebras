
package models.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;

import play.mvc.Content;
import play.mvc.Result;
import views.html.landingPages.PupilLandingPage;

/**
 * @author Sander Demeester
 */

public class Independent extends User{

    private List<String> previousClassList;

    public Independent(UserModel data){
        super(data); //abstract class constructor could init some values
        previousClassList = new ArrayList<String>();
    }

    /**
     * Add an old class
     * @param oldClass
     */
    public void addPreviousClass(String oldClass){
        previousClassList.add(oldClass);
    }

    /**
     * Add a class to Independent user.
     * @param classGroup
     */
    public void addCurrentClass(ClassGroup classGroup){

    }

    /**
     *
     * @return Get currentClass.
     */
    public ClassGroup getCurrentClass(List<ClassGroup> classes){
    	Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR, -12);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date today = c.getTime();
    	for(ClassGroup cl : classes){
    		if(!cl.expdate.before(today))return cl;
    	}
    	return null;
    }
    
    public ClassGroup getCurrentClass(){
    	return this.getCurrentClass(new ArrayList(this.getClasses()));
    }

	@Override
	public Content getLandingPage() {
		
		List<ClassGroup> classes = new ArrayList(this.getClasses());
		ClassGroup current=getCurrentClass(classes);
		if(current != null)classes.remove(current);
		return PupilLandingPage.render(this.data.id,current,classes);
	}

	@Override
	public Result showStatistics() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Queries the database for all class the user is associated with
	 * @return list of classes the 
	 */
	public Collection<ClassGroup> getClasses(){
		ArrayList<ClassGroup> res = new ArrayList<>();
		
		List<ClassPupil> cp = Ebean.find(ClassPupil.class).where().eq("indid", this.data.id).findList();
		for(ClassPupil c : cp){
			ClassGroup cg = Ebean.find(ClassGroup.class).where().eq("id", c.classid).findUnique();
			if(cg != null)res.add(cg);
		}
		
		return res;
	}


}
